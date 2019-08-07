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
    private OnFinished onFinished;
    private TracingPool.Listener listener;

    public TracingTask(int from, int to,
                       Light light,
                       Scene scene,
                       RayTracer tracer,
                       OnFinished onFinished, TracingPool.Listener listener) {

        this.from = from;
        this.to = to - 1;
        this.light = light;
        this.scene = scene;
        this.tracer = tracer;
        this.onFinished = onFinished;
        this.listener = listener;
    }

    @Override
    public void run() {


        String threadName = Thread.currentThread().getName();

        Log.d("tracing-task", "starting: " + threadName);
        //Log.d("tracing-task", "starting: " + from + " - " + to);


        long start = System.currentTimeMillis();

        listener.onStart(threadName, start);

        for (int i = 0; i < light.rays.size(); i++) {
            if (i >= from && i <= to) {
                tracer.trace(light.rays.get(i), scene);
            }
        }

        listener.onEnd(threadName, start, System.currentTimeMillis());

        onFinished.onFinished();

        //Log.d("tracing-task", "finished: " + from + " - " + to);
        Log.d("tracing-task", "finished: " + threadName);
    }

    interface OnFinished {
        void onFinished();
    }
}
