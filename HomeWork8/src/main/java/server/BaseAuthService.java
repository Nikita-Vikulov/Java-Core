package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    @Override
    public void start() {
        System.out.println("Сервис авторизации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис авторизации остановлен");
    }

    @Override
    public String getNickByLoginAndPass(String login, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_chat", "root", "12345")) {
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS `users` (\n" +
                    "\t `UserID` INT(10) NOT NULL,\n" +
                    "\t `login` VARCHAR(500) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',\n" +
                    "\t `password` VARCHAR(500) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',\n" +
                    "\t `nick` VARCHAR(500) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',\n" +
                    "\t PRIMARY KEY (`UserID`) USING BTREE\n" +
                    ")\n" +
                    "COLLATE='utf8mb4_0900_ai_ci'\n" +
                    "ENGINE=InnoDB\n" +
                    ";\n");
            //statement.executeUpdate("insert into `users` (UserID, login, password, nick) values (\"1\",\"ivan\", \"password\", \"neivanov\")");
            //statement.executeUpdate("insert into `users` (UserID, login, password, nick) values (\"2\",\"sharik\", \"gav\", \"Auf\")");
            //statement.executeUpdate("insert into `users` (UserID, login, password, nick) values (\"3\",\"otvertka\", \"shurup\", \"Kruchu-verchu\")");

            ResultSet resultSet = statement.executeQuery("select * from users");
            while (resultSet.next()) {
                StringBuilder loginBase = new StringBuilder();
                StringBuilder passwordBase = new StringBuilder();
                StringBuilder nick = new StringBuilder();
                loginBase.append(resultSet.getString("login"));
                passwordBase.append(resultSet.getString("password"));
                if (login.contentEquals(String.valueOf(loginBase)) && password.contentEquals(String.valueOf(passwordBase))) {
                    nick.append(resultSet.getString("nick"));
                    return String.valueOf(nick);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    /*    for (Entry entry : entries) {
            if (login.equals(entry.login) && password.equals(entry.password)) {
                return entry.nick;
            }
        }*/
        return null;
    }

    public static void renameUserNick(String oldNick, String newNick) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_chat", "root", "12345")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users");

            while (resultSet.next()) {
                StringBuilder nick = new StringBuilder();
                nick.append(resultSet.getString("nick"));
                if(oldNick.contentEquals(String.valueOf(nick))) {
                    statement.executeUpdate("UPDATE users SET nick = '%s'  WHERE nick = '%d' ", new String[]{newNick, oldNick});
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
