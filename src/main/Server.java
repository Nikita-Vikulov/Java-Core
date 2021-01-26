package main;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Server {
    static Socket socket;

   // DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
   // DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    String inputString;
    String outputString;
    String id = UUID.randomUUID().toString();
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            System.out.println("Создали серверный сокет");
            System.out.println("Ожидаем подключение клиента");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Server() throws IOException {
        start();
    }

    public synchronized void start() {
        new Thread(() -> {
            while (true) {
                readMessage();
                writeMessage();
            }
        }).start();

    }

    public synchronized void readMessage() {
        try {
            do {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String inputString = dataInputStream.readUTF();
                Message message = new Gson().fromJson(inputString, Message.class);
                System.out.println("Echo serverId=" + message.getServerId() + ": ");
                System.out.println(message.getMessage());
            } while (!"\\finish".equals(inputString));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage() {
        try {
            do {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                String outputString = scanner.nextLine();
                Message message = new Message();
                message.setMessage(outputString);
                message.setServerId(id);
                dataOutputStream.writeUTF(new Gson().toJson(message));
                dataOutputStream.flush();

            } while (!"\\finish".equals(outputString));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
