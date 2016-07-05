package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by eduardo on 16-07-05.
 */
public class RouteProvider extends ContentProvider {

    static final String PROVIDER_NAME = "br.ufc.dc.sdpm.provider.Route";
    static final String URI = "content://" + PROVIDER_NAME + "/routes";
    static final Uri CONTENT_URI = Uri.parse(URI);

    static final String ID = "id";
    static final String ORIGIN = "origin";
    static final String DESTINY = "destiny";
    static final String BUS = "onibus";
    static final String ROTA = "rota";

    private static HashMap<String, String> ROUTES_PROJECTION_MAP;


    static final int ROUTES = 1;
    static final int ROUTES_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "routes", ROUTES);
        uriMatcher.addURI(PROVIDER_NAME, "routes/#", ROUTES_ID);
    }

    private SQLiteDatabase database;
    static final String DATABASE_NAME = "MyRoutes.db";
    static final int DATABASE_VERSION = 2;
    static final String ROUTES_TABLE_NAME = "routes";
    static final String CREATE_DB_TABLE = "create table " + ROUTES_TABLE_NAME +
            " (id integer primary key autoincrement," +
            " origin text, destiny text, onibus text, rota text);";

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();

        return (database == null) ? false : true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all note records
             */
            case ROUTES:
                return "vnd.android.cursor.dir/vnd.br.ufc.dc.sdpm.myroutes.routes";
            /**
             * Get a particular note
             */
            case ROUTES_ID:
                return "vnd.android.cursor.item/vnd.br.ufc.dc.sdpm.myroutes.routes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ROUTES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ROUTES:
                queryBuilder.setProjectionMap(ROUTES_PROJECTION_MAP);
                break;
            case ROUTES_ID:
                queryBuilder.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = ID;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = database.insert(ROUTES_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri uriAux = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(uriAux, null);
            return uriAux;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case ROUTES:
                count = database.delete(ROUTES_TABLE_NAME, selection, selectionArgs);
                break;
            case ROUTES_ID:
                String id = uri.getPathSegments().get(1);
                count = database.delete(ROUTES_TABLE_NAME, ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case ROUTES:
                count = database.update(ROUTES_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case ROUTES_ID:
                count = database.update(ROUTES_TABLE_NAME, values, ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("drop table if exists " + ROUTES_TABLE_NAME);
            onCreate(database);
        }
    }
}
