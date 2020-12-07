import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Vector;

/**
 * Server that serves each user and keeps track of necessary data.
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Evan Wang CS18000
 * @version 7 December 2020
 */
public class Server implements Constants {
    private Vector<Chat> chats;         // list of chats with members
    private Vector<Account> users;      // list of active users

    private final Object runSync;       // multi-thread gatekeeper

    private boolean run;                // if run is true, the server is not terminated

    public Server() {
        runSync = new Object();
        run = true;

        // reads data from data.txt if it exists; otherwise, initializes lists
        if (!(new File("data.txt").exists())) {
            chats = new Vector<Chat>();
            users = new Vector<Account>();
        } else {
            try (ObjectInputStream fois = new ObjectInputStream(new FileInputStream("data.txt"))) {
                chats = (Vector<Chat>) fois.readObject();
                users = (Vector<Account>) fois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // launches ServerThread
        ServerThread serverThread = new ServerThread();
        serverThread.start();

        System.out.println("Deploying server thread...");
    }

    // adds a chat to the list of chats.
    public void addChat(Chat chat) {
        chats.add(chat);
    }

    // sends a message to the given chat in the list of chats.
    public void writeMessage(Message message) {
        fetchChat(message.getChat()).sendMessage(message);
    }

    // fetches an updated reference to an equivalent chat in the list of chats.
    public Chat fetchChat(Chat clientChat) {
        for (Chat chat : chats) {
            if (chat.equals(clientChat)) {
                return chat;
            }
        }
        return null;
    }

    // fetches an updated reference to an equivalent user in the list of users.
    public Account fetchAccount(Account acc) {
        for (Account a : users) {
            if (a.matchesCredentials(acc)) {
                return a;
            }
        }
        return null;
    }

    // fetches an updated reference to a user with the same username in the list of users.
    public Account matchUsernames(Account acc) {
        for (Account a : users) {
            if (a.matchesUsername(acc)) {
                return a;
            }
        }
        return null;
    }

    // fetches an updated reference to a user with the same credentials in the list of users.
    public Account matchCredentials(Account acc) {
        for (Account a : users) {
            if (a.matchesCredentials(acc)) {
                return a;
            }
        }
        return null;
    }

    // deletes the given account from the list of users.
    public void deleteAccount(Account account) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).matchesCredentials(account)) {
                users.get(i).delete();
                users.remove(i);
                return;
            }
        }
    }

    // removes a chat from the list of chats if it is no longer usable (i.e. it has no members).
    public void sanitize() {
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getUsers().size() < 1) {
                chats.remove(i);
            }
        }
    }

    /**
     * Thread that is responsible for serving a user's queries.
     *
     * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
     *
     * @author Evan Wang CS18000
     * @version 7 December 2020
     */
    class ClientThread extends Thread {
        private final Socket socket;        // socket belonging to the user
        private ObjectInputStream ois;      // input stream for socket
        private ObjectOutputStream oos;     // output stream for socket
        private Account client;             // associated Account for the user

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("Client thread deployed!");
            byte choice = 0;                // the query constant sent to the server.
            while (run) {
                try {
                    choice = ois.readByte();
                    if (choice == LOG_IN) { // logging in.
                        Account acc = fetchAccount((Account) ois.readObject());
                        if (acc == null) {
                            oos.writeByte(DENIED);
                        } else {
                            oos.writeByte(CONTINUE);
                            System.out.println("Successful log-in!");
                            client = acc;
                            oos.writeObject(acc);
                        }
                        oos.flush();
                    } else if (choice == REGISTER_ACCOUNT) { // creating a new account.
                        Account newAcc = (Account) ois.readObject();
                        if (matchUsernames(newAcc) == null) {
                            oos.writeByte(CONTINUE);
                            users.add(newAcc);
                            System.out.println("Successful registration!");
                            client = newAcc;
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == DELETE_ACCOUNT) { // deleting an account.
                        deleteAccount(client);
                        System.out.println(client.getUserName() + " was deleted.");
                        client = null;
                    } else if (choice == EDIT_USERNAME) { // editing an account's username.
                        Account acc = (Account) ois.readObject();
                        if (matchUsernames(acc) == null) {
                            oos.writeByte(CONTINUE);
                            client.setUserName(acc.getUserName());
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == EDIT_PASSWORD) { // editing an account's password.
                        Account acc = (Account) ois.readObject();
                        client.setPassword(acc.getPassword());
                    } else if (choice == SEND_MESSAGE) { // sending a message.
                        Message message = (Message) ois.readObject();
                        writeMessage(new Message(client, message.getMessage(),
                                fetchChat(message.getChat())));
                    } else if (choice == CREATE_CHAT) { // creating a chat
                        Chat chat = new Chat(client, ((Chat) ois.readObject()).getName());
                        if (fetchChat(chat) == null) {
                            oos.writeByte(CONTINUE);
                            addChat(chat);
                            client.joinChat(chat);
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == ADD_USER_TO_CHAT) { // adding a user to a chat.
                        Chat chat = (Chat) ois.readObject();
                        Account acc = matchUsernames((Account) ois.readObject());
                        if (acc == null || fetchChat(chat).getUsers().contains(acc)) {
                            oos.writeByte(DENIED);
                        } else {
                            oos.writeByte(CONTINUE);
                            fetchChat(chat).addUser(acc);
                        }
                        oos.flush();
                    } else if (choice == LEAVE_CHAT) { // leaving a chat.
                        Chat chat = (Chat) ois.readObject();
                        fetchChat(chat).removeUser(client);
                    } else if (choice == EDIT_MESSAGE) { // editing a message.
                        Message message = (Message) ois.readObject();
                        String newText = (String) ois.readObject();
                        fetchChat(message.getChat()).fetchMessage(message).editMessage(newText);
                    } else if (choice == DELETE_MESSAGE) { // deleting a message.
                        Message message = (Message) ois.readObject();
                        fetchChat(message.getChat()).removeMessage(message);
                    }
                    // sending updated Account object so long as the account is not null
                    if (client != null) {
                        oos.writeObject(client);
                        oos.flush();
                    }
                    sanitize();
                    oos.reset();
                } catch (EOFException e) {
                    break;
                } catch (SocketException e) {
                    System.out.println("Client disconnected...");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
                ois.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread responsible for accepting new connections to the server.
     *
     * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
     *
     * @author Wes Turnbull, Evan Wang CS18000, 001
     * @version 7 December 2020
     */
    class ServerThread extends Thread {
        private ServerSocket serverSocket;      // server's ServerSocket

        public ServerThread() {
            System.out.println("Server thread deployed!");
            try {
                serverSocket = new ServerSocket(0xBEEF);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // starts an executioner thread
            new ExecutionerThread().start();
            while (run) { // accepts new connections so long as the server is running
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client accepted!");
                    ClientThread clientThread = new ClientThread(socket);
                    System.out.println("Deploying client thread...");
                    clientThread.start();
                } catch (SocketException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Thread responsible for terminating the Server safely when
         * the maintainer of the Server enters anything into the terminal.
         *
         * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
         *
         * @author Wes Turnbull, Evan Wang CS18000, 001
         * @version 7 December 2020
         */
        class ExecutionerThread extends Thread {
            private Scanner in;     // input for the maintainer of the server

            public ExecutionerThread() {
                in = new Scanner(System.in);
            }

            @Override
            public void run() {
                // this method blocks at in.nextLine() until the maintainer enters anything.
                System.out.println("Respond to this prompt when you desire to close the server.");
                in.nextLine();
                // closes the ServerSocket once something is entered into terminal.
                synchronized (runSync) {
                    run = false;
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // writes current data to data.txt.
                try (ObjectOutputStream foos = new ObjectOutputStream(new FileOutputStream("data.txt"))) {
                    foos.writeObject(chats);
                    foos.writeObject(users);
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

