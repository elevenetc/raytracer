package com.elevenetc.raytracer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.elevenetc.raytracer.debug.CoresViewer;
import com.elevenetc.raytracer.debug.DebugMenu;
import com.elevenetc.raytracer.renderers.RenderThread;
import com.elevenetc.raytracer.views.BackgroundRayTracerView;
import com.elevenetc.raytracer.views.RealTimeRayTracerView;


/**
 * Created by eugene.levenetc on 08/03/2018.
 */

public class RayTracerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealTimeTracer();
    }

    private void initRealTimeTracer() {
        RealTimeRayTracerView view = new RealTimeRayTracerView(this);
        setContentView((View) view);

        new DebugMenu.Builder()
                .setTitle("RayTracer")
                .addDivider("Debug settings")
                .addCheckBox("Scene", false, view::setDebugScene)
                .addCheckBox("Light", false, view::setDebugLight)
                .build(this);
    }

    private void initBackgroundTracer() {
        BackgroundRayTracerView view = new BackgroundRayTracerView(this);
        setContentView((View) view);

        CoresViewer.Binder binder = new CoresViewer.Binder(Runtime.getRuntime().availableProcessors());

        view.setCoresListener(new RenderThread.CoresListener() {
            @Override
            public void onStartCore(String coreIdx, long time) {
                view.post(() -> binder.updateCore(
                        coreIdx,
                        new CoresViewer.Binder.CoreInfo(CoresViewer.Binder.CoreInfo.State.WORKING, time, -1))
                );
            }

            @Override
            public void onEndCore(String coreIdx, long start, long end) {
                view.post(() -> binder.updateCore(
                        coreIdx,
                        new CoresViewer.Binder.CoreInfo(CoresViewer.Binder.CoreInfo.State.IDLE, start, end))
                );
            }
        });

        CoresViewer coresViewer = new CoresViewer();

        coresViewer.setBinder(binder);

        new DebugMenu.Builder()
                .setTitle("RayTracer")
                .addDivider("Debug settings")
                .addCheckBox("Scene", false, view::setDebugScene)
                .addCheckBox("Light", false, view::setDebugLight)
                .addView(coresViewer)
                .build(this);
    }

}