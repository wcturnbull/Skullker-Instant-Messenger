import java.io.Serializable;
import java.util.Vector;
import java.time.LocalDateTime;
import java.awt.Color;

/**
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Wes Turnbull, Evan Wang CS18000, 001
 * @version 7 December 2020
 */
public class Account implements Serializable {
    private String userName;    //Username for a password
    private String password;    //Password for an account
    private String serial;
    private Vector<Chat> chats;

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        serial = String.format("%s %s %s", userName, password, LocalDateTime.now());
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
        this.serial = "";
        for (Chat c : chats) {
            c.deleteUser(this);
        }
    }

    public synchronized boolean matchesUsername(Account acc) {
        return this.userName.equals(acc.getUserName());
    }

    public synchronized boolean matchesCredentials(Account acc) {
        return this.userName.equals(acc.getUserName()) && this.password.equals(acc.getPassword()) ;
    }

    public String getSerial() {
        return serial;
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
            return (this.serial.equals(account.getSerial()));
        }
        return false;
    }

    @Override
    public synchronized String toString() {
        return String.format("%s %s ", userName, password);
    }
}
