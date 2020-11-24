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

    public Account getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void editMessage(String message) {
        this.message = message;
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
                    this.sender.equals(message.getSender()));
        }
        return false;
    }
}
