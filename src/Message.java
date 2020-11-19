public class Message {
    private Account sender;
    private String message;

    public Message(Account sender, String message) {
        this.sender = sender;
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
