package com.example.multithreads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ExampleService extends Service {

    protected MutableLiveData<String> mToken;

    public ExampleService()
    {
        mToken = new MutableLiveData<>("current token");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
