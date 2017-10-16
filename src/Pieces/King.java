package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class King extends Piece {
    private boolean is_check;
    private boolean has_moved; //Used for castling

    public King(int x_position, int y_position, boolean is_white) {
        super(x_position, y_position, 0, is_white);
        is_check = false;
        has_moved = false;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteKing.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackKing.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    public boolean isCheck() {
        return is_check;
    }

    public void setCheck(boolean is_check) {
        this.is_check = is_check;
    }

    public boolean has_moved() {
        return has_moved;
    }

    public void set_has_moved(boolean has_moved) {
        this.has_moved = has_moved;
    }
}
