import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {
    /**
     * Method responsible for creating and displaying the view.
     */
    public static void main(String[] args) throws FileNotFoundException {
//        SuperView superView = new SuperView();

        JFrame mainWindow = new JFrame("Chess");
        JFrame assistantWindow = new JFrame("Chess assistant");
        LoggerViewController loggerContent = new LoggerViewController();
        ChessViewController chessContent = new ChessViewController(mainWindow, loggerContent);

        mainWindow.setContentPane(chessContent);
        mainWindow.setSize(500, 525);
        mainWindow.setLocation(100, 100);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);

//        assistantWindow.setContentPane(loggerContent);
//        assistantWindow.setSize(200, 525);
//        assistantWindow.setLocation(700, 100);
//        assistantWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//        assistantWindow.setResizable(false);
//        assistantWindow.setVisible(true);
    }

    // 5! = 5 * 4 * 3 * 2 * 1
    public int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n-1);
    }
}
