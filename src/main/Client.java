package main;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    String id = UUID.randomUUID().toString();
    Socket socket = new Socket("localhost", 8081);
    Scanner scanner = new Scanner(System.in);
    // DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    // DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
    String inputString;
    String outputString;

    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {
        start();
    }

    public void start() {
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
                System.out.println("Echo clientId=" + message.getClientId() + ": ");
                System.out.println(message.getMessage());
            } while (!"\\finish".equals(inputString));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeMessage() {
        try {
            do {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                String outputString = scanner.nextLine();
                Message message = new Message();
                message.setMessage(outputString);
                message.setClientId(id);
                dataOutputStream.writeUTF(new Gson().toJson(message));
                dataOutputStream.flush();
            } while (!"\\finish".equals(outputString));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


