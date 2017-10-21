import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

/** ViewController for this game. This holds most of the logic and graphics.
 * class synopsis:
 * id   ViewController ();
 * void paintComponent ();
 * void printBoard ();
 * void initialize_board();
 * void fillPossibleMovesForPawn(Pawn pawn);
 * void fillPossibleMovesForKnight(Knight knight);
 * void fillPossibleMovesForBishop(Bishop bishop);
 * void fillPossibleMovesForRook(Rook rook);
 * void fillPossibleMovesForQueen(Queen queen);
 * void fillPossibleMovesForKing (King king);
 * void fillAllMovesDiagonally (Piece piece);
 * void fillAllMovesVertically (Piece piece);
 * void fillPossibleMoves(Piece piece);
 */
public class ViewController extends JPanel implements MouseListener {
    private boolean turn;
    private Piece board[][];
    private Piece currently_viewing;
    private King white_king;
    private King black_king;
    private Vector<BoardPoint> possible_moves;
    private Vector<BoardPoint> white_control;
    private Vector<BoardPoint> black_control;

    ViewController() {
        setSize(600, 600);
        setBackground(new Color(0x000000));

        turn = true;
        board = new Piece[8][8];
        currently_viewing = null;
        possible_moves = new Vector<>();
        white_control = new Vector<>();
        black_control = new Vector<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        initialize_board();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].getControlledSquares(board[i][j].isWhite() ? white_control : black_control);
                }
            }
        }

        addMouseListener(this);
    }

    /**
     * Paints the board and the pieces.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor(g.getColor()==Color.DARK_GRAY ? Color.LIGHT_GRAY:Color.DARK_GRAY);
                g.fillRect(50*(i+1), 50*(j+1), 50, 50);
            }
            g.setColor(g.getColor()==Color.DARK_GRAY ? Color.LIGHT_GRAY:Color.DARK_GRAY);
        }

        Color light_green = new Color(50,205,50);
        Color dark_green = new Color(0,100,0);

        g.setColor(Color.GREEN);
        for (Pieces.BoardPoint point : possible_moves) {
        //for (Pieces.BoardPoint point : white_control) {
        //for (BoardPoint point : black_control) {
            int x = point.x;
            int y = point.y;

            if (x % 2 == y % 2) {
                g.setColor(light_green);
            }
            else {
                g.setColor(dark_green);
            }

            g.fillRect(50*(x+1), 50*(y+1), 50, 50);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].draw(g);
                }
            }
        }
    }

    /**
     * Outputs the board in console for the case that graphics isn't synced with the array
     */
    private void printBoard() {
        for (Piece[] row : board) {
            for (Piece tile : row) {
                if (tile == null) {
                    System.out.printf(" ");
                }
                else if (tile.getClass() == Pawn.class) {
                    System.out.printf("P");
                }
                else if (tile.getClass() == Rook.class) {
                    System.out.printf("R");
                }
                else if (tile.getClass() == Knight.class) {
                    System.out.printf("N");
                }
                else if (tile.getClass() == Bishop.class) {
                    System.out.printf("B");
                }
                else if (tile.getClass() == Queen.class) {
                    System.out.printf("Q");
                }
                else if (tile.getClass() == King.class) {
                    System.out.printf("K");
                }
            }
            System.out.println();
        }
    }

    /**
     * Sets pieces such as how you a player starts a game.
     */
    private void initialize_board() {
        //Rook
        board[0][0] = new Rook(0, 0,false, board);
        board[7][0] = new Rook(7, 0,false, board);
        board[0][7] = new Rook(0, 7,true, board);
        board[7][7] = new Rook(7, 7,true, board);

        //Knight
        board[1][0] = new Knight(1, 0,false, board);
        board[6][0] = new Knight(6, 0,false, board);
        board[1][7] = new Knight(1, 7,true, board);
        board[6][7] = new Knight(6, 7,true, board);

        //Bishop
        board[2][0] = new Bishop(2,0,false, board);
        board[5][0] = new Bishop(5,0,false, board);
        board[2][7] = new Bishop(2,7,true, board);
        board[5][7] = new Bishop(5,7,true, board);

        //Queen
        board[3][0] = new Queen(3, 0,false, board);
        board[3][7] = new Queen(3, 7,true, board);

        //King
        board[4][0] = new King(4, 0,false, board, white_control, black_control);
        black_king = (King)board[4][0];
        board[4][7] = new King(4, 7,true, board, white_control, black_control);
        white_king = (King)board[4][7];

        //Pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(i, 1,false, board);
        }

        for (int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(i, 6,true, board);
        }
    }

    private boolean containsPointInVector(List<BoardPoint> points, BoardPoint point) {
        for (BoardPoint p : points) {
            if (p.equals(point)) {
                return true;
            }
        }
        return false;
    }

    private void move_piece (int x_tile, int y_tile) {
        int prev_x = currently_viewing.getX_position();
        int prev_y = currently_viewing.getY_position();

        board[x_tile][y_tile] = currently_viewing;
        currently_viewing.setX_position(x_tile);
        currently_viewing.setY_position(y_tile);
        board[prev_x][prev_y] = null;

        if (currently_viewing.getClass() == Rook.class) {
            ((Rook) currently_viewing).set_move(true);
        }
        else if (currently_viewing.getClass() == King.class) {
            ((King)currently_viewing).set_has_moved(true);
            int row = currently_viewing.isWhite() ? 7 : 0;

            // Apply Castling king-side
            if (prev_x + 2 == currently_viewing.getX_position()) {
                if ((board[7][row] != null && board[7][row].getClass() == Rook.class)) {
                    Rook castle_piece = (Rook) board[7][row];
                    castle_piece.setX_position(5);

                    board[5][row] = castle_piece;
                    board[7][row] = null;
                }
            }
            // Apply Castling queen-side
            if (prev_x - 2 == currently_viewing.getX_position()) {
                if (board[0][row] != null && board[0][row].getClass() == Rook.class) {
                    Rook castle_piece = (Rook) board[0][row];
                    castle_piece.setX_position(3);

                    board[3][row] = castle_piece;
                    board[0][row] = null;
                }
            }

            if (currently_viewing.isWhite()) {
                white_king = (King)currently_viewing;
            }
            else {
                black_king = (King)currently_viewing;
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    board[i][j].setBoard(board);
                }
            }
        }

        white_control.clear();
        black_control.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].getControlledSquares(board[i][j].isWhite() ? white_control : black_control);
                }
            }
        }
    }

    private boolean isKingInCheck(King king) {
        //return king != null && containsPointInVector(king.isWhite() ? black_control : white_control, new BoardPoint(king.getX_position(), king.getY_position()));
        if (king == null) {
            return false;
        }
        else {
            BoardPoint king_space = new BoardPoint(king.getX_position(), king.getY_position());
            if (king.isWhite()) {
                return containsPointInVector(black_control, king_space);
            }
            else {
                return containsPointInVector(white_control, king_space);
            }
        }
    }

    private void simulatePossibleMoves() {
        Piece[][] temp = deepCloneArray(board);

        int prev_x = currently_viewing.getX_position();
        int prev_y = currently_viewing.getY_position();

        for (int i = 0; i < possible_moves.size(); i++) {
            board = deepCloneArray(temp);

            BoardPoint point = possible_moves.get(i);
            move_piece(point.x, point.y);

            if (turn && isKingInCheck(white_king) || !turn && isKingInCheck(black_king)) {
                possible_moves.remove(i);
                i--;
            }

            move_piece(prev_x, prev_y);
        }

        board = deepCloneArray(temp);
    }

    private Piece[][] deepCloneArray(Piece[][] orig) {
        Piece[][] replica = new Piece[orig.length][orig[0].length];

        for (int i = 0; i < orig.length; i++) {
            for (int j = 0; j < orig[i].length; j++) {
                replica[i][j] = orig[i][j] == null ? null : orig[i][j].clone();
            }
        }

        return replica;
    }

    //
    // Implement methods for MouseListener
    //

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int x_tile = (x/50) - 1;
        int y_tile = (y/50) - 1;

        if (x_tile < 0 || x_tile >= 8 || y_tile < 0 || y_tile >= 8) {
            return;
        }

        Piece clicked_space = board[x_tile][y_tile];

        if (clicked_space == null) {
            if (currently_viewing == null) {
                return;
            }
            if (containsPointInVector(possible_moves, new BoardPoint(x_tile, y_tile))) {
                move_piece(x_tile, y_tile);

                turn = !turn;
                possible_moves.clear();

                repaint();
            }
        }
        else {
            if (clicked_space.isWhite() == turn) {
                currently_viewing = clicked_space;
                possible_moves.clear();
                currently_viewing.getPossibleMoves(possible_moves);
                simulatePossibleMoves();

                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if (board[i][j] != null) {
                            board[i][j].setBoard(board);
                        }
                    }
                }

                repaint();
            }
            else {
                if (currently_viewing != null) {
                    if (containsPointInVector(possible_moves, new BoardPoint(x_tile, y_tile))) {
                        move_piece(x_tile, y_tile);

                        turn = !turn;
                        possible_moves.clear();

                        repaint();
                    }
                }
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
