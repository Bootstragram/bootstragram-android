package com.bootstragram.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * This is the main class for the demo Android project used to test stuff on the
 * Android platform.
 * 
 * @author mick
 * 
 */
public class MainActivity extends Activity {

    @SuppressWarnings("rawtypes")
    private void onClick(int buttonId, final Activity callingActivity, final Class destActivityClass) {
        Button button = (Button) findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(callingActivity, destActivityClass);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the background as 250, 250, 250 - as bootstragram.com
        View backgroundView = findViewById(R.id.my_main_activity_background);
        backgroundView.setBackgroundColor(Color.argb(255, 250, 250, 250));

        onClick(R.id.my_background_demo_button, MainActivity.this, BackgroundOperationDemoActivity.class);
        onClick(R.id.my_services_demo_button, MainActivity.this, ServiceFeedbackActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
