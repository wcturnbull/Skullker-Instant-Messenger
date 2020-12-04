import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testGetSender() {
        Account acc = new Account("test", "1234");
        Object o = new Object();
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals(acc, test.getSender());
    }

    @Test
    public void testGetMessage() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals("hi", test.getMessage());
    }

    @Test
    public void testEditMessage() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        test.editMessage("bye");
        assertEquals("bye", test.getMessage());
    }

    @Test
    public void testGetChat() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals(chat, test.getChat());
    }

    @Test
    public void testGetTime() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        System.out.println(test.getTime());
        assertTrue(test.getTime().matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.[0-9]+$"));
    }

    @Test
    public void testEquals1() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);

        Message equal = test;
        assertEquals(true, test.equals(equal)); //asserts that two references to the same object are equal
        Message test1 = new Message(acc, "hi", chat);
        assertEquals(false, test.equals(test1)); //asserts that two messages with same chat and account
                                                        // but different initialization times are not equal
        assertFalse(test.equals(acc)); //asserts that a message will return true when comparing to
                                                        // a different object entirely
    }

    @Test
    public void testToString1() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "", chat);

        assertEquals("", test.toString());//asserts that test returns a string type
        test.editMessage("hi");
        assertEquals("hi", test.toString());//asserts that test returns the correct string
    }
}