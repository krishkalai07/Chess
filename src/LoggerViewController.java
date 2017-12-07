import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class LoggerViewController extends JPanel {
    private Vector<String> moves;

    LoggerViewController() {
        moves = new Vector<>();

        setSize(200, 700);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("default", Font.ITALIC, 12));
        System.out.println(moves);
        for (int i = 0; i < moves.size(); i++) {
            if (i%2 == 0) {
                g.drawString(i/2 + 1 + ".", 5, 15*i + 15);
                g.drawString(moves.get(i),15, 15*i + 15);
            }
            else {
                g.drawString(moves.get(i),60, 15*(i-1) + 15);
            }

        }
    }

    public void addMove(String move) {
        moves.add(move);
        System.out.println(move);
        System.out.println(moves);
        repaint();
    }
}
