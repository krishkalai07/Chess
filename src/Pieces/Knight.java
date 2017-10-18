package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Knight extends Piece {
    public Knight(int x_position, int y_position, boolean is_white, Piece[][] board) {
        super(x_position, y_position, 3, is_white, board);
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
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    @Override
    public void getPossibleMoves(List<BoardPoint> vector) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (x_position + i >= 0 && x_position + i < 8 && y_position + j >= 0 && y_position + j < 8) {
                    if (board[x_position + i][y_position + j] == null || this.color != board[x_position + i][y_position + j].color) {
                        vector.add(new BoardPoint(x_position + i, y_position + j));
                    }
                }
            }
        }
    }

    @Override
    public void getControlledSquares(List<BoardPoint> vector) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0 || i == j || i == -j || -i == j) {
                    continue;
                }

                if (x_position + i >= 0 && x_position + i < 8 && y_position + j >= 0 && y_position + j < 8) {
                    if (board[x_position + i][y_position + j] == null || this.color != board[x_position + i][y_position + j].color) {
                        vector.add(new BoardPoint(x_position + i, y_position + j));
                    }
                }
            }
        }
    }
}
