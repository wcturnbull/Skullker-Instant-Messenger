import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ChatTest {

    @Test
    public void sendMessage() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.sendMessage(mess);
        assertTrue(chat.getMessages().contains(mess));

    }

    @Test
    public void getMessages() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.sendMessage(mess);
        Vector<Message> vec = new Vector<Message>();
        vec.add(mess);
        assertEquals(vec, chat.getMessages());
    }

    @Test
    public void addUser() {
        Account acc = new Account("test", "1234");
        Account added = new Account("test1", "asdf");
        Chat chat = new Chat(acc, "Chatroom1");
        chat.addUser(added);
        Vector<Account> vec = new Vector<>();
        vec.add(acc);
        vec.add(added);
        assertEquals(vec, chat.getUsers());
    }

    @Test
    public void removeUser() {
        Account acc = new Account("test", "1234");
        Account removed = new Account("test1", "asdf");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.addUser(removed);
        chat.removeUser(removed);
        Vector<Account> vec = new Vector<>();
        vec.add(acc);
        assertEquals(vec, chat.getUsers());

    }

    @Test
    public void deleteUser() {
        Account acc = new Account("test", "1234");
        Account removed = new Account("test1", "asdf");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.addUser(removed);
        chat.removeUser(removed);
        Vector<Account> vec = new Vector<>();
        vec.add(acc);
        assertEquals(vec, chat.getUsers()); //Same as the test for removeUser, the difference in functionality will be
                                            // tested in AccountTest
    }

    @Test
    public void removeMessage() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.sendMessage(mess);
        chat.removeMessage(mess);
        assertEquals(0, chat.getMessages().size());//asserts that removeMessage works with a found message
        chat.sendMessage(mess);
        chat.removeMessage(new Message(acc, "bye", chat));
        assertEquals(1, chat.getMessages().size()); //asserts that removemessage works when message is not found
    }

    @Test
    public void getUsers() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Account u1 = new Account("user1", "asdf");
        Account u2 = new Account("user2", "asdf");
        chat.addUser(u1);
        chat.addUser(u2);
        Vector<Account> vec = new Vector<>();
        vec.add(acc);
        vec.add(u1);
        vec.add(u2);
        assertEquals(vec, chat.getUsers());
    }

    @Test
    public void getName() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        assertEquals("Chatroom1", chat.getName());
    }

    @Test
    public void setName() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        chat.setName("Chatroom2");
        assertEquals("Chatroom2", chat.getName());
    }

    @Test
    public void fetchMessage() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Message m1 = new Message(acc, "1", chat);
        Message m2 = new Message(acc, "2", chat);
        Message m3 = new Message(acc, "3", chat);
        chat.sendMessage(m1);
        chat.sendMessage(m2);
        chat.sendMessage(m3);
        assertEquals(m2, chat.fetchMessage(m2)); //checks the case where message is successfully found
        assertNull(chat.fetchMessage(new Message(acc,"4", chat))); //checks case where message DNE
    }

    @Test
    public void testEquals() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Chat bad = new Chat(acc, "Chatrooom1");
        Chat ref = chat;
        assertTrue(chat.equals(ref));
        assertNotEquals(chat, bad);
    }

    @Test
    public void testToString() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom");
        assertEquals("Chatroom test 1234  \n()", chat.toString());

    }
}