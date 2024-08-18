package com.example.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MyService extends Service {

    public MyService() {
    }

    private NotificationManager notificationManager;

    // Example for the Binder
    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }

    // Called when Activity perform bindService() method
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Example for implement foreground service
        Log.d("MyService", "onCreate executed");

        // Example for Eventbus
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVICE, "Hello from Service using EventBus"));

        ///region NotificationManager
        // Lets notify the user from within the service
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new Notification.Builder(getApplicationContext(), MainActivity.id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setChannelId(MainActivity.id)
                .setContentTitle("MyService")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText("Notification from inside MyService")
                .setNumber(5)
                .build();

        notificationManager.notify(1, notification);
        ///endregion
    }

    @Override
    public void onDestroy() {
        //Unregister EventBus after stop service to avoid OOM (out-of-memory)
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

    //called when startService() method is executed
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    //method to process when receiving MessageEvent
    @Subscribe
    public void onMessageEventService(MessageEvent event) {
        if (event.type == MessageEvent.ACTIVITY)
            Log.d("MyService", "Activity Message Content: " + event.message);
    }
}
