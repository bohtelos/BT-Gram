import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.io.IOException.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.FileReader;

public class MainWindow {
    public static void main(String[] args) {
        File contactsFile = new File("Contacts.txt");
        JFrame frame = new JFrame("BT-Gram");
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(850, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        DefaultListModel<String> userListModel = new DefaultListModel<>();

        if (contactsFile.exists() && contactsFile.canRead()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(contactsFile, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null ) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        userListModel.addElement(trimmed);
                    }
                }

            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Error while reading the file:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        JList<String> userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JLabel titleLabel = new JLabel("BT-Gram by bohtelos");
        JLabel noContactsLabel = new JLabel("No contacts yet. Add your first one!");
        noContactsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noContactsLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        noContactsLabel.setForeground(Color.GRAY);
        noContactsLabel.setVisible(userListModel.isEmpty());
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
                noContactsLabel.setVisible(false);
                nameField.setText("");

                saveToFile(userListModel);

            }
        });

        deleteButton.addActionListener(pressDelete -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {

                String selectedUser = userListModel.getElementAt(selectedIndex);

                int response = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want do delete " + selectedUser + "?",
                        "Confirmation of deletion", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {

                    userListModel.remove(selectedIndex);

                    saveToFile(userListModel);

                }

                if (userListModel.isEmpty()) {
                    noContactsLabel.setVisible(true);
                }

                }
        });

        editButton.addActionListener(pressEdit -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                String currentName = userListModel.getElementAt(selectedIndex);
                String newName = JOptionPane.showInputDialog(frame, "Change name:", currentName);

                if (newName != null && !newName.trim().isEmpty()) {
                    userListModel.setElementAt(newName, selectedIndex);
                }
                saveToFile(userListModel);
            }});

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(editButton);

        JScrollPane scrollPanel = new JScrollPane(userList);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPanel, BorderLayout.CENTER);
        centerPanel.add(noContactsLabel, BorderLayout.NORTH);

        headerPanel.add(titleLabel);
        headerPanel.add(authorLabel);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void saveToFile(DefaultListModel<String>model) {
        try (PrintWriter writer = new PrintWriter("Contacts.txt")) {
            for (int i = 0; i < model.getSize(); i++) {

                writer.println(model.getElementAt(i));

            }
        } catch (FileNotFoundException error) {
            System.err.println("Error: " + error.getMessage());
        }
    }

}
