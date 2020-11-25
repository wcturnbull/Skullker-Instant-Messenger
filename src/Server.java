import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Server implements Constants {
    private ArrayList<Chat> chats;
    private ArrayList<Account> users;
    private ArrayList<Message> messages;
    private ServerThread serverThread;

    // multi-thread gatekeepers
    private final Object CHAT_SYNC;
    private final Object USER_SYNC;
    private final Object MESSAGE_SYNC;

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
        CHAT_SYNC = new Object();
        USER_SYNC = new Object();
        MESSAGE_SYNC = new Object();

        messages = new ArrayList<Message>();
        ServerThread serverThread = new ServerThread();
        serverThread.start();

        System.out.println("Deploying server thread...");
    }

    public void addChat(Chat chat) {
        synchronized (CHAT_SYNC) {
            chats.add(chat);
        }
    }

    public void writeMessage(Chat chat, Message message) {
        Chat currentChat = fetchChat(chat);
        synchronized (CHAT_SYNC) {
            currentChat.sendMessage(message);
        }
    }

    public ArrayList<Message> getMessages(Chat chat) {
        return fetchChat(chat).getMessages();
    }

    public Chat fetchChat(Chat clientChat) {
        synchronized (CHAT_SYNC) {
            for (Chat chat : chats) {
                if (chat.equals(clientChat)) {
                    return chat;
                }
            }
        }
        return clientChat;
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
            byte choice = 0;
            do {
                try {
                    choice = ois.readByte();
                    Chat currentChat = (Chat) ois.readObject();
                    switch (choice) {
                        case SEND_MESSAGE:
                            Message message = (Message) ois.readObject();
                            System.out.println(message.getMessage());
                            writeMessage(currentChat, message);
                            oos.writeInt(getMessages(currentChat).size());
                            break;
                        case CREATE_CHAT:
                            Chat chat = (Chat) ois.readObject();
                            addChat(chat);
                            break;
                    }
                    oos.writeUnshared(getMessages(currentChat));
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while (choice != CLIENT_DISCONNECT);
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

