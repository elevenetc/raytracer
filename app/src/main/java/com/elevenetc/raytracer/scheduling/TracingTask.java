package com.elevenetc.raytracer.scheduling;

import android.util.Log;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.tracers.RayTracer;

public class TracingTask implements Runnable {

    private final int from;
    private final int to;
    private final Light light;
    private final Scene scene;
    private final RayTracer tracer;

    public TracingTask(int from, int to,
                       Light light,
                       Scene scene,
                       RayTracer tracer) {

        this.from = from;
        this.to = to - 1;
        this.light = light;
        this.scene = scene;
        this.tracer = tracer;
    }

    @Override
    public void run() {

        Log.d("tracing-task", "starting: " + from + " - " + to);

        for (int i = 0; i < light.rays.size(); i++) {
            if (i >= from && i <= to) {
                tracer.trace(light.rays.get(i), scene);
            }
        }

        Log.d("tracing-task", "finished: " + from + " - " + to);
    }
}
