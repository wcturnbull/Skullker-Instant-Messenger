import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a message sent by a user.
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Evan Wang CS18000, 001
 * @version 7 December 2020
 */
public class Message implements Serializable {
    private Account sender;         // author of the message
    private String text;            // string content of the message
    private Chat chat;              // the chat the message is in
    private String time;            // time of the message's construction

    public Message(Account sender, String text, Chat chat) {
        this.sender = sender;
        this.text = text;
        this.chat = chat;
        this.time = LocalDateTime.now().toString();
    }

    // gets the author of the message.
    public synchronized Account getSender() {
        return sender;
    }

    // gets the string content of the message.
    public synchronized String getMessage() {
        return text;
    }

    // edits the string content of the message.
    public synchronized void editMessage(String text) {
        this.text = text;
    }

    // gets the chat the message is in.
    public synchronized Chat getChat() {
        return chat;
    }

    // gets the message's time of construction.
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
        if (o instanceof Message) {
            // if the string content of the message, the author, the author's username,
            // the chat, and time of construction of the message are the same, they are equal
            Message message = (Message) o;
            return (this.text.equals(message.getMessage()) &&
                    this.sender.equals(message.getSender()) &&
                    this.sender.matchesUsername(message.getSender()) &&
                    this.chat.equals(message.getChat()) &&
                    this.time.equals(message.getTime()));
        }
        return false;
    }

    public String toString() {
        return text;
    }
}
