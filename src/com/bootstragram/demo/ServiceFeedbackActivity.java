package com.bootstragram.demo;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bootstragram.demo.bean.RandomSingleton;
import com.bootstragram.demo.services.RandomEventsService;

public class ServiceFeedbackActivity extends Activity {
    private final static String TAG = ServiceFeedbackActivity.class.getSimpleName();
    private SingletonObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_feedback_demo);

        this.observer = new SingletonObserver(null);
        RandomSingleton.getInstance().registerObserver(this.observer);

        final Button button = (Button) findViewById(R.id.my_start_service_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Starting service");
                Intent intent = new Intent(ServiceFeedbackActivity.this, RandomEventsService.class);
                startService(intent);
            }
        });
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        RandomSingleton.getInstance().unregisterObserver(this.observer);
        super.onDestroy();
    }

    private void backgroundThreadSafeUpdateTextViewFromSingleton() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView textView = (TextView) findViewById(R.id.my_services_feedback_demo_title);
                textView.setText(RandomSingleton.getInstance().getHexString());
                textView.setBackgroundColor(RandomSingleton.getInstance().getColor());
            }
        });
    }

    private class SingletonObserver extends ContentObserver {

        public SingletonObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.d(TAG, "onChange called");
            backgroundThreadSafeUpdateTextViewFromSingleton();
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }
    }
}
