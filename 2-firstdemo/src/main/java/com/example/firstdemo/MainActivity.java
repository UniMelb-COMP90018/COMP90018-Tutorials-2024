package com.example.firstdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /// Simple enums to handle different examples
    private enum Step
    {
        One,
        Two,
        Three
    }

    private enum IntentStyle
    {
        Implicit,
        Explicit
    }

    private Step step = Step.Three;
    private IntentStyle intent = IntentStyle.Implicit;

    ///////////////////////////

    private final String TAG = "First Demo";
    public static String MESSAGE = "Message";
    public static int MESSAGE_RECEIVED = 1;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button button;

        binding.receivedMessage.setText("ButterKnife has been replaced!");

        if (step == Step.One) {

            //Step One: Show how to output Log from Logcat;
            Log.d(TAG, "onCreate: Step One: This is the first LOG");

        }

        if (step == Step.Two) {
            //Step Two: Show how to add listener to a button
            button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.receivedMessage.setText("Button has been clicked!");
                    Log.d(TAG, "onCreate: Step Two: Click Button!");
                }
            });
        }

        if (step == Step.Three) {
            //Step Three: Show how to use ViewBinding to add listener to a button
            binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.receivedMessage.setText("Button has been clicked from ViewBinding!");
                    Log.d(TAG, "onCreate: Step Three: Click View Binding Button!");
                    triggerButtonPressWithIntent();
                }
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void triggerButtonPressWithIntent()
    {
        Log.d(TAG, "outputLog: Click Button Successful!");

        /*
        Explicit intents specify which component of which application
        will satisfy the intent, by specifying a full ComponentName.
         */
        if (intent == IntentStyle.Explicit) {
            //Explicit Intents
            Log.d(TAG, "outputLog: Step one: Explicit Intent");
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra(MESSAGE, "Hello from the first activity");
            startActivityIntent.launch(intent);
        }

        /*
        Implicit intents do not name a specific component,
        but instead declare a general action to perform,
        which allows a component from another app to handle it.
         */
        if (intent == IntentStyle.Implicit) {
            //Implicit Intents
            Intent intent = new Intent();
            intent.setAction("SecondActivity");
            intent.putExtra(MESSAGE, "Hello from the first activity");
            startActivityIntent.launch(intent);
        }
    }

    /// Register a callback from the activity result
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //if (result.getData() == MESSAGE_RECEIVE) {
                    if (result.getResultCode()==RESULT_OK){
                        binding.receivedMessage.setText(result.getData().getStringExtra(Main2Activity.RECEIVED_MESSAGE));
                    }

                }
            });
}