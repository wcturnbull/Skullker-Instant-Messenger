import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.lang.reflect.*;

/**
 * Test for Server and Client
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Evan Wang CS18000
 * @version 7 December 2020
 */
public class ServerAndClientTest {

    @Test (timeout = 1000)
    public void testServerFields() {    // tests Server instance fields
        Field chats;
        Field users;
        Field runSync;
        Field run;
        try {
            chats = Server.class.getDeclaredField("chats");
            if (Modifier.isPublic(chats.getModifiers())) {
                Assert.fail();
            } else if (!chats.getType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The chats field is missing");
            Assert.fail();
        }
        try {
            users = Server.class.getDeclaredField("users");
            if (Modifier.isPublic(users.getModifiers())) {
                Assert.fail();
            } else if (!users.getType().equals(Vector.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The users field is missing");
        }
        try {
            runSync = Server.class.getDeclaredField("runSync");
            if (Modifier.isPublic(runSync.getModifiers())) {
                Assert.fail();
            } else if (!runSync.getType().equals(Object.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field runSync is missing");
            Assert.fail();
        }
        try {
            run = Server.class.getDeclaredField("run");
            if (Modifier.isPublic(run.getModifiers())) {
                Assert.fail();
            } else if (!run.getType().equals(boolean.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field run is missing");
            Assert.fail();
        }
    }

    @Test
    public void testClientFields() {    // tests Client instance fields
        Field account;
        Field oos;
        Field ois;
        Field welcome;
        Field app;
        Field skullkerLogo;
        Field skullkerLogoIcon;
        try {
            account = Client.class.getDeclaredField("account");
            if (Modifier.isPublic(account.getModifiers())) {
                Assert.fail();
            } else if (!account.getType().equals(Account.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The account field is missing");
            Assert.fail();
        }
        try {
            skullkerLogo = Client.class.getDeclaredField("skullkerLogo");
            if (Modifier.isPublic(skullkerLogo.getModifiers())) {
                Assert.fail();
            } else if (!skullkerLogo.getType().equals(Image.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The skullkerLogo field is missing");
        }
        try {
            skullkerLogoIcon = Client.class.getDeclaredField("skullkerLogoIcon");
            if (Modifier.isPublic(skullkerLogoIcon.getModifiers())) {
                Assert.fail();
            } else if (!skullkerLogoIcon.getType().equals(Image.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The skullkerLogoIcon field is missing");
        }
        try {
            oos = Client.class.getDeclaredField("oos");
            if (Modifier.isPublic(oos.getModifiers())) {
                Assert.fail();
            } else if (!oos.getType().equals(ObjectOutputStream.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field oos is missing");
            Assert.fail();
        }
        try {
            ois = Client.class.getDeclaredField("ois");
            if (Modifier.isPublic(ois.getModifiers())) {
                Assert.fail();
            } else if (!ois.getType().equals(ObjectInputStream.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field ois is missing");
            Assert.fail();
        }
        try {
            welcome = Client.class.getDeclaredField("welcome");
            if (Modifier.isPublic(welcome.getModifiers())) {
                Assert.fail();
            } else if (!welcome.getType().equals(Client.WelcomeGUI.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The welcome field is missing");
            Assert.fail();
        }
        try {
            app = Client.class.getDeclaredField("app");
            if (Modifier.isPublic(app.getModifiers())) {
                Assert.fail();
            } else if (!app.getType().equals(Client.AppGUI.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The app field is missing");
            Assert.fail();
        }
    }

    @Test(timeout = 1000)
    public void testServerClass() {     // tests Server inherits from correct superclass
        Class<?> clazz = Server.class;
        Class<?> superclasses;
        superclasses = clazz.getSuperclass();
        if (!superclasses.equals(Object.class)) {
            Assert.fail();
        }
        try {
            Class.forName("Server");
        } catch (ClassNotFoundException e) {
            System.out.println("Ensure that the Server class exists");
            Assert.fail();
        }
    }

    @Test(timeout = 1000)
    public void testClientClass() {     // tests Client inherits from correct superclass
        Class<?> clazz = Client.class;
        Class<?> superclasses;
        superclasses = clazz.getSuperclass();
        if (!superclasses.equals(Object.class)) {
            Assert.fail();
        }
        try {
            Class.forName("Client");
        } catch (ClassNotFoundException e) {
            System.out.println("Ensure that the Client class exists");
            Assert.fail();
        }
    }

    @Test (timeout = 1000)
    public void testServerConstructor() {       // tests Server constructor
        Constructor<Server> constructor;
        try {
            Class[] classes = new Class[0];
            constructor = Server.class.getConstructor(classes);
            if (Modifier.isPrivate(constructor.getModifiers())) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Ensure that the constructor for the Server class exists and has proper parameters");
            Assert.fail();
        }
    }

    @Test (timeout = 1000)
    public void testClientConstructor() {       // tests Client constructor
        Constructor<Client> constructor;
        try {
            Class[] classes = new Class[0];
            constructor = Client.class.getConstructor(classes);
            if (Modifier.isPrivate(constructor.getModifiers())) {
                Assert.fail();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("Ensure that the constructor for the Server class exists and has proper parameters");
            Assert.fail();
        }
    }
}
