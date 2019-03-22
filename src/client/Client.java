import java.net.*;
import java.io.*;

class ClientInteraction {
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String addr; // ip адрес клиента
    private int port; // порт соединения
    private volatile boolean status;

    public ClientInteraction(String addr, int port) {
        this.addr = addr;
        this.port = port;
        this.status = true;

        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {}

        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Успешное подключение");
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            ClientInteraction.this.downService();
        }
    }

    private void downService() {
        try {
            if (!this.status) return;
            System.err.println("Сервер недоступен");
            this.status = false;
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                System.exit(0);
            }
        } catch (IOException ignored) {}
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true && status) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals(null)) break;
                    if (str.trim().equals("")) continue;
                    System.out.println("> " + str); // пишем сообщение с сервера на консоль
                }
            } catch (Exception e) {
                ClientInteraction.this.downService();
            }
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (status) {
                String str;
                try {
                    str = inputUser.readLine(); // сообщения с консоли
                    out.write(str + '\n');
                    out.flush(); // чистим
                } catch (IOException e) {
                    ClientInteraction.this.downService(); // в случае исключения тоже харакири
                }
            }
        }
    }
}

public class Client {
    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) {
        try {
            new ClientInteraction(ipAddr, port);
        } catch (Exception e) {
            System.out.println("Сервер недоступен");
        }
    }
}
