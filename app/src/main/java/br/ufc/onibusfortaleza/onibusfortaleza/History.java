package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class History extends AppCompatActivity {
    private RouteDAO routeDAO;
    ArrayList<Route> route=new ArrayList<>();
    ArrayAdapter<Route> tA;
    ListView listRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //USANDO DAO
        /*
        routeDAO = new RouteDAO(this);
        final List<Route> routes = routeDAO.list();
        StringBuffer buffer = new StringBuffer();
        */
        //USANDO CONTENT PROVIDER
        final List<Route> routes = new ArrayList<Route>();
        StringBuffer buffer = new StringBuffer();
        String URI = RouteProvider.URI;
        Uri routesURI = Uri.parse(URI);
        Cursor cursor = getContentResolver().query(routesURI, null, null, null, RouteProvider.ID);
        if (cursor.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(cursor.getInt(cursor.getColumnIndex(RouteProvider.ID)));
                route.setOrigin(cursor.getString(cursor.getColumnIndex(RouteProvider.ORIGIN)));
                route.setDestiny(cursor.getString(cursor.getColumnIndex(RouteProvider.DESTINY)));
                route.setBusName(cursor.getString(cursor.getColumnIndex(RouteProvider.BUS)));
                route.setRoute(cursor.getString(cursor.getColumnIndex(RouteProvider.ROTA)));
                routes.add(route);
            } while (cursor.moveToNext());
        }

        //SEMELHANTE A AMBOS
        listRoute=(ListView)findViewById(R.id.listView);
        tA = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routes);
        listRoute.post(new Runnable() {
            @Override
            public void run() {
                listRoute.setAdapter(tA);
                listRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int i = 0;
                        Iterator<Route> it = routes.iterator();
                        Route r = new Route();
                        while (i <= position) {
                            r = it.next();
                            i++;
                        }
                        Intent intent = new Intent();
                        intent.setAction("br.ufc.dc.dspm.action.maps");
                        intent.setComponent(null);
                        intent.addCategory("br.ufc.dc.dspm.category.Cate");
                        intent.setComponent(null);
                        intent.putExtra("id", r.getId());
                        intent.putExtra("origin", r.getOrigin());
                        intent.putExtra("destiny", r.getDestiny());
                        intent.putExtra("bus", r.getBusName());
                        intent.putExtra("route", r.getRoute());
                        startActivity(intent);
                    }
                });
            }
        });

    }

    public void voltar(View view){
        finish();
    }

}
