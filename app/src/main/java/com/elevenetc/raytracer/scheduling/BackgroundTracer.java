package com.elevenetc.raytracer.scheduling;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.tracers.RayTracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BackgroundTracer {

    private final Light light;
    private final Scene scene;
    private final RayTracer tracer;

    private volatile State state;

    public BackgroundTracer(
            Light light,
            Scene scene,
            RayTracer tracer) {

        this.light = light;
        this.scene = scene;
        this.tracer = tracer;


        //ExecutorService pool = Executors.newFixedThreadPool(5);
        //pool.
    }

    public void requestTrace() {
        if (state == State.IDLE) {
            trace();
        } else if (state == State.TRACING) {

        } else if (state == State.READY) {

        }
    }

    private void trace() {
        tracer.trace(light, scene);
    }

    enum State {
        IDLE, TRACING, READY
    }
}
