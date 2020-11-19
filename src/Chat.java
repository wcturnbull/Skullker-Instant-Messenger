import java.util.ArrayList;
public class Chat {
    private ArrayList<Account> users;
    private ArrayList<Message> messages;
    final private long serialNum;

    public Chat(long serialNum) {
        users = new ArrayList<Account>();
        messages = new ArrayList<Message>();
        this.serialNum = serialNum;
    }

    public void sendMessage(Message message) {
        messages.add(message);
    }

    public void addUser(Account user) {
        users.add(user);
    }
}
