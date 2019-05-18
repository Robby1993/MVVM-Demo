package com.nxccontrols.demomvvm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";
    public static boolean isRunning  = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BackgroundService.this, "running", Toast.LENGTH_SHORT).show();
                /*Here I want to do my task forever (reading from database & generate notifications)*/
            }
        }).start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
    }

}