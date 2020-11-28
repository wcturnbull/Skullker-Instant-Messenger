import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import java.net.*;

public class Client extends Thread implements Constants {


    /**
     * add functionality so that client starts a socket that can
     * connect to the server.
     * once connected, allow the user to send messages to the server
     *
     * Neihl's Edit: So i was thinking about how this class will interact with the server class so I'm going to just write
     * them down here to talk about on monday. First and foremost, is this class going to be run through swingutilities
     * like i have it now? or will it be run through everything being implemented through the psvm. personally i think
     * the swingutilities functions way better. Second, the interaction with the server class. What I was envisioning
     * was something like a welcome screen with the options to sign in or create an account and when either option is chosen
     * then I'll send information to the server. All this being said, I'm not sure how to implement the class without the
     * gui aspect, so my final question is, should i just write this class to be run through the intellij terminal and then
     * later we will shift everything to gui's?
     */
    private Account account;
    private ArrayList<Chat> chats;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private WelcomeGUI client;
    private AppGUI app;

    public Client() throws IOException {
        client = new WelcomeGUI();
        app = new AppGUI(account);
        socket = new Socket("localhost", 0xBEEF);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public WelcomeGUI getWelcomeGUI() {
        return client;
    }

    public AppGUI getAppGUI() {
        return app;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                client.getWelcomeGUI().setVisible(true);
            }
        });

        /*
        try {
            Account acc;
            ArrayList<Chat> chats;
            Socket socket = new Socket("localhost", 0xBEEF);
            System.out.println("Connection accepted!");
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //All the TO DO's are mentioned above
            Scanner in = new Scanner(System.in);
            System.out.println("Press 1 to sign in");
            System.out.println("Press 2 to create an account");
            String choice = in.nextLine();
            pw.println(choice);
            byte status;
            if (choice.equals("1")) {
                do {
                    oos.writeByte(LOG_IN);
                    oos.writeObject(null);
                    System.out.println("Please enter your username");
                    String userName = in.nextLine();
                    System.out.println("Please enter your password");
                    String password = in.nextLine();
                    acc = new Account(userName, password);
                    oos.writeUnshared(acc);
                    oos.flush();
                    System.out.println("Confirming credentials");
                    status = ois.readByte();
                    if (status == CONTINUE) {
                        acc = (Account) ois.readObject();
                    } else if (status == INVALID_ACCOUNT) {
                        System.out.println("Invalid Credentials! Please try again");
                    }
                } while (status != CONTINUE);
            } else if (choice.equals("2")) {
                do {
                    oos.writeByte(REGISTER_ACCOUNT);
                    oos.writeObject(null);
                    System.out.println("Please enter a username");
                    String userName = in.nextLine();
                    boolean confirmed = false;
                    String password = "";
                    while (!confirmed) {
                        System.out.println("Please enter the desired password");
                        password = in.nextLine();
                        System.out.println("Please confirm your password");
                        String second = in.nextLine();
                        if (password.equals(second)) {
                            confirmed = true;
                        } else {
                            System.out.println("Passwords were not the same! Please try again");
                        }
                    }
                    Account newAcc = new Account(userName, password);
                    oos.writeObject(newAcc);
                    status = ois.readByte();
                    if (status == CONTINUE) {
                        acc = newAcc;
                    } else if (status == INVALID_ACCOUNT) {
                        System.out.println("An account with those credentials already exists!");
                    }
                } while (status != CONTINUE);
            }
            System.out.println("Beginning to take messages...");

            status = 0;
            Chat currentChat = null;
            while (true) {
                Message message = new Message(in.nextLine());
                oos.writeObject(message);
                oos.flush();
                int size = ois.readInt();
                for (int i = 0; i < size; i ++) {
                    System.out.println(((Message) ois.readObject()).getMessage());
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

         */

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

        class GUIListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sign In Button
                if (e.getSource() == signInButton) {
                    try {
                        oos.writeByte(LOG_IN);
                        oos.writeUnshared(null);
                        oos.writeUnshared(new Account(userName.getText(), String.valueOf(password.getPassword())));
                        oos.flush();

                        byte status = ois.readByte();
                        if (status == CONTINUE) {
                            account = (Account) ois.readObject();
                            oos.writeByte(NO_REQUEST);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    getAppGUI().setVisible(true);
                                }
                            });
                            dispose();
                        } else if (status == INVALID_ACCOUNT) {
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                            oos.writeByte(NO_REQUEST);
                        }
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //Sign Up Button
                if (e.getSource() == signUpButton) {
                    try {
                        oos.writeByte(REGISTER_ACCOUNT);
                        oos.writeUnshared(null);
                        createRegistrationWindow(userName.getText(), String.valueOf(password.getPassword()));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //Register Button
                if (e.getSource() == registerButton) {
                    try {
                        if (userNameRegisterTextField.getText().equals("") ||
                                String.valueOf(passwordRegisterTextField.getPassword()).equals("")) {
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                        } else if (!String.valueOf(passwordRegisterTextField.getPassword()).
                                equals(String.valueOf(confirmPasswordTextField.getPassword()))) {
                            JOptionPane.showMessageDialog(null, "Passwords did not match", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            Account newAccount = new Account(userNameRegisterTextField.getText(),
                                    String.valueOf(passwordRegisterTextField.getPassword()));
                            oos.writeUnshared(newAccount);
                            oos.flush();
                            byte status = ois.readByte();

                            if (status == CONTINUE) {
                                account = newAccount;
                                oos.writeByte(NO_REQUEST);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAppGUI().setVisible(true);
                                    }
                                });
                                registrationFrame.dispose();
                                dispose();
                            } else if (status == INVALID_ACCOUNT) {
                                JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sign In Button
                if (e.getSource() == signInButton) {
                    try {
                        oos.writeByte(LOG_IN);
                        oos.writeUnshared(null);
                        oos.writeUnshared(new Account(userName.getText(), String.valueOf(password.getPassword())));
                        oos.flush();

                        byte status = ois.readByte();
                        if (status == CONTINUE) {
                            account = (Account) ois.readObject();
                            oos.writeByte(NO_REQUEST);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    getAppGUI().setVisible(true);
                                }
                            });
                            dispose();
                        } else if (status == INVALID_ACCOUNT) {
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                            oos.writeByte(NO_REQUEST);
                        }
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //Sign Up Button
                if (e.getSource() == signUpButton) {
                    try {
                        oos.writeByte(REGISTER_ACCOUNT);
                        oos.writeUnshared(null);
                        createRegistrationWindow(userName.getText(), String.valueOf(password.getPassword()));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //Register Button
                if (e.getSource() == registerButton) {
                    try {
                        if (userNameRegisterTextField.getText().equals("") ||
                                String.valueOf(passwordRegisterTextField.getPassword()).equals("")) {
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                        } else if (!String.valueOf(passwordRegisterTextField.getPassword()).
                                equals(String.valueOf(confirmPasswordTextField.getPassword()))) {
                            JOptionPane.showMessageDialog(null, "Passwords did not match", "Messaging App",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            Account newAccount = new Account(userNameRegisterTextField.getText(),
                                    String.valueOf(passwordRegisterTextField.getPassword()));
                            oos.writeUnshared(newAccount);
                            oos.flush();
                            byte status = ois.readByte();

                            if (status == CONTINUE) {
                                account = newAccount;
                                oos.writeByte(NO_REQUEST);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAppGUI().setVisible(true);
                                    }
                                });
                                registrationFrame.dispose();
                                dispose();
                            } else if (status == INVALID_ACCOUNT) {
                                JOptionPane.showMessageDialog(null, "Invalid Account", "Messaging App",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };

        //builds the registration popup window
        public void createRegistrationWindow(String userName, String password) {
            registrationFrame = new JFrame("Register");
            registrationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            registrationInformationPane = new JPanel();
            registrationInformationPane.setLayout(new GridBagLayout());

            registrationLabel = new JLabel("Register New Account");

            userNameRegisterLabel = new JLabel("Username: ", SwingConstants.RIGHT);
            passwordRegisterLabel = new JLabel("Password: ", SwingConstants.RIGHT);
            confirmPasswordLabel = new JLabel("Confirm Password: ", SwingConstants.RIGHT);
            userNameRegisterTextField = new JTextField(userName, 15);
            passwordRegisterTextField = new JPasswordField(password, 15);
            confirmPasswordTextField = new JPasswordField(15);
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
            //TODO: Add actual logo/welcoming info
            mainPanel = new JPanel();
            welcomeContentPanel = new JPanel();
            signInButton = new JButton("Sign In");
            signInButton.addActionListener(actionListener);
            signUpButton = new JButton("Sign Up");
            signUpButton.addActionListener(actionListener);
            buttonPanel = new JPanel();

            userName = new JTextField(15);
            userNameLabel = new JLabel("Username: ", SwingConstants.CENTER);

            password = new JPasswordField(15);
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

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("Messaging App");
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
        private JLabel chatLabel;                       //label that shows the title/users in a selected chat
        private final JButton addUsersButton;           //button to add users into a selected chat
        private final JPanel chatLabelPanel;            //panel that holds the chatLabel and addUsers button
        private JPanel messageContent;                  //MAYBE WILL NEED LATER ON
        private final JScrollBar verticalChatScroller;  //Scroll bar for the chat
        private GridBagConstraints gbc;
        private JMenuBar manipulateMessageMenuBar;
        private JMenu manipulateMessageMenu;
        private JMenuItem editMessageMenuItem;
        private JMenuItem deleteMessageMenuItem;

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

        public AppGUI(Account user) {
            setTitle("Messaging App");

            splitPane = new JSplitPane();

            chatSelectorPanel = new JPanel();
            settingsPanel = new JPanel();

            settingsButton = new JButton("User Settings");
            settingsButton.addActionListener(actionListener);

            createChatPopupButton = new JButton("Create New Chat");
            createChatPopupButton.addActionListener(actionListener);

            chatPanel = new JPanel();

            selectedChat = new JPanel();
            currentChats = new JPanel();

            messagePanel = new JPanel();
            sendMessage = new JTextField();
            sendButton = new JButton("Send");
            sendButton.addActionListener(actionListener);

            //TODO: Reformat chatPanel so that it uses the GridBagLayout
            chatPanel.setLayout(new GridBagLayout());
            gbc = new GridBagConstraints();
            //gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            chatPanel.setBackground(Color.WHITE);
            chatSelectorPanel.setLayout(new BoxLayout(chatSelectorPanel, BoxLayout.Y_AXIS));

            chatLabelPanel = new JPanel();
            chatLabel = new JLabel();
            addUsersButton = new JButton("Add Users");

            chatSelectorScroller = new JScrollPane(chatSelectorPanel);
            chatScroller = new JScrollPane(chatPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            //setting up the split pane
            setPreferredSize(new Dimension(600, 400));

            getContentPane().setLayout(new GridLayout());
            getContentPane().add(splitPane);

            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(200);
            splitPane.setLeftComponent(currentChats);
            splitPane.setRightComponent(selectedChat);

            //setting up the right side of the GUI
            selectedChat.setLayout(new BorderLayout());
            verticalChatScroller = chatScroller.getVerticalScrollBar();

            chatLabelPanel.setLayout(new BorderLayout());
            chatLabelPanel.add(chatLabel, BorderLayout.WEST);
            chatLabelPanel.add(addUsersButton, BorderLayout.EAST);

            selectedChat.setAutoscrolls(true);
            selectedChat.add(chatScroller);
            selectedChat.add(messagePanel, BorderLayout.SOUTH);
            selectedChat.add(chatLabelPanel, BorderLayout.NORTH);

            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
            messagePanel.add(sendMessage);
            sendMessage.addKeyListener(keyListener);
            messagePanel.add(sendButton);

            //setting up the left side of the GUI
            currentChats.setLayout(new BorderLayout());
            chatSelectorScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            chatSelectorScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            currentChats.add(chatSelectorScroller);
            currentChats.add(settingsPanel, BorderLayout.NORTH);
            currentChats.add(createChatPopupButton, BorderLayout.SOUTH);
            settingsPanel.add(settingsButton);
            pack();
            setLocationRelativeTo(null);
        }

        //creates a chat panel with an "open chat" button and the chat's title
        public void createIndividualChatPanel(String chatTitle) {
            createChatPopUp.dispose();
            JPanel newChat = new JPanel();
            JLabel chatLabelLeftPanel = new JLabel(chatTitle, SwingConstants.CENTER);
            chatLabelLeftPanel.setText(chatTitle);
            chatLabel.setText(chatTitle);
            JButton openChatButton = new JButton("Open Chat");
            openChatButton.addActionListener(actionListener);
            newChat.setLayout(new BorderLayout());
            newChat.add(chatLabelLeftPanel, BorderLayout.CENTER);
            newChat.add(openChatButton, BorderLayout.SOUTH);
            Border selectChatBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            newChat.setBorder(selectChatBorder);
            newChat.setMinimumSize(new Dimension(100, 150));
            chatSelectorPanel.add(newChat);
            chatSelectorPanel.revalidate();
            chatLabelPanel.revalidate();
            validate();
        }

        public void createChat() {
            createIndividualChatPanel(createChatNameTextField.getText());
            //TODO: create new chat Object and set the chatPanel to the chat

        }

        public void sendMessage() {
            //if (!sendMessage.getText().equals("")) {
                Insets sentMessageInset = new Insets(5, 40, 0, 0);
                gbc.anchor = GridBagConstraints.FIRST_LINE_END;
                gbc.gridx = 1;
                //gbc.insets = sentMessageInset;

                JPanel messageContent = new JPanel();
                messageContent.setBackground(Color.WHITE);
                JTextArea message = new JTextArea(sendMessage.getText());
                Border messageBorder = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK);
                message.setBorder(messageBorder);
                messageBorder = BorderFactory.createTitledBorder(messageBorder, "you", TitledBorder.RIGHT, TitledBorder.BELOW_BOTTOM);
                message.setBorder(messageBorder);
                message.setMinimumSize(new Dimension(75, 60));
                message.setLineWrap(true);
                message.setWrapStyleWord(true);
                message.setEditable(false);

                messageContent.add(message);
                messageContent.add(createMessageMenu());


                chatPanel.add(messageContent, gbc);
                chatPanel.revalidate();
                validate();

                gbc.gridy++;

                verticalChatScroller.setValue(verticalChatScroller.getMaximum());
                sendMessage.setText("");
            //}
        }

        public JMenuBar createMessageMenu() {
            manipulateMessageMenuBar = new JMenuBar();
            manipulateMessageMenu = new JMenu("...");
            editMessageMenuItem = new JMenuItem("Edit message");
            editMessageMenuItem.addActionListener(actionListener);
            deleteMessageMenuItem = new JMenuItem("Delete message");
            deleteMessageMenuItem.addActionListener(actionListener);
            manipulateMessageMenu.add(editMessageMenuItem);
            manipulateMessageMenu.add(deleteMessageMenuItem);
            manipulateMessageMenuBar.add(manipulateMessageMenu);
            return manipulateMessageMenuBar;
        }

        public void receiveMessage(Message message) {
            Insets receivedMessageInset = new Insets(5, 0, 0, 60);
            gbc.anchor = GridBagConstraints.FIRST_LINE_END;
            gbc.gridx = 0;
            //gbc.insets = receivedMessageInset;
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

            chatPanel.add(messageContent, gbc);
            chatPanel.revalidate();
            validate();

            gbc.gridy++;

            verticalChatScroller.setValue(verticalChatScroller.getMaximum());
            sendMessage.setText("");
        }

        public void createSettingsWindow() {
            userSettingsWindow = new JFrame("User Settings");
            userSettingsContentPane = new JPanel();
            userSettingsLabel = new JLabel("User Settings", SwingConstants.CENTER);
            userSettingsLabel.setFont(userSettingsLabel.getFont().deriveFont(18f));
            editAccountButton = new JButton("Edit Account");
            editAccountButton.addActionListener(actionListener);
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.addActionListener(actionListener);
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(actionListener);

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

        public void createCreateChatPopUp() {
            createChatPopUp = new JFrame();
            createChatTitle = new JLabel("Create Chat", SwingConstants.CENTER);
            createChatTitle.setFont(createChatTitle.getFont().deriveFont(20f));
            createChatContentPane = new JPanel();
            createChatNameLabel = new JLabel("Chat Name: ");
            createChatNameTextField = new JTextField(15);
            createChatNamePane = new JPanel();
            createChatButton = new JButton("Create Chat");
            createChatButton.addActionListener(actionListener);

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

        public void deleteMessage() {
            try {
                oos.writeByte(DELETE_MESSAGE);
                messageContent.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void editMessage() {
            try {
                oos.writeByte(EDIT_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void loadMessages(Account user) throws IOException, ClassNotFoundException {
            //TODO: Add the messages from a chat to the right panel
            user.getChats();

        }

        public void addChats(Account user) throws IOException, ClassNotFoundException {
            //TODO: Add all of the chats that a given user is in to the left panel
            oos.writeByte(REQUEST_DATA);
            oos.flush();
            ois.readObject();
        }

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sendButton) {
                    sendMessage();
                }
                if (e.getSource() == settingsButton) {
                    createSettingsWindow();
                }
                if (e.getSource() == createChatPopupButton) {
                    createCreateChatPopUp();
                }
                if (e.getSource() == createChatButton) {
                    if (createChatNameTextField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a name", "Messaging App",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        createChat();
                    }
                }
                if (e.getSource() == editAccountButton) {
                    //TODO: add edit account functionality
                }
                if (e.getSource() == deleteAccountButton) {
                    //TODO: add delete account functionality

                }
                if (e.getSource() == cancelButton) {
                    userSettingsWindow.dispose();
                }
                if (e.getSource() == editMessageMenuItem) {
                    System.out.println("Edit");
                    editMessage();
                }
                if (e.getSource() == deleteMessageMenuItem) {
                    System.out.println("Delete");
                    deleteMessage();
                }
            }
        };

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    //Test case, won't need later on
                    Account testAccount = new Account("Test Account", "1234");
                    receiveMessage(new Message(testAccount, "Test", new Chat(testAccount, "test Chat")));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
    }
}
