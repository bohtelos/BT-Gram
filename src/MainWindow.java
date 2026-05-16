import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

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
        ArrayList<String> allContactsArchive = new ArrayList<>();

        if (contactsFile.exists() && contactsFile.canRead()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(contactsFile, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null ) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        userListModel.addElement(trimmed);
                        allContactsArchive.add(trimmed);
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
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel("Developed by bohtelos");
        authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        authorLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        authorLabel.setForeground(Color.GRAY);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noContactsLabel = new JLabel("No contacts yet. Add your first one!");
        noContactsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noContactsLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        noContactsLabel.setForeground(Color.GRAY);
        noContactsLabel.setVisible(userListModel.isEmpty());

        JLabel countLabel = new JLabel("Contacts: " + allContactsArchive.size());
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(15);
        JTextField searchField = new JTextField(15);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        searchField.addActionListener(array -> {
            String query = searchField.getText().toLowerCase().trim();
            userListModel.clear();

            for (String contact : allContactsArchive) {
                if (contact.toLowerCase().contains(query)) {
                    userListModel.addElement(contact);
                }
            }

            countLabel.setText("Found: " + userListModel.size());
            searchField.setText("");

            noContactsLabel.setVisible(userListModel.isEmpty());

        });

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");
        JButton refreshButton = new JButton("Refresh");
        JButton sortButton = new JButton("A-Z");

        nameField.addActionListener(enter -> addButton.doClick());

        addButton.addActionListener(pressAdd -> {
            String text = nameField.getText().trim();
            if (!text.isEmpty()) {
                userListModel.addElement(text);
                noContactsLabel.setVisible(false);
                nameField.setText("");
                allContactsArchive.add(text);

                countLabel.setText("Contacts: " + allContactsArchive.size());

                saveToFile(allContactsArchive);

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
                    allContactsArchive.remove(selectedUser);

                    countLabel.setText("Contacts: " + allContactsArchive.size());

                    saveToFile(allContactsArchive);

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

                int archiveIndex = allContactsArchive.indexOf(currentName);
                if (archiveIndex != -1) {
                    allContactsArchive.set(archiveIndex, newName);
                }

                saveToFile(allContactsArchive);
            }});

        refreshButton.addActionListener(refresh -> {
            searchField.setText("");
            userListModel.clear();

            for (String contact : allContactsArchive) {
                userListModel.addElement(contact);
            }

            countLabel.setText("Contacts: " + allContactsArchive.size());

            noContactsLabel.setVisible(userListModel.isEmpty());

        });

        sortButton.addActionListener(sort -> {
            Collections.sort(allContactsArchive);

            userListModel.clear();

            for (String contact : allContactsArchive) {
                userListModel.addElement(contact);

                countLabel.setText("Contacts: " + allContactsArchive.size());

                saveToFile(allContactsArchive);

            }

        });

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(editButton);
        inputPanel.add(refreshButton);
        inputPanel.add(sortButton);

        JScrollPane scrollPanel = new JScrollPane(userList);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPanel, BorderLayout.CENTER);
        centerPanel.add(noContactsLabel, BorderLayout.NORTH);

        headerPanel.add(titleLabel);
        headerPanel.add(authorLabel);
        headerPanel.add(countLabel);
        headerPanel.add(searchPanel);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    private static void saveToFile(ArrayList<String>list) {
        try (PrintWriter writer = new PrintWriter("Contacts.txt")) {
            for (int i = 0; i < list.size(); i++) {

                writer.println(list.get(i));

            }
        } catch (FileNotFoundException error) {
            System.err.println("Error: " + error.getMessage());
        }
    }

}
