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

        printBoard();
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
        board[2][0] = new Bishop(2,0,3,false, board);
        board[5][0] = new Bishop(5,0,3,false, board);
        board[2][7] = new Bishop(2,7,3,true, board);
        board[5][7] = new Bishop(5,7,3,true, board);

        //Queen
        board[3][0] = new Queen(3, 0,false, board);
        board[3][7] = new Queen(3, 7,true, board);

        //King
        board[4][0] = new King(4, 0,false, board, white_control, black_control);
        board[4][7] = new King(4, 7,true, board, white_control, black_control);

        //Pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(i, 1,false, board);
        }

        for (int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(i, 6,true, board);
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

    //
    // Implement methods for MouseListener
    //

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int x_tile = (x/50) - 1;
        int y_tile = (y/50) - 1;

        System.out.println("Click: " + x_tile + " " + y_tile);

        if (x_tile < 0 || x_tile >= 8 || y_tile < 0 || y_tile >= 8) {
            return;
        }

        Piece clicked_space = board[x_tile][y_tile];

        if (clicked_space == null) {
            if (currently_viewing == null) {
                return;
            }
            if (containsPointInVector(possible_moves, new BoardPoint(x_tile, y_tile))) {
                int prev_x = currently_viewing.getX_position();
                int prev_y = currently_viewing.getY_position();

                board[x_tile][y_tile] = currently_viewing;
                currently_viewing.setX_position(x_tile);
                currently_viewing.setY_position(y_tile);
                board[prev_x][prev_y] = null;

                turn = !turn;

                possible_moves.clear();

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[i][j] != null) {
                            board[i][j].getControlledSquares(board[i][j].isWhite() ? white_control : black_control);
                        }
                    }
                }

                repaint();
            }
        }
        else {
            if (clicked_space.isWhite() == turn) {
                currently_viewing = clicked_space;
                possible_moves.clear();
                currently_viewing.getPossibleMoves(possible_moves);
                repaint();
            }
            else {
                if (currently_viewing != null) {
                    if (containsPointInVector(possible_moves, new BoardPoint(x_tile, y_tile))) {
                        int prev_x = currently_viewing.getX_position();
                        int prev_y = currently_viewing.getY_position();

                        board[x_tile][y_tile] = currently_viewing;
                        currently_viewing.setX_position(x_tile);
                        currently_viewing.setY_position(y_tile);
                        board[prev_x][prev_y] = null;

                        turn = !turn;

                        possible_moves.clear();

                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (board[i][j] != null) {
                                    board[i][j].getControlledSquares(board[i][j].isWhite() ? white_control : black_control);
                                }
                            }
                        }

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
