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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainWindow {
    public static void main(String[] args) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        File contactsFile = new File("Contacts.txt");

        try {
            if (!contactsFile.exists()) {
                contactsFile.createNewFile();
                System.out.println("Contacts file not found; Automatically creating a Contacts file.");
            }
            else {
                System.out.println("The Contacts file exists already.");
            }
        }

        catch (IOException error) {
            System.out.println("Error creating Contacts file.");
            error.printStackTrace();
        }

        JFrame frame = new JFrame("BT-Gram");
        frame.setLayout(new BorderLayout(10, 10));
        frame.setSize(1050, 600);
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

                        String[] parts = trimmed.split("\\|");
                        userListModel.addElement(parts[0]);

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
        JTextField countryField = new JTextField(15);
        JTextField infoField = new JTextField(15);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        userList.addListSelectionListener(list -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1 && !list.getValueIsAdjusting()) {

                String selectedName = userListModel.getElementAt(selectedIndex);
                String fullData = null;

                for (String contact : allContactsArchive) {
                    String[] parts = contact.split("\\|");
                    if (parts.length > 0 && parts[0].equals(selectedName)) {
                        fullData = contact;
                        break;
                    }
                }

                if (fullData == null) return;

                String[] parts = fullData.split("\\|");
                String name = parts[0];
                String country = (parts.length > 1) ? parts[1] : "Unknown";
                String info = (parts.length >2) ? parts[2] : "No notes";

                // Not important for others; Checking under hood what happens to the app.

                System.out.println("Clicked on: " + name);

                JDialog contactsCard = new JDialog((JFrame)null, "Contacts Info", true);

                contactsCard.setSize(300, 200);
                contactsCard.setLayout(new GridLayout(3, 1, 10, 10));
                contactsCard.setLocationRelativeTo(null);

                JLabel nameLabel = new JLabel("Name: " + name, SwingConstants.CENTER);
                JLabel countryLabel = new JLabel("Country: " + country, SwingConstants.CENTER);
                JLabel infoLabel = new JLabel("Notes: " + info, SwingConstants.CENTER);

                contactsCard.add(nameLabel);
                contactsCard.add(countryLabel);
                contactsCard.add(infoLabel);

                contactsCard.setVisible(true);

            }
        });

        searchField.addActionListener(array -> {
            String query = searchField.getText().toLowerCase().trim();
            userListModel.clear();

            for (String contact : allContactsArchive) {

                String parts[] = contact.split("\\|");

                if (contact.toLowerCase().contains(query)) {
                    userListModel.addElement(parts[0]);
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
        countryField.addActionListener(enterCountry -> addButton.doClick());
        infoField.addActionListener(enterInfo -> addButton.doClick());

        addButton.addActionListener(pressAdd -> {

            String nameText = nameField.getText().trim();
            String countryText = countryField.getText().trim();
            String infoText = infoField.getText().trim();
            if (!nameText.isEmpty()) {

                if (countryText.isEmpty()) countryText = "Unknown";
                if (infoText.isEmpty()) infoText = "No notes";

                String fullContactLine = nameText + "|" + countryText + "|" + infoText;

                userListModel.addElement(nameText);
                noContactsLabel.setVisible(false);
                allContactsArchive.add(fullContactLine);

                nameField.setText("");
                countryField.setText("");
                infoField.setText("");

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

                    for (int i = 0; i < allContactsArchive.size(); i++) {
                        if (allContactsArchive.get(i).startsWith(selectedUser + "|")) {
                            allContactsArchive.remove(i);
                            break;
                        }
                    }

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

                String selectedUser = userListModel.getElementAt(selectedIndex);

                int archiveIndex = -1;
                for (int i = 0; i < allContactsArchive.size(); i++) {
                    if (allContactsArchive.get(i).startsWith(selectedUser + "|")) {
                        archiveIndex = i;
                        break;
                    }
                }

                if (archiveIndex == -1) return;

                String fullData = allContactsArchive.get(archiveIndex);
                String[] parts = fullData.split("\\|");
                String currentName = parts[0];
                String currentCountry = (parts.length > 1) ? parts[1] : "Unknown";
                String currentInfo = (parts.length > 2) ? parts[2] : "No notes";

                String newName = JOptionPane.showInputDialog(frame, "Change name:", currentName);
                if (newName == null || newName.trim().isEmpty()) {
                    newName = currentName;
                }

                    userListModel.setElementAt(newName, selectedIndex);

                    String newCountry = JOptionPane.showInputDialog(frame, "Change country:", currentCountry);
                    if (newCountry == null || newCountry.trim().isEmpty()) {
                        newCountry = currentCountry;

                        }

                    String newInfo = JOptionPane.showInputDialog(frame, "Change notes:", currentInfo);
                    if (newInfo == null || newInfo.trim().isEmpty()) {
                        newInfo = currentInfo;

                    }

                    String updatedContactLine = newName + "|" + newCountry + "|" + newInfo;
                    System.out.println("Changed element to:" + updatedContactLine);

                    allContactsArchive.set(archiveIndex, updatedContactLine);

                    saveToFile(allContactsArchive);

            }
            });

        refreshButton.addActionListener(refresh -> {
            searchField.setText("");
            userListModel.clear();

            for (String contact : allContactsArchive) {

                String[] parts = contact.split("\\|");

                userListModel.addElement(parts[0]);
            }

            countLabel.setText("Contacts: " + allContactsArchive.size());

            noContactsLabel.setVisible(userListModel.isEmpty());

        });

        sortButton.addActionListener(sort -> {

            allContactsArchive.sort(String.CASE_INSENSITIVE_ORDER);

            userListModel.clear();

            for (String contact : allContactsArchive) {

                String[] parts = contact.split("\\|");

                userListModel.addElement(parts[0]);
            }

            countLabel.setText("Contacts: " + allContactsArchive.size());
            saveToFile(allContactsArchive);

        });

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Country: "));
        inputPanel.add(countryField);
        inputPanel.add(new JLabel("Notes:"));
        inputPanel.add(infoField);
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
