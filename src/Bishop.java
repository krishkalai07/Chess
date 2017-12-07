import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(int file, int rank, boolean is_white, Piece[][] board) {
        super(file, rank, is_white, board);
    }

    @Override
    public void draw(Graphics g) throws IOException {
        BufferedImage img;
        if (super.isWhite()) {
            String filename = "/WhiteBishop.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackBishop.png";
            img = ImageIO.read(getClass().getResource(filename));
        }

        g.drawImage(img,50*(file+1), 50*(rank+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> point_list) {
        // All left up
        for (int i = 1; file - i >= 0 && rank - i >= 0; i++) {
            if (board[file -i][rank -i] == null) {
                point_list.add(new BoardPoint(file -i, rank -i));
            }
            else {
                if (board[file -i][rank -i].color != this.color) {
                    point_list.add(new BoardPoint(file - i, rank - i));
                }
                break;
            }
        }

        // All left down
        for (int i = 1; file - i >= 0 && rank + i < 8; i++) {
            if (board[file - i][rank + i] == null) {
                point_list.add(new BoardPoint(file - i, rank + i));
            }
            else {
                if (board[file - i][rank + i].color != this.color) {
                    point_list.add(new BoardPoint(file - i, rank + i));
                }
                break;
            }
        }

        // All right down
        for (int i = 1; file + i < 8 && rank + i < 8; i++) {
            if (board[file + i][rank + i] == null) {
                point_list.add(new BoardPoint(file + i, rank + i));
            }
            else {
                if (board[file + i][rank + i].color != this.color) {
                    point_list.add(new BoardPoint(file + i, rank + i));
                }
                break;
            }
        }

        // All right up
        for (int i = 1; file + i < 8 && rank - i >= 0; i++) {
            if (board[file + i][rank - i] == null) {
                point_list.add(new BoardPoint(file + i, rank - i));
            }
            else {
                if (board[file + i][rank - i].color != this.color) {
                    point_list.add(new BoardPoint(file + i, rank - i));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> point_list, Piece[][] board) {
        boolean stop_up_left = false;
        boolean stop_up_right = false;
        boolean stop_down_left = false;
        boolean stop_down_right = false;

        for (int i = 1; !stop_up_left || !stop_up_right || !stop_down_left || !stop_down_right; i++) {
            //upward left
            if (file - i < 0 || rank - i < 0) {
                stop_up_left = true;
            }
            if (!stop_up_left) {
                if (board[file - i][rank - i] != null) {
                    stop_up_left = true;
                }
                point_list.add(new BoardPoint(file - i, rank - i));
            }

            //up right
            if (file + i > 7 || rank - i < 0) {
                stop_up_right = true;
            }
            if (!stop_up_right) {
                if (board[file + i][rank - i] != null) {
                    stop_up_right = true;
                }
                point_list.add(new BoardPoint(file + i, rank - i));
            }

            //down left
            if (file - i < 0 || rank + i > 7) {
                stop_down_left = true;
            }
            if (!stop_down_left) {
                if (board[file - i][rank + i] != null) {
                    stop_down_left = true;
                }
                point_list.add(new BoardPoint(file - i, rank + i));
            }

            //down right
            if (file + i > 7 || rank + i > 7) {
                stop_down_right = true;
            }
            if (!stop_down_right) {
                if (board[file + i][rank + i] != null) {
                    stop_down_right = true;
                }
                point_list.add(new BoardPoint(file + i, rank + i));
            }
        }
    }

    @Override
    public boolean validateMove(int dest_file, int dest_rank) {
        int delta_x;
        int delta_y;

        if (file == dest_file && rank == dest_rank) {
            return false;
        }

        if (Math.abs(file - dest_file) == Math.abs(rank - dest_rank)) {
            for (int i = 1; i < Math.abs(file - dest_file); i++) {
                delta_x = (file - dest_file < 0 ? file + i : (file - dest_file > 0 ? file -i : file));
                delta_y = (rank - dest_rank < 0 ? rank + i : (rank - dest_rank > 0 ? rank -i : rank));
                if (board[delta_x][delta_y] != null) {
                    return false;
                }
            }
            return board[dest_file][dest_rank] == null || board[dest_file][dest_rank].color != color;
        }
        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'B' : 'b');
    }

    @Override
    public Bishop clone() throws CloneNotSupportedException {
        return (Bishop)super.clone();
    }
}
