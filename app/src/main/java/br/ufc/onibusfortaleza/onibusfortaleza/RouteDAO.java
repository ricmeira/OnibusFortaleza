package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 16-06-22.
 */
public class RouteDAO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyRoutes.db";
    public static final int DATABASE_VERSION = 1;

    public RouteDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public RouteDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table routes (");
        sql.append("id integer primary key autoincrement,");
        sql.append("origin text,");
        sql.append("destiny text,");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists routes");
        onCreate(db);
    }

    public void create(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("origin", route.getOrigin());
        contentValues.put("destiny", route.getDestiny());
        long id = db.insert("routes", null, contentValues);
        Log.v("SQLite", "create id = " + id);
    }

    public Route retrieve(Integer id) {
        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(id+1);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from routes where id = ?", fieldValues);
        Route route = null;
        result.moveToFirst();
        if (result != null && result.getCount() > 0) {
            route = new Route();
            route.setId(result.getInt(0));
            route.setOrigin(result.getString(1));
            route.setDestiny(result.getString(2));
        }
        return route;
    }

    public void update(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("origin", route.getOrigin());
        contentValues.put("destiny", route.getDestiny());
        String[] fieldValues = new String[1];
        fieldValues[0] = Integer.toString(route.getId());
        db.update("routes", contentValues, " id = ? ", fieldValues);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("routes", " id = ? ", new String[]{Integer.toString(id)});
        //db.execSQL("drop table if exists notes");
    }

    public List<Route> list() {
        List<Route> routes = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from routes order by id", null);
        if (result != null && result.getCount() > 0) {
            routes = new ArrayList<Route>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Route route = new Route();
                route.setId(result.getInt(0));
                route.setOrigin(result.getString(1));
                route.setDestiny(result.getString(2));
                routes.add(route);
                result.moveToNext();
            }
        }
        return routes;
    }
}
