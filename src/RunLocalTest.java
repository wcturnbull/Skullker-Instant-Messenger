import org.junit.Assert;
import org.junit.Test;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.lang.reflect.*;
public class RunLocalTest {

    @Test (timeout = 1000)
    public void testServerFields() {
        Field chats;
        Field users;
        Field RUN_SYNC;
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
            RUN_SYNC = Server.class.getDeclaredField("RUN_SYNC");
            if (Modifier.isPublic(RUN_SYNC.getModifiers())) {
                Assert.fail();
            } else if (!RUN_SYNC.getType().equals(Object.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The field RUN_SYNC is missing");
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
    public void testClientFields() {
        Field account;
        Field socket;
        Field oos;
        Field ois;
        Field welcome;
        Field app;
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
            socket = Client.class.getDeclaredField("socket");
            if (Modifier.isPublic(socket.getModifiers())) {
                Assert.fail();
            } else if (!socket.getType().equals(Socket.class)) {
                Assert.fail();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("The socket field is missing");
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
}
