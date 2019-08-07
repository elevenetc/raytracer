package com.elevenetc.raytracer.scheduling;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.tracers.TracerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TracingPool {

    private int size;
    private Listener listener;
    private ExecutorService pool;
    private AtomicInteger counter = new AtomicInteger(0);

    public TracingPool(int size) {

        this.size = size;

        pool = Executors.newFixedThreadPool(size, new ThreadFactory() {

            int threadId;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadId + "");
                threadId++;
                return thread;
            }
        });

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void requestTracing(Light light,
                               Scene scene,
                               TracerFactory tracerFactory) {

        if (counter.get() > 0) return;


        int tasks = size * 4;
        int batchSize = light.rays.size() / tasks;
        int from = 0;
        int to = batchSize;

        counter = new AtomicInteger(tasks);

        TracingTask.OnFinished onFinished = () -> {
            if (counter.decrementAndGet() == 0) {
                listener.onReadyForRendering();
            }
        };

        for (int i = 0; i < tasks; i++) {
            pool.submit(new TracingTask(from, to, light, scene, tracerFactory.create(), onFinished, listener));
            from = to;
            to = from + batchSize;
        }
    }

    public interface Listener {
        void onStart(String coreIdx, long start);

        void onEnd(String coreIdx, long start, long end);

        void onReadyForRendering();
    }
}
