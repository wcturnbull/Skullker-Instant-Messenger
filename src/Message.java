import java.io.Serializable;

public class Message implements Serializable {
    private Account sender;
    private String message;
    private Chat chat;
    private String time;

    public Message(Account sender, String message, Chat chat, String time) {
        this.sender = sender;
        this.message = message;
        this.chat = chat;
        this.time = time;
    }

    public Message(String message) {
        this.message = message;
    }

    public synchronized Account getSender() {
        return sender;
    }

    public synchronized String getMessage() {
        return message;
    }

    public synchronized void editMessage(String message) {
        this.message = message;
    }

    public synchronized Chat getChat() {
        return chat;
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
        if (o instanceof Message) {
            Message message = (Message) o;
            return (this.message.equals(message.getMessage()) &&
                    this.sender.equals(message.getSender()) &&
                    this.chat.equals(message.getChat()) &&
                    this.time.equals(message.getTime()));
        }
        return false;
    }

    public String toString() {
        return String.format(message);
    }
}
