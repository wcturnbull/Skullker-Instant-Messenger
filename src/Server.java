import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Server {
    private ArrayList<Chat> chats;
    private ServerThread serverThread;

    public Server() {
        chats = new ArrayList<Chat>(0);
        File[] chatFiles = new File("../chats/").listFiles();
        for (File chatFile : chatFiles) {
            try (BufferedReader in = new BufferedReader(new FileReader(chatFile))) {
                chats.add(new Chat(chats.size()));
                Chat chat = new Chat(chats.size() + 1);
                for (int i = 0; i < Integer.parseInt(in.readLine()); i++) {
                    chat.addUser();
                }
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }
}

class ClientThread extends Thread {

}

class ServerThread extends Thread {
    private ArrayList<Socket> clientSockets;
    private ServerSocket serverSocket;
    private ArrayList<ClientThread> clientThreads;

    public ServerThread() {

    }

    @Override
    public void run() {

    }
}
