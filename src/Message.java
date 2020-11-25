import java.io.Serializable;

public class Message implements Serializable {
    private Account sender;
    private String message;
    private Chat chat;

    public Message(Account sender, String message, Chat chat) {
        this.sender = sender;
        this.message = message;
        this.chat = chat;
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
                    this.chat.equals(message.getChat()));
        }
        return false;
    }
}
