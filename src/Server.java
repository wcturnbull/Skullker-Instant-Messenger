import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Server {
    private ArrayList<Chat> chats;
    private ArrayList<Account> users;
    private ArrayList<Message> messages;
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
        messages = new ArrayList<Message>();
        ServerThread serverThread = new ServerThread();
        serverThread.start();

        System.out.println("Deploying server thread...");
    }

    public synchronized void addChat(Chat chat) {
        chats.add(chat);
    }

    public synchronized void writeMessage(Message message) {
        messages.add(message);
    }

    public synchronized ArrayList<Message> getMessages() {
        for (Message m : messages) {
            System.out.println(m.getMessage());
        }
        return messages;
    }

    class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("Client thread deployed!");
            Message message = new Message("");
            do {
                try {
                    message = (Message) ois.readObject();
                    System.out.println(message.getMessage());
                    writeMessage(message);
                    oos.writeObject(getMessages());
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    message = null;
                }
            } while (message != null);
        }

    }

    class ServerThread extends Thread {
        private ArrayList<Socket> clientSockets;
        private ServerSocket serverSocket;
        private ArrayList<ClientThread> clientThreads;

        public ServerThread() {
            clientSockets = new ArrayList<Socket>();
            clientThreads = new ArrayList<ClientThread>();
            System.out.println("Server thread deployed!");
            try {
                serverSocket = new ServerSocket(0xBEEF);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client accepted!");
                    clientSockets.add(socket);
                    ClientThread clientThread = new ClientThread(socket);
                    clientThreads.add(clientThread);
                    System.out.println("Deploying client thread...");
                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server serv = new Server();
    }
}

