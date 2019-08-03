package com.elevenetc.raytracer.renderers;

import android.graphics.Canvas;
import android.graphics.Color;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.drawers.Drawer;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.math.RayMathV1;
import com.elevenetc.raytracer.scheduling.TracingPool;
import com.elevenetc.raytracer.tracers.RayTracer;
import com.elevenetc.raytracer.tracers.RayTracerV1;
import com.elevenetc.raytracer.tracers.TracerFactory;

public class RenderThread {

    private RayTracer tracer;
    private Scene scene;
    private Drawer drawer;
    private final Light light;
    private final Canvas canvas;
    private ReadyListener listener;
    private volatile boolean isDrawing;
    private TracingPool tracingPool = new TracingPool(2);

    public RenderThread(
            RayTracer tracer,
            Scene scene,
            Drawer drawer,
            Light light,
            Canvas canvas,
            ReadyListener listener) {
        this.tracer = tracer;
        this.scene = scene;
        this.drawer = drawer;
        this.light = light;
        this.canvas = canvas;
        this.listener = listener;

        tracingPool.setHandler(new TracingPool.CompleteHandler() {
            @Override
            public void onReadyForRendering() {
                drawer.draw(light, canvas);
                listener.onRendered();
                isDrawing = false;
            }
        });
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void draw() {
        if (!isDrawing) {
            isDrawing = true;
            canvas.drawColor(Color.BLACK);
            tracingPool.requestTracing(light, scene, () -> new RayTracerV1(new RayMathV1()));
        }
    }

}
