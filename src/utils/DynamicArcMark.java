/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author luisb
 */
public class DynamicArcMark extends DrawableMark {
    private double centerX, centerY;
    private double endX, endY;

    public DynamicArcMark(double centerX, double centerY, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.centerX = centerX;
        this.centerY = centerY;
        this.endX = centerX;
        this.endY = centerY;
    }

    public void setEnd(double x, double y) {
        this.endX = x;
        this.endY = y;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor());
        gc.setLineWidth(getStrokeWidth());

        double dx = endX - centerX;
        double dy = endY - centerY;
        double radius = Math.hypot(dx, dy);
        double angle = Math.toDegrees(Math.atan2(-dy, dx)); // -dy: Y grows down in JavaFX
        double length =180; 

        gc.strokeArc(
            centerX - radius, centerY - radius, radius * 2, radius * 2,
            angle, length, javafx.scene.shape.ArcType.OPEN
        );
    }

    @Override
    
public boolean contains(double x, double y) {
    double dx = endX - centerX;
    double dy = endY - centerY;
    double radius = Math.hypot(dx, dy);

    // Punto que estás probando respecto al centro
    double distToCenter = Math.hypot(x - centerX, y - centerY);
    if (Math.abs(distToCenter - radius) > 10) return false; // tolerancia de 10 px

    // Verifica si el ángulo está dentro del arco
    double startAngle = Math.toDegrees(Math.atan2(-dy, dx));
    double testAngle = Math.toDegrees(Math.atan2(-(y - centerY), x - centerX));
    double delta = normalizeAngle(testAngle - startAngle);

    return delta >= 0 && delta <= 90; // cambia 90 si tu arco es mayor o menor
}

// Normaliza ángulo a 0–360°
private double normalizeAngle(double angle) {
    angle = angle % 360;
    if (angle < 0) angle += 360;
    return angle;
}

}
