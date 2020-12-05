import org.junit.Assert;
import org.junit.Test;
import java.time.*;
import static org.junit.Assert.*;
import java.util.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
public class AccountTest {

    @Test (timeout = 1000)
    public void testFields() {
        Field userName;
        Field password;
        Field serial;
        Field chats;
        try {
            userName = Account.class.getDeclaredField("userName");
            if (Modifier.isPublic(userName.getModifiers())) {
                Assert.fail();
            } else if (!userName.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The userName field is missing");
            Assert.fail();
        }
        try {
            password = Account.class.getDeclaredField("password");
            if (Modifier.isPublic(password.getModifiers())) {
                Assert.fail();
            } else if (!password.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The password field is missing");
        }
        try {
            serial = Account.class.getDeclaredField("serial");
            if (Modifier.isPublic(serial.getModifiers())) {
                Assert.fail();
            } else if (!serial.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field serial is missing");
            Assert.fail();
        }
        try {
            chats = Account.class.getDeclaredField("chats");
            if (Modifier.isPublic(chats.getModifiers())) {
                Assert.fail();
            } else if (!chats.getType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field chats is missing");
            Assert.fail();
        }
    }
    @Test
    public void getUserName() {
        Method method;
        try {
            method = Account.class.getMethod("getUserName");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getUserName method does not exist");
        }
        Account acc = new Account("test", "1234");
        assertEquals("test", acc.getUserName());
    }

    @Test
    public void setUserName() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = String.class;
            method = Account.class.getMethod("setUserName", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The setUserName method does not exist");
        }
        Account acc = new Account("test", "1234");
        acc.setUserName("test2");
        assertEquals("test2",acc.getUserName());
    }

    @Test
    public void getPassword() {
        Method method;
        try {

            method = Account.class.getMethod("getPassword");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getPassword method does not exist");
        }
        Account acc = new Account("test", "1234");
        assertEquals("1234",  acc.getPassword());
    }

    @Test
    public void setPassword() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = String.class;
            method = Account.class.getMethod("setPassword", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The setPassword method does not exist");
        }
        Account acc = new Account("test", "1234");
        acc.setPassword("12345");
        assertEquals("12345", acc.getPassword());
    }

    @Test
    public void copy() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Account.class;
            method = Account.class.getMethod("copy", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The copy method does not exist");
        }
        Account acc = new Account("test", "1234");
        Account copy = new Account("test1", "12345");
        assertNotEquals(acc, copy);
        copy.copy(acc);
        assertEquals(acc.getUserName(), copy.getUserName());
        assertEquals(acc.getPassword(), copy.getPassword());
    }

    @Test
    public void joinChat() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Chat.class;
            method = Account.class.getMethod("joinChat", Chat.class);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The joinChat method does not exist");
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        Vector<Chat> vec = new Vector<>();
        vec.add(chat);
        assertEquals(vec, acc.getChats());
    }

    @Test
    public void leaveChat() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Chat.class;
            method = Account.class.getMethod("leaveChat", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The leaveChat method does not exist");
        }
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
        Method method;
        try {
            method = Account.class.getMethod("getChats");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getChats method does not exist");
        }
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
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Chat.class;
            method = Account.class.getMethod("fetchChat", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Chat.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The fetchChat method does not exist");
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        assertEquals(chat, acc.fetchChat(chat));
        assertNull(acc.fetchChat(new Chat(acc, "asdf")));


    }

    @Test
    public void delete() {
        Method method;
        try {
            method = Account.class.getMethod("delete");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The delete method does not exist");
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Testing");
        acc.joinChat(chat);
        acc.delete();
        assertEquals("test (DELETED)", acc.getUserName());
        assertEquals(0, chat.getUsers().size());
    }

    @Test
    public void matchesUsername() {
        Method method;
        try {
            method = Account.class.getMethod("matchesUsername", Account.class);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The matchesUsername method does not exist");
        }
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "12345");
        Account fake = new Account("tEst","1234");
        assertTrue(acc.matchesUsername(account));
        assertFalse(acc.matchesUsername(fake));


    }

    @Test
    public void matchesCredentials() {
        Method method;
        try {
            method = Account.class.getMethod("matchesCredentials", Account.class);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The matchesCredentials method does not exist");
        }
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "12345");
        Account fake = new Account("test","1234");
        assertEquals(false, acc.matchesCredentials(account));
        assertTrue(acc.matchesCredentials(fake));
    }

    @Test
    public void testEquals() {
        Method method;
        try {
            method = Account.class.getMethod("equals", Object.class);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The equals method does not exist");
        }
        Account acc = new Account("test", "1234");
        Account account = new Account("test", "1234");
        Account fake = acc;
        assertNotEquals(acc, account);
        assertEquals(acc, fake);
    }

    @Test
    public void testToString() {
        Method method;
        try {
            method = Account.class.getMethod("toString");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The toString method does not exist");
        }
        Account acc = new Account("test", "1234");
        assertEquals("test 1234 ", acc.toString());
    }
}