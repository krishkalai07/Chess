import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Pawn extends Piece {
    /**
     * Boolean for if a pawn can be captured en-passant.
     */
    private boolean did_move_two_spaces_last_move;

    /**
     * Constructor that defaults the pawn moving 2 spaces to false.
     *
     * @param file     The file the Pawn is placed on the board.
     * @param rank     The rank the Pawn is placed on the board.
     * @param is_white True if the Pawn is white, false otherwise.
     * @param board    Board reference.
     */
    public Pawn(int file, int rank, boolean is_white, Piece[][] board) {
        this(file, rank, is_white, board, false);
    }

    public Pawn(int xPosition, int yPosition, boolean isWhite, Piece[][] board, boolean did_move_two_spaces_last_move) {
        super(xPosition, yPosition, isWhite, board);
        this.did_move_two_spaces_last_move = did_move_two_spaces_last_move;
    }

    /**
     * Gets the value of the field [did_move_two_spaces_last_move].
     *
     * @return The value of the field [did_move_two_spaces_last_move].
     */
    public boolean didMoveTwoSpacesLastMove() {
        return did_move_two_spaces_last_move;
    }

    /**
     * Method to set the [did_move_two_spaces_last_move]'s field's value.
     *
     * @param did_move_two_spaces_last_move Boolean if the Pawn can be captured en-passant.
     */
    public void setMoveTwoSpacesLastMove(boolean did_move_two_spaces_last_move) {
        this.did_move_two_spaces_last_move = did_move_two_spaces_last_move;
    }

    /**
     * Utility method that appends the forward moves of the Pawn.
     * This method adds 2 [BoardPoint]s if the Pawn is on the 2nd rank.
     *
     * @param point_list Reference to List of [BoardPoint]s to append to.
     */
    private void getForwardMoves(List<BoardPoint> point_list) {
        int movement_direction = color ? -1 : 1;

        if (rank == (isWhite() ? 6 : 1)) {
            if (board[file][rank + (2 * movement_direction)] == null) {
                point_list.add(new BoardPoint(file, rank + (2*movement_direction)));
            }
        }

        if (board[file][rank + movement_direction] == null) {
            point_list.add(new BoardPoint(file, rank + movement_direction));
        }
    }

    /**
     * Utility method that appends the capture moves of the Pawn, including capture with en-passant.
     *
     * @param point_list Reference to List of [BoardPoint]s to append to.
     */
    private void getCaptureMoves(List<BoardPoint> point_list) {
        int movement_direction = color ? -1 : 1;

        // Left
        if (file != 0) {
            // Diagonal
            if (board[file - 1][rank + movement_direction] != null) {
                if (board[file - 1][rank + movement_direction].color != color) {
                    point_list.add(new BoardPoint(file - 1, rank + movement_direction));
                }
            }

            // En-passant
            if (rank == (color ? 3 : 4)) {
                if (board[file - 1][rank] != null) {
                    if (board[file - 1][rank] instanceof Pawn) {
                        Pawn castedPiece = (Pawn) (board[file - 1][rank]);
                        if (castedPiece != null) {
                            if (castedPiece.did_move_two_spaces_last_move) {
                                point_list.add(new BoardPoint(file - 1, rank + movement_direction));
                            }
                        }
                    }
                }
            }
        }

        // Right
        if (file != 7) {
            // Diagonal
            if (board[file + 1][rank + movement_direction] != null) {
                if (board[file + 1][rank + movement_direction].color != color) {
                    point_list.add(new BoardPoint(file + 1, rank + movement_direction));
                }
            }

            if (rank == (color ? 3 : 4)) {
                if (board[file + 1][rank] != null) {
                    if (board[file + 1][rank] instanceof Pawn) {
                        if (board[file + 1][rank] != null) {
                            Pawn castedPiece = (Pawn) (board[file + 1][rank]);
                            if (castedPiece.did_move_two_spaces_last_move) {
                                point_list.add(new BoardPoint(file + 1, rank + movement_direction));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) throws java.io.IOException {
        BufferedImage img;
        if (color) {
            String filename = "/WhitePawn.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackPawn.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        g.drawImage(img,50*(getFile()+1), 50*(getRank()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        getForwardMoves(pointList);
        getCaptureMoves(pointList);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        int movement_direction = color ? -1 : 1;

        if (file != 0) {
            pointList.add(new BoardPoint(file - 1, rank + movement_direction));
        }

        if (file != 7) {
            pointList.add(new BoardPoint(file + 1, rank + movement_direction));
        }
    }

    @Override
    public boolean validateMove(int x, int y) {
        int direction = color ? -1 : 1;

        if (file == x && rank == y) {
            return false;
        }

        //Move two spaces
        if (Math.abs(rank - y) == 2 && file == x) {
            if (rank == (color ? 6 : 1)) {
                if (board[file][rank + direction] == null && board[file][rank + (2 * direction)] == null) {
                    return true;
                }
            }
        }
        else if (y - rank == direction) {
            //Move forward
            if (file == x) {
                if (board[x][y] == null) {
                    return true;
                }
            }
            else {
                //Normal capture
                if (board[x][y] != null) {
                    if (board[x][y].color != color){
                        return true;
                    }
                }
                else {
                    // En passant
                    Pawn castedPawn = (Pawn)board[x][y-direction];
                    if (castedPawn != null) {
                        if (castedPawn.did_move_two_spaces_last_move) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public char getAbbreviation() {
        return color ? 'P' : 'p';
    }


    @Override
    public Pawn clone() throws CloneNotSupportedException {
        return (Pawn)super.clone();
    }
}
