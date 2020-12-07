import java.io.Serializable;
import java.util.Vector;
import java.time.LocalDateTime;

/**
 * Represents a chat that users can join and send messages to.
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Wes Turnbull, Evan Wang CS18000
 * @version 7 December 2020
 */
public class Chat implements Serializable {
    private Vector<Account> users;          // list of members
    private Vector<Message> messages;       // list of messages sent to the hcat
    private String name;                    // name of the chat
    private String time;                    // time of the chat's construction

    public Chat(Account owner, String name) {
        users = new Vector<Account>();
        messages = new Vector<Message>();
        users.add(owner);
        this.name = name;
        time = LocalDateTime.now().toString();
    }

    // sends a message to the chat.
    public void sendMessage(Message message) {
        messages.add(message);
    }

    // gets all messages from the chat.
    public Vector<Message> getMessages() {
        return messages;
    }

    // adds user to the chat.
    public void addUser(Account user) {
        users.add(user);
        user.joinChat(this);
    }

    // removes user from chat.
    public void removeUser(Account user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.get(i).leaveChat(this);
                users.remove(i);
            }
        }
    }

    // removes user from chat.
    // different from removeUser() because the user does not remove the chat from their list of chats.
    public void deleteUser(Account user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.remove(i);
            }
        }
    }

    // deletes message.
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    // gets members of the chat.
    public Vector<Account> getUsers() {
        return users;
    }

    // gets name of the chat.
    public synchronized String getName() {
        return name;
    }

    // sets name of the chat.
    public synchronized void setName(String name) {
        this.name = name;
    }

    // gets an updated reference to a message in the chat.
    public Message fetchMessage(Message message) {
        for (Message m : messages) {
            if (m.equals(message)) {
                return m;
            }
        }
        return null;
    }

    // gets the chat's time of creation
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
            // if the names, owners, and times of construction of two chats are equal, they are the same
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
