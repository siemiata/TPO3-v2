import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsServer extends JFrame {
    private JLabel loggedUserLabel;
    private JButton addTopicButton;
    private JButton removeTopicButton;
    private JButton reloadButton;
    private JButton openServer;
    public static HashMap<String, String> hashMap;


    public NewsServer() {
        super("NEWS SERVER");
        setLayout(new FlowLayout());
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loggedUserLabel = new JLabel("Zalogowany jako: ADMIN");
        add(loggedUserLabel);

        addTopicButton = new JButton("ADD TOPIC");
        add(addTopicButton);

        openServer = new JButton("UPDATE :)");
        add(openServer);

        openServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertHashMap("NewsData");
                try {
                    sendString();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Server has been updated");;
            }
        });
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
                reload();
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
            File folder = new File("NewsData");
            if (!folder.exists()) {
                folder.mkdir();
            }
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
            System.out.println("File " + filePath + " has been deleted.");
        } else {
            System.out.println("Cannot delete file " + filePath + ". File is not exist.");
        }
    }

    public void sendString() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            serverSocket.setSoTimeout(10000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
        }
        Socket clientSocket = null;
        System.out.println("Waiting for connection.....");
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Connection successful");
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        OutputStream out = clientSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(hashMap);

        oos.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void insertHashMap(String source){
        File folder = new File(source);
        hashMap = new HashMap<>();
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            StringBuilder contentsBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contentsBuilder.append(line).append("\n");
                }
                hashMap.put(fileName, contentsBuilder.toString());
            } catch (IOException e) {
                System.err.println("Błąd podczas czytania pliku: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
            NewsServer mainFrame = new NewsServer();
            mainFrame.setVisible(true);
        }
    }
