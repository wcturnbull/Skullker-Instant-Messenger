import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class ChatTest {

    @Test(timeout = 1000)
    public void testClass() {
        Class<?> clazz = Chat.class;
        Class<?> superclasses;
        superclasses = clazz.getSuperclass();
        if (!superclasses.equals(Object.class)) {
            Assert.fail();
        }
        try {
            Class.forName("Chat");
        } catch (ClassNotFoundException e) {
            System.out.println("Ensure that the Chat class exists");
            Assert.fail();
        }
    }
    @Test (timeout = 1000)
    public void testFields() {
        Field users;
        Field messages;
        Field name;
        try {
            users = Chat.class.getDeclaredField("users");
            if (Modifier.isPublic(users.getModifiers())) {
                Assert.fail();
            } else if (!users.getType().equals(Vector.class)) {
                Assert.fail();
            }

        } catch (NoSuchFieldException e) {
            System.out.println("The users field is missing");
            Assert.fail();
        }
        try {
            messages = Chat.class.getDeclaredField("messages");
            if (Modifier.isPublic(messages.getModifiers())) {
                Assert.fail();
            } else if (!messages.getType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field messages is missing");
            Assert.fail();
        }
        try {
            name = Chat.class.getDeclaredField("name");
            if (Modifier.isPublic(name.getModifiers())) {
                Assert.fail();
            } else if (!name.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The name field is missing");
        }
    }
    @Test (timeout = 1000)
    public void testConstructor() {
        Constructor<Chat> constructor;
        try {
            Class[] classes = new Class[2];
            classes[0] = Account.class;
            classes[1] = String.class;
            constructor = Chat.class.getConstructor(classes);
            if (Modifier.isPrivate(constructor.getModifiers())) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Ensure that the constructor exists and has the correct parameters");
            Assert.fail();
        }
        Account acc = new Account("testing", "1234");
        Chat chat = new Chat(acc, "TestChat");
        assertEquals("1234", chat.getName());
        assertTrue(chat.getUsers().contains(acc));
    }
    @Test
    public void sendMessage() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Message.class;
            method = Chat.class.getMethod("sendMessage", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The sendMessage method is missing");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Message mess = new Message(acc, "hi", chat);
        chat.sendMessage(mess);
        assertTrue(chat.getMessages().contains(mess));
    }

    @Test
    public void getMessages() {
        Method method;
        try {
            method = Chat.class.getMethod("getMessages");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getMessages method is missing");
            Assert.fail();
        }
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
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Account.class;
            method = Chat.class.getMethod("addUser", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The addUser method is missing");
        }
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
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Account.class;
            method = Chat.class.getMethod("removeUser", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The removeUser method is missing");
            Assert.fail();
        }
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
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Account.class;
            method = Chat.class.getMethod("deleteUser", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The deleteUser method does not exist");
            Assert.fail();
        }
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
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Message.class;
            method = Chat.class.getMethod("removeMessage", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The removeMessage method does not exist");
            Assert.fail();
        }
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
        Method method;
        try {
            method = Chat.class.getMethod("getUsers");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getUsers method does not exist");
            Assert.fail();
        }
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
        Method method;
        try {
            method = Chat.class.getMethod("getName");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getName method does not exist");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        assertEquals("Chatroom1", chat.getName());
    }

    @Test
    public void fetchMessage() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Message.class;
            method = Chat.class.getMethod("fetchMessage", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(Message.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The removeMessage method does not exist");
            Assert.fail();
        }
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
    public void testGetTime() {
        Method testMethod;
        try {
            testMethod = Chat.class.getMethod("getTime");
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getTime method does not exist");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat test = new Chat(acc, "Chat");
        assertTrue(test.getTime().matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.[0-9]+$"));
    }

    @Test
    public void testEquals() {
        Method method;
        try {
            Class[] classes = new Class[1];
            classes[0] = Object.class;
            method = Chat.class.getMethod("equals", classes);
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The equals method does not exist");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom1");
        Chat bad = new Chat(acc, "Chatrooom1");
        Chat ref = chat;
        assertTrue(chat.equals(ref));
        assertNotEquals(chat, bad);
    }

    @Test
    public void testToString() {
        Method method;
        try {
            method = Chat.class.getMethod("toString");
            if (Modifier.isPrivate(method.getModifiers())) {
                Assert.fail();
            } else if (!method.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The toString method does not exist");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chatroom");
        assertEquals("Chatroom test 1234  \n()", chat.toString());
    }
}