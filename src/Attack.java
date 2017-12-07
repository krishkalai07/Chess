import java.util.List;

public class Attack {
    public static void attack_per_direction (Piece[][] board, List<BoardPoint> point_list, int from_x, int from_y, int delta_x, int delta_y, boolean check_for_same_color) {
        for (int target_x = from_x + delta_x, target_y = from_y + delta_y; target_x >= 0 && target_x < 8 && target_y >= 0 && target_y < 8; target_x += delta_x, target_y += delta_y) {
            if (board[target_x][target_y] == null) {
                // Square is empty, unconditionally push it
                point_list.add(new BoardPoint(target_x, target_y));
            }
            else {
                if (check_for_same_color || board[target_x][target_y].isWhite() != board[target_x][target_y].isWhite()) {
                    // Square has a piece on it, add it conditionally.
                    point_list.add(new BoardPoint(target_x, target_y));
                }
                break;
            }
        }
    }

    public static void queen_attack (Piece[][] board, List<BoardPoint> point_list, int from_x, int from_y, boolean check_for_same_color) {
        bishop_attack(board, point_list, from_x, from_y, check_for_same_color);
        rook_attack(board, point_list, from_x, from_y, check_for_same_color);
    }

    /**
     *
     *
     * @param board
     * @param point_list
     * @param from_x
     * @param from_y
     * @param check_for_same_color
     */
    public static void rook_attack (Piece[][] board, List<BoardPoint> point_list, int from_x, int from_y, boolean check_for_same_color) {
        attack_per_direction(board, point_list, from_x, from_y, 0, 1, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, 1, 0, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, -1, 0, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, 0, -1, check_for_same_color);
    }

    public static void bishop_attack (Piece[][] board, List<BoardPoint> point_list, int from_x, int from_y, boolean check_for_same_color) {
        attack_per_direction(board, point_list, from_x, from_y, -1, -1, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, -1, 1, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, 1, -1, check_for_same_color);
        attack_per_direction(board, point_list, from_x, from_y, 1, 1, check_for_same_color);
    }
}
