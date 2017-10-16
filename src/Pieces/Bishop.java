package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bishop extends Piece {
    public Bishop(int x_position, int y_position, int capture_weight, boolean is_white) {
        super(x_position, y_position, 0, is_white);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteBishop.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackBishop.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read: Bishop");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }
}
