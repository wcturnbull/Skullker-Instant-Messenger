# Skullker: a social network chat application
by Wes Turnbull, Evan Wang, & Neihl Wang

### How to run Skullker
First, run one instance of `Server.java`. From there, you can run as many simultaneous, seperate instances of `Client.java` as you want! To close the server, force-terminate it (this will not result in data being written to a file) or simply enter anything into the terminal running your server (this will save data to a file). Lastly, it does not matter whether the logo images are in the directory or directly outside the directory of Client.java; Client functions either way. Anywhere else, however, does not work (to our knowledge). However, data will only be read by the server if the `data.txt` file is in the same directory as Server.java.

### Known Bugs/Issues/Notes
For the benefit of the graders, here is a small list of known issues or things that might seem like issues.
- Deleting the owner `Account` of a `Chat` causes all members currently viewing that `Chat` to close their viewing of that `Chat`. This is not unintended behaviour, but is something that I thought graders should know.
- Clients on the welcome screen GUI will only get an error message in the event that the server has closed after their attempt to send a log-in/registration query to the server. This is not unintended, but is important to note.
- Signing out was the last optional feature added to our project; as such, it has some bugs with it that we have not fully seen/solved. For instance, signing out and logging back in to the main application GUI temporarily desyncs the client from the server, which only leads to issues if the server is subsequently safely closed, resulting in the "Server Closed" error message not appearing at the right time for the client, and in the server not terminating until the desynced client disconnects.

In all other cases, `Server` and `Client` should and appear to function exactly as intended.

## How our application fills Project 5 Requirements
Like our title suggests, we made our project for the third option of Project 5, the social network chat application. Because ours is a 3 person team, we were only required to finish 1/3 of the additional selection requirements for option 3; instead, we did all 3/3 selection requirements, in addition to some extraneous features.

### Core Requirements
- [x] **Simultaneous use of Skullker by multiple users is supported.** Due to the multi-threaded structure of our server, multiple users can communicate with the server simultaneously.
- [x] **All user interactions are through graphical user interfaces.** All client-side usage of Skullker is through GUIs rather than through a simple text-based interface.
- [x] **Data is persistent between client sessions.** Because all data is updated serverside and the server runs independently of client-side connections, the server maintains data between client sessions. Data will always be maintained so long as the server is running.
- [x] **Our application client is updated in real-time.** On client-side, we have a `java.swing.Timer` which, every .1 seconds, automatically sends a query to the server to return updated information. The client GUI then updates its various panels and text fields accordingly.
- [x] **The client (to our knowledge) never crashes.** All possible user-related errors (such as pressing a button before it is functional) are handled either through the use of descriptive `JOptionPanes`, or by simply not allowing the user to interact with said GUI elements. 
- [x] **Users can create, edit, and delete their accounts.** Our client GUI opens with a welcome screen in which the user can either register for an account with a unique username or log into a pre-existing account. In the main client GUI, the user has the option to open a "Settings" window, in which they can edit their user credentials or delete their account entirely.
- [x] **Users are required to maintain unique usernames.** After an account is created with a given username, no other user can create an account with that same username or edit their username to become that username. That username becomes available again upon its corresponding account's deletion. 
- [x] **Users can send messages to groups or individuals.** Users can create conversations which include any number of other users (including 0). Members of those chats can then send messages to those conversations.

### Selection Requirements
- [x] **Users can create conversations with groups and individuals.** As aforementioned, yes, the user can do this.
- [x] **Users can see a list of their conversations.** On the right side of our main client GUI, the user can see a panel containing a list of chats of which they are a member.
- [x] **Users can delete chats.** From the aforementioned chat panel, users can select conversations to either open or to leave (our interpretation of "deleting" a chat).

### Extra Features
- Our project allows the person running the server the ability to either forcefully or safely terminate the Server process. If they do the latter, the Server will write all its current data to a local text file. This file is then read upon the Server's next construction. By doing this, we allow data to persist between server sessions. Though multiple TAs said that it would be good to make data persist between server sessions, it was not mentioned on the handout, so we consider it to be an extra feature.
- Users can sign out of their account without closing their application.
- Deleted users' messages remain present in conversations, albeit with "(DELETED)" appended to the end of the message's sender's name. This allows the message history to remain readable, but makes it apparent when a conversation's member has been deleted.
- Our project also has a custom logo (courtesy of Evan Wang) that is used in our welcome screen and as an application icon. It's a small detail, but it makes our project special.

# Classes
Our project contains 5 classes: `Message`, `Chat`, `Account`, `Server`, and `Client`, ignoring our test case files. It is important to note that our project actually contains more than 6 classes, as `Server` and `Client` contain multiple inner classes each (`ServerThread`, `ClientThread`, `ExecutionerThread`, `WelcomeGUI`, and `AppGUI`). The reason we chose to use inner classes rather than seperating them into seperate .java files was because each inner class had very little functionality outside of their outer classes, and because each inner class required frequent access to their outer classes' instance variables. Our classes basically universally use `java.util.Vector`s rather than `ArrayList` due to `Vector`'s accessor and mutator methods being inherently synchronized.

## The ADTs
These classes are foundational to our application's functionality, and primarily serve to structure and organize data.

### Message
This ADT represents the `Message`s sent by members of `Chat`s. 
| Instance field | Description |
|-|-|
| `private Account sender` | The author of the `Message` |
| `private String text` | The `String` content of the `Message` |
| `private Chat chat` | The `Chat` the `Message` is in |
| `private String time` | The time at which this `Message` was recieved server-side |

| Method | Description |
|-|-|
| `public Message(Account sender, String text, Chat chat)` | Constructor for the `Message`; instead of time being supplied as an argument, it is measured at time of constructor |
| `public synchronized Account getSender()` | Accessor for the author of the `Message` |
| `public synchronized String getMessage()` | Accessor for the `String` text of the `Message` |
| `public synchronized void editMessage(String text)` | Sets the `text` of the `Message` to the `String` provided |
| `public Chat getChat()` | Accessor for the `Message`'s `Chat` of origin |
| `public String getTime()` | Accessor for the `Message`'s `time` of construction |
| `public synchronized boolean equals(Object o)` | Tests the `Message`'s equality with the provided `Object` |
| `public synchronized String toString()` | Accessor for a `String` representation of the `Message` |

### Chat
This ADT represents the `Chat`s that different `Account`s can join and send `Messages` in. 
| Instance field | Description |
|-|-|
| `private Vector<Account> users` | The members of the `Chat` |
| `private Vector<Message> messages` | The `Message`s sent to this `Chat` |
| `private final String name` | The name of the `Chat` |
| `private final String time` | The time at which the `Chat` was created at server-side |
  
| Method | Description |
|-|-|
| `public Chat(Account owner, String name)` | Constructor for the chat; the owner is added to the list of users, the list of `Message`s is initialized as empty, and `time` is measured at time of construction |
| `public synchronized void sendMessage(Message message)` | Sends `Message` to the `Chat` |
| `public synchronized Vector<Message> getMessages()` | Accessor for the `Message`s in the `Chat` |
| `public synchronized void addUser(Account user)` | Adds a user as a member of the `Chat` |
| `public synchronized void removeUser(Account user)` | Removes the given user as a member of the `Chat` |
| `public synchronized void deleteUser(Account user)` | Similar to removeUser(), but the given user does not remove this `Chat` from their list of `Chat`s |
| `public synchronized void removeMessage(Message message)` | Deletes the given `Message` from the `Chat` |
| `public synchronized Vector<Account> getUsers()` | Accessor for the members of the `Chat` |
| `public synchronized String getName()` | Accessor for the `name` of the `Chat` |
| `public synchronized Message fetchMessage(Message message)` | Fetches an updated reference for an equivalent `Message` in this `Chat` |
| `public String getTime()` | Accessor for the `Chat`'s `time` of construction |
| `public synchronized boolean equals(Object o)` | Tests for `Object` equivalence |
| `public synchronized String toString()` | Accessor for a `String` representation of the `Chat` |

### Account
This ADT represents the `Account`s that users can create.
| Instance field | Description |
|-|-|
| `private String userName` | The `Account`'s username |
| `private String password` | The `Account`'s password |
| `private String serial` | A constant, unique serial code containing the account's `userName`, `password`, and time of construction |
| `private Vector<Chat> chats` | The list of `Chat`s of which the user is a member |

| Method | Description |
|-|-|
| `public Account(String userName, String password)` | Constructor for the `Account`; time is measured at time of construction |
| `public synchronized String getUserName()` | Accessor for `userName` |
| `public synchronized void setUserName(String userName)` | Mutator for the `userName` |
| `public synchronized String getPassword()` | Accessor for the `password` |
| `public synchronized void setPassword(String password)` | Mutator for the `password` |
| `public synchronized void joinChat(Chat chat)` | Adds the user as a member to the given `Chat` |
| `public synchronized void leaveChat(Chat chat)` | Removes the given `Chat` from the `Account`'s list of `chats` |
| `public synchronized Vector<Chat> getChats()` | Accessor for the list of `chats` |
| `public synchronized Chat fetchChat(Chat chat)` | Fetches an updated reference for the given `Chat` |
| `public synchronized void delete()` | Deletes the user from their `chats`, appends `"(DELETED)"` to their `userName`, and sets their `serial` to "" |
| `public synchronized boolean matchesUsername(Account acc)` | Checks whether the `Account`s share a `userName` |
| `public synchronized boolean matchesCredentials(Account acc)` | Checks whether the given `Account` has the same credentials as the `Account` |
| `public synchronized String getSerial()` | Accessor for the `serial` code |
| `public synchronized boolean equals(Object o)` | Checks the `Object` and the `Account` for equality |
| `public synchronized String toString()` | Accessor for a `String` representation of the `Account` |

## Interfaces
Our project only used one interface, `Constants`, which is implemented by both `Server` and `Client`.

### Constants
This interface is a constant interface pattern that defines hexadecimal `byte` values which correspond to different queries the `Client` and the `Server` can send to one another. Appropriately, only `Server` and `Client` implement this interface. The `byte` values follows a sort of lexicon:
- `0x1` is the hexadecimal prefix for queries dealing with `Message`s
- `0x2` is the hexadecimal prefix for queries dealing with `Account`s
- `0x3` is the hexadecimal prefix for queries dealing with `Chat`s
- `0x` is the hexademical prefix for generic queries not about any specific class.

| Fields | Description |
|-|-|
| `byte SEND_MESSAGE = 0x1a` | Constant for a `Client` query to send a `Message` |
| `byte DELETE_MESSAGE = 0x1d` | Constant for a `Client` query to delete a `Message` |
| `byte EDIT_MESSAGE = 0x1e` | Constant for a `Client` query to edit a `Message` |
| `byte REGISTER_ACCOUNT = 0x2a` | Constant for a `Client` query to register for an `Account` |
| `byte LOG_IN = 0x2b` | Constant for a `Client` query to log into an `Account` |
| `byte DELETE_ACCOUNT = 0x2d | Constant for a `Client` query to delete their `Account` |
| `byte EDIT_USERNAME = 0x21` | Constant for a `Client` query to edit their `Account`'s username |
| `byte EDIT_PASSWORD = 0x22` | Constant for a `Client` query to edit their `Account`'s password |
| `byte ADD_USER_TO_CHAT = 0x3a` | Constant for a `Client` query to add a user to a `Chat` |
| `byte CREATE_CHAT = 0x3c` | Constant for a `Client` query to create a `Chat` |
| `byte LEAVE_CHAT = 0x3d` | Constant for a `Client` query to leave (delete) a `Chat` |
| `byte REQUEST_DATA = 0x0` | Constant for a `Client` query for their updated `Account` |
| `byte CONTINUE = 0xc` | Constant for a `Server` positive response to a `Client` query |
| `byte DENIED = 0xd` | Constant for a `Server` negative response to a `Client` query |

## Main Classes
These classes are the ones actually ran by users when they want to use Skullker.

### Server
This class is responsible for preserving user data, accepting new client connections, and handling client queries to the server.
| Instance field | Description |
|-|-|
| `private Vector<Chat> chats` | The list of active `Chat`s (i.e. `Chat`s with more than 0 members) |
| `private Vector<Account> users` | The list of active `User`s |
| `private boolean run` | As long as this `boolean` is true, the server will not stop running |
| `private final Object runSync` | A gatekeeper object for our `run` `boolean` |

| Method | Description |
|-|-|
| `public Server()` | Constructor for the server; if "data.txt" exists, the server will attempt to construct the `chats` and `users` `Vector`s using data from the file; this constructor also starts a `ServerThread` |
| `public void addChat(Chat chat)` | Adds the given `Chat` to `chats` |
| `public void writeMessage(Message message)` | Sends the given `Message` to its appropriate `Chat` |
| `public Chat fetchChat(Chat clientChat)` | Fetches an updated reference to an equivalent `Chat` |
| `public Account fetchAccount(Account acc)` | Fetches an updated reference to an equivalent `Account` |
| `public Account matchUsernames(Account acc)` | Fetches an `Account` that has the same username as the given `Account` |
| `public Account matchCredentials(Account acc)` | Fetches an `Account` that has the same credentials as the given `Account` |
| `public void deleteAccount(Account account)` | Deletes the given account from `users` |
| `public void sanitize()` | Removes non-active `Chat`s from `chats` |
| `public static void main(String[] args)` | Constructs a `Server` object |

### Server.ServerThread
This `Thread` is responsible for accepting new clients' connections.
| Instance field | Description |
|-|-|
| `private ServerSocket serverSocket` | The `Server`'s `ServerSocket` |

| Method | Description |
|-|-|
| `public ServerThread()` | Opens the `ServerSocket` on the port `0xBEEF` (because it's funny) |
| `public void run()` | Starts an `ExecutionerThread` and begins accepting new connections as long as `run` is `true` |

### Server.ClientThread
This `Thread` is responsible for handling client queries to the server and updating data accordingly.
| Instance field | Description |
|-|-|
| `private final Socket socket` | `Socket` corresponding to the `ClientThread`'s client |
| `private ObjectInputStream ois` | Input stream for `socket` |
| `private ObjectOutputStream oos` | Output stream for `socket` |
| `private Account client` | The user's corresponding `Account` |

| Method | Description |
|-|-|
| `public ClientThread(Socket socket)` | Constructs the `ClientThread`, initializes `ois` and `oos` |
| `public void run()` | Takes client queries while `run` is still `true` |

### Server.ServerThread.ExecutionerThread
This `Thread` is responsible for terminating `Server` once the person running it enters something into terminal, and then writes the `Server`'s data to `data.txt`.
| Instance field | Description |
|-|-|
| `private Scanner in` | The `Scanner` for taking input from terminal |

| Method | Description |
|-|-|
| `public void run()` | Blocks until the person running the `Server` enters something into the terminal, and then uses an `ObjectOutputStream` to write to `data.txt` |

### Client
This is the client-side part of the application which communicates with the `Server` and handles user interaction through GUIs.
| Instance field | Description |
|-|-|
| `private Account account` | The user's `Account` |
| `private ObjectOutputStream oos` | `OutputStream` for server-client communications |
| `private ObjectInputStream ois` | `InputStream` for server-client communications |
| `private final WelcomeGUI welcome` | Welcome screen GUI |
| `private final AppGUI app` | Main application GUI which is launched after `WelcomeGUI` is disposed |
| `private Image skullkerLogo` | Our logo |
| `private Image skullkerLogoIcon` | The logo used for the GUIs' icon |

| Method | Description |
|-|-|
| `public Client() throws SocketException` | Constructor for the `Client`, attempts to connect to the server on `localhost` & `0xBEEF`; also constructs a `WelcomeGUI` and `AppGUI` |
| `public WelcomeGUI getWelcomeGUI()` | Accessor for the `Client`'s `WelcomeGUI` |
| `public AppGUI getAppGUI()` | Accessor for the `Client`'s `AppGUI` |
| `public static void main(String[] args)` | Constructs a `Client` object and begins running `Client`'s `WelcomeGUI` on `Swing`'s `invokeLater` `Thread` |

### Client.WelcomeGUI
This is the welcome screen, from which the user can log in or register for a new `Account`. After the user gets their `Account`, `WelcomeGUI` is disposed and then an `AppGUI` is ran on `Swing`'s `invokeLater` `Thread`.
| Instance field | Description |
|-|-|
| `private final JTextField userName` | Input text field for the username on the welcome screen |
| `private final JPasswordField password` | Input password field for the password on the welcome screen |
| `private final JButton signInButton` | Button for logging in |
| `private final JButton signUpButton` | Button for registering |
| `private JFrame registrationFrame` | Frame of the registration window |
| `private JTextField userNameRegisterTextField` | Username text field in the registration window |
| `private JPasswordField passwordRegisterTextField` | Password field in the registration window |
| `private JPasswordField confirmPasswordTextField` | Password confirmation field in the registration window |
| `private JButton registerButton` | Button for registering |
| `private ActionListener actionListener` | Action listener for all buttons for `WelcomeGUI` |

| Method | Description |
|-|-|
| `public void signIn()` | Server-client communication method for signing into an account |
| `public void register()` | Server-client communication method for registering an account |
| `public void createRegistrationWindow()` | Builds the registration window |
| `public WelcomeGUI()` | Sets up the welcome screen's layout |

### Client.AppGUI
This is the Skullker's main application screen, from which the user can do all the functionality outlined in the sections above.
| Instance field | Description |
|-|-|
| `private final JPanel chatSelectorPanel` | Panel that holds all available chats |
| `private final JPanel chatPanel` | Panel that holds the content of a chat |
| `private final JTextField sendMessage` | Input field for entering messages |
| `private final JButton sendButton` | Button for sending a message |
| `private final JButton settingsButton` | Button for opening settings window |
| `private final JButton signOutButton` | Button for signing out (somewhat buggy) |
| `private final JButton createChatPopupButton` | Button which opens the window for creating a chat |
| `private final JLabel chatLabel` | Label which shows a chat's title and members |
| `private final JButton addUsersButton` | Button for adding a user to a chat |
| `private final JPanel chatLabelPanel` | Panel that holds the chat label |
| `private final JScrollBar verticalChatScroller` | Scrollbar for the chat window |
| `private final GridBagConstraints gbcChatPanel` | Grid bag constraints for the chat panel |
| `private final JScrollPane chatSelectorScroller` | Scroll pane for the chat list |
| `private JFrame createChatPopUp` | Window for creating a chat window |
| `private JTextField createChatNameTextField` | Text field for entering a name for a new chat |
| `private JButton createChatButton` | Creates the window for creating a chat |
| `private JFrame userSettingsWindow` | User settings window |
| `private JButton editAccountButton` | Button for editing account credentials |
| `private JButton deleteAccountButton` | Button for deleting user's account |
| `private JButton cancelButton` | Button for canceling out of the settings window |
| `private JFrame editAccountFrame` | Window for editing account credentials |
| `private JTextField editUsernameTextField` | Text field for editing the username |
| `private JTextField editPasswordTextField` | Text field for editing the password |
| `private JFrame addUsersWindow` | Window for adding a user to a chat |
| `private JTextField addUsernameTextField` | Text field for adding the given user to a chat |
| `private JButton addInputtedUserButton` | Button for adding the given user to a chat |
| `private JFrame editMessageFrame` | Window for editing a message |
| `private JTextArea editMessageTextArea` | Text area field for the new message content |
| `private final Timer timer` | Timer for automatic refreshes and queries to the server every .1 seconds |
| `private Chat previousChat` | Reference holding the previous state of the chat |
| `private Chat currentChat` | Reference holding the current state of the chat |
| `private Vector<Chat> previousChats` | Reference holding the user's previous list of chats |
| `private boolean chatOpen` | Boolean stating whether the user has a chat open currently |

| Method | Description |
|-|-|
| `public AppGUI()` | Constructor that sets up all layouts and starts the timer. |
| `public void disposeAllFrames()` | Disposes of all frames |
| `public Timer getTimer()` | Accessor for the reference to the timer |
| `public void createMessageEditor(Message message)` | Creates the message editor window |
| `public void createIndividualChatPanel(Chat chat)` | Creates a panel for the chat list panel including the individual chat's title, a button for opening the chat, and a button for leaving the chat |
| `public void createEditAccountWindow()` | Creates window for the user to edit their account |
| `public void createAddUsersWindow()` | Creates window for the user to add users to a chat |
| `public void createSettingsWindow()` | Creates the settings window |
| `public void createCreateChatPopUp()` | Creates window for creating a chat |
| `public void createChat(String chatName)` | Performs server-client communication for actually creating a chat |
| `public Chat fetchCurrentChat(Chat chat)` | Fetches an updated reference for an equivalent chat from `Client`'s `account` instance field |
| `public void createSendMessagePane(Message message)` | Creates panel for a message sent by the user in a chat's panel, including a dropdown menu of edit and delete buttons |
| `public void sendMessage(Message message)` | Performs server-client communication for actually sending a message |
| `public void createReceiveMessagePane(Message message)` | Creates panel for a message not sent by the user in a chat's panel |
| `public void createFillerMessagePane()` | Creates an invisible message panel for aesthetic purposes |
| `public void deleteMessage(Message message)` | Performs server-client communication for actually deleting a message |
| `public void editMessage(Message message)` | Launches the message editor window |
| `public void loadChat(Chat chat)` | Loads a chat by populating the chat panel with its messages, title, and members |
| `public void addChats()` | Adds all the available chats of a user to their chat list panel |
| `public void addUser()` | Performs server-client communication to add a user to a chat |

### Client.AppGUI.AppGUIListener
This is the `ActionListener` added to most buttons in the application GUI, which does an appropriate action depending on the button which called it.
| Method | Description |
|-|-|
| `public void actionPerformed(ActionEvent e)` | Performs an appropriate action depending on `e.getSource()` |

## Test Classes
To test the three classes that aren't covered in GUI testing, we implemented four different test classes for the `Account`, `Message`, `Chat`, `Server`, and `Client` classes:
- `AccountTest`: Tests each of `Account`'s methods (including the constructor), its superclasses, and its instance fields.
- `MessageTest`: Tests each of `Message`'s methods (including the constructor), its superclasses, and its instance fields.
- `ChatTest`: Tests each of `Chat`'s methods (including the constructor), its superclasses, and its instance fields.
- `ServerAndClientTest`: Tests `Server`'s and `Client`'s instance fields, superclasses, and constructors.
Within each class there is a comprehensive list of test methods that ensure that all the methods work as intended for the program, all the methods exist with the correct modifier and return types, the instance variables exist with the correct modifier, and the constructors exist with the correct parameters and modifers.

All tests were created with JUnit4 as the mainframe, we used the standard assertEquals/assertTrue methods for most of the testing in regards to actually making sure the methods work as intended. The rest of the work was done utilizing java.lang.reflect which enabled us to grab certain data that previously would have been very tough to verify such as getting methods, modifiers, and fields. With this information, it was simple enough to use the methods provided by java.lang.reflect to create a whole and comprehensive test class for each of the three classes that could not be tested through the GUI.

For classes and methods which had to be tested through the GUI, we implemented each feature one at a time and tested them in combination with past features.
