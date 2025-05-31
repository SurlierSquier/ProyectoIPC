/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author luisb
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CrossLineMark extends DrawableMark {
    private final double x;
    private final double y;
    private final double canvasWidth;
    private final double canvasHeight;

    public CrossLineMark(double x, double y, double canvasWidth, double canvasHeight, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor());
        gc.setLineWidth(getStrokeWidth());
        // Línea horizontal
        gc.strokeLine(0, y, canvasWidth, y);
        // Línea vertical
        gc.strokeLine(x, 0, x, canvasHeight);
    }

    @Override
    public boolean contains(double px, double py) {
        // Define un rango pequeño para "clicable"
        double margin = 5;
        return Math.abs(px - x) < margin || Math.abs(py - y) < margin;
    }
}

