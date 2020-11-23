import java.io.Serializable;

public class Message implements Serializable {
    private Account sender;
    private String message;

    public Message(Account sender, String message) {
        this.sender = sender;
        this.message = message;
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
}
