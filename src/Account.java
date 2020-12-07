import java.io.Serializable;
import java.util.Vector;
import java.time.LocalDateTime;

/**
 * Represents an account a user can have.
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Wes Turnbull, Evan Wang CS18000
 * @version 7 December 2020
 */
public class Account implements Serializable {
    private String userName;        // username of account
    private String password;        // password of account
    private String serial;          // serial code of the account
    private Vector<Chat> chats;     // list of chats the user is in

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
        serial = String.format("%s %s %s", userName, password, LocalDateTime.now());
        chats = new Vector<Chat>();
    }

    // gets username of account.
    public String getUserName() {
        return userName;
    }

    // sets username of account.
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // gets password of account.
    public String getPassword() {
        return password;
    }

    // sets password of account.
    public void setPassword(String password) {
        this.password = password;
    }

    // copies credentials of other account.
    public void copy(Account other) {
        this.userName = other.getUserName();
        this.password = other.password;
    }

    // adds chat to the user's list of chats.
    public void joinChat(Chat chat) {
        chats.add(chat);
    }

    // leaves a chat.
    public void leaveChat(Chat chat) {
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).equals(chat)) {
                chats.remove(i);
            }
        }
    }

    // accessor for the user's list of chats.
    public Vector<Chat> getChats() {
        return chats;
    }

    // gets updated reference for equivalent chat.
    public Chat fetchChat(Chat chat) {
        for (Chat c : chats) {
            if (c.equals(chat)) {
                return c;
            }
        }
        return null;
    }

    // deletes the account.
    public void delete() {
        this.setUserName(this.userName + " (DELETED)");
        this.serial = "";
        for (Chat c : chats) {
            c.deleteUser(this);
        }
    }

    // sees if the accounts' usernames match.
    public synchronized boolean matchesUsername(Account acc) {
        return this.userName.equals(acc.getUserName());
    }

    // sees if the accounts' credentials match.
    public synchronized boolean matchesCredentials(Account acc) {
        return this.userName.equals(acc.getUserName()) && this.password.equals(acc.getPassword()) ;
    }

    // gets the serial code of the account.
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
            // if two accounts' serials are the same, they are equal.
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
