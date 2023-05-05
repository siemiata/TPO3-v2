import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NioServer {
    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 3000;
    private static final int SERVER_PORT = 9999;

    private static Map<String, String> hashMap = new HashMap<>();

    public static void main(String[] args) {

        try {
            reciveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select(TIMEOUT) == 0) {
                    System.out.println("No incoming connections.");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isAcceptable()) {
                        handleAcceptEvent(serverSocketChannel, selector);
                    } else if (selectionKey.isReadable()) {
                        handleReadEvent(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        handleWriteEvent(selectionKey);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAcceptEvent(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel clientSocketChannel = serverSocketChannel.accept();
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("New client connected: " + clientSocketChannel.getRemoteAddress());
    }

    private static void handleReadEvent(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        int bytesRead = clientSocketChannel.read(buffer);
        if (bytesRead == -1) {
            System.out.println("Client disconnected: " + clientSocketChannel.getRemoteAddress());
            clientSocketChannel.close();
            return;
        }

        String message = new String(buffer.array()).trim();
        System.out.println("Message received from client: " + message);

        // Check if client wants to get the HashMap
        if (message.equalsIgnoreCase("getHashMap")) {
            // Convert the HashMap to a string and send it to the client
            String hashMapAsString = hashMap.toString();
            ByteBuffer responseBuffer = ByteBuffer.wrap(hashMapAsString.getBytes());
            clientSocketChannel.register(selectionKey.selector(), SelectionKey.OP_WRITE, responseBuffer);
        }
    }

    private static void handleWriteEvent(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

        clientSocketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    public static void reciveData() throws IOException, ClassNotFoundException {
        Socket socket = null;
        String host = "localhost"; // adres IP lub nazwa hosta serwera
        int port = 8080; // numer portu, na którym działa serwer

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server");
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + host + " on port: " + port);
            System.exit(1);
        }

        InputStream in = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(in);

        // Pobranie hashmapy od serwera
        hashMap = (HashMap<String, String>) ois.readObject();

        // Wyświetlenie zawartości hashmapy
        System.out.println("HashMap:");
        for (String key : hashMap.keySet()) {
            System.out.println(key + " = " + hashMap.get(key));
        }

        ois.close();
        in.close();
        socket.close();

    }
}
