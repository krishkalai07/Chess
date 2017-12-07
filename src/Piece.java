import java.awt.*;
import java.io.IOException;
import java.util.List;

public abstract class Piece implements Cloneable {
    protected int file;
    protected int rank;
    protected boolean color;
    protected Piece board[][];

    /**
     * Constructor for a piece.
     *
     *
     * @param file    The file the piece is on in the board.
     * @param rank    The rank the piece is on in the board.
     * @param isWhite True if the piece color is white, false otherwise.
     * @param board   Current board state for referring other pieces.
     */
    Piece (int file, int rank, boolean isWhite, Piece[][] board) {
        this.file = file;
        this.rank = rank;
        this.color = isWhite;
        this.board = board;
    }

    /**
     * Gets the current file of the Piece in the board. This method does not check the reflection of the board.
     *
     * @return This Piece's current file (as an integer from 0 to 7), generally reflective of the board.
     */
    public int getFile() {
        return file;
    }

    /**
     * Sets the current file of the piece.
     *
     * @param file The new file to be set to. The file should reflect off the board.
     */
    public void setFile(int file) {
        this.file = file;
    }

    /**
     * Gets the current rank of the Piece in the board. This method does not check the reflection of the board.
     *
     * @return This Piece's current rank (as an integer from 0 to 7), generally reflective of the board.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the current rank of the piece.
     *
     * @param rank The new rank to be set to. The rank should reflect off the board.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Gets the color of the piece as a boolean.
     *
     * @return The piece's color.
     */
    public boolean isWhite() {
        return color;
    }

    /**
     * An interface method to get the squares controlled by this piece.
     * This method does not return the List, but modifies in place.
     * Overriding methods should not clear the list, for this may hold controlling squares
     * for other pieces.
     *
     * @param point_list The list of [BoardPoint]s to append controlled squares to.
     */
    public void getControlledSquares(List<BoardPoint> point_list) {
        getControlledSquares(point_list, board);
    }

    /**
     * Draw this piece on the current graphics context.
     *
     * @param g Graphics context to use for drawing.
     * @throws IOException if the drawing file cannot be found.
     */
    public abstract void draw (Graphics g) throws IOException;

    /**
     * Gets a list of the possible moves, regardless of being legal or not.
     * This method does not return the List, instead modifies in place.
     * Overriding methods should not clear the list, for this may hold possible moves
     * for other pieces.
     *
     * @param point_list The list of [BoardPoint]s to append possible moves to.
     */
    public abstract void getPossibleMoveList(List<BoardPoint> point_list);

    /**
     * An interface method that gets the squares controlled by this piece on a different board reference.
     * This method does not return the List, instead modifies in place.
     * Overriding methods should not clear the list, for this may hold controlling squares
     * for other pieces.
     *
     * @param point_list The list of [BoardPoint]s to append controlled squares to.
     * @param board The board reference to base the move list from.
     */
    public abstract void getControlledSquares(List<BoardPoint> point_list, Piece[][] board);

    /**
     * Method to determine if a piece can move to the specified location.
     * Implementations of this method should test for if the piece can move there legally, regardless of putting
     * its King into check.
     *
     * @param file The destination file as an integer from 0 <= file <= 7.
     * @param rank The destination file as an integer from 0 <= rank <= 7.
     * @return True if the piece can move there legally, false if it cannot.
     */
    public abstract boolean validateMove(int file, int rank);

    /**
     * Gets the abbreviation of the piece. This uses standard ASCII English abbreviation notation.
     *
     * @return A character of the abbreviation of the piece.
     */
    public abstract char getAbbreviation();

    /**
     * Clones the Piece object.
     *
     * @return A reference of the cloned version of this Piece.
     * @throws CloneNotSupportedException if the clone operation is not supported (should not happen)
     */
    @Override
    public Piece clone() throws CloneNotSupportedException {
        return (Piece)super.clone();
    }
}
