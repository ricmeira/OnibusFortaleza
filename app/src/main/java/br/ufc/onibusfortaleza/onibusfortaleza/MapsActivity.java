package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private RouteDAO routeDAO;
    private Spinner spinnerBusOptions;
    private ArrayList<Route> busOption;
    private ArrayAdapter<Route> spinnerAdapter;
    private Route rotaAtual = null;
    private ProgressBar progressBar;
    private EditText origin;
    private EditText destiny;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        origin = (EditText)findViewById(R.id.editTextOrigin);
        destiny = (EditText)findViewById(R.id.editTextDestiny);
        destiny.requestFocus();

        routeDAO = new RouteDAO(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();


        busOption = new ArrayList<Route>();
        spinnerBusOptions = (Spinner) findViewById(R.id.spinnerBusOptions);
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = new ArrayAdapter<Route>(this, android.R.layout.simple_spinner_item, busOption);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown);
        // Apply the adapter to the spinner
        spinnerBusOptions.setAdapter(spinnerAdapter);


        //get Intent History
        Intent intent = getIntent();
        if(intent.getStringExtra("bus") != null){
            Route rota = new Route();
            String dest = intent.getStringExtra("destiny");
            String ori = intent.getStringExtra("origin");
            String route = intent.getStringExtra("route");
            String bus = intent.getStringExtra("bus");

            rota.setBusName(bus);
            rota.setRoute(route);
            rota.setOrigin(ori);
            rota.setDestiny(dest);

            spinnerAdapter.clear();
            spinnerAdapter.add(rota);

            origin.setText(ori.toString());
            destiny.setText(dest.toString());

        }

        //Spinner Listener
        spinnerBusOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                rotaAtual = spinnerAdapter.getItem(position);
                mMap.clear();

                Log.e("OnItem",rotaAtual.toString());
                mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(rotaAtual.getRoute())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        // Campo de texto Destino aciona a busca de rotas
        destiny.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyboard();
                    calcularRota();
                    handled = true;
                }
                return handled;
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney2"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(latLng).title("Posição atual"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void calcularRota() {

        progressBar.setVisibility(View.VISIBLE);
        GetRouteAsyncTask getRouteAsyncTask = new GetRouteAsyncTask(mMap, spinnerAdapter, progressBar);
        getRouteAsyncTask.execute(origin.getText().toString(), destiny.getText().toString());

    }


    public void history(View view){

        //ACESSANDO VIA DAO
        //List<Route> routes = routeDAO.list();

        //ACESSANDO VIA CONTENTPROVIDER
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

        //IGUAL para ambos os casos: modificar o if para routes!=null caso usando DAO
        if(routes.size() > 0) {
            Intent i = new Intent();
            i.setAction("br.ufc.dc.dspm.action.history");
            i.setComponent(null);
            i.addCategory("br.ufc.dc.dspm.category.Categoria");
            i.setComponent(null);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Nenhuma rota salva.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Salva a rota selecionada (rotaAtual) no histórico
     *
     * @param view
     */
    public void save(View view){
        if(rotaAtual != null){

            //USANDO DAO
            /*
            routeDAO.create(rotaAtual);
            Toast.makeText(this,"A rota foi salva com sucesso",Toast.LENGTH_SHORT).show();
            */

            // Rescuperar origem e destino antes de salvar
            rotaAtual.setOrigin(origin.getText().toString());
            rotaAtual.setDestiny(destiny.getText().toString());

            //USANDO CONTENTPROVIDER
            ContentValues values = new ContentValues();
            values.put(RouteProvider.ORIGIN, rotaAtual.getOrigin());
            values.put(RouteProvider.DESTINY, rotaAtual.getDestiny());
            values.put(RouteProvider.BUS, rotaAtual.getBusName());
            values.put(RouteProvider.ROTA, rotaAtual.getRoute());

            Uri uri = getContentResolver().insert(RouteProvider.CONTENT_URI, values);
            Toast.makeText(this,"A rota foi salva com sucesso!",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
