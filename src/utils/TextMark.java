/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TextMark extends DrawableMark {
    private double x, y;
    private String text;

    public TextMark(double x, double y, String text, Color color, double strokeWidth) {
        super(color, strokeWidth);
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillText(text, x, y);
    }

    @Override
    public boolean contains(double px, double py) {
        return Math.abs(px - x) < 20 && Math.abs(py - y) < 20;
    }
}

