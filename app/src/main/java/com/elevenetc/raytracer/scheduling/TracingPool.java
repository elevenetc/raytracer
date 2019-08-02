package com.elevenetc.raytracer.scheduling;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.tracers.RayTracer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TracingPool {

    private int size;
    private List<TracingThread> threads = new ArrayList<>();
    private volatile AtomicInteger working = new AtomicInteger();
    private CompleteHandler handler;

    public TracingPool(int size) {

        this.size = size;

        for (int i = 0; i < size; i++) {
            threads.add(new TracingThread(i, this));
            threads.get(i).start();
        }
    }

    public void setHandler(CompleteHandler handler) {
        this.handler = handler;
    }

    public void requestTracing(Light light,
                               Scene scene,
                               RayTracer tracer) {

        if (working.get() > 0) return;

        working.set(threads.size());

        int batchSize = light.rays.size() / this.size;
        int from = 0;
        int to = batchSize;
        for (TracingThread thread : threads) {
            thread.submit(new TracingTask(from, to, light, scene, tracer));
            from = to;
            to = from + batchSize;
        }
    }

    private void onCompleted() {
        if (working.decrementAndGet() == 0) {
            handler.onReadyForRendering();
        }
    }

    static class TracingThread extends Thread {

        private Object lock = new Object();
        private volatile TracingTask task;
        private volatile State state = State.IDLE;
        private int id;
        private TracingPool pool;

        public TracingThread(int id, TracingPool pool) {
            this.id = id;
            this.pool = pool;
        }

        public void submit(TracingTask task) {
            this.task = task;
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                    state = State.TRACING;
                    task.run();
                    state = State.DONE;
                    pool.onCompleted();
                } catch (InterruptedException e) {

                }
            }
        }

        enum State {
            IDLE, TRACING, DONE
        }
    }

    public interface CompleteHandler {
        void onReadyForRendering();
    }
}
