import java.util.List;
import java.util.Vector;

import static java.lang.Math.abs;

public class MoveValidation {
    private static transient volatile int BOARD_SIZE = 8;

    public static boolean simulate_move(Piece[][] board, int from_x, int from_y, int to_x, int to_y, boolean turn) throws NoKingException {
        Vector<BoardPoint> controlled_squares = new Vector<>();
        King king = null;

        move_piece(board, from_x, from_y, to_x, to_y);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].isWhite() != turn) {
                        board[i][j].getControlledSquares(controlled_squares, board);
                    }
                    if (board[i][j].isWhite() == turn && board[i][j] instanceof King) {
                        king = (King)board[i][j];
                    }
                }
            }
        }

        if (king == null) {
            throw new NoKingException("King is null");
        }

        if (vectorContainsPoint(controlled_squares, king.getFile(), king.getRank())) {
            move_piece(board, to_x, to_y, from_x, from_y);
            return false;
        }

        move_piece(board, to_x, to_y, from_x, from_y);
        return true;
    }

    public static void move_piece(Piece[][] board, int from_x, int from_y, int to_x, int to_y) {
        Piece viewing_piece = board[from_x][from_y];
        board[to_x][to_y] = viewing_piece;
        viewing_piece.setFile(to_x);
        viewing_piece.setRank(to_y);
        board[from_x][from_y] = null;
    }

    boolean is_legal_move (Piece[][] board, int from_x, int from_y, int to_x, int to_y, King king) {
        move_piece(board, from_x, from_y, to_x, to_y);

        if (board[from_x][from_y] instanceof King) {
            int direction = get_relative_location(board, from_x, from_y, king);
            if (direction == 1) {
                move_piece(board, to_x, to_y, from_x, from_y);
                return true;
            }

            int tmp_x = king.getFile();
            int tmp_y = king.getRank();

            while (true) {
                if (direction % 2 == 0) {
                    tmp_x++;
                }
                if (direction % 3 == 0) {
                    tmp_x--;
                }
                if (direction % 5 == 0) {
                    tmp_y--;
                }
                if (direction % 7 == 0) {
                    tmp_y++;
                }

                if (tmp_x < 0 || tmp_x >= 8 || tmp_y < 0 || tmp_y >= 8) {
                    move_piece(board, to_x, to_y, from_x, from_y);
                    return true;
                }

                if (board[tmp_x][tmp_y] != null) {
                    if (direction == 2 || direction == 3 || direction == 5 || direction == 7) {
                        if (board[tmp_x][tmp_y].isWhite() != king.isWhite()) {
                            if (board[tmp_x][tmp_y] instanceof Rook || board[tmp_x][tmp_y] instanceof Queen) {
                                move_piece(board, to_x, to_y, from_x, from_y);
                                if (tmp_x == to_x && tmp_y == to_y) {
                                    return board[from_x][from_y].isWhite() == king.isWhite();
                                } else {
                                    return board[tmp_y][tmp_y].isWhite() == king.isWhite();
                                }
                            } else {
                                move_piece(board, to_x, to_y, from_x, from_y);
                                return true;
                            }
                        }
                        else {
                            move_piece(board, to_x, to_y, from_x, from_y);
                            return true;
                        }
                    }
                    else if (direction == 10 || direction == 14 || direction == 25 || direction == 21) {
                        if (board[tmp_x][tmp_y].isWhite() != king.isWhite()) {
                            if (board[tmp_x][tmp_y] instanceof Queen || board[tmp_x][tmp_y] instanceof King) {
                                move_piece(board, to_x, to_y, from_x, from_y);
                                if (tmp_x == to_x && tmp_y == to_y) {
                                    return board[from_x][from_y].isWhite() == king.isWhite();
                                }
                                else {
                                    return board[tmp_y][tmp_y].isWhite() == king.isWhite();
                                }
                            }
                            else {
                                move_piece(board, to_x, to_y, from_x, from_y);
                                return true;
                            }
                        }
                    else {
                            move_piece(board, to_x, to_y, from_x, from_y);
                            return true;
                        }
                    }
                }
            }
        }
        else {
            move_piece(board, to_x, to_y, from_x, from_y);
            Vector<BoardPoint> point_list = new Vector<>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    point_list.clear();
                    if (i == 0 && i == j) {
                        continue;
                    }

                    Attack.attack_per_direction(board, point_list, to_x, to_y, i, j, false);

                    for (BoardPoint p : point_list) {
                        if (board[p.x][p.y] != null) {
                            if ((i == 0 || j == 0) && (board[p.x][p.y] instanceof Rook || board[p.x][p.y] instanceof Queen)) {
                                return false;
                            }
                            if ((abs(i) == 1 && abs(j) == 1) && (board[p.x][p.y] instanceof Bishop || board[p.x][p.y] instanceof Queen)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        move_piece(board, to_x, to_y, from_x, from_y);
        return true;
    }

    public static int get_relative_location (Piece[][] board, int from_x, int from_y, King king) {
        int king_x = king.getFile();
        int king_y = king.getRank();
        if (king_x == from_x) {
            return king_y - from_y > 0 ? 5 : 7;
        }
        else if (king_y == from_y) {
            return king_x - from_x > 0 ? 2 : 3;
        }
        else if (king_y - from_y == -(king_x - from_x)) {
            return king_y - from_y > 0 ? 10 : 21;
        }
        else if (king_y - from_y == king_x - from_x) {
            return king_y - from_y > 0 ? 15 : 14;
        }
        else {
            return 1;
        }
    }

    private static boolean vectorContainsPoint(List<BoardPoint> pointList, int x, int y) {
        for (BoardPoint point : pointList) {
            if (x == point.x && y == point.y) {
                return true;
            }
        }
        return false;
    }
}
