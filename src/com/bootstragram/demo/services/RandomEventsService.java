package com.bootstragram.demo.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bootstragram.demo.bean.RandomSingleton;

public class RandomEventsService extends IntentService {
    private final static String TAG = RandomEventsService.class.getSimpleName();

    public RandomEventsService() {
        super(RandomEventsService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        while (true) {
            final RandomSingleton singleton = RandomSingleton.getInstance();
            singleton.increment();

            long endTime = System.currentTimeMillis() + 2 * 1000;
            Log.d(TAG, "Waking up at " + endTime);
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(TAG, "Couldn't sleep in peace", e);
                    }
                }
                singleton.increment();

                Log.d(TAG, "Woke up with " + singleton);
            }
        }
    }
}
