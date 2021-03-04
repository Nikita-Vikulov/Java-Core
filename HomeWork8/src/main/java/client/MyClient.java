package client;

import server.Message;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.ArrayList;

public class MyClient extends JFrame {

    private ServerService serverService;

    public MyClient() {
        super("Чат");
        long a = System.currentTimeMillis();

        serverService = new SocketServerService();
        serverService.openConnection();
        JPanel jPanel = new JPanel();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.setSize(300, 20);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(400, 400, 500, 300);

        JTextArea mainChat = new JTextArea();
        mainChat.setSize(400, 300);

        //   mainPanel.add(scrollPane, BorderLayout.CENTER);
        // JFrame.add(scrollPane);
        // scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        initLoginPanel(mainChat);

        JTextField myMessage = new JTextField();

        JButton send = new JButton("Send");
        send.addActionListener(actionEvent -> sendMessage(myMessage));

        myMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(myMessage);
                }
            }
        });

        if (serverService.isConnected()) {
            new Thread(() -> {
                while (true) {
                    printToUI(mainChat, serverService.readMessages());
                }
            }).start();
        }
        //JScrollPane scrollPane = new JScrollPane(jPanel);

        add(mainChat);
        jPanel.add(send);
        jPanel.add(myMessage);
        add(new JScrollPane(mainChat));
        add(jPanel);


        new Thread(() -> Connection(a)).start();
    }

    private void initLoginPanel(JTextArea mainChat) {
        JTextField login = new JTextField();
        login.setToolTipText("Логин");
        JPasswordField password = new JPasswordField();
        password.setToolTipText("Пароль");
        JButton authButton = new JButton("Авторизоваться");

        JLabel authLabel = new JLabel("Offline");

        authButton.addActionListener(actionEvent -> {
            String lgn = login.getText();
            String psw = new String(password.getPassword());
            if (lgn != null && psw != null && !lgn.isEmpty() && !psw.isEmpty()) {
                try {
                    String nick = serverService.authorization(lgn, psw);
                    authLabel.setText("Online, nick " + nick);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(() -> {
                    while (true) {
                        printToUI(mainChat, serverService.readMessages());
                    }
                }).start();
            }
        });
        add(login);
        add(password);
        add(authButton);
        add(authLabel);

    }

    private void sendMessage(JTextField myMessage) {
        serverService.sendMessage(myMessage.getText());
        myMessage.setText("");
    }

    private void printToUI(JTextArea mainChat, Message message) {
        mainChat.append("\n");
        if (message.getNick() != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\shiel\\IdeaProjects\\II\\HomeWork8\\src\\main\\java\\client\\demo.txt", true))) {
                   //  for (int i = 0; i < 100; i++) { writer.write("\nСообщение N: " + i); }
                mainChat.append(message.getNick() + " написал: " + message.getMessage());
                writer.write("\n" + message.getNick() + " написал: " + message.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mainChat.append("Сервер" + " написал: " + message.getMessage());
            try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\shiel\\IdeaProjects\\II\\HomeWork8\\src\\main\\java\\client\\demo.txt"))) {
                String str;
                int i = 0;
                ArrayList<String> arr = new ArrayList<>();
                while ((str = reader.readLine()) != null) {
                    i++;
                    arr.add("\n" + str);
                }
                if (i < 100) {
                    mainChat.append(String.valueOf(arr));
                } else {
                    int a = i - 100;
                    arr.subList(0, a).clear();
                    mainChat.append(String.valueOf(arr));
                }
                // for(int i = 0; i>str.length(); i++){}

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Connection(long a) {
        do {
            if (System.currentTimeMillis() - a >= 120000 && !serverService.isConnected()) {
                serverService.closeConnection();
                System.exit(0);
            }
        } while (true);
    }
}
