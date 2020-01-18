package ketank.bloodbank.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import ketank.bloodbank.Fragments.User.NearbyActivity;
import ketank.bloodbank.R;

public class UserMainActivity extends AppCompatActivity {
   CardView users,hospitals;
    LocationTracker tracker;
    int REQUEST_LOCATION=5;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        users= (CardView) findViewById(R.id.card1);
        hospitals = (CardView) findViewById(R.id.card2);



        preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserMainActivity.this, NearbyActivity.class);
                startActivity(intent);

            }
        });

        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 showMap();
            }
        });

        getLocation();



    }

    void showMap(){
        if (ActivityCompat.checkSelfPermission(UserMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UserMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Intent intent = new Intent(UserMainActivity.this, MapsActivity.class);
            startActivity(intent);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMap();
            }
        }

        if (requestCode==2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                {

                                getLocation();}



            }
        }

    }


    void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
           final ProgressDialog dialog = new ProgressDialog(this);

            dialog.show();



            TrackerSettings settings =
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            .setUsePassive(true)
                            .setTimeBetweenUpdates(5*1000);

            tracker = new LocationTracker(this, settings) {
                @Override
                public void onLocationFound(Location location)
                {
                    dialog.dismiss();

                    if (location != null) {
                      Double  lat_all =location.getLatitude();
                      Double  long_all =location.getLongitude();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("myLat", ""+lat_all);
                        editor.putString("mylang", ""+long_all);

                        editor.apply();




                    }


                }

                @Override
                public void onTimeout() {

                    getLocation();

                }
            };

            tracker.startListening();

        }

    }

    public void LogOut(View view) {

        Intent intent=new Intent(UserMainActivity.this, LoginActivity.class);
        SharedPreferences.Editor editor= preferences.edit() ;
        editor.putBoolean("Userlogin",false);
        editor.putBoolean("BankLogin",false);
        editor.apply();
        startActivity(intent);
        finish();
    }
}
