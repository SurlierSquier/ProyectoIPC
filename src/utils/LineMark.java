/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LineMark extends DrawableMark {
    private double startX, startY, endX, endY;

    public LineMark(double startX, double startY, double endX, double endY, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void setEnd(double endX, double endY) {
    this.endX = endX;
    this.endY = endY;
}
public double getStartX() { return startX; }
public double getStartY() { return startY; }


    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeLine(startX, startY, endX, endY);
    }

    @Override
    public boolean contains(double x, double y) {
        double distance = distanceToSegment(x, y, startX, startY, endX, endY);
        return distance <= strokeWidth + 3;
    }

    private double distanceToSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0) return Math.hypot(px - x1, py - y1);
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }
}


