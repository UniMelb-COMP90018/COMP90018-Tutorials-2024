package com.example.sensor_barometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

public class Barometer implements SensorEventListener
{
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor sensor;


    private int primarySensor = Sensor.TYPE_PRESSURE;

    public Barometer(Context context, TextView sensorLabel)
    {
        mContext = context;
        enableSensor(sensorLabel);
    }

    public void enableSensor(TextView sensorLabel) {
        Log.v("Sensor...", "Enabling sensor...");
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        Log.v("Sensor...", mSensorManager == null ? "Sensors not supported" : "Sensors are supported");

        if (mSensorManager == null) {
            // Sensors are not available on this device
            sensorLabel.setText(mContext.getResources().getString(R.string.sensor_invalid));
            return;
        }

        sensor = mSensorManager.getDefaultSensor(primarySensor);
        if (sensor == null) {
            // Failure! No pressure sensor is available
            Log.v("Sensor..", "Pressure sensor is not supported");
            sensorLabel.setText(mContext.getResources().getString(R.string.pressure_sensor_invalid));
        }
        else {
            // Success! There's a pressure sensor
            sensor = mSensorManager.getDefaultSensor(primarySensor);
            Log.v("Sensor..", mSensorManager.getDefaultSensor(primarySensor).getName() +
                    " is supported");
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorLabel.setText(mContext.getResources().getString(R.string.pressure_label));
        }
    }

    public void disableSensor()
    {
        if (mSensorManager != null)
        {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == null) {
            Log.v("Sensor...", "OnSensorChanged.sensor = null");
            return;
        }

        if (sensorEvent.sensor.getType() == sensor.getType()) {
            Log.v("Sensor...", "Posting value");
            EventBus.getDefault().post(new BarometerMessage(sensorEvent.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
