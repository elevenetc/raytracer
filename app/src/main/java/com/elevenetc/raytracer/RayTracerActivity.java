package com.elevenetc.raytracer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.elevenetc.raytracer.debug.CoresViewer;
import com.elevenetc.raytracer.debug.DebugMenu;
import com.elevenetc.raytracer.renderers.RenderThread;
import com.elevenetc.raytracer.views.BackgroundRayTracerView;


/**
 * Created by eugene.levenetc on 08/03/2018.
 */

public class RayTracerActivity extends AppCompatActivity {
    private BackgroundRayTracerView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        view = new RealTimeRayTracerView(this);
        view = new BackgroundRayTracerView(this);
        setContentView((View) view);

        CoresViewer.Binder binder = new CoresViewer.Binder(8);

        view.setCoresListener(new RenderThread.CoresListener() {
            @Override
            public void onStartCore(int coreIdx, long time) {

                view.post(new Runnable() {
                    @Override
                    public void run() {

                        binder.updateCore(coreIdx, new CoresViewer.Binder.CoreInfo(CoresViewer.Binder.CoreInfo.State.WORKING, time, -1));
                    }
                });
            }

            @Override
            public void onEndCore(int coreIdx, long start, long end) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        binder.updateCore(coreIdx, new CoresViewer.Binder.CoreInfo(CoresViewer.Binder.CoreInfo.State.IDLE, start, end));
                    }
                });
            }
        });

        CoresViewer coresViewer = new CoresViewer();

        coresViewer.setBinder(binder);

        new DebugMenu.Builder()
                .setTitle("RayTracer")
                .addDivider("Debug settings")
                .addCheckBox("Scene", false, checked -> view.setDebugScene(checked))
                .addCheckBox("Light", false, checked -> view.setDebugLight(checked))
                .addView(coresViewer)
                .build(this);
    }

}