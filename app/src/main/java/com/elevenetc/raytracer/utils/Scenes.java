package com.elevenetc.raytracer.utils;


import android.graphics.Color;

import com.elevenetc.raytracer.Scene;
import com.elevenetc.raytracer.SceneBuilder;
import com.elevenetc.raytracer.edges.EdgeFactories;
import com.elevenetc.raytracer.shapes.Path;

public class Scenes {
    public static Scene randomSquares(int width, int height) {
        SceneBuilder sceneBuilder = new SceneBuilder(width, height);
        return sceneBuilder
                .setPadding(60)
                .addRect(700, 300, 150, 150)
                .addRect(300, 470, 150, 150)
                .addRect(300, 670, 250, 20)
                .addRect(400, 770, 50, 50)
                .addRect(400, 850, 50, 50)
                .addRect(400, 930, 50, 50)
                .addRect(600, 1200, 550, 20)
                .build();
    }

    public static Scene curvePath(int width, int height, double initX, double initY) {

        return new SceneBuilder(width, height)
                .add(new Path.Builder()
                        .setFactory(EdgeFactories.transparent(Color.GREEN))
                        .add(initX, initY)
                        .append(100, 100)
                        .append(100, 0)
                        .append(100, -50)
                        .append(100, 150)
                        .append(0, 150)
                        .append(-100, 0)
                        .append(-100, -100)
                        .append(-100, 0)
                        .append(0, -50)
                        .append(-50, 0)
                        .append(0, 100)
                        .append(100, 0)
                        .append(100, 100)
                        .append(200, 0)
                        .append(0, -300)
                        .append(-100, -100)
                        .append(-100, 0)
                        .append(-100, 50)
                        .add(initX, initY)
                        .build())
                .build();
    }

    public static Scene justVertical(int width, int height) {
        return new SceneBuilder(width, height)
                .addEdge(width / 2, 200,
                        width / 2, height - 200
                )
                .build();
    }

    public static Scene justVerticalTransparent(int width, int height) {
        return new SceneBuilder(width, height)
                .addTransparentEdge(width / 2, 350,
                        width / 2, height - 350,
                        "middle-trans")
                .build();
    }

    public static Scene basicPrism(int width, int height) {
        return new SceneBuilder(width, height)
                .addBasicPrism(width / 2, height / 3, 3)
                .build();
    }

    public static Scene basicLens(int width, int height) {

        double x = width / 2;
        double y = height / 2;
        double size = 10;

        return new SceneBuilder(width, height)
                .addBasicLens(x, y, 200, 75, 0.5)
                .build();
    }
}
