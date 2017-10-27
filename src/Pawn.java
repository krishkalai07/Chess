import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Pawn extends Piece {
    private boolean didMoveTwoSpacesLastMove;

    public Pawn(int xPosition, int yPosition, boolean isWhite, Piece[][] board) {
        this(xPosition, yPosition, isWhite, board, false);
    }

    public Pawn(int xPosition, int yPosition, boolean isWhite, Piece[][] board, boolean didMoveTwoSpacesLastMove) {
        super(xPosition, yPosition, isWhite, board);
        this.didMoveTwoSpacesLastMove = didMoveTwoSpacesLastMove;
    }

    public boolean isDidMoveTwoSpacesLastMove() {
        return didMoveTwoSpacesLastMove;
    }

    public void setMoveTwoSpacesLastMove(boolean didMoveTwoSpacesLastMove) {
        this.didMoveTwoSpacesLastMove = didMoveTwoSpacesLastMove;
    }

    private void getForwardMoves(List<BoardPoint> pointList) {
        int movement_direction = color ? -1 : 1;

        if (yPosition == (isWhite() ? 6 : 1)) {
            if (board[xPosition][yPosition + (2 * movement_direction)] == null) {
                pointList.add(new BoardPoint(xPosition, yPosition + (2*movement_direction)));
            }
        }

        if (board[xPosition][yPosition + movement_direction] == null) {
            pointList.add(new BoardPoint(xPosition, yPosition + movement_direction));
        }
    }

    private void getCaptureMoves(List<BoardPoint> pointList) {
        int movement_direction = color ? -1 : 1;

        // Left
        if (xPosition != 0) {
            // Diagonal
            if (board[xPosition - 1][yPosition + movement_direction] != null) {
                if (board[xPosition - 1][yPosition + movement_direction].color != color) {
                    pointList.add(new BoardPoint(xPosition - 1, yPosition + movement_direction));
                }
            }

            // En-passant
            if (yPosition == (color ? 3 : 4)) {
                if (board[xPosition - 1][yPosition] != null) {
                    if (board[xPosition - 1][yPosition] instanceof Pawn) {
                        Pawn castedPiece = (Pawn) (board[xPosition - 1][yPosition]);
                        if (castedPiece != null) {
                            if (castedPiece.didMoveTwoSpacesLastMove) {
                                pointList.add(new BoardPoint(xPosition - 1, yPosition + movement_direction));
                            }
                        }
                    }
                }
            }
        }

        // Right
        if (xPosition != 7) {
            // Diagonal
            if (board[xPosition + 1][yPosition + movement_direction] != null) {
                if (board[xPosition + 1][yPosition + movement_direction].color != color) {
                    pointList.add(new BoardPoint(xPosition + 1, yPosition + movement_direction));
                }
            }

            if (yPosition == (color ? 3 : 4)) {
                if (board[xPosition + 1][yPosition] != null) {
                    if (board[xPosition + 1][yPosition] instanceof Pawn) {
                        if (board[xPosition + 1][yPosition] != null) {
                            Pawn castedPiece = (Pawn) (board[xPosition + 1][yPosition]);
                            if (castedPiece.didMoveTwoSpacesLastMove) {
                                pointList.add(new BoardPoint(xPosition + 1, yPosition + movement_direction));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (isWhite()) {
                String filename = "vc_assets/WhitePawn.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackPawn.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read: Pawn");
        }
        g.drawImage(img,50*(getXPosition()+1), 50*(getYPosition()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        getForwardMoves(pointList);
        getCaptureMoves(pointList);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList) {
        getControlledSquares(pointList, board);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        int movement_direction = color ? -1 : 1;

        if (xPosition != 0) {
            pointList.add(new BoardPoint(xPosition - 1, yPosition + movement_direction));
        }

        if (xPosition != 7) {
            pointList.add(new BoardPoint(xPosition + 1, yPosition + movement_direction));
        }
    }

    @Override
    public boolean validateMove(int x, int y) {
        int direction = color ? -1 : 1;

        if (xPosition == x && yPosition == y) {
            return false;
        }

        //Move two spaces
        if (Math.abs(yPosition - y) == 2 && xPosition == x) {
            if (yPosition == (color ? 6 : 1)) {
                if (board[xPosition][yPosition + direction] == null && board[xPosition][yPosition + (2 * direction)] == null) {
                    return true;
                }
            }
        }
        else if (y - yPosition == direction) {
            //Move forward
            if (xPosition == x) {
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
                        if (castedPawn.didMoveTwoSpacesLastMove) {
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
    public Pawn clone() {
        return (Pawn)super.clone();
    }
}
