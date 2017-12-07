import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Queen extends Piece {
    public Queen(int file, int rank, boolean is_white, Piece[][] board) {
        super(file, rank, is_white, board);
    }

    @Override
    public void draw(Graphics g) throws IOException {
        BufferedImage img;
        if (super.isWhite()) {
            String filename = "/WhiteQueen.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackQueen.png";
            img = ImageIO.read(getClass().getResource(filename));
        }

        g.drawImage(img,50*(getFile()+1), 50*(getRank()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> point_list) {
        // All left up
        for (int i = 1; file - i >= 0 && rank - i >= 0; i++) {
            if (board[file -i][rank -i] == null) {
                point_list.add(new BoardPoint(file -i, rank -i));
            }
            else {
                if (board[file -i][rank -i].color != color) {
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
                if (board[file + i][rank + i].color != color) {
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
            if (board[file -i][rank] == null) {
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
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        boolean stop_up_left = false;
        boolean stop_up_right = false;
        boolean stop_down_left = false;
        boolean stop_down_right = false;
        boolean stop_upward = false;
        boolean stop_rightward = false;
        boolean stop_downward = false;
        boolean stop_leftward = false;

        for (int i = 1; !stop_up_left || !stop_up_right || !stop_down_left || !stop_down_right || !stop_upward || !stop_downward || !stop_leftward || !stop_rightward; i++) {
            //upward left
            if (file - i < 0 || rank - i < 0) {
                stop_up_left = true;
            }
            if (!stop_up_left) {
                if (board[file - i][rank - i] != null) {
                    stop_up_left = true;
                }
                pointList.add(new BoardPoint(file - i, rank - i));
            }

            //up right
            if (file + i > 7 || rank - i < 0) {
                stop_up_right = true;
            }
            if (!stop_up_right) {
                if (board[file + i][rank - i] != null) {
                    stop_up_right = true;
                }
                pointList.add(new BoardPoint(file + i, rank - i));
            }

            //down left
            if (file - i < 0 || rank + i > 7) {
                stop_down_left = true;
            }
            if (!stop_down_left) {
                if (board[file - i][rank + i] != null) {
                    stop_down_left = true;
                }
                pointList.add(new BoardPoint(file - i, rank + i));
            }

            //down right
            if (file + i > 7 || rank + i > 7) {
                stop_down_right = true;
            }
            if (!stop_down_right) {
                if (board[file + i][rank + i] != null) {
                    stop_down_right = true;
                }
                pointList.add(new BoardPoint(file + i, rank + i));
            }

            //upward
            if (rank - i < 0) {
                stop_upward = true;
            }
            if (!stop_upward) {
                if (board[file][rank - i] != null) {
                    stop_upward = true;
                }
                pointList.add(new BoardPoint(file, rank - i));
            }

            //downward
            if (rank + i > 7) {
                stop_downward = true;
            }
            if (!stop_downward) {
                if (board[file][rank + i] != null) {
                    stop_downward = true;
                }
                pointList.add(new BoardPoint(file, rank + i));
            }

            //rightward
            if (file + i > 7) {
                stop_rightward = true;
            }
            if (!stop_rightward) {
                if (board[file + i][rank] != null) {
                    stop_rightward = true;
                }
                pointList.add(new BoardPoint(file + i, rank));
            }

            //leftward
            if (file - i < 0) {
                stop_leftward = true;
            }
            if (!stop_leftward) {
                if (board[file - i][rank] != null) {
                    stop_leftward = true;
                }
                pointList.add(new BoardPoint(file - i, rank));
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

        if ((Math.abs(file - dest_file) != 0 && Math.abs(rank - dest_rank) == 0) ||
                (Math.abs(file - dest_file) == 0 && Math.abs(rank - dest_rank) != 0) ||
                (Math.abs(file - dest_file) == Math.abs(rank - dest_rank))) {
            int condition = Math.abs(file - dest_file) > Math.abs(rank - dest_rank) ? Math.abs(file - dest_file) : Math.abs(rank - dest_rank);
            for (int i = 1; i < condition; i++) {
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
        return (color ? 'Q' : 'q');
    }

    @Override
    public Queen clone() throws CloneNotSupportedException {
        return (Queen)super.clone();
    }
}
