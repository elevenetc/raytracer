package com.elevenetc.raytracer.tracers;

import com.elevenetc.raytracer.Ray;
import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.lights.Light;

public interface RayTracer {
    void trace(Light light, Scene scene);

    void trace(Ray ray, Scene scene);
}
