import java.awt.*;
import javax.swing.JFrame;

public class Application extends JFrame {

    public Application() {
        initUI();
    }

    private void initUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);

        setTitle("Sus Fighter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var dimension = Toolkit.getDefaultToolkit().getScreenSize();
        add(new Game(dimension.getWidth(), dimension.getHeight()));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}