package com.elevenetc.raytracer.math;


import com.elevenetc.raytracer.edges.Edge;
import com.elevenetc.raytracer.geometry.Point;

public class Intersection {

    public Point point;
    public Edge edge;
    public Edge.Side side;
    public int outColor;
    public boolean hasOutColor;

    public boolean exists() {
        return point != null;
    }
}
