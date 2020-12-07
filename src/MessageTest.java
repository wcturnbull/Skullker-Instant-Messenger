import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

import java.lang.reflect.*;
/**
 * A test class for Message
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Neihl Wang
 * @version 7 December 2020
 */


public class MessageTest {

    @Test(timeout = 1000)
    public void testClass() {
        Class<?> clazz = Message.class;
        Class<?> superclasses;
        superclasses = clazz.getSuperclass();
        if (!superclasses.equals(Object.class)) {
            Assert.fail();
        }
        try {
            Class.forName("Message");
        } catch (ClassNotFoundException e) {
            System.out.println("Ensure that the Message class exists");
            Assert.fail();
        }
    }
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
    @Test (timeout = 1000)
    public void testConstructor() {
        Constructor<Message> constructor;
        try {
            Class[] classes = new Class[3];
            classes[0] = Account.class;
            classes[1] = String.class;
            classes[2] = Chat.class;
            constructor = Message.class.getConstructor(classes);
            if (Modifier.isPrivate(constructor.getModifiers())) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Ensure that the constructor for the message class exists and has proper parameters");
            Assert.fail();
        }
        Account acc = new Account("asdf", "1234");
        Chat chat = new Chat(acc, "Testing");
        Message test = new Message(acc, "hello", chat);
        assertEquals(acc, test.getSender());
        assertEquals(chat, test.getChat());
        assertEquals("hello", test.getMessage());
    }
    @Test
    public void testGetSender() {
        Account acc = new Account("test", "1234");
        Object o = new Object();
        Chat chat = new Chat(acc, "Chat");
        Message test = new Message(acc, "hi", chat);
        assertEquals(acc, test.getSender());

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
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
