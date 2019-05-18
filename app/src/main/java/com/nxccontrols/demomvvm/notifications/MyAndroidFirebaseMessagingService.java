package com.nxccontrols.demomvvm.notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nxccontrols.demomvvm.MainActivity;
import com.nxccontrols.demomvvm.R;

public class MyAndroidFirebaseMessagingService extends FirebaseMessagingService {

    public String TAG = "MyAndroidFirebaseMessagingService";


    @Override
    public void onNewToken(String refreshedToken) {
        Log.d(TAG, "token" + refreshedToken);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("token", refreshedToken).apply();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "notification" + remoteMessage.getNotification());

        String title = "";
        String message = "";
        String link = null;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();

        }

        if (remoteMessage.getData().size() > 0) {
            try {
                link = remoteMessage.getData().get("link");
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("link", link);

                showNotification(title, message, resultIntent);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

            Log.d(TAG, "deepLink: " + link);
        }

    }

    private void showNotification(String message, String title, Intent resultIntent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = null;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            notificationBuilder = new NotificationCompat.Builder(this, "0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, "0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(
                    "0",
                    "CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationBuilder.setChannelId("0");
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 3, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, notificationBuilder.build());

    }
}