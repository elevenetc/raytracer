package com.elevenetc.raytracer.utils;

import android.util.Log;

public class Timer {
    private String tag;
    private long start;

    public Timer(String tag) {

        this.tag = tag;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        long end = System.currentTimeMillis();
        Log.d("timer-" + tag, (end - start) + "ms");
    }
}
