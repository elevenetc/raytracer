package com.elevenetc.raytracer.tracers;

import android.support.annotation.NonNull;

import com.elevenetc.raytracer.Ray;
import com.elevenetc.raytracer.RaySegment;
import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.geometry.Point;
import com.elevenetc.raytracer.lights.Light;
import com.elevenetc.raytracer.math.Intersection;
import com.elevenetc.raytracer.math.RayMath;

/**
 * Created by eugene.levenetc on 08/03/2018.
 */

public class RayTracerV1 implements RayTracer {

    private RayMath math;

    public RayTracerV1(RayMath math) {

        this.math = math;
    }

    @Override
    public void trace(Light light, Scene scene) {
        for (Ray ray : light.rays)
            trace(ray, scene);
    }

    @Override
    public void trace(Ray ray, Scene scene) {
        ray.reset();
        traceInternal(ray, ray.initSegment, scene, 0, ray.initSegment.color);
    }

    private void traceInternal(Ray ray, RaySegment initSegment, Scene scene, double currentLength, int prevColor) {

        if (currentLength >= ray.length) {
            return;
        }

        Intersection intersection = math.getClosestIntersection(initSegment, scene);

        if (intersection.exists()) {
            Point point = intersection.point;

            RaySegment newSegment = new RaySegment(initSegment, point.x, point.y);

            setColor(prevColor, intersection, newSegment);

            if (((int) newSegment.length()) == 0) {
                //stop tracing on collision case
                return;
            }

            //calc fading and rest length
            currentLength = setFading(ray, currentLength, newSegment);


            ray.reflectedOrRefracted().add(newSegment);

            //continue tracing
            traceInternal(ray, rotateInitVector(initSegment, intersection), scene, currentLength, intersection.hasOutColor ? intersection.outColor : prevColor);

        } else if (currentLength < ray.length) {//no intersection but light still has energy

            RaySegment newSegment = new RaySegment(initSegment);

            setColor(prevColor, intersection, newSegment);

            setFading(ray, currentLength, newSegment);

            ray.reflectedOrRefracted().add(newSegment);
        }

        TracingObjectsPool.put(intersection);
    }

    private void setColor(int prevColor, Intersection intersection, RaySegment newSegment) {
        if (!intersection.exists()) {
            newSegment.color = prevColor;
            return;
        } else {
            if (intersection.side.isInside()) {
                newSegment.color = intersection.outColor;
            } else {
                newSegment.color = prevColor;
            }
        }
    }

    private double setFading(Ray ray, double currentLength, RaySegment newSegment) {
        newSegment.startAlpha = currentLength == 0 ? 0 : (float) (currentLength / ray.length);

        double length = newSegment.length();
        currentLength += length;
        newSegment.endAlpha = (float) ((currentLength / ray.length));
        return currentLength;
    }

    @NonNull
    private RaySegment rotateInitVector(RaySegment initVector, Intersection intersection) {

        double inputAngle = math.angleBetween(initVector, intersection.edge);
        Point point = intersection.point;
        RaySegment reflected = initVector.copy();

        reflected.translateTo(point);

        double outputAngle;

        if (intersection.side.isTransparent()) {
            double dot = math.dotProduct(initVector.normalized(), intersection.edge.normalized());
            outputAngle = 20 * dot;
        } else {
            outputAngle = (inputAngle - 180) * 2;
        }

        math.rotate(reflected, outputAngle);

        return reflected;
    }
}