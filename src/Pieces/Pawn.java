package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pawn extends Piece {
    private boolean in_5th_row; //for en-passant

    public Pawn(int x_location, int y_location, boolean is_white) {
        super(x_location, y_location, 1, is_white);
        in_5th_row = false;
    }

    public boolean isIn_5th_row() {
        return in_5th_row;
    }

    public void setIn_5th_row(boolean in_5th_row) {
        this.in_5th_row = in_5th_row;
    }

    public void draw (Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhitePawn.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackPawn.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }
}
