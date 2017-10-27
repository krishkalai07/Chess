import java.awt.*;
import java.util.List;

public abstract class Piece implements Cloneable {
    protected int xPosition;
    protected int yPosition;
    protected boolean color;
    protected Piece board[][];

    /**
     * Constructor for a piece.
     *
     * @param xPosition     The row the piece is on in the board.
     * @param yPosition     The column the piece is on in the board.
     * @param isWhite       True if the piece color is white, false otherwise.
     */
    Piece (int xPosition, int yPosition, boolean isWhite, Piece[][] board) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = isWhite;
        this.board = board;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public boolean isWhite() {
        return color;
    }

    public void setColor(boolean isWhite) {
        this.color = color;
    }

    public abstract void draw (Graphics g);
    public abstract void getPossibleMoveList(List<BoardPoint> pointList);
    public abstract void getControlledSquares(List<BoardPoint> pointList);
    public abstract void getControlledSquares(List<BoardPoint> pointList, Piece[][] board);
    public abstract boolean validateMove(int x, int y);
    public abstract char getAbbreviation();

    public Piece clone() {
        try {
            return (Piece)super.clone();
        } catch (CloneNotSupportedException x) {
            return null;
        }
    }
}
