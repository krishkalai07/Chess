import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(int xPosition, int yPosition, boolean isWhite, Piece[][] board) {
        super(xPosition, yPosition, isWhite, board);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                //String filename = "vc_assets/WhiteBishop.png";
                //img = ImageIO.read(new File(filename));
                String filename = "/WhiteBishop.png";
                img = ImageIO.read(getClass().getResource(filename));
            }
            else {
//                String filename = "vc_assets/BlackBishop.png";
//                img = ImageIO.read(new File(filename));
                String filename = "/BlackBishop.png";
                img = ImageIO.read(getClass().getResource(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getXPosition()+1), 50*(getYPosition()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        // All left up
        for (int i = 1; xPosition - i >= 0 && yPosition - i >= 0; i++) {
            if (board[xPosition-i][yPosition-i] == null) {
                pointList.add(new BoardPoint(xPosition-i, yPosition-i));
            }
            else {
                if (board[xPosition-i][yPosition-i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition - i, yPosition - i));
                }
                break;
            }
        }

        // All left down
        for (int i = 1; xPosition- i >= 0 && yPosition + i < 8; i++) {
            if (board[xPosition - i][yPosition + i] == null) {
                pointList.add(new BoardPoint(xPosition - i, yPosition + i));
            }
            else {
                if (board[xPosition - i][yPosition + i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition - i, yPosition + i));
                }
                break;
            }
        }

        // All right down
        for (int i = 1; xPosition + i < 8 && yPosition + i < 8; i++) {
            if (board[xPosition + i][yPosition + i] == null) {
                pointList.add(new BoardPoint(xPosition + i, yPosition + i));
            }
            else {
                if (board[xPosition + i][yPosition + i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition + i, yPosition + i));
                }
                break;
            }
        }

        // All right up
        for (int i = 1; xPosition + i < 8 && yPosition - i >= 0; i++) {
            if (board[xPosition+ i][yPosition - i] == null) {
                pointList.add(new BoardPoint(xPosition + i, yPosition - i));
            }
            else {
                if (board[xPosition + i][yPosition - i].color != this.color) {
                    pointList.add(new BoardPoint(xPosition + i, yPosition - i));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList) {
        getControlledSquares(pointList, board);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        boolean stop_up_left = false;
        boolean stop_up_right = false;
        boolean stop_down_left = false;
        boolean stop_down_right = false;

        for (int i = 1; !stop_up_left || !stop_up_right || !stop_down_left || !stop_down_right; i++) {
            //upward left
            if (xPosition - i < 0 || yPosition - i < 0) {
                stop_up_left = true;
            }
            if (!stop_up_left) {
                if (board[xPosition - i][yPosition - i] != null) {
                    stop_up_left = true;
                }
                pointList.add(new BoardPoint(xPosition - i, yPosition - i));
            }

            //up right
            if (xPosition + i > 7 || yPosition - i < 0) {
                stop_up_right = true;
            }
            if (!stop_up_right) {
                if (board[xPosition + i][yPosition - i] != null) {
                    stop_up_right = true;
                }
                pointList.add(new BoardPoint(xPosition + i, yPosition - i));
            }

            //down left
            if (xPosition - i < 0 || yPosition + i > 7) {
                stop_down_left = true;
            }
            if (!stop_down_left) {
                if (board[xPosition - i][yPosition + i] != null) {
                    stop_down_left = true;
                }
                pointList.add(new BoardPoint(xPosition - i, yPosition + i));
            }

            //down right
            if (xPosition + i > 7 || yPosition + i > 7) {
                stop_down_right = true;
            }
            if (!stop_down_right) {
                if (board[xPosition + i][yPosition + i] != null) {
                    stop_down_right = true;
                }
                pointList.add(new BoardPoint(xPosition + i, yPosition + i));
            }
        }
    }

    @Override
    public boolean validateMove(int toX, int toY) {
        int delta_x;
        int delta_y;

        if (xPosition == toX && yPosition == toY) {
            return false;
        }

        if (Math.abs(xPosition - toX) == Math.abs(yPosition - toY)) {
            for (int i = 1; i < Math.abs(xPosition - toX); i++) {
                delta_x = (xPosition - toX < 0 ? xPosition + i : (xPosition - toX > 0 ? xPosition-i : xPosition));
                delta_y = (yPosition - toY < 0 ? yPosition + i : (yPosition - toY > 0 ? yPosition-i : yPosition));
                if (board[delta_x][delta_y] != null) {
                    return false;
                }
            }
            return board[toX][toY] == null || board[toX][toY].color != color;
        }
        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'B' : 'b');
    }

    @Override
    public Bishop clone() {
        return (Bishop)super.clone();
    }
}
