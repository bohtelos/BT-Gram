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

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement("BulgarEnthusiast");
        userListModel.addElement("Ander the British");
        userListModel.addElement("Kastovian");
        userListModel.addElement("Daniel Ivanescu");
        userListModel.addElement("Lennick Arthur");
        userListModel.addElement("Gorrick Mike");

        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

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

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JTextField nameField = new JTextField(15);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        nameField.addActionListener(enter -> addButton.doClick());

        addButton.addActionListener(pressAdd -> {
            String text = nameField.getText().trim();
            if (!text.isEmpty()) {
                userListModel.addElement(text);
                nameField.setText("");

            }
        });

        deleteButton.addActionListener(pressDelete -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                userListModel.remove(selectedIndex);
            }
        });

        editButton.addActionListener(pressEdit -> {
            int selectedIndex = userList.getSelectedIndex();
            String currentName = userListModel.getElementAt(selectedIndex);
            String newName = JOptionPane.showInputDialog(frame, "Change name:", currentName);

            if (selectedIndex != -1) {

                if (newName != null && !newName.trim().isEmpty()) {
                    userListModel.setElementAt(newName, selectedIndex);
                }
            }});

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(editButton);

        JScrollPane scrollPanel = new JScrollPane(userList);
        frame.add(scrollPanel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);
        headerPanel.add(titleLabel);
        headerPanel.add(authorLabel);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

    }
}
