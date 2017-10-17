import Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ViewController extends JPanel implements MouseListener {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;

    private Piece board[][];
    private Piece currently_viewing;
    private Vector<BoardPoint> possible_moves;

    ViewController() {
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setBackground(new Color(0x000000));

        board = new Piece[8][8];
        currently_viewing = null;
        possible_moves = new Vector<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        initialize_board();


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

        g.setColor(Color.GREEN);
        for (BoardPoint point : possible_moves) {
            System.out.println("Drawing hints");
            int x = point.x;
            int y = point.y;
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    System.out.printf(" ");
                }
                else {
                    if (board[i][j].getClass() == Pawn.class) {
                        System.out.printf("P");
                    }
                    else if (board[i][j].getClass() == Rook.class) {
                        System.out.printf("R");
                    }
                    else if (board[i][j].getClass() == Knight.class) {
                        System.out.printf("N");
                    }
                    else if (board[i][j].getClass() == Bishop.class) {
                        System.out.printf("B");
                    }
                    else if (board[i][j].getClass() == Queen.class) {
                        System.out.printf("Q");
                    }
                    else if (board[i][j].getClass() == King.class) {
                        System.out.printf("K");
                    }
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
        board[0][0] = new Rook(0, 0,false);
        board[7][0] = new Rook(7, 0,false);
        board[0][7] = new Rook(0, 7,true);
        board[7][7] = new Rook(7, 7,true);

        //Knight
        board[1][0] = new Knight(1, 0,false);
        board[6][0] = new Knight(6, 0,false);
        board[1][7] = new Knight(1, 7,true);
        board[6][7] = new Knight(6, 7,true);

        //Bishop
        board[2][0] = new Bishop(2,0,3,false);
        board[5][0] = new Bishop(5,0,3,false);
        board[2][7] = new Bishop(2,7,3,true);
        board[5][7] = new Bishop(5,7,3,true);

        //Queen
        board[3][0] = new Queen(3, 0, false);
        board[3][7] = new Queen(3, 7, true);

        //King
        board[4][0] = new King(4, 0,false);
        board[4][7] = new King(4, 7,true);

        //Pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(i, 1, false);
        }

        for (int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(i, 6, true);
        }
    }

    private void fillPossibleMovesForPawn(Pawn pawn) {
        //Clear vector for safety.
        possible_moves.clear();

        if (pawn.isWhite()) {
            // All moves allow moving one space forwards
            if (board[pawn.getX_position()][pawn.getY_position() - 1] == null) {
                possible_moves.add(new BoardPoint(pawn.getX_position(), pawn.getY_position() - 1));
            }

            // First move can move 2 spaces forwards
            if (possible_moves.size() > 0) {
                if (pawn.getY_position() == 6) {
                    if (board[pawn.getX_position()][pawn.getY_position() - 2] == null) {
                        possible_moves.add(new BoardPoint(pawn.getX_position(), pawn.getY_position() - 2));
                    }
                }
            }

            //If there is a piece one space forward diagonally from the pawn, the pawn can move (and capture).
            if (pawn.getX_position() != 0 && board[pawn.getX_position() - 1][pawn.getY_position() - 1] != null) {
                possible_moves.add(new BoardPoint(pawn.getX_position() - 1, pawn.getY_position() - 1));
            }
            if (pawn.getX_position() != 7 && board[pawn.getX_position() + 1][pawn.getY_position() - 1] != null) {
                possible_moves.add(new BoardPoint(pawn.getX_position() + 1, pawn.getY_position() - 1));
            }
        }
        else {
            // All moves can allow moving 1 space forward.
            if (board[pawn.getX_position()][pawn.getY_position() + 1] == null) {
                possible_moves.add(new BoardPoint(pawn.getX_position(), pawn.getY_position() + 1));
            }

            // First move can move 2 spaces forward.
            if (possible_moves.size() > 0) {
                if (pawn.getY_position() == 1) {
                    if (board[pawn.getX_position()][pawn.getY_position() + 2] == null) {
                        possible_moves.add(new BoardPoint(pawn.getX_position(), pawn.getY_position() + 2));
                    }
                }
            }

            // If there is a piece one space forward diagonally from the pawn, the pawn can move (and capture).
            if (pawn.getX_position() != 0 && board[pawn.getX_position() - 1][pawn.getY_position() + 1] != null) {
                possible_moves.add(new BoardPoint(pawn.getX_position() - 1, pawn.getY_position() + 1));
            }
            if (pawn.getX_position() != 7 && board[pawn.getX_position() + 1][pawn.getY_position() + 1] != null) {
                possible_moves.add(new BoardPoint(pawn.getX_position() + 1, pawn.getY_position() + 1));
            }
        }
    }

    private void fillPossibleMovesForKnight(Knight knight) {
        possible_moves.clear();

        int x = knight.getX_position();
        int y = knight.getY_position();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (x+i >= 0 && x+i < 8 && y+j >= 0 && y+j < 8) {
                    if (board[x + i][y + j] == null || knight.isWhite() != board[x + i][y + j].isWhite()) {
                        possible_moves.add(new BoardPoint(x + i, y + j));
                    }
                }
            }
        }
    }

    private void fillPossibleMovesForBishop(Bishop bishop) {
        possible_moves.clear();
        fillAllMovesDiagonally(bishop);
    }

    private void fillPossibleMovesForRook(Rook rook) {
        possible_moves.clear();
        fillAllMovesStraight(rook);
    }

    private void fillPossibleMovesForQueen (Queen queen) {
        possible_moves.clear();
        fillAllMovesDiagonally(queen);
        fillAllMovesStraight(queen);
    }

    private void fillAllMovesDiagonally(Piece piece) {
        final int x = piece.getX_position();
        final int y = piece.getY_position();

        // All left up
        for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
            if (board[x-i][y-i] == null) {
                possible_moves.add(new BoardPoint(x-i, y-i));
            }
            else {
                if (board[x-i][y-i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x - i, y - i));
                }
                break;
            }
        }

        // All left down
        for (int i = 1; x - i >= 0 && y + i < 8; i++) {
            if (board[x - i][y + i] == null) {
                possible_moves.add(new BoardPoint(x - i, y + i));
            }
            else {
                if (board[x - i][y + i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x - i, y + i));
                }
                break;
            }
        }

        // All right down
        for (int i = 1; x + i < 8 && y + i < 8; i++) {
            if (board[x + i][y + i] == null) {
                possible_moves.add(new BoardPoint(x + i, y + i));
            }
            else {
                if (board[x + i][y + i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x + i, y + i));
                }
                break;
            }
        }

        // All right up
        for (int i = 1; x + i < 8 && y - i >= 0; i++) {
            if (board[x + i][y - i] == null) {
                possible_moves.add(new BoardPoint(x + i, y - i));
            }
            else {
                if (board[x + i][y - i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x + i, y - i));
                }
                break;
            }
        }
    }

    private void fillAllMovesStraight (Piece piece) {
        int x = piece.getX_position();
        int y = piece.getY_position();

        //Up
        for (int i = 1; y - i >= 0; i++) {
            if (board[x][y-i] == null) {
                possible_moves.add(new BoardPoint(x, y - i));
            }
            else {
                if (board[x][y - i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x, y - i));
                }
                break;
            }
        }

        // Right
        for (int i = 1; x + i < 8; i++) {
            if (board[x+i][y] == null) {
                possible_moves.add(new BoardPoint(x+i, y));
            }
            else {
                if (board[x+i][y].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x + i, y));
                }
                break;
            }
        }

        //Down
        for (int i = 1; y + i < 8; i++) {
            if (board[x][y+i] == null) {
                possible_moves.add(new BoardPoint(x, y + i));
            }
            else {
                if (board[x][y + i].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x, y + i));
                }
                break;
            }
        }

        //Left
        for (int i = 1; x - i >= 0; i++) {
            if (board[x-i][y] == null) {
                possible_moves.add(new BoardPoint(x - i, y));
            }
            else {
                if (board[x - i][y].isWhite() != piece.isWhite()) {
                    possible_moves.add(new BoardPoint(x - i, y));
                }
                break;
            }
        }
    }

    private void fillPossibleMovesForKing (King king) {
        possible_moves.clear();
        final int x = king.getX_position();
        final int y = king.getY_position();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Prevent checking the square it's on.
                if (i == 0 && i == j) {
                    continue;
                }

                if (x+i >= 0 && x+i < 8 && y+j >= 0 && y+j < 8) {
                    if (board[x+i][y+j] == null) {
                        possible_moves.add(new BoardPoint(x+i, y+j));
                    }
                    else {
                        if (king.isWhite() != board[x+i][y+j].isWhite()) {
                            possible_moves.add(new BoardPoint(x+i, y+j));
                        }
                    }
                }
            }
        }

        // Castling
        if (!king.has_moved()) {
            // Castling king-side
            if (board[x+1][y] == null && board[x+2][y] == null) {
                System.out.println("Is clear");
                if (board[x+3][y] != null && board[x+3][y].getClass() == Rook.class && !((Rook)board[x+3][y]).did_move()) {
                    System.out.println("Rook exists");
                    possible_moves.add(new BoardPoint(x + 2, y));
                }
            }
            // Castling queen-side
            if (board[x-1][y] == null && board[x-2][y] == null && board[x-3][y] == null) {
                if (board[x-4][y] != null && board[x-4][y].getClass() == Rook.class && !((Rook)board[x-4][y]).did_move()) {
                    possible_moves.add(new BoardPoint(x - 2, y));
                }
            }
        }
    }

    /**
     * Method to branch which piece is selected based on down-casted type.
     */
    private void fillPossibleMoves(Piece piece) {
        if (piece.getClass() == Pawn.class) {
            fillPossibleMovesForPawn((Pawn)(piece));
        }
        else if (piece.getClass() == Rook.class) {
            fillPossibleMovesForRook((Rook) piece);
        }
        else if (piece.getClass() == Knight.class) {
            fillPossibleMovesForKnight(((Knight) piece));
        }
        else if (piece.getClass() == Bishop.class) {
            fillPossibleMovesForBishop((Bishop) piece);
        }
        else if (piece.getClass() == Queen.class) {
            fillPossibleMovesForQueen((Queen) piece);
        }
        else if (piece.getClass() == King.class) {
            fillPossibleMovesForKing((King) piece);
        }
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

        if (currently_viewing == null) {
            currently_viewing = board[x_tile][y_tile];
            if (currently_viewing != null) {
                fillPossibleMoves(currently_viewing);
                System.out.printf("Moves: %s\n", possible_moves);
                repaint();
                return;
            }
        }

        if (currently_viewing != null) {
            for (BoardPoint possible_move : possible_moves) {
                if (possible_move.x == x_tile && possible_move.y == y_tile) {
                    int prev_x = currently_viewing.getX_position();
                    int prev_y = currently_viewing.getY_position();

                    board[x_tile][y_tile] = currently_viewing;
                    currently_viewing.setX_position(x_tile);
                    currently_viewing.setY_position(y_tile);
                    board[prev_x][prev_y] = null;

                    // If a rook is moving, then set did_move to true so player can't castle that side.
                    if (currently_viewing.getClass() == Rook.class) {
                        ((Rook)currently_viewing).set_move(true);
                    }
                    // Apply King's castling.
                    else if (currently_viewing.getClass() == King.class) {
                        ((King)currently_viewing).set_has_moved(true);

                        // Apply Castling king-side
                        if (prev_x + 2 == currently_viewing.getX_position()) {
                            if ((board[7][7] != null && board[7][7].getClass() == Rook.class) || (board[7][0].getClass() != null && board[7][0].getClass() == Rook.class)) {
                                if (currently_viewing.isWhite()) {
                                    Rook castle_piece = (Rook) board[7][7];
                                    castle_piece.setX_position(5);

                                    board[5][7] = castle_piece;
                                    board[7][7] = null;
                                }
                                else {
                                    Rook castle_piece = (Rook) board[7][0];
                                    castle_piece.setX_position(5);

                                    board[5][0] = castle_piece;
                                    board[7][0] = null;
                                }
                            }
                        }
                        // Apply Castling queen-side
                        if (prev_x - 2 == currently_viewing.getX_position()) {
                            if (board[0][7] != null && board[0][7].getClass() == Rook.class || board[0][0] != null && board[0][0].getClass() == Rook.class) {
                                if(currently_viewing.isWhite()) {
                                    Rook castle_piece = (Rook) board[0][7];
                                    castle_piece.setX_position(3);

                                    board[3][7] = castle_piece;
                                    board[0][7] = null;
                                }
                                else {
                                    Rook castle_piece = (Rook)board[0][0];
                                    castle_piece.setX_position(3);

                                    board[3][0] = castle_piece;
                                    board[0][0] = null;
                                }
                            }
                        }
                    }
                    break;
                }
            }
            currently_viewing = null;
        }
        possible_moves.clear();
        repaint();
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
