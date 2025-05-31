/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class ArcMark extends DrawableMark {
    private double centerX, centerY, radius;

    public ArcMark(double centerX, double centerY, double radius, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 180, ArcType.OPEN);
    }

    @Override
    public boolean contains(double x, double y) {
        // Crude hit-test based on radius
        double dist = Math.hypot(x - centerX, y - centerY);
        return Math.abs(dist - radius) <= strokeWidth + 3;
    }
}
*/
