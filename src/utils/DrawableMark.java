/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class DrawableMark {
    protected Color color;
    protected double strokeWidth;

    public DrawableMark(Color color, double strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public abstract void draw(GraphicsContext gc);
    public abstract boolean contains(double x, double y);

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
    public Color getColor() {
    return color;
}

public double getStrokeWidth() {
    return strokeWidth;
}

}

