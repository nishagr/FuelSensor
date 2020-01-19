package ketank.bloodbank.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ketank.bloodbank.Adapters.BankListAdapter;
import ketank.bloodbank.Models.BloodBank;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.Other.ClickListener;
import ketank.bloodbank.Other.RecyclerTouchListener;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;
import ketank.bloodbank.rating;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,RoutingListener, com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    /*static final int RequeSharedPrefst_Camera  = 2 ;
    private File imageFile;*/

    private Location currentLocation;
    private Marker currentLocationMarker;
    private LocationManager mlocationManager;
    private float start_rotation;
    String mlat,mlong;
    Float distance = Float.valueOf(1000000);

    String placelat,placelang;

    private Polyline polyline;

    private GoogleMap mMap;
    ProgressDialog dialog;
    RecyclerView recycle;
    private List<Polyline> polylines = new ArrayList<>();
    SharedPreferences preferences;
    final ArrayList<BloodBank> bloodBanks = new ArrayList<>();
    LatLng userLatLng;

    private LocationManager locationManager;
    private boolean isPermission;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mlocationRequest;

    private long UPDATE_INTERVAL = 20;
    private long FASTEST_INTERVAL = 2000;

    String latitude,longitude;

    LatLng latLngDestination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        recycle =  findViewById(R.id.recycle);



        /*userLatLng = new LatLng(Double.parseDouble(preferences.getString("myLat", "")), Double.parseDouble(preferences.getString("mylang", "")));*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycle.setLayoutManager(layoutManager);


        if (requestSinglePermission()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkLocation();
        }


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
                    LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
                    latLngDestination = new LatLng(Double.parseDouble(bloodBanks.get(position).getLat()),Double.parseDouble(bloodBanks.get(position).getLang()));

                    String mylat = String.format("%.4f",mLocation.getLatitude());
                    String mylang = String.format("%.4f", mLocation.getLongitude());
                    placelat = String.format("%.4f", Double.parseDouble(bloodBanks.get(position).getLat()));
                    placelang = String.format("%.4f", Double.parseDouble(bloodBanks.get(position).getLang()));


                  /*  Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + mylat + "," + mylang + "&daddr=" + placelat + "," + placelang));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);*/

                    /*GoogleDirection.withServerKey("AIzaSyBPs9eolVNUuDJgOz1M8zn7GozvShe1Ghk")
                            .from(latLng)
                            .to(latLngDestination)
                            .transportMode(TransportMode.DRIVING)
                            .alternativeRoute(true)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction) {
                                    if(direction.isOK()){
                                    }
                                }
                                @Override
                                public void onDirectionFailure(Throwable t) {
                                }
                            });*/

                    Routing routing = new Routing.Builder()
                            .key("AIzaSyBPs9eolVNUuDJgOz1M8zn7GozvShe1Ghk")
                            .travelMode(Routing.TravelMode.DRIVING)
                            .withListener(MapsActivity.this)
                            .waypoints(latLng, new LatLng(Double.parseDouble(placelat), Double.parseDouble(placelang)))
                            .build();



                    routing.execute();
                } catch (Exception e) {
                    Log.d("ffw", e.toString());
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
        if(userLatLng != null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(userLatLng);
            markerOptions.title("I am here");
            currentLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,16));
        }

        googleMap.setOnMarkerClickListener(this);


        getData(All_urls.values.GetAllBloodBanks);


    }


    private void getData(String url) {

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray p) {
                        // display response
                        Log.d("Response", p.toString());

                        dialog.dismiss();
                        try {


                            if (p.length() > 0) {

                                for (int i = 0; i < p.length(); i++) {
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

                                    Location dest = new Location("a");
                                    dest.setLongitude(Double.parseDouble(post.getString("Lang")));
                                    dest.setLatitude(Double.parseDouble(post.getString("Lat")));
                                    Float d = mLocation.distanceTo(dest);
                                    if(d<distance){
                                        distance=d;
                                        mlat=post.getString("Lat");
                                        mlong=post.getString("Lang");

                                    }


                                    bloodBanks.add(mapModel);

                                    LatLng place = new LatLng(Double.parseDouble(mapModel.getLat()), Double.parseDouble(mapModel.getLang()));
                                    mMap.addMarker(new MarkerOptions().position(place).title("\nName:" + mapModel.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


                                }
if(userLatLng!=null){
                                Routing routing = new Routing.Builder()
                                        .key("AIzaSyBPs9eolVNUuDJgOz1M8zn7GozvShe1Ghk")
                                        .travelMode(Routing.TravelMode.DRIVING)
                                        .withListener(MapsActivity.this)
                                        .waypoints(userLatLng, new LatLng(Double.parseDouble(placelat), Double.parseDouble(placelang)))
                                        .build();



                                routing.execute();}

                                BankListAdapter adapter = new BankListAdapter(bloodBanks, MapsActivity.this);
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

        Intent i=new Intent(this, rating.class);
        i.putExtra("message",marker.getId());

        startActivity(i);
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


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,16));


        if (polylines.size() > 0) {
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
        polyline = mMap.addPolyline(polyOptions);
        polylines.add(polyline);





    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onLocationChanged(Location location) {
        location = mLocation;
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        LatLng latLngNew = new LatLng(location.getLatitude(),location.getLongitude());


        if(polyline!=null) {
            if (!PolyUtil.isLocationOnPath(userLatLng, polyline.getPoints(), true, 20)) {
                polyline.remove();
                Routing routing = new Routing.Builder()
                        .key("AIzaSyBPs9eolVNUuDJgOz1M8zn7GozvShe1Ghk")
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(MapsActivity.this)
                        .waypoints(latLngNew, new LatLng(Double.parseDouble(placelat), Double.parseDouble(placelang)))
                        .build();

                routing.execute();


            }
        }

        if (currentLocationMarker != null) {

            moveMarker(currentLocationMarker, location);
            /*rotateMarker(currentLocationMarker,location.getBearing(),start_rotation);*/
        } else {
            userLatLng = new LatLng(location.getLatitude(),location.getLongitude());

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    public void moveMarker(final Marker myMarker, final Location finalPosition) {
        final LatLng startPosition = myMarker.getPosition();

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + (finalPosition.getLatitude()) * t,
                        startPosition.longitude * (1 - t) + (finalPosition.getLongitude()) * t);
                myMarker.setPosition(currentPosition);
                // myMarker.setRotation(finalPosition.getBearing());


                // Repeat till progress is completeelse
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                    // handler.postDelayed(this, 100);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
            }
        });
    }

    private boolean checkLocation() {

        if (!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();

    }

    private void showAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Please Enable Location")
                .setPositiveButton("LocationSettings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();

    }

    private boolean isLocationEnabled() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        return isPermission;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {

        mlocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    public void rotateMarker(final Marker marker, final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = st;
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;


                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                start_rotation = -rot > 180 ? rot / 2 : rot;
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

}