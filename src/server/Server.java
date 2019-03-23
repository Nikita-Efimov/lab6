import java.io.*;
import java.net.*;
import java.util.*;

class Handle extends Thread {
    private Socket socket; // сокет, через который сервер общается с клиентом,
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private static CmdWorker worker;

    static {
        worker = new CmdWorker();
    }

    public Handle(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String str;
        try {
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals(null)) break;
                    processInput(str);
                }
            } catch (NullPointerException ignored) {}
        } catch (IOException e) {
            this.downService();
        }
    }

    private void getSerializedData(String data) {
        try {
            PriorityQueue<City> pq = (PriorityQueue<City>)Convertr.convertFromByteString(data);
            worker.set(pq);
         } catch (Exception e) {}
    }

    private void processInput(String clientData) {
        // System.out.println("> " + clientData);
        final String PREAMBLE = "#####";
        if (clientData.indexOf(PREAMBLE) == 0)
            getSerializedData(clientData.substring(PREAMBLE.length()));
        else {
            String out = worker.doCmd(clientData);
            if (!out.equals(""))
                send(out);
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + '\n');
            out.flush();
        } catch (IOException ignored) {}
    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (Handle vr : Server.serverList) {
                    if(vr.equals(this))
                        vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}

public class Server {
    public static final int PORT = 8080;
    public static LinkedList<Handle> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Сервер запущен");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Handle(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
