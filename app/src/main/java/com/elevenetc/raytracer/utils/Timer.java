package com.elevenetc.raytracer.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {

    private String tag;
    public long start;
    public long end;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");//yyyy-MM-dd HH:

    public Timer(String tag) {

        this.tag = tag;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        end = System.currentTimeMillis();
        long duration = end - start;
        String startStr = this.format.format(new Date(start));
        String endStr = this.format.format(new Date(end));
        Log.d("timer-" + tag, "duration: " + duration + "ms" + " from:" + startStr + " to: " + endStr);
    }
}
