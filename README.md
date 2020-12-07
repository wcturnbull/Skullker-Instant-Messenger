# Skullker: a social network chat application
by Wes Turnbull, Evan Wang, & Neihl Wang

### How to run Skullker
First, run one instance of Server.java. From there, you can run as many simultaneous, seperate instances of Client.java as you want! To close the server, force-terminate it (this will not result in data being written to a file) or simply enter anything into the terminal running your server (this will save data to a file). Lastly, it does not matter whether the logo images are in the directory or directly outside the directory of Client.java; Client functions either way. Anywhere else, however, does not work (to our knowledge). However, data will only be read by the server if the data.txt file is in the same directory as Server.java.

### Known Bugs/Issues/Notes
For the benefit of the graders, here is a small list of known issues or things that might seem like issues.
- Deleting the owner Account of a Chat causes all members currently viewing that Chat to close their viewing of that Chat. This is not unintended behaviour, but is something that I thought graders should know.
- Clients on the welcome screen GUI will only get an error message in the event that the server has closed after their attempt to send a log-in/registration query to the server. This is not unintended, but is important to note.
- Signing out was the last optional feature added to our project; as such, it has some bugs with it that we have not fully seen/solved. For instance, signing out and logging back in to the main application GUI temporarily desyncs the client from the server, which only leads to issues if the server is subsequently safely closed, resulting in the "Server Closed" error message not appearing at the right time for the client, and in the server not terminating until the desynced client disconnects.

In all other cases, Server and Client should and appear to function exactly as intended.

## How our application fills Project 5 Requirements
Like our title suggests, we made our project for the third option of Project 5, the social network chat application. Because ours is a 3 person team, we were only required to finish 1/3 of the additional selection requirements for option 3; instead, we did all 3/3 selection requirements, in addition to some extraneous features.

#### Core Requirements
- [x] **Simultaneous use of Skullker by multiple users is supported.** Due to the multi-threaded structure of our server, multiple users can communicate with the server simultaneously.
- [x] **All user interactions are through graphical user interfaces.** All client-side usage of Skullker is through GUIs rather than through a simple text-based interface.
- [x] **Data is persistent between client sessions.** Because all data is updated serverside and the server runs independently of client-side connections, the server maintains data between client sessions. Data will always be maintained so long as the server is running.
- [x] **Our application client is updated in real-time.** On client-side, we have a Java Swing Timer which, every .1 seconds, automatically sends a query to the server to return updated information. The client GUI then updates its various panels and text fields accordingly.
- [x] **The client (to our knowledge) never crashes.** All possible user-related errors (such as pressing a button before it is functional) are handled either through the use of descriptive JOptionPanes, or by simply not allowing the user to interact with said GUI elements. 
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

## Classes
Our project contains 6 .java files: Message, Chat, Account, Constants, Server, and Client, ignoring our test case files. It is important to note that our project actually contains more than 6 classes, as Server and Client contain multiple inner classes each (ServerThread, ClientThread, ExecutionerThread, WelcomeGUI, and AppGUI). The reason we chose to use inner classes rather than seperating them into seperate .java files was because each inner class had very little functionality outside of their outer classes, and because each inner class required frequent access to their outer classes' instance variables. Our classes basically universally use Java's Vector classes rather than ArrayList due to Vector's accessor and mutator methods being inherently synchronized.

### The ADTs
These classes are foundational to our application's functionality, and primarily serve to structure and organize data.

#### Message
This ADT represents the Messages sent by members of Chats. 
| Instance field | Description |
|-|-|
| private Account sender | The author of the message |
| private String text | The string content of the message |
| private Chat chat | The chat the message is in |
| private String time | The time at which this message was recieved server-side |

| Method | Description |
|-|-|
| public Message(Account sender, String text, Chat chat) | Constructor for the message; instead of time being supplied as an argument, it is measured at time of constructor. |
| public synchronized Account getSender() | Accessor for the author of the message |
| public synchronized String getMessage() | Accessor for the string text of the message |
| public synchronized void editMessage(String text) | Sets the text of the message to the string provided |
| public Chat getChat() | Accessor for the message's chat of origin |
| public String getTime() | Accessor for the message's time of construction |
| public synchronized boolean equals(Object o) | Tests the message's equality with the provided object |
| public synchronized String toString() | Accessor for a string representation of the message |

#### Chat
This ADT represents the Chats that different Accounts can join and send messages in. 
| Instance field | Description |
|-|-|
| private Vector<Account> users | The members of the chat |
| private Vector<Message> messages | The messages sent to this chat |
| private final String name | The name of the chat |
| private final String time | The time at which the chat was created at server-side |
  
| Method | Description |
|-|-|
| public Chat(Account owner, String name) | Constructor for the chat; the owner is added to the list of users, the list of messages is initialized as empty, and time is measured at time of construction |
| public synchronized void sendMessage(Message message) | Sends message to the chat |
| public synchronized Vector<Message> getMessages() | Accessor for the messages in the chat |
| public synchronized void addUser(Account user) | Adds a user as a member of the chat |
| public synchronized void removeUser(Account user) | Removes the given user as a member of the chat |
| public synchronized void deleteUser(Account user) | Similar to removeUser(), but the given user does not remove this chat from their list of chats |
| public synchronized void removeMessage(Message message) | Deletes the given message from the chat |
| public synchronized Vector<Account> getUsers() | Accessor for the members of the chat |
| public synchronized String getName() | Accessor for the name of the chat |
| public synchronized Message fetchMessage(Message message) | Fetches an updated reference for an equivalent message in this chat |
| public String getTime() | Accessor for the chat's time of construction |
| public synchronized boolean equals(Object o) | Tests for object equivalence |
| public synchronized String toString() | Accessor for a string representation of the chat |

#### Account
This ADT represents the Accounts that users can create.
| Instance field | Description |
|-|-|
| private String userName | The account's username |
| private String password | The account's password |
| private String serial | A constant, unique serial code containing the account's username, password, and time of construction |
| private Vector<Chat> chats | The list of chats of which the user is a member |

| Method | Description |
|-|-|
| public Account(String userName, String password) | Constructor for the account; time is measured at time of construction |
| public synchronized String getUserName() | Accessor for username |
| public synchronized void setUserName(String userName) | Mutator for the username |
| public synchronized String getPassword() | Accessor for the password |
| public synchronized void setPassword(String password) | Mutator for the password |
| public synchronized void joinChat(Chat chat) | Adds the user as a member to the given chat |
| public synchronized void leaveChat(Chat chat) | Removes the given chat from the account's list of chats |
| public synchronized Vector<Chat> getChats() | Accessor for the list of chats |
| public synchronized Chat fetchChat(Chat chat) | Fetches an updated reference for the given chat |
| public synchronized void delete() | Deletes the user from their chats, appends "(DELETED)" to their username, and sets their serial to "" |
| public synchronized boolean matchesUsername(Account acc) | Checks whether the accounts share a username |
| public synchronized boolean matchesCredentials(Account acc) | Checks whether the given account has the same credentials as the account |
| public synchronized String getSerial() | Accessor for the serial code |
| public synchronized boolean equals(Object o) | Checks the object and the account for equality |
| public synchronized String toString() | Accessor for a string representation of the account |

### Main Classes

#### Server


#### Server.ServerThread

#### Server.ClientThread

#### Server.ExecutionerThread

#### Client

#### Client.WelcomeGUI

#### Client.AppGUI

## Test Classes

To test the three classes that aren't covered in the GUI testing, we implemented three different test classes for the Account, Message, and Chat classes.
Within each class there is a comprehensive list of test methods that ensure that all the methods work as intended for the program, all the methods exist with the correct modifier and return types, the instance variables exist with the correct modifier, and the constructors exist with the correct parameters and modifers.

All tests were created with JUnit4 as the mainframe, we used the standard assertEquals/assertTrue methods for most of the testing in regards to actually making sure the methods work as intended. The rest of the work was done utilizing java.lang.reflect which enabled us to grab certain data that previously would have been very tough to verify such as getting methods, modifiers, and fields. With this information it was simple enough to use the methods provided by java.lang.reflect to create a whole and comprehensive test class for each of the three classes that could not be tested through the GUI.
