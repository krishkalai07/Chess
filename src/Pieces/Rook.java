package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Rook extends Piece {
    private boolean did_move; // Used for castling.

    public Rook(int x_position, int y_position, boolean is_white, Piece[][] board) {
        super(x_position, y_position, 5, is_white, board);
        did_move = false;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
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
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    public boolean did_move() {
        return did_move;
    }

    public void set_move(boolean did_move) {
        this.did_move = did_move;
    }

    @Override
    public void getPossibleMoves(List<BoardPoint> vector) {
        // Up
        for (int i = 1; y_position - i >= 0; i++) {
            if (board[x_position][y_position-i] == null) {
                vector.add(new BoardPoint(x_position, y_position - i));
            }
            else {
                if (board[x_position][y_position - i].color != this.color) {
                    vector.add(new BoardPoint(x_position, y_position - i));
                }
                break;
            }
        }

        // Right
        for (int i = 1; x_position + i < 8; i++) {
            if (board[x_position+i][y_position] == null) {
                vector.add(new BoardPoint(x_position+i, y_position));
            }
            else {
                if (board[x_position+i][y_position].color != this.color) {
                    vector.add(new BoardPoint(x_position + i, y_position));
                }
                break;
            }
        }

        // Down
        for (int i = 1; y_position + i < 8; i++) {
            if (board[x_position][y_position+i] == null) {
                vector.add(new BoardPoint(x_position, y_position + i));
            }
            else {
                if (board[x_position][y_position + i].isWhite() != this.color) {
                    vector.add(new BoardPoint(x_position, y_position + i));
                }
                break;
            }
        }

        //Left
        for (int i = 1; x_position - i >= 0; i++) {
            if (board[x_position-i][y_position] == null) {
                vector.add(new BoardPoint(x_position - i, y_position));
            }
            else {
                if (board[x_position - i][y_position].color != this.color) {
                    vector.add(new BoardPoint(x_position - i, y_position));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> vector) {
        // Up
        for (int i = 1; y_position - i >= 0; i++) {
            vector.add(new BoardPoint(x_position, y_position - i));
            if (board[x_position][y_position-i] != null) {
                break;
            }
        }

        // Right
        for (int i = 1; x_position + i < 8; i++) {
            vector.add(new BoardPoint(x_position+i, y_position));
            if (board[x_position+i][y_position] != null) {
                break;
            }
        }

        // Down
        for (int i = 1; y_position + i < 8; i++) {
            vector.add(new BoardPoint(x_position, y_position + i));
            if (board[x_position][y_position+i] != null) {
                break;
            }
        }

        //Left
        for (int i = 1; x_position - i >= 0; i++) {
            vector.add(new BoardPoint(x_position - i, y_position));
            if (board[x_position-i][y_position] != null) {
                break;
            }
        }
    }
}
