import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.*;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {


    @Test(timeout = 1000)
    public void testFields() {
        Field testingSender;
        Field testingMessage;
        Field testingChat;
        Field testingTime;
        try {
            testingSender = Message.class.getDeclaredField("sender");
            if (!Modifier.isPrivate(testingSender.getModifiers())) {
                Assert.fail();
            } else if (!testingSender.getType().equals(Account.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The sender field does not exist!");
            Assert.fail();
        }
        try {
            testingMessage = Message.class.getDeclaredField("message");
            if (!Modifier.isPrivate(testingMessage.getModifiers())) {
                Assert.fail();
            } else if (!testingMessage.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The message field does not exist");
            Assert.fail();
        }
        try {
            testingChat = Message.class.getDeclaredField("chat");
            if (!Modifier.isPrivate(testingChat.getModifiers())) {
                Assert.fail();
            } else if (!testingChat.getType().equals(Chat.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The chat field does not exist");
            Assert.fail();
        }
        try {
            testingTime = Message.class.getDeclaredField("time");
            if (!Modifier.isPrivate(testingTime.getModifiers())) {
                Assert.fail();
            } else if (!testingTime.getType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The time field does not exist");
            Assert.fail();
        }
    }
    @Test
    public void testGetSender() {
        Method testMethod;
        try {
            testMethod = Message.class.getMethod("getSender");
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(Account.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getSender method does not exist!");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Object o = new Object();
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals(acc, test.getSender());
    }

    @Test
    public void testGetMessage() {
        Method testMethod;
        try {
            testMethod = Message.class.getMethod("getMessage");
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(String.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getMessage method does not exist");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals("hi", test.getMessage());
    }

    @Test
    public void testEditMessage() {
        Method testMethod;
        try {
            Class[] cArgs = new Class[1];
            cArgs[0] = String.class;
            testMethod = Message.class.getMethod("editMessage", cArgs);
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(Void.TYPE)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The editMessage method does not exist!");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        test.editMessage("bye");
        assertEquals("bye", test.getMessage());
    }

    @Test
    public void testGetChat() {
        Method testMethod;
        try {
            testMethod = Message.class.getMethod("getChat");
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(Chat.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The getChat method does not exist!");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals(chat, test.getChat());
    }

    @Test
    public void testGetTime() {
        Method testMethod;
        try {
            testMethod = Message.class.getMethod("getTime");
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
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertTrue(test.getTime().matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.[0-9]+$"));
    }

    @Test
    public void testEquals1() {
        Method testMethod;
        Class[] classes = new Class[1];
        classes[0] = Object.class;
        try {
            testMethod = Message.class.getMethod("equals", classes);
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("The equals method does not exist!");
            Assert.fail();
        }

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
        Method testMethod;
        try {
            testMethod = Message.class.getMethod("toString");
            if (!Modifier.isPublic(testMethod.getModifiers())) {
                Assert.fail();
            } else if (!testMethod.getReturnType().equals(String.class)) {
                Assert.fail();
            }

        } catch (NoSuchMethodException e) {
            System.out.println("The toString method does not exist!");
            Assert.fail();
        }
        Account acc = new Account("test", "1234");
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "", chat);


        assertEquals("", test.toString());//asserts that test returns a string type
        test.editMessage("hi");
        assertEquals("hi", test.toString());//asserts that test returns the correct string


    }
}