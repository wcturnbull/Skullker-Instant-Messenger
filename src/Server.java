import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Vector;

public class Server implements Constants {
    private Vector<Chat> chats;
    private Vector<Account> users;
    private Vector<Message> messages;
    private ServerThread serverThread;

    // multi-thread gatekeepers
    private final Object CHAT_SYNC;
    private final Object USER_SYNC;
    private final Object MESSAGE_SYNC;
    private final Object RUN_SYNC;

    private boolean run;

    public Server() {
        CHAT_SYNC = new Object();
        USER_SYNC = new Object();
        MESSAGE_SYNC = new Object();
        RUN_SYNC = new Object();

        run = true;

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
        ServerThread serverThread = new ServerThread();
        serverThread.start();

        System.out.println("Deploying server thread...");
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void writeMessage(Message message) {
        fetchChat(message.getChat()).sendMessage(message);
    }

    public Chat fetchChat(Chat clientChat) {
        for (Chat chat : chats) {
            if (chat.equals(clientChat)) {
                return chat;
            }
        }
        return null;
    }

    public Account fetchAccount(Account acc) {
        for (Account a : users) {
            if (a.equals(acc)) {
                return a;
            }
        }
        return null;
    }

    public Account matchAccounts(Account acc) {
        for (Account a : users) {
            if (a.matches(acc)) {
                return a;
            }
        }
        return null;
    }

    public void deleteAccount(Account account) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(account)) {
                users.get(i).delete();
                users.remove(i);
                return;
            }
        }
    }

    class ClientThread extends Thread {
        private final Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Account client;

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
            byte choice = 0;
            while (run) {
                try {
                    choice = ois.readByte();
                    if (choice == LOG_IN) {
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
                    } else if (choice == REGISTER_ACCOUNT) {
                        Account newAcc = (Account) ois.readObject();
                        if (matchAccounts(newAcc) == null) {
                            oos.writeByte(CONTINUE);
                            users.add(newAcc);
                            System.out.println("Successful registration!");
                            client = newAcc;
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == DELETE_ACCOUNT) {
                        deleteAccount(client);
                        System.out.println(client.getUserName() + " was deleted.");
                        client = null;
                    } else if (choice == EDIT_USERNAME) {
                        Account acc = (Account) ois.readObject();
                        if (matchAccounts(acc) == null) {
                            oos.writeByte(CONTINUE);
                            client.setUserName(acc.getUserName());
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == EDIT_PASSWORD) {
                        Account acc = (Account) ois.readObject();
                        client.setPassword(acc.getPassword());
                    } else if (choice == SEND_MESSAGE) {
                        writeMessage(new Message(client, (String) ois.readObject(),
                                fetchChat((Chat) ois.readObject()), (String) ois.readObject()));
                    } else if (choice == CREATE_CHAT) {
                        Chat chat = new Chat(client, ((Chat) ois.readObject()).getName());
                        if (fetchChat(chat) == null) {
                            oos.writeByte(CONTINUE);
                            addChat(chat);
                            client.joinChat(chat);
                        } else {
                            oos.writeByte(DENIED);
                        }
                        oos.flush();
                    } else if (choice == ADD_USER_TO_CHAT) {
                        Chat chat = (Chat) ois.readObject();
                        Account acc = matchAccounts((Account) ois.readObject());
                        if (acc == null || fetchChat(chat).getUsers().contains(acc)) {
                            oos.writeByte(DENIED);
                        } else {
                            oos.writeByte(CONTINUE);
                            fetchChat(chat).addUser(acc);
                        }
                        oos.flush();
                    } else if (choice == LEAVE_CHAT) {
                        Chat chat = (Chat) ois.readObject();
                        fetchChat(chat).removeUser(client);
                    } else if (choice == EDIT_MESSAGE) {
                        Message message = (Message) ois.readObject();
                        String newText = (String) ois.readObject();
                        fetchChat(message.getChat()).fetchMessage(message).editMessage(newText);
                    } else if (choice == DELETE_MESSAGE) {
                        Message message = (Message) ois.readObject();
                        fetchChat(message.getChat()).removeMessage(message);
                    }
                    if (client != null) {/**
                        System.out.println("Echoing client info...");
                        System.out.println(client.getUserName());
                        for (Chat c : client.getChats()) {
                            System.out.println(c);
                        }**/
                        oos.writeObject(client);
                        oos.flush();
                    }
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

    class ServerThread extends Thread {
        private ServerSocket serverSocket;

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
            new ExecutionerThread().start();
            while (run) {
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

        class ExecutionerThread extends Thread {
            private Scanner in;

            public ExecutionerThread() {
                in = new Scanner(System.in);
            }

            @Override
            public void run() {
                System.out.println("Respond to this prompt when you desire to close the server.");
                in.nextLine();
                synchronized (RUN_SYNC) {
                    run = false;
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

