package ketank.bloodbank.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ketank.bloodbank.Adapters.BankListAdapter;
import ketank.bloodbank.Models.BloodBank;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.Other.ClickListener;
import ketank.bloodbank.Other.RecyclerTouchListener;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,RoutingListener {


    static final int Request_Camera  = 2 ;
    private File imageFile;
    private GoogleMap mMap;
    ProgressDialog dialog;
    RecyclerView recycle;
    private List<Polyline> polylines=new ArrayList<>();
    SharedPreferences preferences;
    final ArrayList<BloodBank> bloodBanks = new ArrayList<>();
    LatLng source;
    MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dialog=new ProgressDialog(this);
preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);
        recycle= (RecyclerView) findViewById(R.id.recycle);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        spinner.setItems("All", "O+","O-","A+", "A-","B+","B-","AB+","AB-");

       source= new LatLng(Double.parseDouble(preferences.getString("myLat","")),Double.parseDouble(preferences.getString("mylang","")));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycle.setLayoutManager(layoutManager);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dialog.setTitle("Getting Data...");
       dialog.show();

        recycle.addOnItemTouchListener(new RecyclerTouchListener(this, recycle, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {


                    String mylat = String.format("%.4f", source.latitude);
                    String mylang = String.format("%.4f", source.longitude);
                    String placelat = String.format("%.4f", Double.parseDouble(bloodBanks.get(position).getLat()));
                    String placelang = String.format("%.4f", Double.parseDouble(bloodBanks.get(position).getLang()));


                  /*  Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + mylat + "," + mylang + "&daddr=" + placelat + "," + placelang));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);*/
                Routing routing = new Routing.Builder()
                        .key("AIzaSyBPs9eolVNUuDJgOz1M8zn7GozvShe1Ghk")
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(MapsActivity.this)
                        .waypoints(new LatLng(Double.parseDouble(mylat),Double.parseDouble(mylang)),new LatLng(Double.parseDouble(placelat),Double.parseDouble(placelang)))
                        .build();

                routing.execute();
                }catch (Exception e){
                    Log.d("ffw",e.toString());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position==0){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();


                    BankListAdapter adapter = new BankListAdapter(bloodBanks,MapsActivity.this);
                     recycle.setAdapter(adapter);



                }    else   if(position==1){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getOps()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                }
                else   if(position==2){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getOng()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==3){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getAps()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==4){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getAng()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==5){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getBps()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==6){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getBng()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==7){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getAbps()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                } else   if(position==8){
                    final ArrayList<BloodBank> bloodBanks1 = new ArrayList<>();

                    for(BloodBank bank:bloodBanks){
                        if(bank.getAbng()>0) {
                            bloodBanks1.add(bank);
                        }
                    }

                    BankListAdapter adapter = new BankListAdapter(bloodBanks1,MapsActivity.this);
                    recycle.setAdapter(adapter);



                }

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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

           googleMap.setOnMarkerClickListener(this);



        // Add a marker in Sydney and move the camera

       getData(All_urls.values.GetAllBloodBanks);




    }




    private void getData(String url) {

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray p) {
                        // display response
                        Log.d("Response", p.toString());

                        dialog.dismiss();
                        try {



                            if (p.length() > 0) {

                                for (int i=0;i<p.length();i++){
                                    BloodBank mapModel = new BloodBank();

                                   JSONObject post = p.getJSONObject(i);
                                   mapModel.setId(post.getString("Id"));
                                    mapModel.setImgUrl(post.getString("ImgUrl"));
                                    mapModel.setLat(post.getString("Lat"));
                                    mapModel.setLang(post.getString("Lang"));
                                    mapModel.setName(post.getString("Name"));
                                    mapModel.setPlace(post.getString("Place"));
                                    mapModel.setOps(post.getInt("Ops"));
                                    mapModel.setOng(post.getInt("Ong"));
                                    mapModel.setAng(post.getInt("Ang"));
                                    mapModel.setAps(post.getInt("Aps"));
                                    mapModel.setAbps(post.getInt("ABps"));
                                    mapModel.setAbng(post.getInt("ABng"));
                                    mapModel.setBps(post.getInt("Bps"));
                                    mapModel.setBng(post.getInt("Bng"));


                                    bloodBanks.add(mapModel);

                                    LatLng place = new LatLng(Double.parseDouble(mapModel.getLat()), Double.parseDouble(mapModel.getLang()));
                                    mMap.addMarker(new MarkerOptions().position(place).title("\nName:"+mapModel.getName()));


                                }

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(bloodBanks.get(0).getLat()),Double.parseDouble(bloodBanks.get(0).getLang())),12));

                                BankListAdapter adapter = new BankListAdapter(bloodBanks,MapsActivity.this);
                                recycle.setAdapter(adapter);

                            }


                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            dialog.dismiss();



                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");
                        dialog.dismiss();


                    }
                }
        );


        AppController.getInstance().addToRequestQueue(getRequest);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(),marker.getTitle(),Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(source,16);
        mMap.addMarker(new MarkerOptions().position(source));




        mMap.moveCamera(center);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.


            //In case of more than 5 alternative routes


            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.colorPrimary));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(arrayList.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);




    }

    @Override
    public void onRoutingCancelled() {

    }
}
