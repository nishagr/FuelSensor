package ketank.bloodbank.Firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import ketank.bloodbank.R;

/**
 * Created by Ketan-PC on 12/15/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences preferences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        preferences= getSharedPreferences("mypref", Context.MODE_PRIVATE);
        
        String data = remoteMessage.getData().get("data");
        
        // ...

        // TODO(developer): Handle FCM messages here.r

        CreateNotification(data,"Life Stream");




    }





    void CreateNotification(String text,String title){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(title==null){
            title="Life Stream";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */

            // Sets an ID for the notification, so it can be updated.
            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence CHANNEL_Name = "channel - name"; // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_Name, importance);

            mNotificationManager.createNotificationChannel(channel);

            // Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(this)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{250})
                    .setWhen(System.currentTimeMillis())
                    .build();

            // Issue the notification.
            Random rand = new Random();
            mNotificationManager.notify(rand.nextInt(10000) , notification);

        } else {
            Notification notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{250})
                    .setWhen(System.currentTimeMillis())
                    .build();


            Random rand = new Random();

            mNotificationManager.notify(rand.nextInt(10000), notificationBuilder);

        }



    }
}
