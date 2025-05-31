/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointMark extends DrawableMark {
    private double x, y;

    public PointMark(double x, double y, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - strokeWidth / 2, y - strokeWidth / 2, strokeWidth, strokeWidth);
    }

    @Override
    public boolean contains(double px, double py) {
        return Math.hypot(px - x, py - y) <= strokeWidth;
    }
}
