import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Vector;

/**
 * Client class
 *
 *
 * <p>Purdue University -- CS18000 -- Fall 2020 -- Project 5</p>
 *
 * @author Wes Turnbull, Evan Wang CS18000, 001
 * @version 7 December 2020
 */
public class Client implements Constants {

    private Account account;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private WelcomeGUI welcome;
    private AppGUI app;

    public Client() throws SocketException {
        try {
            socket = new Socket("localhost", 0xBEEF);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (SocketException exception) {
            JOptionPane.showMessageDialog(null, "Connection Refused", "Skullker",
                    JOptionPane.ERROR_MESSAGE);
            throw exception;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        welcome = new WelcomeGUI();
        app = new AppGUI();
    }

    public WelcomeGUI getWelcomeGUI() {
        return welcome;
    }

    public AppGUI getAppGUI() {
        return app;
    }

    public static void main(String[] args) {
        Client client;
        try {
            client = new Client();
        } catch (SocketException exception) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                client.getWelcomeGUI().setVisible(true);
            }
        });
    }

    public class WelcomeGUI extends JFrame {
        //Welcome panel fields
        public final JTextField userName;           //Sign in username input
        public final JPasswordField password;       //Sign in password input
        private final JPanel mainPanel;             //main welcome panel
        private final JButton signInButton;         //sign in button
        private final JButton signUpButton;         //sign up button
        private BufferedImage logo;                 //logo for app
        private final JLabel logoLabel;             //allows the logo to show up
        private final JPanel buttonPanel;           //panel that holds sign in/sign up buttons
        private final JLabel userNameLabel;         //Username Label
        private final JLabel passwordLabel;         //Password Label
        private final JPanel welcomeContentPanel;   //panel that holds the username/password information

        //Registration panel fields
        private JFrame registrationFrame;                   //main frame for the registration popup
        private JPanel registrationInformationPane;         //holds all of the registration content
        private JLabel registrationLabel;                   //Title of the registration window
        private JLabel userNameRegisterLabel;               //Username registration label
        private JTextField userNameRegisterTextField;       //Username registration text field
        private JLabel passwordRegisterLabel;               //Password registration label
        private JPasswordField passwordRegisterTextField;   //Password registration text field
        private JLabel confirmPasswordLabel;                //Confirm password label
        private JPasswordField confirmPasswordTextField;    //Confirm password text field
        private JButton registerButton;                     //Register button

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sign In Button
                if (e.getSource() == signInButton) {
                    signIn();
                }
                //Sign Up Button
                if (e.getSource() == signUpButton) {
                    createRegistrationWindow();
                }
                //Register Button
                if (e.getSource() == registerButton) {
                    register();
                }
            }
        };

        public void signIn() {
            try {
                try {
                    oos.writeByte(LOG_IN);
                    oos.writeObject(new Account(userName.getText(), String.valueOf(password.getPassword())));
                    oos.flush();
                } catch (SocketException exception) {
                    dispose();
                    getAppGUI().disposeAllFrames();
                    getAppGUI().getTimer().stop();

                    JOptionPane.showMessageDialog(null, "Server Closed", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                }

                byte status = ois.readByte();
                if (status == CONTINUE) {
                    account = (Account) ois.readObject();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            getAppGUI().setVisible(true);
                        }
                    });
                    userName.setText("");
                    password.setText("");
                    account = (Account) ois.readObject();
                    if (registrationFrame != null) {
                        registrationFrame.dispose();
                    }
                    dispose();
                } else if (status == DENIED) {
                    JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SocketException exception) {
                dispose();
                getAppGUI().disposeAllFrames();
                getAppGUI().getTimer().stop();

                JOptionPane.showMessageDialog(null, "Server Closed", "Skullker",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }

        public void register() {
            try {
                if (userNameRegisterTextField.getText().equals("") ||
                        userNameRegisterTextField.getText().indexOf(' ') >= 0 ||
                        String.valueOf(passwordRegisterTextField.getPassword()).equals("") ||
                        String.valueOf(passwordRegisterTextField.getPassword()).indexOf(' ') >= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!String.valueOf(passwordRegisterTextField.getPassword()).
                        equals(String.valueOf(confirmPasswordTextField.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "Passwords did not match",
                            "Skullker", JOptionPane.ERROR_MESSAGE);
                } else {
                    oos.writeByte(REGISTER_ACCOUNT);
                    Account newAccount = new Account(userNameRegisterTextField.getText(),
                            String.valueOf(passwordRegisterTextField.getPassword()));
                    oos.writeObject(newAccount);
                    oos.flush();
                    byte status = ois.readByte();

                    if (status == CONTINUE) {
                        account = newAccount;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                getAppGUI().setVisible(true);
                            }
                        });
                        account = (Account) ois.readObject();
                        registrationFrame.dispose();
                        dispose();
                    } else if (status == DENIED) {
                        JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SocketException exception) {
                registrationFrame.dispose();
                dispose();
                getAppGUI().disposeAllFrames();
                getAppGUI().getTimer().stop();

                JOptionPane.showMessageDialog(null, "Server Closed", "Skullker",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        //builds the registration popup window
        public void createRegistrationWindow() {
            registrationFrame = new JFrame("Register");
            registrationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            registrationInformationPane = new JPanel();
            registrationInformationPane.setLayout(new GridBagLayout());

            registrationLabel = new JLabel("Register New Account");

            userNameRegisterLabel = new JLabel("Username: ", SwingConstants.RIGHT);
            passwordRegisterLabel = new JLabel("Password: ", SwingConstants.RIGHT);
            confirmPasswordLabel = new JLabel("Confirm Password: ", SwingConstants.RIGHT);
            userNameRegisterTextField = new JTextField(userName.getText(), 15);
            userNameRegisterTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        register();
                    }
                }
            });
            passwordRegisterTextField = new JPasswordField(String.valueOf(password.getPassword()), 15);
            passwordRegisterTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        register();
                    }
                }
            });
            confirmPasswordTextField = new JPasswordField(15);
            confirmPasswordTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        register();
                    }
                }
            });
            registerButton = new JButton("Register");
            registerButton.addActionListener(actionListener);

            Insets leftSpace = new Insets(5, 15, 0, 0);
            Insets rightSpace = new Insets(5, 0, 0, 15);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.PAGE_START;
            gbc.insets = new Insets(5, 0, 15, 0);
            gbc.gridwidth = 2;
            registrationInformationPane.add(registrationLabel, gbc);
            gbc.weighty = 0.2;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = leftSpace;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            registrationInformationPane.add(userNameRegisterLabel, gbc);
            gbc.insets = rightSpace;
            gbc.gridx = 1;
            registrationInformationPane.add(userNameRegisterTextField, gbc);
            gbc.insets = leftSpace;
            gbc.gridx = 0;
            gbc.gridy = 2;
            registrationInformationPane.add(passwordRegisterLabel, gbc);
            gbc.insets = rightSpace;
            gbc.gridx = 1;
            registrationInformationPane.add(passwordRegisterTextField, gbc);
            gbc.insets = leftSpace;
            gbc.gridx = 0;
            gbc.gridy = 3;
            registrationInformationPane.add(confirmPasswordLabel, gbc);
            gbc.insets = rightSpace;
            gbc.gridx = 1;
            registrationInformationPane.add(confirmPasswordTextField, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.insets = new Insets(15, 0, 5, 0);
            gbc.anchor = GridBagConstraints.PAGE_END;
            registrationInformationPane.add(registerButton, gbc);

            registrationFrame.add(registrationInformationPane);
            registrationFrame.setSize(new Dimension(400, 300));
            registrationFrame.setVisible(true);
            registrationFrame.pack();
            registrationFrame.setLocationRelativeTo(null);
        }

        public WelcomeGUI() {
            mainPanel = new JPanel();
            welcomeContentPanel = new JPanel();
            signInButton = new JButton("Sign In");
            signInButton.addActionListener(actionListener);
            signUpButton = new JButton("Sign Up");
            signUpButton.addActionListener(actionListener);
            buttonPanel = new JPanel();

            userName = new JTextField(15);
            userName.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        signIn();
                    }
                }
            });
            userNameLabel = new JLabel("Username: ", SwingConstants.CENTER);

            password = new JPasswordField(15);
            password.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        signIn();
                    }
                }
            });
            passwordLabel = new JLabel("Password: ", SwingConstants.CENTER);

            if (logo == null) {
                try {
                    logo = ImageIO.read(new File("skullker.png"));
                } catch (IOException e) {
                    try {
                        logo = ImageIO.read(new File("../skullker.png"));
                    } catch (IOException ignored) {
                        e.printStackTrace();
                    }
                }
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            logoLabel = new JLabel(new ImageIcon(logo));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.add(signInButton);
            buttonPanel.add(signUpButton);
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            welcomeContentPanel.setLayout(new GridBagLayout());
            welcomeContentPanel.setSize(50, 50);
            welcomeContentPanel.add(userNameLabel, gbc);
            gbc.gridx = 1;
            welcomeContentPanel.add(userName, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            welcomeContentPanel.add(passwordLabel, gbc);
            gbc.gridx = 1;
            welcomeContentPanel.add(password, gbc);

            mainPanel.add(logoLabel);
            mainPanel.add(welcomeContentPanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));


            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                    getAppGUI().disposeAllFrames();
                    getAppGUI().getTimer().stop();
                }
            });


            setTitle("Skullker");
            setBackground(Color.WHITE);
            setSize(new Dimension(600, 400));
            getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            getContentPane().add(mainPanel);
            setLocationRelativeTo(null);
        }
    }

    public class AppGUI extends JFrame {
        //main app window
        private final JSplitPane splitPane;             //splits the window
        private final JPanel chatSelectorPanel;         //panel that holds all of the chats a user is in
        private final JPanel chatPanel;                 //panel that holds the content of a chat
        private final JScrollPane chatSelectorScroller; //scroller for the left panel
        private final JScrollPane chatScroller;         //scroller for the right panel
        private final JPanel selectedChat;              //main right panel
        private final JPanel currentChats;              //main left panel
        private final JPanel messagePanel;              //panel that allows a user to enter and send a message
        private final JTextField sendMessage;           //text field for a user to enter their message
        private final JButton sendButton;               //button that sends a message to the other user(s) in the chat
        private final JPanel settingsPanel;             //panel that holds the settings button
        private final JButton settingsButton;           //button that allows a user to edit/delete their account
        private final JButton createChatPopupButton;    //button that allows a user to create a new chat
        private final JLabel chatLabel;                 //label that shows the title/users in a selected chat
        private final JButton addUsersButton;           //button to add users into a selected chat
        private final JPanel chatLabelPanel;            //panel that holds the chatLabel and addUsers button
        private final JScrollBar verticalChatScroller;  //Scroll bar for the chat
        private GridBagConstraints gbc;                 //Grid bag Constraints for the chatPanel

        //create chat window
        private JFrame createChatPopUp;
        private JPanel createChatContentPane;
        private JPanel createChatNamePane;
        private JLabel createChatTitle;
        private JLabel createChatNameLabel;
        private JTextField createChatNameTextField;
        private JButton createChatButton;

        //user settings window
        private JFrame userSettingsWindow;
        private JPanel userSettingsContentPane;
        private JLabel userSettingsLabel;
        private JButton editAccountButton;
        private JButton deleteAccountButton;
        private JButton cancelButton;

        //edit account window
        private JFrame editAccountFrame;
        private JPanel editAccountContentPane;
        private JLabel editAccountTitle;
        private JLabel editUsernameLabel;
        private JTextField editUsernameTextField;
        private JButton editUsernameConfirmButton;
        private JLabel editPasswordLabel;
        private JTextField editPasswordTextField;
        private JButton editPasswordConfirmButton;
        private JButton doneEditingButton;

        //add users window
        private JFrame addUsersWindow;
        private JPanel addUsersContentPanel;
        private JLabel addUserTitle;
        private JLabel addUsernameLabel;
        private JTextField addUsernameTextField;
        private JPanel addUsernamePanel;
        private JButton addInputedUserButton;

        //message editing window
        private JFrame editMessageFrame;
        private JPanel editMessageContentPane;
        private JLabel editMessageTitle;
        private JTextArea editMessageTextArea;
        private JScrollPane editMessageScrollPane;
        private JButton editMessageDoneButton;

        private final Timer timer;

        private Chat previousChat;
        private Chat currentChat;
        private Vector<Chat> previousChats;
        private boolean chatOpen;

        public AppGUI() {
            chatOpen = false;
            setTitle("Skullker -- " + welcome.userName.getText());

            splitPane = new JSplitPane();

            chatSelectorPanel = new JPanel();
            settingsPanel = new JPanel();

            settingsButton = new JButton("User Settings");
            settingsButton.addActionListener(new AppGUIListener());

            createChatPopupButton = new JButton("Create New Chat");
            createChatPopupButton.addActionListener(new AppGUIListener());

            chatPanel = new JPanel();

            selectedChat = new JPanel();
            currentChats = new JPanel();

            messagePanel = new JPanel();
            sendMessage = new JTextField();
            sendButton = new JButton("Send");
            sendButton.addActionListener(new AppGUIListener());

            chatPanel.setLayout(new GridBagLayout());

            chatPanel.setBackground(Color.WHITE);
            chatSelectorPanel.setLayout(new BoxLayout(chatSelectorPanel, BoxLayout.Y_AXIS));

            chatLabelPanel = new JPanel();
            chatLabel = new JLabel();
            chatLabel.setAutoscrolls(true);
            addUsersButton = new JButton("Add Users");
            addUsersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chatOpen) {
                        createAddUsersWindow();
                    }
                }
            });

            chatSelectorScroller = new JScrollPane(chatSelectorPanel);
            chatScroller = new JScrollPane(chatPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            //setting up the split pane
            setPreferredSize(new Dimension(600, 400));

            getContentPane().setLayout(new GridLayout());
            getContentPane().add(splitPane);

            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(220);
            splitPane.setLeftComponent(currentChats);
            splitPane.setRightComponent(selectedChat);

            //setting up the right side of the GUI
            selectedChat.setLayout(new BorderLayout());
            verticalChatScroller = chatScroller.getVerticalScrollBar();
            verticalChatScroller.setUnitIncrement(5);

            chatLabelPanel.setLayout(new BorderLayout());
            chatLabelPanel.add(chatLabel, BorderLayout.WEST);
            chatLabelPanel.add(addUsersButton, BorderLayout.EAST);

            selectedChat.setAutoscrolls(true);
            selectedChat.add(chatScroller);
            selectedChat.add(messagePanel, BorderLayout.SOUTH);
            selectedChat.add(chatLabelPanel, BorderLayout.NORTH);

            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
            messagePanel.add(sendMessage);
            sendMessage.setEditable(false);
            sendMessage.addKeyListener(new KeyAdapter() {@Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        Message newMessage = new Message(account, sendMessage.getText().trim(), currentChat);
                        sendMessage(newMessage);
                        sendMessage.setText("");
                    }
                }
            });
            messagePanel.add(sendButton);

            //setting up the left side of the GUI
            currentChats.setLayout(new BorderLayout());
            chatSelectorScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            chatSelectorScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            currentChats.add(chatSelectorScroller);
            currentChats.add(settingsPanel, BorderLayout.NORTH);
            currentChats.add(createChatPopupButton, BorderLayout.SOUTH);
            settingsPanel.add(settingsButton);

            gbc = new GridBagConstraints();

            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    disposeAllFrames();
                    timer.stop();
                }
            });

            previousChat = null;
            previousChats = new Vector<Chat>();

            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (account != null) {
                        try {
                            oos.writeByte(REQUEST_DATA);
                            oos.flush();
                            account = (Account) ois.readObject();
                        } catch (SocketException | EOFException exception) {
                            JOptionPane.showMessageDialog(null, "Server Closed", "Skullker",
                                    JOptionPane.ERROR_MESSAGE);
                            timer.stop();
                            disposeAllFrames();
                        } catch (IOException | ClassNotFoundException exception) {
                            exception.printStackTrace();
                        }

                        // updates chatSelectorPanel if there is an update detected in the user's chats
                        if (!previousChats.equals(account.getChats())) {
                            addChats();
                        }
                        previousChats = account.getChats();

                        setTitle("Skullker -- " + account.getUserName());

                        // updates previousChat and currentChat references
                        if (currentChat != null) {
                            previousChat = currentChat;
                            currentChat = fetchCurrentChat(new Chat(currentChat.getUsers().get(0),
                                    currentChat.getName()));
                        }

                        // the following code block properly updates chatPanel's title and properties
                        if (currentChat != null) {
                            chatOpen = true;
                            sendMessage.setEditable(true);
                            StringBuilder usersInChat = new StringBuilder(account.getUserName());
                            if (currentChat.getUsers().size() > 1) {
                                for (Account user : currentChat.getUsers()) {
                                    if (!user.getUserName().equals(account.getUserName())) {
                                        usersInChat.append(", ").append(user.getUserName());
                                    }
                                }
                                chatLabel.setText(" " + currentChat.getName() + " -- " + usersInChat);
                            } else {
                                chatLabel.setText(" " + currentChat.getName());
                            }
                        }
                        // the following conditional only updates chatPanel if there is a change in currentChat
                        if (currentChat == null || !(previousChat.getMessages().equals(currentChat.getMessages()))) {
                            chatPanel.removeAll();
                            chatPanel.revalidate();
                            chatPanel.repaint();
                            validate();
                            sendMessage.setEditable(false);
                            chatOpen = false;
                            chatLabel.setText("");

                            if (currentChat != null) {
                                loadChat(currentChat);
                            }
                        }
                    }
                }
            });
            timer.setRepeats(true);
            timer.start();

        }

        public void disposeAllFrames() {
            dispose();
            try {
                createChatPopUp.dispose();
                userSettingsWindow.dispose();
                editAccountFrame.dispose();
                addUsersWindow.dispose();
                editMessageFrame.dispose();
            } catch (NullPointerException e) {
                return;
            }
        }

        public Timer getTimer() {
            return timer;
        }

        //creates a fully functional message editor
        public void createMessageEditor(Message message) {
            editMessageFrame = new JFrame("Message Editor");
            editMessageContentPane = new JPanel();
            editMessageTitle = new JLabel("Edit: ");
            editMessageTextArea = new JTextArea(message.getMessage());
            editMessageScrollPane = new JScrollPane(editMessageTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            editMessageScrollPane.setBounds(10, 60, 780, 500);
            editMessageDoneButton = new JButton("Done");
            editMessageDoneButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editMessageTextArea.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid Message",
                                "Skullker", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        timer.restart();
                        oos.writeByte(EDIT_MESSAGE);
                        oos.writeObject(message);
                        oos.writeObject(editMessageTextArea.getText().trim());
                        oos.flush();
                        account = (Account) ois.readObject();
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                    editMessageFrame.dispose();
                }
            });

            editMessageContentPane.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(5, 0, 0, 0);

            editMessageContentPane.add(editMessageTitle, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.3;
            gbc.weighty = 0.7;
            editMessageScrollPane.setPreferredSize(new Dimension(200, 125));
            editMessageTextArea.setWrapStyleWord(true);
            editMessageTextArea.setLineWrap(true);
            editMessageContentPane.add(editMessageScrollPane, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            gbc.weightx = 0;
            gbc.insets = new Insets(0, 0, 16, 0);
            editMessageContentPane.add(editMessageDoneButton, gbc);

            editMessageFrame.add(editMessageContentPane);

            editMessageFrame.setSize(250, 250);
            //editMessageFrame.pack();
            editMessageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editMessageFrame.setLocationRelativeTo(null);
            editMessageFrame.setVisible(true);
        }

        //creates a chat panel with an "open chat" button and the chat's title
        public void createIndividualChatPanel(Chat chat) {
            //currentChat = chat;
            JPanel newChat = new JPanel();
            String chatTitle = chat.getName();
            JLabel chatLabelLeftPanel = new JLabel(chatTitle, SwingConstants.CENTER);
            chatLabelLeftPanel.setText(chatTitle);
            JButton openChatButton = new JButton("Open Chat");
            openChatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage.setEditable(true);
                    chatOpen = true;
                    currentChat = fetchCurrentChat(new Chat(chat.getUsers().get(0), chat.getName()));
                    loadChat(currentChat);
                }
            });
            JButton leaveChatButton = new JButton("Leave Chat");
            leaveChatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        timer.restart();
                        oos.writeByte(LEAVE_CHAT);
                        oos.writeObject(chat);
                        account = (Account) ois.readObject();
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            newChat.setLayout(new BorderLayout());
            newChat.add(chatLabelLeftPanel, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2));
            buttonPanel.add(openChatButton);
            buttonPanel.add(leaveChatButton);
            newChat.add(buttonPanel, BorderLayout.SOUTH);
            Border selectChatBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            newChat.setBorder(selectChatBorder);
            newChat.setMinimumSize(new Dimension(100, 150));
            chatOpen = true;
            chatSelectorPanel.add(newChat);
            chatSelectorPanel.revalidate();
            chatLabelPanel.revalidate();
            validate();
        }

        //creates a window that allows a user to edit their account information (NEEDS WORK)
        public void createEditAccountWindow() {
            editAccountFrame = new JFrame("Edit Account");
            editAccountFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            editAccountContentPane = new JPanel();
            editAccountTitle = new JLabel("Edit Account:", SwingConstants.CENTER);
            editAccountTitle.setFont(editAccountTitle.getFont().deriveFont(14f));

            editUsernameLabel = new JLabel("New Username: ");
            editUsernameTextField = new JTextField(account.getUserName(), 15);
            editUsernameConfirmButton = new JButton("Confirm");
            editUsernameConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editUsernameTextField.getText().equals("") ||
                            editUsernameTextField.getText().trim().indexOf(' ') >= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid Username",
                                "Skullker", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            timer.restart();
                            oos.writeByte(EDIT_USERNAME);
                            oos.writeObject(new Account(editUsernameTextField.getText(),
                                    editPasswordTextField.getText()));
                            if (ois.readByte() == DENIED) {
                                JOptionPane.showMessageDialog(null, "Invalid Username",
                                        "Skullker", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Username Edited",
                                        "Skullker", JOptionPane.INFORMATION_MESSAGE);
                            }
                            account = (Account) ois.readObject();
                        } catch (IOException | ClassNotFoundException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            editPasswordLabel = new JLabel("New Password: ");
            editPasswordTextField = new JTextField(account.getPassword(), 15);
            editPasswordConfirmButton = new JButton("Confirm");
            editPasswordConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editPasswordTextField.getText().equals("") ||
                            editPasswordTextField.getText().trim().indexOf(' ') >= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid Password",
                                "Skullker", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            timer.restart();
                            oos.writeByte(EDIT_PASSWORD);
                            oos.writeObject(new Account(editUsernameTextField.getText(),
                                    editPasswordTextField.getText()));
                            account = (Account) ois.readObject();
                            JOptionPane.showMessageDialog(null, "Password Edited",
                                    "Skullker", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException | ClassNotFoundException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            doneEditingButton = new JButton("Exit Menu");
            doneEditingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editAccountFrame.dispose();
                }
            });

            editAccountContentPane.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.insets = new Insets(0, 0, 20, 0);
            editAccountContentPane.add(editAccountTitle, gbc);

            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            editAccountContentPane.add(editUsernameLabel, gbc);

            gbc.gridx++;
            editAccountContentPane.add(editUsernameTextField, gbc);

            gbc.gridx++;
            editAccountContentPane.add(editUsernameConfirmButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            editAccountContentPane.add(editPasswordLabel, gbc);

            gbc.gridx++;
            editAccountContentPane.add(editPasswordTextField, gbc);

            gbc.gridx++;
            editAccountContentPane.add(editPasswordConfirmButton, gbc);

            gbc.insets = new Insets(20, 0, 0 , 0);
            gbc.gridy++;
            editAccountContentPane.add(doneEditingButton, gbc);

            editAccountFrame.add(editAccountContentPane);
            editAccountFrame.setSize(new Dimension(400, 200));
            editAccountFrame.setVisible(true);
            editAccountFrame.setLocationRelativeTo(null);
        }

        //creates a window that allows a user to add other users to the chat (NOT FUNCTIONAL)
        public void createAddUsersWindow() {
            addUsersWindow = new JFrame("Add Users");
            addUsersWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addUsersContentPanel = new JPanel();
            addUserTitle = new JLabel("Add a user:");

            addUserTitle.setFont(addUserTitle.getFont().deriveFont(14f));

            addUsernameLabel = new JLabel("Username: ");
            addUsernamePanel = new JPanel();
            addUsernameTextField = new JTextField(15);
            addUsernameTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        addUser();
                    }
                }
            });

            addInputedUserButton = new JButton("Add User");
            addInputedUserButton.addActionListener(new AppGUIListener());


            addUsersContentPanel.setLayout(new BoxLayout(addUsersContentPanel, BoxLayout.Y_AXIS));

            addUsernamePanel.setLayout(new BoxLayout(addUsernamePanel, BoxLayout.X_AXIS));
            addUsernamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
            addUsernamePanel.add(addUsernameLabel);
            addUsernamePanel.add(addUsernameTextField);
            addUsernamePanel.add(Box.createRigidArea(new Dimension(10, 0)));

            addUsersContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            addUsersContentPanel.add(addUserTitle);
            addUsersContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            addUsersContentPanel.add(addUsernamePanel);
            addUsersContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            addUsersContentPanel.add(addInputedUserButton);
            addUsersContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            addUsersWindow.add(addUsersContentPanel);

            addUsersWindow.setSize(300, 150);
            addUsersWindow.setVisible(true);

            addUsersWindow.pack();
            addUsersWindow.setLocationRelativeTo(null);
        }

        //user settings window
        public void createSettingsWindow() {
            userSettingsWindow = new JFrame("User Settings");
            userSettingsContentPane = new JPanel();
            userSettingsLabel = new JLabel("User Settings", SwingConstants.CENTER);
            userSettingsLabel.setFont(userSettingsLabel.getFont().deriveFont(18f));
            editAccountButton = new JButton("Edit Account");
            editAccountButton.addActionListener(new AppGUIListener());
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.addActionListener(new AppGUIListener());
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new AppGUIListener());

            userSettingsContentPane.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.insets = new Insets(5, 0, 15, 0);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            userSettingsContentPane.add(userSettingsLabel, gbc);

            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            userSettingsContentPane.add(editAccountButton, gbc);
            gbc.gridx = 1;
            userSettingsContentPane.add(deleteAccountButton, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.insets = new Insets(5, 5, 10, 5);
            userSettingsContentPane.add(cancelButton, gbc);

            userSettingsWindow.setSize(400, 150);
            userSettingsWindow.setLocationRelativeTo(null);
            userSettingsWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            userSettingsWindow.setVisible(true);

            userSettingsWindow.add(userSettingsContentPane, BorderLayout.CENTER);
        }

        //chat creator popup
        public void createCreateChatPopUp() {
            createChatPopUp = new JFrame();
            createChatTitle = new JLabel("Create Chat", SwingConstants.CENTER);
            createChatTitle.setFont(createChatTitle.getFont().deriveFont(20f));
            createChatContentPane = new JPanel();
            createChatNameLabel = new JLabel("Chat Name: ");
            createChatNameTextField = new JTextField(15);
            createChatNameTextField.addKeyListener(new KeyAdapter() {@Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (createChatNameTextField.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Please enter a name",
                                    "Skullker", JOptionPane.ERROR_MESSAGE);
                        } else {
                            createChat(createChatNameTextField.getText());
                            createChatPopUp.dispose();
                        }
                    }
                }
            });
            createChatNamePane = new JPanel();
            createChatButton = new JButton("Create Chat");
            createChatButton.addActionListener(new AppGUIListener());

            createChatContentPane.setLayout(new BoxLayout(createChatContentPane, BoxLayout.Y_AXIS));
            createChatNamePane.add(createChatNameLabel);
            createChatNamePane.add(createChatNameTextField);

            createChatContentPane.add(createChatTitle);
            createChatContentPane.add(Box.createRigidArea(new Dimension(0, 15)));
            createChatContentPane.add(createChatNamePane);
            createChatContentPane.add(createChatButton);
            createChatContentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            createChatContentPane.setAlignmentX(Component.CENTER_ALIGNMENT);

            createChatPopUp.setSize(300, 150);
            createChatPopUp.setLocationRelativeTo(null);
            createChatPopUp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            createChatPopUp.setVisible(true);

            createChatPopUp.add(createChatContentPane, BorderLayout.CENTER);
        }

        //creates a chat, adds it to the user's account, and sets the current chat to the created chat
        public void createChat(String chatName) {
            timer.restart();
            Chat chat = new Chat(account, chatName);
            currentChat = chat;
            loadChat(currentChat);
            try {
                oos.writeByte(CREATE_CHAT);
                oos.writeObject(chat);
                if (ois.readByte() == DENIED) {
                    JOptionPane.showMessageDialog(null, "Chat Already Exists", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                    currentChat = null;
                }
                account = (Account) ois.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
            sendMessage.setEditable(true);
            if (currentChat != null) {
                createIndividualChatPanel(chat);
            }
        }

        public Chat fetchCurrentChat(Chat chat) {
            for (Chat c : account.getChats()) {
                if (c.equals(chat)) {
                    return c;
                }
            }
            return null;
        }

        //panel that holds a user's sent message and a menu for message manipulation
        public void createSendMessagePane(Message message) {
            gbc.gridx = 1;

            JPanel messageContent = new JPanel();
            messageContent.setBackground(Color.WHITE);
            JTextArea messageTextArea = new JTextArea(message.getMessage());
            Border messageBorder = BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK);
            messageTextArea.setBorder(messageBorder);
            messageBorder = BorderFactory.createTitledBorder(messageBorder, "you",
                    TitledBorder.RIGHT, TitledBorder.BELOW_BOTTOM);
            messageTextArea.setBorder(messageBorder);
            messageTextArea.setMinimumSize(new Dimension(75, 60));
            messageTextArea.setLineWrap(true);
            messageTextArea.setWrapStyleWord(true);
            messageTextArea.setEditable(false);

            messageContent.add(messageTextArea);
            JMenuBar manipulateMessageMenuBar = new JMenuBar();
            JMenu manipulateMessageMenu;
            JMenuItem editMessageMenuItem;
            JMenuItem deleteMessageMenuItem;
            manipulateMessageMenu = new JMenu("...");
            editMessageMenuItem = new JMenuItem("Edit message");
            editMessageMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editMessage(message);
                }
            });
            deleteMessageMenuItem = new JMenuItem("Delete message");
            deleteMessageMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteMessage(message);
                }
            });

            manipulateMessageMenu.add(editMessageMenuItem);
            manipulateMessageMenu.add(deleteMessageMenuItem);
            manipulateMessageMenuBar.add(manipulateMessageMenu);
            messageContent.add(manipulateMessageMenuBar);

            gbc.gridy++;
            chatPanel.add(messageContent, gbc);
            chatPanel.revalidate();
            validate();

        }

        //sends a message to the server and builds a sendMessagePane
        public void sendMessage(Message message) {
            if (chatOpen) {
                if (!sendMessage.getText().trim().equals("")) {
                    try {
                        timer.restart();
                        oos.writeByte(SEND_MESSAGE);
                        oos.writeObject(message);
                        account = (Account) ois.readObject();
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        //panel that holds a received message
        public void createReceiveMessagePane(Message message) {
            gbc.gridx = 0;

            JPanel messageContent = new JPanel();
            messageContent.setBackground(Color.WHITE);
            JTextArea receivedMessage = new JTextArea(message.getMessage());
            Border messageBorder = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.RED);
            receivedMessage.setBorder(messageBorder);
            messageBorder = BorderFactory.createTitledBorder(messageBorder, message.getSender().getUserName(),
                    TitledBorder.LEFT, TitledBorder.BELOW_BOTTOM);
            receivedMessage.setBorder(messageBorder);
            receivedMessage.setMinimumSize(new Dimension(75, 60));
            receivedMessage.setLineWrap(true);
            receivedMessage.setWrapStyleWord(true);
            receivedMessage.setEditable(false);

            messageContent.add(receivedMessage);

            gbc.gridy++;
            chatPanel.add(messageContent, gbc);
            chatPanel.revalidate();
            validate();

        }

        //receives a message from the server and builds a receiveMessagePane
        public void receiveMessage(Message message) {
            createReceiveMessagePane(message);
        }

        //tells the server a message is deleted
        public void deleteMessage(Message message) {
            try {
                timer.restart();
                oos.writeByte(DELETE_MESSAGE);
                oos.writeObject(message);
                account = (Account) ois.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }

        }

        //builds the message editor and tells the server a message is edited
        public void editMessage(Message message) {
            createMessageEditor(message);
        }

        //loads all of the messages from a chat into the right panel (Needs to be tested with receiving messages)
        public void loadChat(Chat chat) {
            Vector<Message> allMessages = currentChat.getMessages();
            for (Message message : allMessages) {
                if (message.getSender().equals(account)) {
                    createSendMessagePane(message);
                } else {
                    createReceiveMessagePane(message);
                }
            }

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    verticalChatScroller.setValue(verticalChatScroller.getMaximum());
                }
            });

            chatLabelPanel.add(addUsersButton, BorderLayout.EAST);
            chatPanel.repaint();
            chatPanel.revalidate();
            validate();

        }

        //Adds all of a user's chats onto the left panel (not functional)
        public void addChats() {
            chatSelectorPanel.removeAll();
            Vector<Chat> userChats = account.getChats();
            for (Chat chat : userChats) {
                createIndividualChatPanel(chat);
            }
            chatSelectorPanel.revalidate();
            chatSelectorPanel.repaint();
        }

        public void addUser() {
            try {
                if (addUsernameTextField.getText().equals(account.getUserName())) {
                    JOptionPane.showMessageDialog(null, "Invalid User", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                timer.restart();
                oos.writeByte(ADD_USER_TO_CHAT);
                oos.writeObject(currentChat);
                oos.writeObject(new Account(addUsernameTextField.getText(), ""));

                byte status = ois.readByte();
                if (status == DENIED) {
                    JOptionPane.showMessageDialog(null, "Invalid User", "Skullker",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    addUsersWindow.dispose();
                }
                account = (Account) ois.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        class AppGUIListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sendButton) {
                    Message newMessage = new Message(account, sendMessage.getText().trim(), currentChat);
                    sendMessage(newMessage);
                    sendMessage.setText("");
                }
                if (e.getSource() == settingsButton) {
                    createSettingsWindow();
                }
                if (e.getSource() == createChatPopupButton) {
                    createCreateChatPopUp();
                }
                if (e.getSource() == createChatButton) {
                    if (createChatNameTextField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a name",
                                "Skullker", JOptionPane.ERROR_MESSAGE);
                    } else {
                        createChat(createChatNameTextField.getText());
                        createChatPopUp.dispose();
                    }
                }
                if (e.getSource() == editAccountButton) {
                    createEditAccountWindow();
                }
                if (e.getSource() == deleteAccountButton) {
                    timer.restart();
                    int yes_no = JOptionPane.showConfirmDialog(null,
                            "Are you sure you would like to delete your account?",
                            "Skullker", JOptionPane.YES_NO_OPTION);
                    if (yes_no == JOptionPane.YES_OPTION) {
                        try {
                            timer.restart();
                            chatSelectorPanel.removeAll();
                            oos.writeByte(DELETE_ACCOUNT);
                            oos.flush();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        account = null;
                        userSettingsWindow.dispose();
                        app.dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                welcome.setVisible(true);
                            }
                        });
                    }
                }
                if (e.getSource() == cancelButton) {
                    timer.restart();
                    userSettingsWindow.dispose();
                }
                if (e.getSource() == addInputedUserButton) {
                    timer.restart();
                    addUser();
                }
            }
        }
    }
}
