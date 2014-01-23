package com.bootstragram.demo.bean;

import java.util.UUID;

import android.database.ContentObservable;
import android.graphics.Color;

public class RandomSingleton extends ContentObservable {
    private static final RandomSingleton singleton = new RandomSingleton();

    public static RandomSingleton getInstance() {
        return singleton;
    }

    private String hexString;
    private long count = 0;
    private int color;

    private int newColor() {
        return ((int) (Math.random() * 256.0));
    }

    public void increment() {
        this.hexString = UUID.randomUUID().toString();
        this.color = Color.argb(255, newColor(), newColor(), newColor());
        this.count++;

        dispatchChange(false, null);
    }

    private RandomSingleton() {
        super();
        increment();
    }

    public String getHexString() {
        return hexString;
    }

    public long getCount() {
        return count;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return hexString + "/" + count + "/" + color;
    }
}
