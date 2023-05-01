import java.io.*;
import java.net.*;

public class MainServer {
    public static void main(String[] args) {
        newsServer();
    }

     public static void newsServer(){
         try {
             ServerSocket serverSocket = new ServerSocket(5000);
             System.out.println("Server started");
             Socket clientSocket;
             InputStream input;
             BufferedReader reader;

             while(true) {
                 clientSocket = serverSocket.accept();
                 System.out.println("Client connected");
                 input = clientSocket.getInputStream();
                 reader = new BufferedReader(new InputStreamReader(input));
                 String message = reader.readLine();
                 System.out.println("Received message: " + message);
                 clientSocket.close();
             }

         } catch (IOException ex) {
             System.out.println("Server exception: " + ex.getMessage());
             ex.printStackTrace();
         }
     }

}
