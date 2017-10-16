package Pieces;

import java.awt.*;

public abstract class Piece {
    private int x_position;
    private int y_position;
    private int capture_weight;
    private boolean color;

    Piece (int x_position, int y_position, int capture_weight, boolean is_white) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.capture_weight = capture_weight;
        this.color = is_white;
    }

    public abstract void draw(Graphics g);

    public int getX_position() {
        return x_position;
    }

    public void setX_position(int x_position) {
        this.x_position = x_position;
    }

    public int getY_position() {
        return y_position;
    }

    public void setY_position(int y_position) {
        this.y_position = y_position;
    }

    public int getCapture_weight() {
        return capture_weight;
    }

    public void setCapture_weight(int capture_weight) {
        this.capture_weight = capture_weight;
    }

    public boolean isWhite() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }
}
