import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Client extends Thread {
    /**
     * add functionality so that client starts a socket that can
     * connect to the server.
     * once connected, allow the user to send messages to the server
     *
     * Neihl's Edit: So i was thinking about how this class will interact with the server class so I'm going to just write
     * them down here to talk about on monday. First and foremost, is this class going to be run through swingutilities
     * like i have it now? or will it be run through everything being implemented through the psvm. personally i think
     * the swingutilities functions way better. Second, the interaction with the server class. What I was envisioning
     * was something like a welcome screen with the options to sign in or create an account and when either option is chosen
     * then I'll send information to the server. All this being said, I'm not sure how to implement the class without the
     * gui aspect, so my final question is, should i just write this class to be run through the intellij terminal and then
     * later we will shift everything to gui's?
     */
    private Account acc;
    private ArrayList<Chat> chats;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 0xBEEF);
            System.out.println("Connection accepted!");
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //All the TO DO's are mentioned above
            System.out.println("Beginning to take messages...");
            Scanner in = new Scanner(System.in);
            while (true) {
                Message message = new Message(in.nextLine());
                oos.writeObject(message);
                oos.flush();
                ArrayList<Message> messages = (ArrayList<Message>) ois.readObject();
                for (Message m : messages) {
                    System.out.println(m.getMessage());
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }/**
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }**/


}
