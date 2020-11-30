import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Account implements Serializable {
    private String userName;    //Username for a password
    private String password;    //Password for an account
    private ArrayList<Chat> chats;

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        chats = new ArrayList<>();
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

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public synchronized Chat fetchChat(Chat chat) {
        for (Chat c : chats) {
            if (c.equals(chat)) {
                return c;
            }
        }
        return null;
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
}
