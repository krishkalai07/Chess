package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Pawn extends Piece {
    private boolean in_5th_row; //for en-passant

    public Pawn(int x_location, int y_location, boolean is_white, Piece[][] board) {
        super(x_location, y_location, 1, is_white, board);
        in_5th_row = false;
    }

    public boolean isIn_5th_row() {
        return in_5th_row;
    }

    public void setIn_5th_row(boolean in_5th_row) {
        this.in_5th_row = in_5th_row;
    }

    public void draw (Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhitePawn.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackPawn.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoves(List<BoardPoint> vector) {
        final int motion_direction = color ? -1 : 1;

        // All moves allow moving one space forwards
        if (board[x_position][y_position + motion_direction] == null) {
            vector.add(new BoardPoint(x_position, y_position + motion_direction));
        }

        if (vector.size() > 0) {
            if (getY_position() == (isWhite() ? 6 : 1)) {
                if (board[x_position][y_position + (2*motion_direction)] == null) {
                    vector.add(new BoardPoint(x_position, y_position + (2*motion_direction)));
                }
            }
        }

        // Testing left
        if (x_position != 0 && board[x_position - 1][y_position + motion_direction] != null) {
            if (board[x_position - 1][y_position + motion_direction].isWhite() != color) {
                vector.add(new BoardPoint(x_position - 1, y_position + motion_direction));
            }
        }

        // Testing right
        if (x_position != 7 && board[x_position + 1][y_position + motion_direction] != null) {
            if (board[x_position + 1][y_position + motion_direction].isWhite() != color) {
                vector.add(new BoardPoint(x_position + 1, y_position + motion_direction));
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> vector) {
        final int motion_direction = isWhite() ? -1 : 1;

        if (x_position != 0) {
            BoardPoint point = new BoardPoint(x_position - 1, y_position + motion_direction);
            if (!vector.contains(point)) {
                vector.add(point);
            }
        }
        if (y_position != 7) {
            BoardPoint point = new BoardPoint(x_position + 1, y_position + motion_direction);
            if (!vector.contains(point)) {
                vector.add(point);
            }
        }
    }
}
