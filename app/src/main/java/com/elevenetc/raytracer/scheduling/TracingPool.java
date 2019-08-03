package com.elevenetc.raytracer.scheduling;

import android.util.Log;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.tracers.TracerFactory;
import com.elevenetc.raytracer.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TracingPool {

    private int size;
    private List<TracingThread> threads = new ArrayList<>();
    private volatile AtomicInteger working = new AtomicInteger();
    private Listener listener;

    public TracingPool(int size) {

        this.size = size;

        for (int i = 0; i < size; i++) {
            threads.add(new TracingThread(i, this));
            threads.get(i).start();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void requestTracing(Light light,
                               Scene scene,
                               TracerFactory tracerFactory) {

        if (working.get() > 0) return;

        working.set(threads.size());

        int batchSize = light.rays.size() / this.size;
        int from = 0;
        int to = batchSize;
        for (TracingThread thread : threads) {
            thread.submit(new TracingTask(from, to, light, scene, tracerFactory.create()));
            from = to;
            to = from + batchSize;
        }
    }

    private void onCompleted() {
        if (working.decrementAndGet() == 0) {
            listener.onReadyForRendering();
        }
    }

    static class TracingThread extends Thread {

        private Object lock = new Object();
        private volatile TracingTask task;
        private volatile State state = State.IDLE;
        private int id;
        private TracingPool pool;
        private Timer timer;

        public TracingThread(int id, TracingPool pool) {
            this.id = id;
            this.pool = pool;
            timer = new Timer(String.valueOf(id));
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
                    updateState(State.TRACING);


                    timer.start();

                    pool.listener.onStart(id, timer.start);

                    task.run();
                    timer.stop();

                    pool.listener.onEnd(id, timer.start, timer.end);

                    updateState(State.DONE);
                    pool.onCompleted();
                } catch (InterruptedException e) {

                }
            }
        }

        private void updateState(State state) {
            Log.d("tracing-thread", state.toString());
            this.state = state;
        }

        enum State {
            IDLE, TRACING, DONE
        }
    }

    public interface Listener {
        void onStart(int coreIdx, long start);

        void onEnd(int coreIdx, long start, long end);

        void onReadyForRendering();
    }
}
