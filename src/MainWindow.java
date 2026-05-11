import javax.swing.*;
import java.awt.*;

public class MainWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BT-Gram");
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(850, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        JLabel titleLabel = new JLabel("BT-Gram by bohtelos");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel("Developed by bohtelos");
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        authorLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        authorLabel.setForeground(Color.GRAY);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement("BulgarEnthusiast");
        userListModel.addElement("Ander the British");
        userListModel.addElement("Kastovian");
        userListModel.addElement("Daniel Ivanescu");
        userListModel.addElement("Lennick Arthur");
        userListModel.addElement("Gorrick Mike");

        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane scrollPanel = new JScrollPane(userList);
        frame.add(scrollPanel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);
        headerPanel.add(titleLabel);
        headerPanel.add(authorLabel);
        frame.setVisible(true);

    }
}