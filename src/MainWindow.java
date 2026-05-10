import javax.swing.*;
import java.awt.*;

public class MainWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BT-Gram");
        frame.setSize(850, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);

        JLabel titleLabel = new JLabel("BT-Gram by bohtelos");
        titleLabel.setBounds(0, 20, 850, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        frame.add(titleLabel);

        JLabel authorLabel = new JLabel("Developed by bohtelos");
        authorLabel.setBounds(0, 55, 850, 20);
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        authorLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        authorLabel.setForeground(Color.GRAY);
        frame.add(authorLabel);

    }
}
