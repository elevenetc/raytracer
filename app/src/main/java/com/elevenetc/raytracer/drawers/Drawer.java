package com.elevenetc.raytracer.drawers;

import android.graphics.Canvas;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;


public interface Drawer {
    void draw(Light light, Canvas canvas);

    void draw(Scene scene, Canvas canvas);
}
