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
        CHAT_SYNC = new Object();
        USER_SYNC = new Object();
        MESSAGE_SYNC = new Object();

        users = new ArrayList<Account>();
        chats = new ArrayList<Chat>();
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
        return null;
    }

    public void addUser(Account newAcc) {
        synchronized (USER_SYNC) {
            users.add(newAcc);
        }
    }

    public Account fetchAccount(Account clientAcc) {
        synchronized (USER_SYNC) {
            for (Account acc : users) {
                if (acc.equals(clientAcc)) {
                    return acc;
                }
            }
        }
        return null;
    }

    public void deleteAccount(Account account) {
        synchronized (USER_SYNC) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).equals(account)) {
                    users.remove(i);
                    return;
                }
            }
        }
    }

    class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Account client;

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
            while (true) {
                try {
                    choice = ois.readByte();
                    switch (choice) {
                        case LOG_IN:
                            Account acc = fetchAccount((Account) ois.readObject());
                            if (acc == null) {
                                oos.writeByte(DENIED);
                            } else {
                                oos.writeByte(CONTINUE);
                                System.out.println("Successful log-in!");
                                client = acc;
                                oos.writeUnshared(acc);
                            }
                            oos.flush();
                            break;
                        case REGISTER_ACCOUNT:
                            Account newAcc = (Account) ois.readObject();
                            if (fetchAccount(newAcc) == null) {
                                addUser(newAcc);
                                System.out.println("Successful registration!");
                                oos.writeByte(CONTINUE);
                                client = newAcc;
                            } else {
                                oos.writeByte(DENIED);
                            }
                            oos.flush();
                            break;/**
                        case SEND_MESSAGE:
                            Message message = (Message) ois.readObject();
                            System.out.println(message.getMessage());
                            writeMessage(currentChat, message);
                            oos.writeUnshared(getMessages(currentChat));
                            break;
                        case CREATE_CHAT:
                            Chat chat = (Chat) ois.readObject();
                            addChat(chat);
                            break;**/
                            /*
                            TODO: send message
                            TODO: create chat
                            TODO: delete account
                            TODO: delete message
                            TODO: edit message
                            TODO: edit account
                            TODO: server persistence
                            TODO: add user to chat
                            TODO: delete chat
                             */
                    }
                    if (client != null) {
                        oos.writeUnshared(client);
                    }
                } catch (EOFException e) {
                    break;
                } catch (SocketException e) {
                    System.out.println("Client disconnected...");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                in.close();
                out.close();

                ois.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

