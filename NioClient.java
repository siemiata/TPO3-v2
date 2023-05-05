import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class NioClient {
    private static final int BUFFER_SIZE = 1024;
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {

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
            Map<String, String> mojaMapa = new HashMap<>();
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
}
