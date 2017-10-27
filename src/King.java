import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class King extends Piece {
    private boolean hasMoved; //Used for castling
    private Vector<BoardPoint> controlledSquares;

    public King(int xPosition, int yPosition, boolean isWhite, Piece[][] board, Vector<BoardPoint> controlledSquares) {
        super(xPosition, yPosition, isWhite, board);
        hasMoved = false;
        this.controlledSquares = controlledSquares;
    }

    public King(int xPosition, int yPosition, boolean isWhite, Piece[][] board, Vector<BoardPoint> controlledSquares, boolean hasMoved) {
        super(xPosition, yPosition, isWhite, board);
        this.hasMoved = hasMoved;
        this.controlledSquares = controlledSquares;
    }

    private boolean vectorContainsPoint(List<BoardPoint> pointList, int x, int y) {
        for (int i = 0; i < pointList.size(); i++) {
            if (x == pointList.get(i).x && y == pointList.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck () {
        update_controlled_spaces();
        return vectorContainsPoint(controlledSquares, xPosition, yPosition);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    private void update_controlled_spaces() {
        controlledSquares.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].color != color) {
                    board[i][j].getControlledSquares(controlledSquares);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteKing.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackKing.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getXPosition()+1), 50*(getYPosition()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && j == 0) {
                    continue;
                }

                if (xPosition + i >= 0 && xPosition + i < 8 && yPosition + j >= 0 && yPosition + j < 8) {
                    if (board[xPosition + i][yPosition + j] == null) {
                        BoardPoint point = new BoardPoint(xPosition + i, yPosition + j);
                        if (this.color && !vectorContainsPoint(controlledSquares, point.x, point.y)) {
                            pointList.add(point);
                        }
                        else if (!this.color && !vectorContainsPoint(controlledSquares, point.x, point.y)) {
                            pointList.add(point);
                        }
                    }
                    else {
                        if (this.color != board[xPosition + i][yPosition + j].color) {
                            pointList.add(new BoardPoint(xPosition + i, yPosition + j));
                        }
                    }
                }
            }
        }

        // Castling
        if (!hasMoved) {
            if (vectorContainsPoint(controlledSquares, xPosition, yPosition)) {
                return;
            }

            // Castling king-side
            if (board[xPosition+1][yPosition] == null && board[xPosition+2][yPosition] == null) {
                if (!vectorContainsPoint(controlledSquares, xPosition + 1, yPosition) && !vectorContainsPoint(controlledSquares, xPosition + 2, yPosition)) {
                    if (board[xPosition + 3][yPosition] != null && (board[xPosition + 3][yPosition]).getClass() == Rook.class && !((Rook)board[xPosition + 3][yPosition]).hasMoved()) {
                        pointList.add(new BoardPoint(xPosition + 2, yPosition));
                    }
                }
            }
            // Castling queen-side
            if (board[xPosition-1][yPosition] == null && board[xPosition-2][yPosition] == null && board[xPosition-3][yPosition] == null) {
                if (!vectorContainsPoint(controlledSquares, xPosition - 1, yPosition) && !vectorContainsPoint(controlledSquares, xPosition - 2, yPosition)) {
                    if (board[xPosition - 4][yPosition] != null && board[xPosition - 4][yPosition].getClass() == Rook.class && !((Rook)(board[xPosition - 4][yPosition])).hasMoved()) {
                        pointList.add(new BoardPoint(xPosition - 2, yPosition));
                    }
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList) {
        getControlledSquares(pointList, board);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && j == 0) {
                    continue;
                }

                if (xPosition+i >= 0 && xPosition+i < 8 && yPosition+j >= 0 && yPosition+j < 8) {
                    pointList.add(new BoardPoint(xPosition+i, yPosition+j));
                }
            }
        }
    }

    @Override
    public boolean validateMove(int toX, int toY) {
        if (xPosition == toX && yPosition == toY) {
            return false;
        }

        update_controlled_spaces();

        if (Math.abs(xPosition - toX) == 2) {
            if (!hasMoved) {
                Rook edge_rook = (Rook)(xPosition - toX == 2 ? board[7][(color ? 7 : 0)] : board[0][(color ? 7 : 0)]);
                if (edge_rook != null && !edge_rook.hasMoved()) {
                    if ((toX - xPosition > 0 && board[xPosition+1][yPosition] == null && board[xPosition+2][yPosition] == null) ||
                            (toX - xPosition < 0 && board[xPosition-1][yPosition] == null && board[xPosition-2][yPosition] == null)) {
                        if (!isInCheck()) {
                            if (!vectorContainsPoint(controlledSquares, xPosition - toX > 0 ? xPosition - 1 : xPosition + 1, yPosition) &&
                                    !vectorContainsPoint(controlledSquares, xPosition - toX > 0 ? xPosition - 2 : xPosition + 2, yPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        else {
            if (Math.abs(xPosition - toX) <= 1 && Math.abs(yPosition - toY) <= 1) {
                if (!vectorContainsPoint(controlledSquares, toX, toY)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'K' : 'k');
    }

}
