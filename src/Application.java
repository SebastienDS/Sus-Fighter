import java.awt.EventQueue;
import javax.swing.JFrame;

public class Application extends JFrame {

    public Application() {
        initUI();
    }

    private void initUI() {
        add(new Game());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);

        setTitle("Sus Fighter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}