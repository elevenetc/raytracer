package com.elevenetc.raytracer;

import com.elevenetc.raytracer.shapes.Shape;

import java.util.LinkedList;
import java.util.List;


public class Scene {

    public List<Shape> objects = new LinkedList<>();

    public void add(Shape shape) {
        objects.add(shape);
    }

    public List<Shape> objects() {
        return objects;
    }
}