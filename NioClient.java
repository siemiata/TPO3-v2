import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class NioClient {
    private static final int BUFFER_SIZE = 1024;
    private static final int SERVER_PORT = 9999;
    static Map<String, String> mojaMapa;

    public static void main(String[] args) {
        gui();
    }

    public static void connect(){
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(SERVER_PORT));
            String message = "getHashMap";
            ByteBuffer requestBuffer = ByteBuffer.wrap(message.getBytes());
            socketChannel.write(requestBuffer);
            ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            int bytesRead = socketChannel.read(responseBuffer);
            String hashMapAsString = new String(responseBuffer.array(), 0, bytesRead).trim();
            mojaMapa = new HashMap<>();
            String[] keyValuePairs = hashMapAsString.substring(1, hashMapAsString.length() - 1).split(",");
            for (String pair : keyValuePairs) {
                String[] entry = pair.trim().split("=");
                mojaMapa.put(entry[0], entry[1]);
            }
            System.out.println("mojaMapa: " + mojaMapa);
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void gui(){
        connect();
        JFrame frame = new JFrame("ComboBox Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        JPanel panel = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>();
        for (String key : mojaMapa.keySet()) {
            comboBox.addItem(String.valueOf(key));
        }
        JButton button = new JButton("LOAD");
        panel.add(comboBox);
        panel.add(button);
        JTextArea textArea = new JTextArea(10, 20);
        textArea.setEditable(false);

        button.addActionListener(e -> {
            connect();
            String selectedKey = (String) comboBox.getSelectedItem();
            String selectedOption = mojaMapa.get(selectedKey);
            textArea.setText(selectedOption);
        });
        frame.getContentPane().add(panel, "North");
        frame.getContentPane().add(new JScrollPane(textArea), "Center");
        frame.setVisible(true);
    }
}
