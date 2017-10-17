package Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Rook extends Piece {
    private boolean did_move; // Used for castling.

    public Rook(int x_position, int y_position, boolean is_white) {
        super(x_position, y_position, 5, is_white);
        did_move = false;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        try {
            if (super.isWhite()) {
                String filename = "vc_assets/WhiteRook.png";
                img = ImageIO.read(new File(filename));
            }
            else {
                String filename = "vc_assets/BlackRook.png";
                img = ImageIO.read(new File(filename));
            }
        } catch (IOException e) {
            System.err.println("File cannot be read");
        }
        g.drawImage(img,50*(getX_position()+1), 50*(getY_position()+1), 50, 50, null);
    }

    public boolean did_move() {
        return did_move;
    }

    public void set_move(boolean did_move) {
        this.did_move = did_move;
    }
}
