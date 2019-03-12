package com.elevenetc.raytracer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.elevenetc.raytracer.debug.DebugMenu;
import com.elevenetc.raytracer.views.RayTraceView;
import com.elevenetc.raytracer.views.RealTimeRayTracerView;


/**
 * Created by eugene.levenetc on 08/03/2018.
 */

public class RayTracerActivity extends AppCompatActivity {
    private RayTraceView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new RealTimeRayTracerView(this);
//        view = new BackgroundRayTracerView(this);
        setContentView((View) view);

        new DebugMenu.Builder()
                .setTitle("RayTracer")
                .addDivider("Debug settings")
                .addCheckBox("Scene", false, checked -> view.setDebugScene(checked))
                .addCheckBox("Light", false, checked -> view.setDebugLight(checked))
                .build(this);
    }

}