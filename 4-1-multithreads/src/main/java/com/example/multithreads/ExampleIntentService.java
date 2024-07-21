package com.example.multithreads;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class ExampleIntentService extends IntentService {
    private static final String ACTION_EXAMPLE_START = "example.start";
    public static final String ACTION_EXAMPLE_END = "example.end";

    public static final String RESULT_PARAM = "result";
    public static final String RESULT_DURATION = "duration";

    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    public static void startExample(Context context) {
        Intent intent = new Intent(context, ExampleIntentService.class);
        intent.setAction(ACTION_EXAMPLE_START);
        context.startService(intent);
    }

    // Called when receiving Intent from other components
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EXAMPLE_START.equals(action)) {
                handleActionExample();
            }
        }
    }

    // the method to process the intent contains action of ACTION_EXAMPLE_START
    private void handleActionExample() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Communicate by broadcasting Intent
        Intent intent = new Intent();
        intent.setAction(ACTION_EXAMPLE_END);
        intent.putExtra(RESULT_PARAM, "IntentService created this message.");
        intent.putExtra(RESULT_DURATION, System.currentTimeMillis());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
