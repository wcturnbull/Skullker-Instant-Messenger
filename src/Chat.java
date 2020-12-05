import java.io.Serializable;
import java.util.Vector;
import java.time.LocalDateTime;

/**
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Wes Turnbull, Evan Wang CS18000, 001
 * @version 7 December 2020
 */
public class Chat implements Serializable {
    private Vector<Account> users;
    private Vector<Message> messages;
    private String name;
    private String time;

    public Chat(Account owner, String name) {
        users = new Vector<Account>();
        messages = new Vector<Message>();
        users.add(owner);
        this.name = name;
        time = LocalDateTime.now().toString();
    }

    public void sendMessage(Message message) {
        messages.add(message);
    }

    public Vector<Message> getMessages() {
        return messages;
    }

    public void addUser(Account user) {
        users.add(user);
        user.joinChat(this);
    }

    public void removeUser(Account user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.get(i).leaveChat(this);
                users.remove(i);
            }
        }
    }

    public void deleteUser(Account user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.remove(i);
            }
        }
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public Vector<Account> getUsers() {
        return users;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public Message fetchMessage(Message message) {
        for (Message m : messages) {
            if (m.equals(message)) {
                return m;
            }
        }
        return null;
    }

    public String getTime() {
        return time;
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
            if (chat.getUsers().size() != 0 && users.size() != 0) {
                return (this.name.equals(chat.getName()) &&
                        this.users.get(0).equals(((Chat) o).getUsers().get(0)) &&
                        this.time.equals(chat.getTime()));
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(String.format("%s %s ", name, users.get(0)));
        out.append("\n(");
        for (int i = 1; i < users.size(); i++) {
            out.append(users.get(i).getUserName()).append(", ");
        }
        out.substring(0, out.length() - 2);
        out.append(")");
        for (Message m : messages) {
            out.append(String.format("\n\t%s", m));
        }
        return out.toString();
    }
}
