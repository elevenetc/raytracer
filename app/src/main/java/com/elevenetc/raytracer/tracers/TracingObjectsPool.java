package com.elevenetc.raytracer.tracers;

import com.elevenetc.raytracer.math.Intersection;

import java.util.LinkedList;

public class TracingObjectsPool {

    static LinkedList<Intersection> intersections = new LinkedList<>();

    public static Intersection get() {

        synchronized (intersections) {
            if (intersections.isEmpty()) {
                return new Intersection();
            } else {
                return intersections.removeFirst();
            }
        }
    }

    public static void put(Intersection intersection) {
        synchronized (intersections) {
            intersections.add(intersection);
        }
    }


}
