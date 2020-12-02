import java.io.Serializable;
import java.util.Vector;

public class Chat implements Serializable {
    private Vector<Account> users;
    private Vector<Message> messages;
    private String name;

    public Chat(Account owner, String name) {
        users = new Vector<Account>();
        messages = new Vector<Message>();
        users.add(owner);
        owner.addChat(this);
        this.name = name;
    }

    public synchronized void sendMessage(Message message) {
        messages.add(message);
    }

    public synchronized Vector<Message> getMessages() {
        return messages;
    }

    public synchronized void addUser(Account user) {
        users.add(user);
        user.addChat(this);
    }

    public synchronized void removeMessage(Message message) {
        messages.remove(message);
    }

    public synchronized Vector<Account> getUsers() {
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
