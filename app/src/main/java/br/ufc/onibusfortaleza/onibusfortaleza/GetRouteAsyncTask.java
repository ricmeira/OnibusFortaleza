package br.ufc.onibusfortaleza.onibusfortaleza;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufc.onibusfortaleza.onibusfortaleza.model.ApiResponse;
import br.ufc.onibusfortaleza.onibusfortaleza.model.Legs;
import br.ufc.onibusfortaleza.onibusfortaleza.model.Steps;
import br.ufc.onibusfortaleza.onibusfortaleza.model.TransitDetails;

/**
 * Created by eduardo on 16-06-22.
 */
public class GetRouteAsyncTask extends AsyncTask<String, String, String> {

    ApiResponse routes;
    GoogleMap map;
    ArrayAdapter<Route> adapter;
    String origin;
    String dest;
    ProgressBar progressBar;

    public GetRouteAsyncTask(GoogleMap map, ArrayAdapter adapter, ProgressBar progressBar) {
        this.map = map;
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    protected String doInBackground(String... params) {
        try {
            origin = params[0];
            dest = params[1];
            String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+" fortaleza&destination="+dest+" fortaleza&alternatives=true&mode=transit&key=AIzaSyAEXVH95CyyTHZE9dle3My_2J_yyo0xcxo";
            directionsUrl = directionsUrl.replace(" ","%20");

            //connection
            URL url = new URL(directionsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.connect();

            //read content
            InputStream is = conn.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = null;
            String content = "";
            while ((data = reader.readLine()) != null) {
                content += data + "\n";
            }
            content.replace(" ","%20");
            parseJson(content);
            System.out.println("JSON CONTENT:\n" + content);

            //return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String result) {
    }

    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.INVISIBLE);

        if(routes.getRoutes().size() > 0) {
            Log.e("POLYLINE", routes.getRoutes().get(0).getOverview_polyline().getPoints());
            adapter.clear();
            adapter.notifyDataSetChanged();

            for(int i = 0; i < routes.getRoutes().size(); i++) {
                Route route = new Route();
                List<TransitDetails> busList = new ArrayList<TransitDetails>();

                List<String> busRoutes = new ArrayList<String>();
                // Cada Leg corresponde a uma rota diferente. Uma Leg pode conter 1 ou mais ônibus
                for(Legs l : routes.getRoutes().get(i).getLegs()) {
                    for(Steps s : l.getSteps()) {
                        if(s.getTransit_details() != null) {
                            busList.add(s.getTransit_details());

                            //TODO: adiciona a rota do onibus na polyline
                            //busRoutes.add(s.getPolyline().getPoints());
                        }
                    }
                }

                String busName = "";
                for (int j = 0; j < busList.size(); j++) {
                    TransitDetails b = busList.get(j);
                    busName += b.getLine().getShort_name() + " - " + b.getLine().getName();

                    if(j != busList.size() - 1 )
                        busName += " -> ";
                }

                // mostrar apenas o nome do ônibus no spinner (sem destino e origem)
                route.setBusName(busName);
                route.setRoute(routes.getRoutes().get(i).getOverview_polyline().getPoints());
                adapter.add(route);
            }


            adapter.notifyDataSetChanged();

            map.clear();
            map.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(adapter.getItem(0).getRoute())));
        }
        //map.addPolyline(new PolylineOptions().addAll(PolyUtil.decode("b_xUbn~iFg@|Bw@bEoEaAy@Q_A`EOh@o@rCaAvE_AbEERzGxAvBd@dDt@~GzAnE~@nCl@j@NbFhAtBd@p@N~D|@tA\\RDfB`@t@NxIlBi@`CUjAeAzEcAtES`Ao@vCa@nBi@|BGZy@rDaAtEcArE[tAe@|B_AjEcArEaArEm@pCU~@gA~ECPw@vDEPk@lC{@xDg@hCWfAS~@w@rDrCn@x@RzCr@q@tCa@dBi@hC_A~DXFYGgCrIuCeCgCoBc@pBm@lCw@rDGT{@xDCPxIhBcAnEaAzEeBhEcAtEYpAQr@o@`C{Cl@aD\\]BFb@lArJN~@JdA^pCNRVjBXtBAn@Fd@r@zF^|C`@bD`@dD\\bDNPThB@FRzAAr@vAvKd@hDLTJz@@JJz@A`@@Lj@xEl@~EdAlIP|ALJPrA")));
    }

    protected void parseJson(String json){
        Gson gson = new Gson();
        routes = gson.fromJson(json, ApiResponse.class);
    }
}
