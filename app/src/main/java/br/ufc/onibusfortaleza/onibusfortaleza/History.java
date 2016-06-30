package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.Intent;
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

        routeDAO = new RouteDAO(this);
        final List<Route> notes = routeDAO.list();
        StringBuffer buffer = new StringBuffer();
        listRoute=(ListView)findViewById(R.id.listView);
        tA = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeDAO.list());
        listRoute.post(new Runnable() {
            @Override
            public void run() {
                listRoute.setAdapter(tA);
                listRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int i = 0;
                        Iterator<Route> it = notes.iterator();
                        Route r = new Route();
                        while (i <= position) {
                            r = it.next();
                            i++;
                        }
                        Intent intent = new Intent();
                        intent.setAction("br.ufc.dc.dspm.action.main");
                        intent.setComponent(null);
                        intent.addCategory("br.ufc.dc.dspm.category.CategoriaMain");
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
