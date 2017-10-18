package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class King extends Piece {
    private boolean is_check;
    private boolean has_moved; //Used for castling
    private Vector<BoardPoint> white_control;
    private Vector<BoardPoint> black_control;

    public King(int x_position, int y_position, boolean is_white, Piece[][] board, Vector<BoardPoint> white_control, Vector<BoardPoint> black_control) {
        super(x_position, y_position, 0, is_white, board);
        is_check = false;
        has_moved = false;
        this.white_control = white_control;
        this.black_control = black_control;
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
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    public boolean isCheck() {
        return is_check;
    }

    public void setCheck(boolean is_check) {
        this.is_check = is_check;
    }

    public boolean has_moved() {
        return has_moved;
    }

    public void set_has_moved(boolean has_moved) {
        this.has_moved = has_moved;
    }

    private void fillNormalKingMoves(List<BoardPoint> vector) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && j == 0) {
                    continue;
                }

                if (x_position+i >= 0 && x_position+i < 8 && y_position+j >= 0 && y_position+j < 8) {
                    if (board[x_position+i][y_position+j] == null) {
                        BoardPoint point = new BoardPoint(x_position+i, y_position+j);
                        System.out.println("Point: " + point);
                        //System.out.println("Contains: " + containsPointInVector(black_control, point));
                        if (this.color && !containsPointInVector(black_control, point)) {
                            vector.add(new BoardPoint(x_position+i, y_position+j));
                        }
                        else if (!this.color && !containsPointInVector(white_control, point)) {
                            vector.add(new BoardPoint(x_position+i, y_position+j));
                        }
                    }
                    else {
                        if (this.color != board[x_position+i][y_position+j].isWhite()) {
                            vector.add(new BoardPoint(x_position+i, y_position+j));
                        }
                    }
                }
            }
        }
    }


    public boolean containsPointInVector(List<BoardPoint> points, BoardPoint point) {
        for (BoardPoint p : points) {
            if (p.equals(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getPossibleMoves(List<BoardPoint> vector) {
        fillNormalKingMoves(vector);

        // Castling
        if (!has_moved) {
            // Castling king-side
            if (board[x_position+1][y_position] == null && board[x_position+2][y_position] == null) {
                if (board[x_position+3][y_position] != null && board[x_position+3][y_position].getClass() == Rook.class && !((Rook)board[x_position+3][y_position]).did_move()) {
                    vector.add(new BoardPoint(x_position + 2, y_position));
                }
            }
            // Castling queen-side
            if (board[x_position-1][y_position] == null && board[x_position-2][y_position] == null && board[x_position-3][y_position] == null) {
                if (board[x_position-4][y_position] != null && board[x_position-4][y_position].getClass() == Rook.class && !((Rook)board[x_position-4][y_position]).did_move()) {
                    vector.add(new BoardPoint(x_position - 2, y_position));
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> vector) {
        fillNormalKingMoves(vector);
    }
}
