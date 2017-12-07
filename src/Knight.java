import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Knight extends Piece {
    public Knight(int file, int rank, boolean is_white, Piece[][] board) {
        super(file, rank, is_white, board);
    }

    @Override
    public void draw(Graphics g) throws IOException {
        BufferedImage img;
        if (super.isWhite()) {
            String filename = "/WhiteKnight.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        else {
            String filename = "/BlackKnight.png";
            img = ImageIO.read(getClass().getResource(filename));
        }
        g.drawImage(img,50*(file+1), 50*(rank+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoveList(List<BoardPoint> pointList) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (file + i >= 0 && file + i < 8 && rank + j >= 0 && rank + j < 8) {
                    if (board[file + i][rank + j] == null || this.color != board[file + i][rank + j].color) {
                        pointList.add(new BoardPoint(file + i, rank + j));
                    }
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> pointList, Piece[][] board) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (file + i >= 0 && file + i < 8 && rank + j >= 0 && rank + j < 8) {
                    pointList.add(new BoardPoint(file + i, rank + j));
                }
            }
        }
    }

    @Override
    public boolean validateMove(int to_file, int to_rank) {
        if (file == to_file && rank == to_rank) {
            return false;
        }

        if ((Math.abs(file - to_file) == 2 && Math.abs(rank - to_rank) == 1) || (Math.abs(file - to_file) == 1 && Math.abs(rank - to_rank) == 2)) {
            if (board[to_file][to_rank] == null) {
                return true;
            }
            else {
                if (board[to_file][to_rank].color != color) {
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
    public Knight clone() throws CloneNotSupportedException {
        return (Knight)super.clone();
    }
}
