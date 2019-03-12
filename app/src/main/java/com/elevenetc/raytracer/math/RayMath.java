package com.elevenetc.raytracer.math;

import android.support.annotation.NonNull;

import com.elevenetc.raytracer.RaySegment;
import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.edges.Edge;
import com.elevenetc.raytracer.geometry.Point;
import com.elevenetc.raytracer.geometry.Segment;

public interface RayMath {
    void rotate(Segment segment, double cx, double cy, double degrees);

    void rotate(Segment segment, double degrees);

    double dotProduct(Segment a, Segment b);

    double dotProduct(double x1, double y1, double x2, double y2,
                      double x3, double y3, double x4, double y4);

    double angleBetween(Segment a, Segment b);

    double angleBetween(double ax1, double ay1, double ax2, double ay2,
                        double bx1, double by1, double bx2, double by2);

    Intersection isReflectedByNormalAndIntersection(Segment ray, Edge edge);

    Intersection getIntersectionByNormal(Segment ray, Edge edge);

    double distance(double x1, double y1, double x2, double y2);

    @NonNull
    Intersection getClosestIntersection(RaySegment ray, Scene scene);

    Point getIntersection(RaySegment a, Edge b);

    Point getIntersection(double x1, double y1, double x2, double y2,
                          RaySegment b);

    Point getIntersectionPointWithEndsCheck(Segment a, Segment b);

    @NonNull
    Point getIntersection(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4);

    boolean hasIntersection(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4
    );
}
