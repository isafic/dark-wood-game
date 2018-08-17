import javax.swing.*;

public class GameWindow extends JFrame{
    private JPanel outer;
    private JTabbedPane tabbedPane1;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    GameWindow() {
        this.setSize(WIDTH, HEIGHT);
        this.setContentPane(outer);
        this.setLocationRelativeTo(null);
    }
}
