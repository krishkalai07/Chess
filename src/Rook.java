import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Rook extends Piece {
    /**
     * Used for individual side castling rights.
     */
    private boolean did_move;

    public Rook(int file, int rank, boolean is_white, Piece[][] board) {
        super(file, rank, is_white, board);
        did_move = false;
    }

    public boolean hasMoved() {
        return did_move;
    }

    public void setHasMoved(boolean did_move) {
        this.did_move = did_move;
    }

    @Override
    public void draw(Graphics g) throws IOException {
        BufferedImage img;
        if (super.color) {
            String filename = "/WhiteRook.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackRook.png";
            img = ImageIO.read(getClass().getResource(filename));
        }

        g.drawImage(img,50*(getFile()+1), 50*(getRank()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> point_list) {
        // Up
        for (int i = 1; rank - i >= 0; i++) {
            if (board[file][rank -i] == null) {
                point_list.add(new BoardPoint(file, rank - i));
            }
            else {
                if (board[file][rank - i].color != this.color) {
                    point_list.add(new BoardPoint(file, rank - i));
                }
                break;
            }
        }

        // Right
        for (int i = 1; file + i < 8; i++) {
            if (board[file +i][rank] == null) {
                point_list.add(new BoardPoint(file +i, rank));
            }
            else {
                if (board[file +i][rank].color != this.color) {
                    point_list.add(new BoardPoint(file + i, rank));
                }
                break;
            }
        }

        // Down
        for (int i = 1; rank + i < 8; i++) {
            if (board[file][rank +i] == null) {
                point_list.add(new BoardPoint(file, rank + i));
            }
            else {
                if (board[file][rank + i].color != this.color) {
                    point_list.add(new BoardPoint(file, rank + i));
                }
                break;
            }
        }

        //Left
        for (int i = 1; file - i >= 0; i++) {
            if (board[file - i][rank] == null) {
                point_list.add(new BoardPoint(file - i, rank));
            }
            else {
                if (board[file - i][rank].color != this.color) {
                    point_list.add(new BoardPoint(file - i, rank));
                }
                break;
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> point_list, Piece[][] board) {
        boolean stop_upward = false;
        boolean stop_rightward = false;
        boolean stop_downward = false;
        boolean stop_leftward = false;

        for (int i = 1; !stop_upward || !stop_downward || !stop_leftward || !stop_rightward; i++) {
            //upward
            if (rank - i < 0) {
                stop_upward = true;
            }
            if (!stop_upward) {
                if (board[file][rank - i] != null) {
                    stop_upward = true;
                }
                point_list.add(new BoardPoint(file, rank - i));
            }

            //downward
            if (rank + i > 7) {
                stop_downward = true;
            }
            if (!stop_downward) {
                if (board[file][rank + i] != null) {
                    stop_downward = true;
                }
                point_list.add(new BoardPoint(file, rank + i));
            }

            //rightward
            if (file + i > 7) {
                stop_rightward = true;
            }
            if (!stop_rightward) {
                if (board[file + i][rank] != null) {
                    stop_rightward = true;
                }
                point_list.add(new BoardPoint(file + i, rank));
            }

            //leftward
            if (file - i < 0) {
                stop_leftward = true;
            }
            if (!stop_leftward) {
                if (board[file - i][rank] != null) {
                    stop_leftward = true;
                }
                point_list.add(new BoardPoint(file - i, rank));
            }
        }
    }

    @Override
    public boolean validateMove(int dest_file, int dest_rank) {
        int delta_x;
        int delta_y;
        int condition = Math.abs(file - dest_rank) > Math.abs(rank - dest_rank) ? Math.abs(file - dest_rank) : Math.abs(rank - dest_rank);

        if (file == dest_file && rank == dest_rank) {
            return false;
        }

        if ((Math.abs(file - dest_file) != 0 && Math.abs(rank - dest_rank) == 0) ||
                (Math.abs(file - dest_file) == 0 && Math.abs(rank - dest_rank) != 0)) {
            for (int i = 1; i < condition; i++) {
                delta_x = (file - dest_file < 0 ? file + i : (file - dest_file > 0 ? file - i : file));
                delta_y = (rank - dest_rank < 0 ? rank + i : (rank - dest_rank > 0 ? rank - i : rank));

                if (board[delta_x][delta_y] != null) {
                    return false;
                }
            }
            return ((board[dest_file][dest_rank] == null) || (board[dest_file][dest_rank].color != color));
        }

        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'R' : 'r');
    }

    @Override
    public Rook clone() throws CloneNotSupportedException {
        return (Rook)super.clone();
    }
}
