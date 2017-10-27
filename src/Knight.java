import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Knight extends Piece {
    public Knight(int xPosition, int yPosition, boolean isWhite, Piece[][] board) {
        super(xPosition, yPosition, isWhite, board);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteKnight.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackKnight.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getXPosition()+1), 50*(getYPosition()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (xPosition + i >= 0 && xPosition + i < 8 && yPosition + j >= 0 && yPosition + j < 8) {
                    if (board[xPosition + i][yPosition + j] == null || this.color != board[xPosition + i][yPosition + j].color) {
                        pointList.add(new BoardPoint(xPosition + i, yPosition + j));
                    }
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList) {
        getControlledSquares(pointList, board);
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (xPosition + i >= 0 && xPosition + i < 8 && yPosition + j >= 0 && yPosition + j < 8) {
                    pointList.add(new BoardPoint(xPosition + i, yPosition + j));
                }
            }
        }
    }

    @Override
    public boolean validateMove(int toX, int toY) {
        if (xPosition == toX && yPosition == toY) {
            return false;
        }

        if ((Math.abs(xPosition - toX) == 2 && Math.abs(yPosition - toY) == 1) || (Math.abs(xPosition - toX) == 1 && Math.abs(yPosition - toY) == 2)) {
            if (board[toX][toY] == null) {
                return true;
            }
            else {
                if (board[toX][toY].color != color) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public char getAbbreviation() {
        return (color ? 'N' : 'n');
    }

    @Override
    public Knight clone() {
        return (Knight)super.clone();
    }
}
