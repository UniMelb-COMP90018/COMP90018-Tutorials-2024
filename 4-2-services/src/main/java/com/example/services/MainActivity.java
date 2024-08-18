package com.example.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.services.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    /// Notification Channel ///
    public static String id = "channel_01";
    NotificationManager notificationManager;
    ActivityResultLauncher<String[]> rpl;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};

    private MyService.DownloadBinder downloadBinder;
    boolean bound = false;

    // To ServiceConnection for monitoring the change of communication between Service and Activity
    private ServiceConnection connection = new ServiceConnection() {

        // called when disconnected to Service
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }

        // called when connecting to Service
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
            bound = true;
        }
    };

    /**
     * Create a notification channel to submit notifications from the application
     */
    private void createNotificationChannel() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setShowBadge(true);
        channel.setVibrationPattern(new long[]{100, 100, 100});

        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Check to confirm that we have the necessary permissions to show notifications (in > Tiramisu)
     * @return if all permissions are granted
     */
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// region NotificationManager
        // for notifications permission now required in api 33
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> isGranted) {
                        boolean granted = true;
                        for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                            Log.d("MainActivity", x.getKey() + " is " + x.getValue());
                            if (!x.getValue()) granted = false;
                        }
                        if (granted)
                            Log.d("MainActivity", "Permissions granted for api 33+");
                    }
                }
        );

        createNotificationChannel();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS);
            }
        }
        /// endregion

        /// region onClickListeners
        binding.startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, MyService.class);
                startService(startIntent); // Start Service
                Toast.makeText(binding.getRoot().getContext(), "Service starting", Toast.LENGTH_SHORT).show();
            }
        });

        binding.stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopIntent = new Intent(MainActivity.this, MyService.class);
                stopService(stopIntent); // Stop Service
                makeNotification("Stopped Service", 2);
                Toast.makeText(binding.getRoot().getContext(), "Service stopping", Toast.LENGTH_SHORT).show();
            }
        });

        binding.bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bindIntent = new Intent(MainActivity.this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE); // Bind Service
                Toast.makeText(binding.getRoot().getContext(), "Binding Service", Toast.LENGTH_SHORT).show();
            }
        });

        binding.unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bound) {
                    unbindService(connection); // Unbind Service
                    Toast.makeText(binding.getRoot().getContext(), "Unbinding Service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /// endregion

        // Register for EventBus Library
        EventBus.getDefault().register(this);
    }

    /**
     * A reusable method to construct necessary notifications
     */
    public void makeNotification(String message, int msgCount) {

        Notification notification = new Notification.Builder(getApplicationContext(), MainActivity.id)
                .setSmallIcon(R.mipmap.ic_launcher)     // set the small icon <REQUIRED>
                .setContentTitle("Service")             // title of the notification <REQUIRED>
                .setContentText(message)                // message of the notification <REQUIRED>
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())    // when did the notification occur
                .setChannelId(MainActivity.id)          // the notification channel to use
                .setAutoCancel(true)                    // allow the message to be cancelled
                .build();

        //Show the notification
        notificationManager.notify(msgCount, notification);
    }

    @Override
    protected void onStop() {
        // Unregister to avoid Android OOM (out-of-memory)
        EventBus.getDefault().unregister(this);
        super.onStop();
        unbindService(connection);
        bound = false;
    }

    //  Method to process when receiving MessageEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventActivity(MessageEvent event) {
        if (event.type == MessageEvent.SERVICE) {
            Log.d("MyService", "Service Message Content: " + event.message);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.ACTIVITY, "Hello from Activity!"));
        }
    }

}
