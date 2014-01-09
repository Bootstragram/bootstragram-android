package com.bootstragram.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bootstragram.demo.currencyrates.CurrencyRate;

public class BackgroundOperationDemoActivity extends Activity {
    private static final String TAG = BackgroundOperationDemoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_operation_demo);

        // Button to start the background operation demo activity
        final Button backgroundOperationButton = (Button) findViewById(R.id.my_start_network_operation);
        backgroundOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Starting background operation");
                backgroundOperationButton.setEnabled(false);
                new DownloadRates().execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            }
        });
    }

    private void updateResults(List<CurrencyRate> currencyRatesList) {
        if (currencyRatesList == null) {
            final Button backgroundOperationButton = (Button) findViewById(R.id.my_start_network_operation);
            backgroundOperationButton.setText(getResources().getString(R.string.connection_error));
        } else {
            final ListView listview = (ListView) findViewById(R.id.my_listview);
            final ArrayAdapter<CurrencyRate> adapter = new ArrayAdapter<CurrencyRate>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, currencyRatesList);
            listview.setAdapter(adapter);
        }
    }

    private class DownloadRates extends AsyncTask<String, Void, List<CurrencyRate>> {

        @Override
        protected List<CurrencyRate> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, getResources().getString(R.string.connection_error), e);
                return null;
            } catch (XmlPullParserException e) {
                Log.e(TAG, getResources().getString(R.string.xml_error), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CurrencyRate> result) {
            updateResults(result);

            final Button backgroundOperationButton = (Button) findViewById(R.id.my_start_network_operation);
            backgroundOperationButton.setEnabled(true);
        }

        private List<CurrencyRate> loadXmlFromNetwork(String urlString) throws IOException, XmlPullParserException {
            InputStream stream = null;
            ExchangeRatesParser parser = new ExchangeRatesParser();
            List<CurrencyRate> rates = null;
            try {
                stream = downloadUrl(urlString);
                rates = parser.parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return rates;
        }
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
