import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class King extends Piece {
    private boolean has_moved; //Used for castling
    private Vector<BoardPoint> controlled_squares;

    public King(int file, int rank, boolean is_white, Piece[][] board, Vector<BoardPoint> controlled_squares) {
        super(file, rank, is_white, board);
        has_moved = false;
        this.controlled_squares = controlled_squares;
    }

    public King(int file, int rank, boolean isWhite, Piece[][] board, Vector<BoardPoint> controlled_squares, boolean has_moved) {
        super(file, rank, isWhite, board);
        this.has_moved = has_moved;
        this.controlled_squares = controlled_squares;
    }

    private boolean vectorContainsPoint(List<BoardPoint> point_list, int x, int y) {
        for (BoardPoint aPointList : point_list) {
            if (x == aPointList.x && y == aPointList.y) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck () {
        updateControlledSquares();
        return vectorContainsPoint(controlled_squares, file, rank);
    }

    public boolean hasMoved() {
        return has_moved;
    }

    public void setHasMoved(boolean has_moved) {
        this.has_moved = has_moved;
    }

    private void updateControlledSquares() {
        controlled_squares.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].color != color) {
                    board[i][j].getControlledSquares(controlled_squares);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) throws IOException {
        BufferedImage img;

        if (super.isWhite()) {
            String filename = "/WhiteKing.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackKing.png";
            img = ImageIO.read(getClass().getResource(filename));
        }

        g.drawImage(img,50*(file+1), 50*(rank+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> point_list) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && j == 0) {
                    continue;
                }

                if (file + i >= 0 && file + i < 8 && rank + j >= 0 && rank + j < 8) {
                    if (board[file + i][rank + j] == null) {
                        BoardPoint point = new BoardPoint(file + i, rank + j);
                        if (this.color && !vectorContainsPoint(controlled_squares, point.x, point.y)) {
                            point_list.add(point);
                        }
                        else if (!this.color && !vectorContainsPoint(controlled_squares, point.x, point.y)) {
                            point_list.add(point);
                        }
                    }
                    else {
                        if (this.color != board[file + i][rank + j].color) {
                            point_list.add(new BoardPoint(file + i, rank + j));
                        }
                    }
                }
            }
        }

        // Castling
        if (!has_moved) {
            if (vectorContainsPoint(controlled_squares, file, rank)) {
                return;
            }

            // Castling king-side
            if (board[file +1][rank] == null && board[file +2][rank] == null) {
                if (!vectorContainsPoint(controlled_squares, file + 1, rank) && !vectorContainsPoint(controlled_squares, file + 2, rank)) {
                    if (board[file + 3][rank] != null && board[file + 3][rank] instanceof Rook && !((Rook)board[file + 3][rank]).hasMoved()) {
                        point_list.add(new BoardPoint(file + 2, rank));
                    }
                }
            }
            // Castling queen-side
            if (board[file -1][rank] == null && board[file -2][rank] == null && board[file -3][rank] == null) {
                if (!vectorContainsPoint(controlled_squares, file - 1, rank) && !vectorContainsPoint(controlled_squares, file - 2, rank)) {
                    if (board[file - 4][rank] != null && board[file - 4][rank] instanceof Rook && !((Rook)(board[file - 4][rank])).hasMoved()) {
                        point_list.add(new BoardPoint(file - 2, rank));
                    }
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && j == 0) {
                    continue;
                }

                if (file +i >= 0 && file +i < 8 && rank +j >= 0 && rank +j < 8) {
                    pointList.add(new BoardPoint(file +i, rank +j));
                }
            }
        }
    }

    @Override
    public boolean validateMove(int toX, int toY) {
        if (file == toX && rank == toY) {
            return false;
        }

        updateControlledSquares();

        if (Math.abs(file - toX) == 2) {
            if (!has_moved) {
                Rook edge_rook = (Rook)(file - toX == 2 ? board[7][(color ? 7 : 0)] : board[0][(color ? 7 : 0)]);
                if (edge_rook != null && !edge_rook.hasMoved()) {
                    if ((toX - file > 0 && board[file +1][rank] == null && board[file +2][rank] == null) ||
                            (toX - file < 0 && board[file -1][rank] == null && board[file -2][rank] == null)) {
                        if (!isInCheck()) {
                            if (!vectorContainsPoint(controlled_squares, file - toX > 0 ? file - 1 : file + 1, rank) &&
                                    !vectorContainsPoint(controlled_squares, file - toX > 0 ? file - 2 : file + 2, rank)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        else {
            if (Math.abs(file - toX) <= 1 && Math.abs(rank - toY) <= 1) {
                if (!vectorContainsPoint(controlled_squares, toX, toY)) {
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

    @Override
    public King clone() throws CloneNotSupportedException {
        return (King)super.clone();
    }
}
