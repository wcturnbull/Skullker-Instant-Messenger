import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {
    private ArrayList<Account> users;
    private ArrayList<Message> messages;
    private String name;

    public Chat(Account owner, String name) {
        users = new ArrayList<Account>();
        messages = new ArrayList<Message>();
        users.add(owner);
        this.name = name;
    }

    public synchronized void sendMessage(Message message) {
        messages.add(message);
    }

    public synchronized ArrayList<Message> getMessages() {
        return messages;
    }

    public synchronized void addUser(Account user) {
        users.add(user);
    }

    public synchronized ArrayList<Account> getUsers() {
        return users;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Chat) {
            Chat chat = (Chat) o;
            return (this.name.equals(chat.getName()) &&
                    this.users.get(0).equals(((Chat) o).getUsers().get(0)));
        }
        return false;
    }
}
