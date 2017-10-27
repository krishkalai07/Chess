import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Rook extends Piece {
    private boolean didMove; // Used for castling.

    public Rook(int xPosition, int yPosition, boolean isWhite, Piece[][] board) {
        super(xPosition, yPosition, isWhite, board);
        didMove = false;
    }

    public boolean didMove() {
        return didMove;
    }

    public void setDidMove(boolean didMove) {
        this.didMove = didMove;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.color) {
                String filename = "vc_assets/WhiteRook.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackRook.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getXPosition()+1), 50*(getYPosition()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        // Up
        for (int i = 1; yPosition - i >= 0; i++) {
            if (board[xPosition][yPosition-i] == null) {
                pointList.add(new BoardPoint(xPosition, yPosition - i));
            }
            else {
                if (board[xPosition][yPosition - i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition, yPosition - i));
                }
                break;
            }
        }

        // Right
        for (int i = 1; xPosition + i < 8; i++) {
            if (board[xPosition+i][yPosition] == null) {
                pointList.add(new BoardPoint(xPosition+i, yPosition));
            }
            else {
                if (board[xPosition+i][yPosition].color != this.color) {
                    pointList.add(new BoardPoint(xPosition + i, yPosition));
                }
                break;
            }
        }

        // Down
        for (int i = 1; yPosition + i < 8; i++) {
            if (board[xPosition][yPosition+i] == null) {
                pointList.add(new BoardPoint(xPosition, yPosition + i));
            }
            else {
                if (board[xPosition][yPosition + i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition, yPosition + i));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> point_list) {
        getControlledSquares(point_list, board);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        boolean stop_upward = false;
        boolean stop_rightward = false;
        boolean stop_downward = false;
        boolean stop_leftward = false;

        for (int i = 1; !stop_upward || !stop_downward || !stop_leftward || !stop_rightward; i++) {
            //upward
            if (yPosition - i < 0) {
                stop_upward = true;
            }
            if (!stop_upward) {
                if (board[xPosition][yPosition - i] != null) {
                    stop_upward = true;
                }
                pointList.add(new BoardPoint(xPosition, yPosition - i));
            }

            //downward
            if (yPosition + i > 7) {
                stop_downward = true;
            }
            if (!stop_downward) {
                if (board[xPosition][yPosition + i] != null) {
                    stop_downward = true;
                }
                pointList.add(new BoardPoint(xPosition, yPosition + i));
            }

            //rightward
            if (xPosition + i > 7) {
                stop_rightward = true;
            }
            if (!stop_rightward) {
                if (board[xPosition + i][yPosition] != null) {
                    stop_rightward = true;
                }
                pointList.add(new BoardPoint(xPosition + i, yPosition));
            }

            //leftward
            if (xPosition - i < 0) {
                stop_leftward = true;
            }
            if (!stop_leftward) {
                if (board[xPosition - i][yPosition] != null) {
                    stop_leftward = true;
                }
                pointList.add(new BoardPoint(xPosition - i, yPosition));
            }
        }
    }

    @Override
    public boolean validateMove(int toX, int toY) {
        int delta_x;
        int delta_y;
        int condition = Math.abs(xPosition - toY) > Math.abs(yPosition - toY) ? Math.abs(xPosition - toY) : Math.abs(yPosition - toY);

        if (xPosition == toX && yPosition == toY) {
            return false;
        }

        if ((Math.abs(xPosition - toX) != 0 && Math.abs(yPosition - toY) == 0) ||
                (Math.abs(xPosition - toX) == 0 && Math.abs(yPosition - toY) != 0)) {
            for (int i = 1; i < condition; i++) {
                delta_x = (xPosition - toX < 0 ? xPosition + i : (xPosition - toX > 0 ? xPosition - i : xPosition));
                delta_y = (yPosition - toY < 0 ? yPosition + i : (yPosition - toY > 0 ? yPosition - i : yPosition));

                if (board[delta_x][delta_y] != null) {
                    return false;
                }
            }
            return ((board[toX][toY] == null) || (board[toX][toY].color != color));
        }

        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'R' : 'r');
    }

    @Override
    public Rook clone() {
        return (Rook)super.clone();
    }
}
