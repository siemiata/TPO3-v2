import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsServer extends JFrame {
    private JLabel loggedUserLabel;
    private JButton addTopicButton;
    private JButton removeTopicButton;
    private JButton reloadButton;

    public NewsServer() {
        super("Forum App");
        setLayout(new FlowLayout());
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loggedUserLabel = new JLabel("Zalogowany jako: ADMIN");
        add(loggedUserLabel);

        addTopicButton = new JButton("ADD TOPIC");
        add(addTopicButton);
        addTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTopic();
            }
        });

        removeTopicButton = new JButton("REMOVE TOPIC");
        add(removeTopicButton);
        removeTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTopic();
            }
        });

        reloadButton = new JButton("RELOAD");
        add(reloadButton);
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });
    }

    private void addTopic() {
        JFrame addTopicFrame = new JFrame("Add Topic");
        addTopicFrame.setLayout(new GridLayout(3, 2));
        addTopicFrame.setSize(400, 150);
        JLabel titleLabel = new JLabel("TITLE:");
        JTextField titleField = new JTextField();
        JLabel fullTextLabel = new JLabel("FULL TEXT:");
        JTextField fullTextField = new JTextField();
        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String fullText = fullTextField.getText();
                System.out.println("TOPIC: " + title + " " + fullText);
                saveToFile(title,fullText);
            }
        });
        addTopicFrame.add(titleLabel);
        addTopicFrame.add(titleField);
        addTopicFrame.add(fullTextLabel);
        addTopicFrame.add(fullTextField);
        addTopicFrame.add(saveButton);
        addTopicFrame.setVisible(true);
    }

    private void removeTopic() {
        JFrame removeTopicFrame = new JFrame("Remove Topic");
        removeTopicFrame.setLayout(new GridLayout(2, 1));
        removeTopicFrame.setSize(300, 150);
        String[] topics = listTxtFiles("NewsData");
        JComboBox<String> topicComboBox = new JComboBox<>(topics);
        JButton deleteButton = new JButton("DELETE");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTopic = (String) topicComboBox.getSelectedItem();
                System.out.println("Selected topic: " + selectedTopic);
                deleteTxtFile("NewsData/" + selectedTopic);
            }
        });
        removeTopicFrame.add(topicComboBox);
        removeTopicFrame.add(deleteButton);
        removeTopicFrame.setVisible(true);
    }

    private void reload() {
        System.out.println("Reloading...");
        dispose();
        NewsServer newFrame = new NewsServer();
        newFrame.setVisible(true);
    }
    private void saveToFile(String title, String fullText) {
        String fileTitle;
        fileTitle = title;
        try {
            // Tworzenie folderu, jeśli nie istnieje
            File folder = new File("NewsData");
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Tworzenie pliku i zapisanie do niego zawartości
            File file = new File("NewsData/" + fileTitle + ".txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write(title + ":" + fullText + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] listTxtFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        List<String> txtFiles = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                txtFiles.add(file.getName());
            }
        }
        return txtFiles.toArray(new String[txtFiles.size()]);
    }
    public void deleteTxtFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
            System.out.println("Plik " + filePath + " został usunięty.");
        } else {
            System.out.println("Nie można usunąć pliku " + filePath + ". Plik nie istnieje lub nie jest plikiem.");
        }
    }



    public static void main(String[] args) {
            NewsServer mainFrame = new NewsServer();
            mainFrame.setVisible(true);
        }
    }
