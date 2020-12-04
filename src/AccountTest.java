import org.junit.Test;
import java.time.*;
import static org.junit.Assert.*;
import java.util.*;
public class AccountTest {

    @Test
    public void getUserName() {
        Account acc = new Account("test", "1234");
        assertEquals("test", acc.getUserName());
    }

    @Test
    public void setUserName() {
        Account acc = new Account("test", "1234");
        acc.setUserName("test2");
        assertEquals("test2",acc.getUserName());
    }

    @Test
    public void getPassword() {
        Account acc = new Account("test", "1234");
        assertEquals("1234",  acc.getPassword());
    }

    @Test
    public void setPassword() {
        Account acc = new Account("test", "1234");
        acc.setPassword("12345");
        assertEquals("12345", acc.getPassword());
    }

    @Test
    public void copy() {
        Account acc = new Account("test", "1234");
        Account copy = new Account("test1", "12345");
        assertNotEquals(acc, copy);
        copy.copy(acc);
        assertEquals(acc.getUserName(), copy.getUserName());
        assertEquals(acc.getPassword(), copy.getPassword());
    }

    @Test
    public void joinChat() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        Vector<Chat> vec = new Vector<>();
        vec.add(chat);
        assertEquals(vec, acc.getChats());
    }

    @Test
    public void leaveChat() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        Vector<Chat> vec = new Vector<>();
        acc.leaveChat(chat);
        assertFalse(acc.getChats().contains(chat));
        assertFalse(acc.getChats().contains(new Chat(acc, "asdfasdf")));

    }

    @Test
    public void getChats() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        Chat chat1 = new Chat(acc, "Testing1");
        Chat chat2 = new Chat(acc, "Testing2");
        acc.joinChat(chat);
        acc.joinChat(chat1);
        acc.joinChat(chat2);
        Vector<Chat> vec = new Vector<>();
        vec.add(chat);
        vec.add(chat1);
        vec.add(chat2);
        assertEquals(vec, acc.getChats());

    }

    @Test
    public void fetchChat() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        assertEquals(chat, acc.fetchChat(chat));
        assertNull(acc.fetchChat(new Chat(acc, "asdf")));


    }

    @Test
    public void delete() {
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        acc.delete();
        assertEquals("test (DELETED)", acc.getUserName());
        assertEquals(0, chat.getUsers().size());
    }

    @Test
    public void matchesUsername() {
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "12345");
        Account fake = new Account("tEst","1234");
        assertTrue(acc.matchesUsername(account));
        assertFalse(acc.matchesUsername(fake));


    }

    @Test
    public void matchesCredentials() {
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "12345");
        Account fake = new Account("test","1234");
        assertEquals(false, acc.matchesCredentials(account));
        assertTrue(acc.matchesCredentials(fake));
    }

    @Test
    public void testEquals() {
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "1234");
        Account fake = acc;
        assertNotEquals(acc, account);
        assertEquals(acc, fake);
    }

    @Test
    public void testToString() {
        Account acc = new Account("test", "1234");
        assertEquals("test 1234 ", acc.toString());
    }
}