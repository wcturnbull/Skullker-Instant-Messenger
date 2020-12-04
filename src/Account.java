import java.io.Serializable;
import java.util.Vector;

public class Account implements Serializable {
    private String userName;    //Username for a password
    private String password;    //Password for an account
    private Vector<Chat> chats;

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        chats = new Vector<Chat>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void copy(Account other) {
        this.userName = other.getUserName();
        this.password = other.password;
    }

    public void joinChat(Chat chat) {
        chats.add(chat);
    }

    public void leaveChat(Chat chat) {
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).equals(chat)) {
                chats.remove(i);
            }
        }
    }

    public Vector<Chat> getChats() {
        return chats;
    }

    public Chat fetchChat(Chat chat) {
        for (Chat c : chats) {
            if (c.equals(chat)) {
                return c;
            }
        }
        return null;
    }

    public void delete() {
        this.setUserName(this.userName + " (DELETED)");
        for (Chat c : chats) {
            c.removeUser(this);
        }
    }

    public synchronized boolean matches(Account acc) {
        return this.userName.equals(acc.getUserName());
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Account) {
            Account account = (Account) o;
            return (userName.equals(account.getUserName()) &&
                    password.equals(account.getPassword()));
        }
        return false;
    }

    @Override
    public synchronized String toString() {
        return String.format("%s %s ", userName, password);
    }
}
