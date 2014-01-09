package com.bootstragram.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bootstragram.demo.currencyrates.CurrencyRate;

/**
 * This parser is 90% inspired by the following tutorial: <a
 * href="http://developer.android.com/training/basics/network-ops/xml.html"
 * >Parsing XML Data</a>.
 * 
 * @author mick
 */
public class ExchangeRatesParser {
    // We don't use namespaces
    private static final String ns = null;
    private static final String TAG = ExchangeRatesParser.class.getSimpleName();

    public List<CurrencyRate> parse(InputStream stream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            return readRates(parser);
        } finally {
            stream.close();
        }
    }

    private List<CurrencyRate> readRates(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<CurrencyRate> res = null;

        parser.require(XmlPullParser.START_TAG, ns, "gesmes:Envelope");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Cube")) {
                parser.nextTag();
                res = readParentCube(parser);
            } else {
                skip(parser);
            }
        }
        return res;
    }

    private List<CurrencyRate> readParentCube(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Cube");
        List<CurrencyRate> rates = new ArrayList<CurrencyRate>();
        Log.d(TAG, "Found parent cube");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Cube")) {
                rates.add(readChildCube(parser));
            } else {
                skip(parser);
            }
        }
        Log.d(TAG, "Finished parent cube");
        return rates;
    }

    // Processes title tags in the feed.
    private CurrencyRate readChildCube(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Cube");
        Log.d(TAG, "Found child cube");
        String currency = parser.getAttributeValue(ns, "currency");
        String rate = parser.getAttributeValue(ns, "rate");
        Log.i(TAG, "" + (currency == null ? "null" : currency) + " : " + (rate == null ? "null" : rate));
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "Cube");
        Log.d(TAG, "Finished child cube");
        return new CurrencyRate(currency, rate);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
}
