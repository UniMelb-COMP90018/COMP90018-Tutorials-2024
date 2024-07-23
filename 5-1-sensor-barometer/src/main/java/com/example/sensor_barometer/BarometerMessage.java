package com.example.sensor_barometer;

public class BarometerMessage {

    private float pressure;

    public BarometerMessage(float pressure)
    {
        this.pressure = pressure;
    }

    public float getPressure()
    {
        return pressure;
    }
}
