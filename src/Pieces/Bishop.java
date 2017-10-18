package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(int x_position, int y_position, int capture_weight, boolean is_white, Piece[][] board) {
        super(x_position, y_position, 0, is_white, board);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteBishop.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackBishop.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read: Bishop");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoves(List<BoardPoint> vector) {
        // All left up
        for (int i = 1; x_position - i >= 0 && y_position - i >= 0; i++) {
            if (board[x_position-i][y_position-i] == null) {
                vector.add(new BoardPoint(x_position-i, y_position-i));
            }
            else {
                if (board[x_position-i][y_position-i].isWhite() != color) {
                    vector.add(new BoardPoint(x_position - i, y_position - i));
                }
                break;
            }
        }

        // All left down
        for (int i = 1; x_position- i >= 0 && y_position + i < 8; i++) {
            if (board[x_position - i][y_position + i] == null) {
                vector.add(new BoardPoint(x_position - i, y_position + i));
            }
            else {
                if (board[x_position - i][y_position + i].color != this.color) {
                    vector.add(new BoardPoint(x_position - i, y_position + i));
                }
                break;
            }
        }

        // All right down
        for (int i = 1; x_position + i < 8 && y_position + i < 8; i++) {
            if (board[x_position + i][y_position + i] == null) {
                vector.add(new BoardPoint(x_position + i, y_position + i));
            }
            else {
                if (board[x_position + i][y_position + i].isWhite() != color) {
                    vector.add(new BoardPoint(x_position + i, y_position + i));
                }
                break;
            }
        }

        // All right up
        for (int i = 1; x_position + i < 8 && y_position - i >= 0; i++) {
            if (board[x_position+ i][y_position - i] == null) {
                vector.add(new BoardPoint(x_position + i, y_position - i));
            }
            else {
                if (board[x_position + i][y_position - i].color != this.color) {
                    vector.add(new BoardPoint(x_position + i, y_position - i));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> vector) {
        // All left up
        for (int i = 1; x_position - i >= 0 && y_position - i >= 0; i++) {
            vector.add(new BoardPoint(x_position - i, y_position - i));
            if (board[x_position - i][y_position - i] != null) {
                break;
            }
        }

        // All left down
        for (int i = 1; x_position - i >= 0 && y_position + i < 8; i++) {
            vector.add(new BoardPoint(x_position - i, y_position + i));
            if (board[x_position - i][y_position + i] != null) {
                break;
            }

        }

        // All right down
        for (int i = 1; x_position + i < 8 && y_position + i < 8; i++) {
            vector.add(new BoardPoint(x_position + i, y_position + i));
            if (board[x_position + i][y_position + i] != null) {
                break;
            }
        }

        // All right up
        for (int i = 1; x_position + i < 8 && y_position - i >= 0; i++) {
            vector.add(new BoardPoint(x_position + i, y_position - i));
            if (board[x_position + i][y_position - i] != null) {
                break;
            }
        }
    }
}
