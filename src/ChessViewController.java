import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

public class ChessViewController extends JPanel implements MouseListener {
    public final int BOARD_SIZE = 8;

    JFrame superview;
    LoggerViewController assistant;

    private Piece board[][];
    private Vector<BoardPoint> possibleMoves;
    private Vector<BoardPoint> whiteControl;
    private Vector<BoardPoint> blackControl;
    private Piece currentlyViewing;
    private int moveNumber;
    private int halfmoveCounter;
    private boolean turn;
    private boolean draw;
    private boolean gameEnded;

    ChessViewController(JFrame superview, LoggerViewController assistant) {
        this.superview = superview;
        this.assistant = assistant;
        this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.possibleMoves = new Vector<>();
        this.whiteControl = new Vector<>();
        this.blackControl = new Vector<>();
        this.currentlyViewing = null;
        this.turn = true;
        this.moveNumber = 1;
        this.halfmoveCounter = 0;

        initialize_board();
        //printBoard();

        setBackground(Color.BLACK);
        addMouseListener(this);
    }

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
        board[4][0] = new King(4, 0,false, board, whiteControl);
        //black_king = (King)board[4][0];
        board[4][7] = new King(4, 7,true, board, blackControl);
        //white_king = (King)board[4][7];

        //Pawns
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(i, 1,false, board);
        }

        for (int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(i, 6,true, board);
        }
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[j][i] != null) {
                    System.out.printf("%c", board[j][i].getAbbreviation());
                }
                else {
                    System.out.printf(" ");
                }
            }
            System.out.println();
        }
    }

    public boolean simulateMove (int srcX, int srcY, int destX, int destY) {
        Piece tBoard[][] = deepCopyArray(board);
        Vector<BoardPoint> tWhiteControl = new Vector<>();
        Vector<BoardPoint> tBlackControl = new Vector<>();
        King whiteKing = null;
        King blackKing = null;

        movePiece(tBoard, srcX, srcY, destX, destY);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (tBoard[i][j] != null) {
                    tBoard[i][j].getControlledSquares(tBoard[i][j].isWhite() ? tWhiteControl : tBlackControl, tBoard);
                    if (tBoard[i][j] != null) {
                        if (tBoard[i][j].getClass() == King.class) {
                            if (tBoard[i][j].isWhite()) {
                                whiteKing = (King)tBoard[i][j];
                            }
                            else {
                                blackKing = (King)tBoard[i][j];
                            }
                        }
                    }
                }
            }
        }

        // Just to clear the warnings of a possible NullPointerException.
        if (whiteKing == null || blackKing == null) {
            return false;
        }

        if (turn) {
            if (vectorContainsPoint(tBlackControl, whiteKing.getXPosition(), whiteKing.getYPosition())) {
                return false;
            }
        }
        else {
            if (vectorContainsPoint(tWhiteControl, blackKing.getXPosition(), blackKing.getYPosition())) {
                return false;
            }
        }

        return true;
    }

    private boolean vectorContainsPoint(List<BoardPoint> pointList, int x, int y) {
        for (int i = 0; i < pointList.size(); i++) {
            if (x == pointList.get(i).x && y == pointList.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public void movePiece(Piece[][] board, int srcX, int srcY, int destX, int destY) {
        Piece sourcePiece = board[srcX][srcY];
        board[destX][destY] = sourcePiece;
        sourcePiece.setXPosition(destX);
        sourcePiece.setYPosition(destY);
        board[srcX][srcY] = null;
    }

    private Piece[][] deepCopyArray(Piece[][] board) {
        Piece[][] tBoard = new Piece[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                //tBoard[i][j] = board[i][j];
                if (board[i][j] != null) {
                    tBoard[i][j] = board[i][j].clone();
                }
            }
        }
        return tBoard;
    }

    public void attemptMovePiece(int destX, int destY) {
        if (currentlyViewing != null) {
            if (currentlyViewing.validateMove(destX, destY) && simulateMove(currentlyViewing.getXPosition(), currentlyViewing.getYPosition(), destX, destY)) {
                int srcX = currentlyViewing.getXPosition();
                int srcY = currentlyViewing.getYPosition();
                boolean isCapture = board[destX][destY] != null;

                // En passant
                if (currentlyViewing instanceof Pawn) {
                    if (destX != srcX) {
                        Pawn pawn = (Pawn) (currentlyViewing);
                        if (board[destX][destY] == null) {
                            board[destX][pawn.isWhite() ? destY + 1 : destY - 1] = null;
                        }
                    }
                }

                //halfmove counter for 50 move rule
                halfmoveCounter++;
                if (board[destX][destY] != null) {
                    halfmoveCounter = 0;
                }

                movePiece(board, srcX, srcY, destX, destY);

                //Move number
                if (!turn) {
                    moveNumber++;
                }
                turn = !turn;

                //Castling
                if (currentlyViewing instanceof King) {
                    int rank = currentlyViewing.isWhite() ? 7 : 0;

                    if (destX - srcX == 2) {
                        movePiece(board, 7, rank, 5, rank);
                    }
                    else if (destX - srcX == -2){
                        movePiece(board, 0, rank, 3, rank);
                    }
                    ((King)(currentlyViewing)).setHasMoved(true);
                }
                // This pawn can be taken en passant.
                else if (currentlyViewing instanceof Pawn) {
                    Pawn pawn = (Pawn)currentlyViewing;
                    halfmoveCounter = 0;

                    if (Math.abs(destY - srcY) == 2) {
                        pawn.setMoveTwoSpacesLastMove(true);
                    }
                }

                // The time for en passant has expired for all pawns.
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        if (board[i][j] != null && board[i][j] instanceof Pawn && board[i][j] != currentlyViewing) {
                            ((Pawn)board[i][j]).setMoveTwoSpacesLastMove(false);
                        }
                    }
                }

                if (halfmoveCounter >= 50) {
                    draw = true;
                }

                King king = null;
                possibleMoves.clear();
                whiteControl.clear();
                blackControl.clear();
                // Reestablish controlled squares
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        if (board[i][j] != null) {
                            if (board[i][j] instanceof King && board[i][j].isWhite() == turn) {
                                king = (King)(board[i][j]);
                            }
                            board[i][j].getControlledSquares(board[i][j].isWhite() ? whiteControl : blackControl);
                        }
                    }
                }

                int count = 0;
                // get a list of possible moves.
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        if (board[i][j] != null) {
                            if (board[i][j].isWhite() == turn) {
                                possibleMoves.clear();
                                board[i][j].getPossibleMoveList(possibleMoves);

                                int t_from_x = board[i][j].getXPosition();
                                int t_from_y = board[i][j].getYPosition();

                                for (int k = 0; k < possibleMoves.size();) {
                                    if (!simulateMove(t_from_x, t_from_y, possibleMoves.get(k).x, possibleMoves.get(k).y)) {
                                        possibleMoves.remove(k);
                                        continue;
                                    }
                                    k++;
                                }
                                count += possibleMoves.size();
                            }
                        }
                    }
                }

                // To prevent warning of NullPointerException.
                if (king == null) {
                    return;
                }

                // Has the game ended?
                if (count == 0) {
                    if (king.isInCheck()) {
                        gameEnded = true;
                        repaint();
                        //JOptionPane.showMessageDialog(superview, turn ? "Black won the game" : "White won the game", "Game over" , JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        draw = true;
                        repaint();
                        //JOptionPane.showMessageDialog(superview, "Game is a draw", "Game over" , JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        }
        possibleMoves.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor(g.getColor() == Color.DARK_GRAY ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                g.fillRect(50*(i+1), 50*(j+1), 50, 50);
            }
            g.setColor(g.getColor()==Color.DARK_GRAY ? Color.LIGHT_GRAY:Color.DARK_GRAY);
        }

        Color lightGreen = new Color(50,205,50);
        Color darkGreen = new Color(0,100,0);
        g.setColor(Color.GREEN);
        for (BoardPoint point : possibleMoves) { // Draws move hints
            int x = point.x;
            int y = point.y;

            g.setColor(x % 2 == y % 2 ? lightGreen : darkGreen);
            g.fillRect(50*(x+1), 50*(y+1), 50, 50);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].draw(g);
                }
            }
        }

//        System.out.println();
//        printBoard();
//        System.out.println();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameEnded || draw) {
            System.out.println("Game over");
            return;
        }

        int downX = e.getX();
        int downY = e.getY();

        int xTile = (downX/50) - 1;
        int yTile = (downY/50) - 1;

        if (xTile < 0 || xTile > 7 || yTile < 0 || yTile >= 8) {
            return;
        }

        possibleMoves.clear();
        Piece clickedSpace = board[xTile][yTile];
        if (clickedSpace == null) {
            attemptMovePiece(xTile, yTile);
            repaint();
        }
        else {
            if (clickedSpace.isWhite() == turn) {
                currentlyViewing = clickedSpace;
                possibleMoves.clear();
                currentlyViewing.getPossibleMoveList(possibleMoves);

                for (int i = 0; i < possibleMoves.size();) {
                    if (!simulateMove(currentlyViewing.getXPosition(), currentlyViewing.getYPosition(), possibleMoves.get(i).x, possibleMoves.get(i).y)) {
                        possibleMoves.remove(i);
                        continue;
                    }
                    i++;
                }
                repaint();
            }
            else {
                attemptMovePiece(xTile, yTile);
                repaint();
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
