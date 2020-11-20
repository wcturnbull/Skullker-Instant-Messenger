import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Server {
    private ArrayList<Chat> chats;
    private ArrayList<Account> users;
    private ServerThread serverThread;

    public Server() {
        /**
        chats = new ArrayList<Chat>(0);
        File[] chatFiles = new File("../chats/").listFiles();
        for (File chatFile : chatFiles) {
            try (BufferedReader in = new BufferedReader(new FileReader(chatFile))) {
                chats.add(new Chat(chats.size()));
                Chat chat = new Chat(chats.size() + 1);
                for (int i = 0; i < Integer.parseInt(in.readLine()); i++) {

                }
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }**/
        ServerThread serverThread = new ServerThread();
        serverThread.run();
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            do {

            } while (true);
        }

    }

    class ServerThread extends Thread {
        private ArrayList<Socket> clientSockets;
        private ServerSocket serverSocket;
        private ArrayList<ClientThread> clientThreads;

        public ServerThread() {
            clientSockets = new ArrayList<Socket>();
            clientThreads = new ArrayList<ClientThread>();
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(0xBEEF);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    clientSockets.add(socket);
                    ClientThread clientThread = new ClientThread(socket);
                    clientThreads.add(clientThread);
                    clientThread.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

