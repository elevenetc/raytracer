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
import com.elevenetc.raytracer.utils.Timer;

public class RenderThread {

    private RayTracer tracer;
    private Scene scene;
    private Drawer drawer;
    private final Light light;
    private final Canvas canvas;
    private ReadyListener listener;
    private volatile boolean isDrawing;
    private int cores = Runtime.getRuntime().availableProcessors();
    private TracingPool tracingPool = new TracingPool(cores);
    private Timer timer = new Timer("total-work");
    private CoresListener coresListener;

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

        tracingPool.setListener(new TracingPool.Listener() {

            @Override
            public void onStart(int coreIdx, long start) {
                coresListener.onStartCore(coreIdx, start);
            }

            @Override
            public void onEnd(int coreIdx, long start, long end) {
                coresListener.onEndCore(coreIdx, start, end);
            }

            @Override
            public void onReadyForRendering() {
                timer.stop();
                drawer.draw(light, canvas);
                listener.onRendered();
                isDrawing = false;
            }
        });
    }

    public void setCoresListener(CoresListener coresListener) {
        this.coresListener = coresListener;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void draw() {
        if (!isDrawing) {
            isDrawing = true;
            canvas.drawColor(Color.BLACK);
            timer.start();
            tracingPool.requestTracing(light, scene, () -> new RayTracerV1(new RayMathV1()));
        }
    }



    public interface CoresListener {
        void onStartCore(int coreIdx, long time);
        void onEndCore(int coreIdx, long time, long end);
    }

}
