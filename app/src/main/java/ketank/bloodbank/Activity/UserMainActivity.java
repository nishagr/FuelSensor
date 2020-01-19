package ketank.bloodbank.Activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.scwang.wave.MultiWaveHeader;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import io.reactivex.disposables.Disposable;
import ketank.bloodbank.Fragments.User.NearbyActivity;
import ketank.bloodbank.FuelReadingApi.ApiClient;
import ketank.bloodbank.FuelReadingApi.ApiInterface;
import ketank.bloodbank.Models.FuelRange;
import ketank.bloodbank.Other.SharedPref;
import ketank.bloodbank.R;
import me.itangqi.waveloadingview.WaveLoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMainActivity extends AppCompatActivity {

    //MultiWaveHeader fuel_wave;
    WaveLoadingView waveLoadingView;
    CardView users, hospitals;
    LocationTracker tracker;
    int REQUEST_LOCATION = 5;
    FuelRange fuelRange;
    SharedPref fuelPref;
    SharedPreferences preferences;

    //Disposable disposable;

    @Override
    protected void onResume() {
        super.onResume();
        startFuelRangingElapseThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        fuelPref = new SharedPref();
        //users= (CardView) findViewById(R.id.card1);
        hospitals = (CardView) findViewById(R.id.card2);

        //fuel gauge animation
        waveLoadingView = findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(50);
        waveLoadingView.setAnimDuration(2000);


        waveLoadingView.setOnClickListener(v -> {


            Toast.makeText(UserMainActivity.this, "clicked :", Toast.LENGTH_LONG).show();


        });

        //for testing in app notification
        /*for(int i=1;i<101;i++)
        {
            if(i==10)
            {
                showNotification("FUEL SENSOR","You are low on fuel");

            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

//        users.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(UserMainActivity.this, NearbyActivity.class);
//                startActivity(intent);
//
//            }
//        });

        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMap();
            }
        });

        getLocation();


        //api calling after every 5sec

        startFuelRangingElapseThread();


    }

    private void startFuelRangingElapseThread() {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    loadJson(1);

                    try {
                        synchronized (this) {
                            Thread.sleep(10000);
                            notify();
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //CALL FOR FUELDATA
    public void loadJson(final int s) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<FuelRange> call = apiInterface.getFuelRange(s);

        call.enqueue(new Callback<FuelRange>() {
            @Override
            public void onResponse(Call<FuelRange> call, Response<FuelRange> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Tambe :", response.body().getFuelVal() + " " + response.body().getCfl());
                    //fuelRange =response.body();
//                    fuelPref.addFuelConsumption(UserMainActivity.this,response.body());

                    int cfl = response.body().getCfl();
                    waveLoadingView.setProgressValue(cfl);
                    if (cfl < 25) {
                        waveLoadingView.setWaveColor(Color.RED);
                        showNotification("FUEL SENSOR", "You are low on fuel");
                    } else waveLoadingView.setWaveColor(Color.GREEN);
                }
            }

            @Override
            public void onFailure(Call<FuelRange> call, Throwable t) {

                Toast.makeText(UserMainActivity.this, "Network failure", Toast.LENGTH_LONG).show();

            }
        });
    }

    void showMap() {
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

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMap();
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                {

                    getLocation();
                }


            }
        }

    }


    //Notification inapp
    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
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
                            .setTimeBetweenUpdates(5 * 1000);

            tracker = new LocationTracker(this, settings) {
                @Override
                public void onLocationFound(Location location) {
                    dialog.dismiss();

                    if (location != null) {
                        Double lat_all = location.getLatitude();
                        Double long_all = location.getLongitude();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("myLat", "" + lat_all);
                        editor.putString("mylang", "" + long_all);

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

        Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Userlogin", false);
        editor.putBoolean("BankLogin", false);
        editor.apply();
        startActivity(intent);
        finish();
    }
}
