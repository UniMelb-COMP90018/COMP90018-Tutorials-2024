package com.example.multithreads;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.multithreads.databinding.ActivityMainBinding;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static final int UPDATE_TEXT_RUNNABLE = 1;
    public static final int UPDATE_TEXT_THREAD = 2;
    public static final int UPDATE_TEXT_FUTURETASK = 3;
    public static final int UPDATE_TEXT_HANDLER_SEND = 4;
    public static final int UPDATE_TEXT_HANDLER_RECEIVE = 5;

    private TextView text;
    private TextView buttonPressed;
    private FutureTask<String> future;
    private HandlerThread exampleHandlerThread;
    private BroadcastReceiver broadcastReceiver;


    private long actionedTime, elapsedTime;

    private LocalBroadcastManager localBroadcastManager;

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @SuppressLint("DefaultLocale")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT_RUNNABLE: {
                    elapsedTime = (System.currentTimeMillis() - actionedTime) / 1000;
                    text.setText(format("Message Received from Runnable Interface after %d seconds", elapsedTime));
                    break;
                }
                case UPDATE_TEXT_THREAD: {
                    elapsedTime = (System.currentTimeMillis() - actionedTime) / 1000;
                    text.setText(format("Message Received from Thread Class after %d seconds", elapsedTime));
                    break;
                }
                case UPDATE_TEXT_FUTURETASK: {
                    text.setText(format("%s Message received after %d seconds", msg.obj, elapsedTime));
                    break;
                }
                case UPDATE_TEXT_HANDLER_RECEIVE: {
                    text.setText(format("Message Received from HandlerThread Class after %d seconds", elapsedTime));
                    exampleHandlerThread.quit();
                    break;
                }
                default:
                    break;
            }
            return true;
        }
    });

    private void updateAfterClick(String v)
    {
        buttonPressed.setText(getResources().getString(R.string.button_press) + v);
        actionedTime = System.currentTimeMillis();
        text.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        text = binding.text;
        buttonPressed = binding.buttonPress;

        binding.crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAfterClick(" A");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Future Tasks", "Thread(Done method): " + Thread.currentThread().getId());
                        Log.d("Future Tasks", "TextView Object Reference(Done method): " + text);
                        /// Let's access UI thread components from the thread....
                        elapsedTime = (System.currentTimeMillis() - actionedTime) / 1000;
                        text.setText("Crash Happens after " + elapsedTime + " seconds");
                    }
                }).start();

            }
        });

        binding.changeTextRunnable.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  updateAfterClick(" B");
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(2000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                          // Send the Message object towards UI threads
                          Message message = new Message();
                          message.what = UPDATE_TEXT_RUNNABLE;
                          handler.sendMessage(message);
                      }
                  }).start();
              }
        });

        binding.changeTextThreads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAfterClick(" C");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Send the Message object towards UI threads
                        Message message = new Message();
                        message.what = UPDATE_TEXT_THREAD;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });

        binding.changeTextFutureTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAfterClick(" D");
                ExecutorService executor = new ScheduledThreadPoolExecutor(2);
                // Implement Callable for futureTask, detail explained in Tutorial slides
                future = new FutureTask<String>(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(2000);
                        return "This message was created in the FutureTask thread!";
                    }
                })
                        //  Called when the worker thread finish their work (from isDone() of Future Class)
                {
                    @Override
                    protected void done() {
                        String textContent = null;
                        try {
                            textContent = future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = UPDATE_TEXT_FUTURETASK;
                        message.obj = textContent;
                        handler.sendMessage(message);
                    }
                };
                executor.execute(future);
            }
        });

        /**
         * Async Task was deprecated in API 30 : https://developer.android.com/reference/android/os/AsyncTask
         *
         * This implementation uses no reference point, this stops the task from being Garbage Collected if the activity is destroyed, resulting in a memory leak.
         * This example is provided for historical purposes, but you should use one of the other threading examples that are still supported.
         */
        binding.changeTextAsynctask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateAfterClick(" E");
                new AsyncTask<Integer, Integer, String>() {
                    @Override
                    protected String doInBackground(Integer... integers) {
                        for (int i = 0; i < integers[0]; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            publishProgress(i);
                        }
                        elapsedTime = (System.currentTimeMillis() - actionedTime) / 1000;
                        return "Message Received from AsyncTask after " + elapsedTime + " seconds";
                    }

                    @Override
                    protected void onProgressUpdate(Integer... progress) {
                        text.setText("Progress: " + progress[0] * 10 + "%");
                        super.onProgressUpdate(progress);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        text.setText(result);
                    }
                }.execute(10);
            }
        });

        binding.changeTextHandlerThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAfterClick(" F");
                exampleHandlerThread = new HandlerThread("example");
                exampleHandlerThread.start();

                Message msg = new Message();
                msg.what = UPDATE_TEXT_HANDLER_SEND;

                new Handler(exampleHandlerThread.getLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        switch (message.what) {
                            case UPDATE_TEXT_HANDLER_SEND: {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = UPDATE_TEXT_HANDLER_RECEIVE;
                                handler.sendMessage(msg);
                                break;
                            }
                        }
                        return false;
                    }
                }).sendMessage(msg);
            }
        });

        binding.changeTextIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateAfterClick(" G");
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ExampleIntentService.ACTION_EXAMPLE_END);
                localBroadcastManager = LocalBroadcastManager.getInstance(binding.getRoot().getContext());
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String message = intent.getStringExtra(ExampleIntentService.RESULT_PARAM);
                        elapsedTime = (intent.getLongExtra(ExampleIntentService.RESULT_DURATION, System.currentTimeMillis()) - actionedTime) / 1000;
                        text.setText(format("%s Received in %d seconds.", message, elapsedTime));
                        localBroadcastManager.unregisterReceiver(broadcastReceiver);
                    }
                };
                localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
                ExampleIntentService.startExample(binding.getRoot().getContext());
            }
        });

        binding.changeTextEventBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAfterClick(" H");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new MessageEvent(
                                "Message Received from Thread through EventBus after " + elapsedTime + " seconds"));
                    }
                }.start();
            }
        });


        // To show the thread id
        Log.d("Future Tasks", "Thread Number: " + Thread.currentThread().getId());
        // To show the reference of TextView
        Log.d("Future Tasks", "TextView Object Reference: " + text);
    }

    @Override
    public void onStart() {
        super.onStart();
        // register for receiving EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        // Unregister EventBus to avoid Android OOM (out-of-memory)
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    // The method to process when receiving MessageEvent
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        text.setText(event.message);
    }

}

