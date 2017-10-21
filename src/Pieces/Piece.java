package Pieces;

import java.awt.*;
import java.util.List;

public abstract class Piece {
    protected int x_position;
    protected int y_position;
    private int capture_weight;
    protected boolean color;
    protected Piece board[][];

    /**
     * Constructor for a piece.
     *
     * @param x_position     The row the piece is on in the board.
     * @param y_position     The column the piece is on in the board.
     * @param capture_weight The value of the piece (in pawns).
     * @param is_white       True if the piece color is white, false otherwise.
     */
    Piece (int x_position, int y_position, int capture_weight, boolean is_white, Piece[][] board_ref) {
        this.x_position = x_position;
        this.y_position = y_position;
        this.capture_weight = capture_weight;
        this.color = is_white;
        this.board = board_ref;
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

    public boolean isWhite() {
        return color;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public abstract void getPossibleMoves(List<BoardPoint> vector);

    public abstract void getControlledSquares(List<BoardPoint> vector);

    public abstract Piece clone();
}
