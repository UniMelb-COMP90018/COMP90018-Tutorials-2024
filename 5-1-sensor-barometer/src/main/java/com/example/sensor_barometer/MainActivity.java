package com.example.sensor_barometer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.sensor_barometer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    TextView sensorLabel;
    TextView pressure;
    Barometer barometer;
    Button switchLayout;

    OutputVisibility output = OutputVisibility.Text;

    private final float ROT_ZERO = -125;
    private final float ROT_VALUE = 0.893f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorLabel = binding.sensorLabel;
        pressure = binding.barometerValue;
        switchLayout = binding.button;

        // the image is scaled such as:
        // 0 = -125 rotations
        // 280 = +125 rotation
        // 1 value = 0.893f
        binding.point.setRotation(ROT_ZERO);

        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output = output.values()[(output.ordinal() + 1) %3];

                if (output == OutputVisibility.Image)
                {
                    binding.panel.setVisibility(View.VISIBLE);
                    binding.point.setVisibility(View.VISIBLE);
                    binding.sensorLabel.setVisibility(View.INVISIBLE);
                    binding.barometerValue.setVisibility(View.INVISIBLE);
                }
                else if (output == OutputVisibility.Text) {
                    binding.panel.setVisibility(View.INVISIBLE);
                    binding.point.setVisibility(View.INVISIBLE);
                    binding.sensorLabel.setVisibility(View.VISIBLE);
                    binding.barometerValue.setVisibility(View.VISIBLE);
                }
                else {
                    binding.panel.setVisibility(View.VISIBLE);
                    binding.point.setVisibility(View.VISIBLE);
                    binding.sensorLabel.setVisibility(View.VISIBLE);
                    binding.barometerValue.setVisibility(View.VISIBLE);
                }
            }
        });

        barometer = new Barometer(this, sensorLabel);
        EventBus.getDefault().register(this);
    }

    public void setSensorLabel(String s) {
        sensorLabel.setText(s);
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);

        barometer.disableSensor();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(BarometerMessage event)
    {
        pressure.setText(String.valueOf(event.getPressure()));
        binding.point.setRotation(ROT_ZERO + (event.getPressure() * ROT_VALUE));
    }

    private enum OutputVisibility
    {
        Text,
        Image,
        Both
    }

}