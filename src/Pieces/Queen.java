package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Queen extends Piece {
    public Queen(int x_location, int y_location, boolean is_white) {
        super(x_location, y_location, 9, is_white);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteQueen.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackQueen.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }
}
