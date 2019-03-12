package com.elevenetc.raytracer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.drawers.DebugDrawer;
import com.elevenetc.raytracer.drawers.Drawer;
import com.elevenetc.raytracer.drawers.InterferenceDrawer;
import com.elevenetc.raytracer.drawers.SpotDrawer;
import com.elevenetc.raytracer.lights.*;
import com.elevenetc.raytracer.math.RayMath;
import com.elevenetc.raytracer.math.RayMathV1;
import com.elevenetc.raytracer.tracers.RayTracer;
import com.elevenetc.raytracer.tracers.RayTracerV1;
import com.elevenetc.raytracer.utils.Scenes;

/**
 * Created by eugene.levenetc on 08/03/2018.
 */

public class RealTimeRayTracerView extends View implements RayTraceView {

    private Scene scene;
    private Drawer debugDrawer = new DebugDrawer();
//    private Drawer drawer = new SpotDrawer();
    private Drawer drawer = new InterferenceDrawer();
    private RayMath math = new RayMathV1();
    private RayTracer tracer = new RayTracerV1(math);

    public RealTimeRayTracerView(Context context) {
        super(context);
        init();
    }

    LightController lightController;

    private boolean initRender;
    private Light light;
    private boolean debugScene = false;
    private boolean debugLight = false;

    public RealTimeRayTracerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        preRenderInit(width, height);

        if (debugScene) debugDrawer.draw(scene, canvas);
        if (debugLight) debugDrawer.draw(light, canvas);

        drawer.draw(light, canvas);
        lightController.draw(canvas);
    }

    private void init() {

    }

    private void preRenderInit(int width, int height) {
        if (!initRender) {
            initRender = true;

            double cx = width / 2;
            double cy = height / 2;

            //scene = Scenes.justVertical(width, height);
//            scene = Scenes.justVerticalTransparent(width, height);
//            scene = Scenes.basicPrism(width, height);
            scene = Scenes.basicLens(width, height);
            initLight(cx, cy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (lightController.onTouch(event)) {
            tracer.trace(light, scene);
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    private void initLight(double cx, double cy) {
//        light = new SingleRayLight(cx, cy, cx, cy + 450, Color.WHITE);
        light = new ConeLight(cx, cy, cx + 400, cy + 400, Color.WHITE, 100, math);
//        light = new PlaneLight(cx, cy, cx + 500, cy, 2, Color.WHITE, 80, math);
//        light = new PointLight(cx, cy, 300, 50);
//        lightController = new UndirectedLightController(light);
        light.setBrightness(0.05f);
        lightController = new DirectedLightController((DirectedLight) light);
        tracer.trace(light, this.scene);
    }


    @Override
    public void setDebugScene(boolean debug) {
        this.debugScene = debug;
        invalidate();
    }

    @Override
    public void setDebugLight(boolean debug) {
        this.debugLight = debug;
        invalidate();
    }
}
