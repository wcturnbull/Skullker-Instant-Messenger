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

    public Client() throws IOException, ClassNotFoundException {
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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
                            userName.setText("");
                            password.setText("");
                            dispose();
                        } else if (status == INVALID_ACCOUNT) {
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
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
                            JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
                                    JOptionPane.ERROR_MESSAGE);
                        } else if (!String.valueOf(passwordRegisterTextField.getPassword()).
                                equals(String.valueOf(confirmPasswordTextField.getPassword()))) {
                            JOptionPane.showMessageDialog(null, "Passwords did not match", "Skullker",
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
                                JOptionPane.showMessageDialog(null, "Invalid Account", "Skullker",
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
        private JLabel chatLabel;                       //label that shows the title/users in a selected chat
        private final JButton addUsersButton;           //button to add users into a selected chat
        private final JPanel chatLabelPanel;            //panel that holds the chatLabel and addUsers button
        private JPanel messageContent;                  //MAYBE WILL NEED LATER ON
        private final JScrollBar verticalChatScroller;  //Scroll bar for the chat
        private GridBagConstraints gbc;

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
        private JButton addUserButton;

        //message editing window
        private JFrame editMessageFrame;
        private JPanel editMessageContentPane;
        private JLabel editMessageTitle;
        private JTextArea editMessageTextArea;
        private JButton editMessageDoneButton;

        Chat currentChat;
        boolean chatOpen = false;

        public AppGUI(Account user) throws IOException, ClassNotFoundException {
            setTitle("Skullker -- " + client.userName.getText());

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
            addUsersButton.addActionListener(new AppGUIListener());

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
            sendMessage.setEditable(false);
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
            /**
             * Having some issues with this addChats(account) if someone wants
             * to try to fix this bug. It's late and I'm tired lol
             */
            //addChats(account);
            pack();
            setLocationRelativeTo(null);
        }

        //creates a fully functional message editor (NEEDS VISUAL WORK)
        public void createMessageEditor(Message message) {
            //TODO: Make it look good
            editMessageFrame = new JFrame("Message Editor");
            editMessageContentPane = new JPanel();
            editMessageTitle = new JLabel("Edit: ");
            editMessageTextArea = new JTextArea(message.getMessage());
            editMessageDoneButton = new JButton("Done");
            editMessageDoneButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == editMessageDoneButton) {
                        message.editMessage(editMessageTextArea.getText());
                        loadChat(currentChat);
                        editMessageFrame.dispose();
                    }
                }
            });

            editMessageContentPane.setLayout(new GridBagLayout());

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;

            editMessageContentPane.add(editMessageTitle, gbc);

            gbc.gridx = 1;
            //gbc.weightx = 2.0;
            editMessageContentPane.add(editMessageTextArea, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            editMessageContentPane.add(editMessageDoneButton);

            editMessageFrame.add(editMessageContentPane);

            editMessageFrame.setSize(400, 300);
            editMessageFrame.pack();
            editMessageFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            editMessageFrame.setLocationRelativeTo(messageContent);
            editMessageFrame.setVisible(true);
        }

        //creates a chat panel with an "open chat" button and the chat's title
        public void createIndividualChatPanel(Chat chat) {
            createChatPopUp.dispose();
            currentChat = chat;
            JPanel newChat = new JPanel();
            String chatTitle = chat.getName();
            JLabel chatLabelLeftPanel = new JLabel(chatTitle, SwingConstants.CENTER);
            chatLabelLeftPanel.setText(chatTitle);
            chatLabel.setText(chatTitle);
            JButton openChatButton = new JButton("Open Chat");
            openChatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == openChatButton) {
                        sendMessage.setEditable(true);
                        chatOpen = true;
                        currentChat = chat;
                        loadChat(chat);
                        chatLabel.setText(chat.getName());
                    }
                }
            });
            newChat.setLayout(new BorderLayout());
            newChat.add(chatLabelLeftPanel, BorderLayout.CENTER);
            newChat.add(openChatButton, BorderLayout.SOUTH);
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
            //TODO: Make this fully functional
            editAccountFrame = new JFrame("Edit Account");
            editAccountFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            editAccountContentPane = new JPanel();
            editAccountTitle = new JLabel("Edit Account:");

            editUsernameLabel = new JLabel("New Username: ");
            editUsernameTextField = new JTextField(account.getUserName(), 15);
            editUsernameConfirmButton = new JButton("Confirm");
            editUsernameConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == editUsernameConfirmButton) {
                        account.setUserName(editUsernameTextField.getText());
                    }
                }
            });

            editPasswordLabel = new JLabel("New Password: ");
            editPasswordTextField = new JTextField(account.getPassword(), 15);
            editPasswordConfirmButton = new JButton("Confirm");
            editPasswordConfirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == editPasswordConfirmButton) {
                        account.setPassword(editPasswordTextField.getText());
                    }
                }
            });

            doneEditingButton = new JButton("Done Editing");
            doneEditingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == doneEditingButton) {
                        account.setUserName(editUsernameTextField.getText());
                        account.setPassword(editPasswordTextField.getText());
                        editAccountFrame.dispose();
                    }
                }
            });



            editAccountContentPane.setLayout(new GridBagLayout());

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            editAccountContentPane.add(editAccountTitle);

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

            gbc.gridy++;
            editAccountContentPane.add(doneEditingButton, gbc);

            editAccountFrame.add(editAccountContentPane);
            editAccountFrame.setSize(new Dimension(400, 300));
            editAccountFrame.setVisible(true);
            editAccountFrame.setLocationRelativeTo(null);
        }

        //creates a window that allows a user to add other users to the chat (NOT FUNCTIONAL)
        public void createAddUsersWindow() {
            addUsersWindow = new JFrame("Add Users");
            addUsersContentPanel = new JPanel();
            addUserTitle = new JLabel("Add a user:");
            addUserTitle.setFont(addUserTitle.getFont().deriveFont(14f));

            addUsernameLabel = new JLabel("Username: ");
            addUsernamePanel = new JPanel();
            addUsernameTextField = new JTextField(15);

            addUserButton = new JButton("Add User");
            addUserButton.addActionListener(new AppGUIListener());

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
            addUsersContentPanel.add(addUsersButton);
            addUsersContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            addUsersWindow.add(addUsersContentPanel);

            addUsersWindow.setSize(300, 150);
            addUsersWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        public void createChat() {
            Chat chat = new Chat(account, createChatNameTextField.getText());
            sendMessage.setEditable(true);
            account.addChat(chat);
            createIndividualChatPanel(chat);
            loadChat(chat);
        }

        //panel that holds a user's sent message and a menu for message manipulation
        public void createSendMessagePane(Message message) {
            gbc.anchor = GridBagConstraints.FIRST_LINE_END;
            gbc.gridx = 1;

            JPanel messageContent = new JPanel();
            messageContent.setBackground(Color.WHITE);
            JTextArea messageTextArea = new JTextArea(message.getMessage());
            Border messageBorder = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK);
            messageTextArea.setBorder(messageBorder);
            messageBorder = BorderFactory.createTitledBorder(messageBorder, "you", TitledBorder.RIGHT, TitledBorder.BELOW_BOTTOM);
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
                    if (e.getSource() == editMessageMenuItem) {
                        System.out.println("Edit");
                        editMessage(message);
                    }
                }
            });
            deleteMessageMenuItem = new JMenuItem("Delete message");
            deleteMessageMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == deleteMessageMenuItem) {
                        System.out.println("Delete");
                        currentChat.removeMessage(message);
                        deleteMessage(message);
                        loadChat(currentChat);
                    }
                }
            });
            manipulateMessageMenu.add(editMessageMenuItem);
            manipulateMessageMenu.add(deleteMessageMenuItem);
            manipulateMessageMenuBar.add(manipulateMessageMenu);
            messageContent.add(manipulateMessageMenuBar);


            chatPanel.add(messageContent, gbc);
            chatPanel.revalidate();
            validate();

            gbc.gridy++;

            verticalChatScroller.setValue(verticalChatScroller.getMaximum());
            sendMessage.setText("");
        }

        //sends a message to the server and builds a sendMessagePane
        public void sendMessage(Message message) {
            if (chatOpen) {
                if (!sendMessage.getText().equals("")) {
                    createSendMessagePane(message);
                    currentChat.sendMessage(message);
                }
            }
        }

        //panel that holds a received message
        public void createReceiveMessagePane(Message message) {
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

        //receives a message from the server and builds a receiveMessagePane
        //TODO: the received messages need to be added to the Chat's messages ArrayList
        public void receiveMessage(Message message) {
            createReceiveMessagePane(message);
            //currentChat.sendMessage(message);
        }

        //tells the server a message is deleted (INCOMPLETE)
        public void deleteMessage(Message message) {
            //TODO: Tell the server a message is deleted

        }

        //builds the message editor and tells the server a message is edited
        public void editMessage(Message message) {
            //TODO: Tell the server a message is being updated
            createMessageEditor(message);
        }

        //loads all of the messages from a chat into the right panel (Needs to be tested with receiving messages)
        public void loadChat(Chat chat) {
            //TODO: Test to see if it loads received messages properly
            chatPanel.removeAll();

            ArrayList<Message> allMessages = chat.getMessages();

            for (int i = 0; i < allMessages.size(); i++) {
                if (allMessages.get(i).getSender() == account) {
                    createSendMessagePane(allMessages.get(i));
                } else {
                    createReceiveMessagePane(allMessages.get(i));
                }
            }

            chatPanel.repaint();
            chatPanel.revalidate();
            validate();
        }

        //Adds all of a user's chats onto the left panel (not functional)
        public void addChats(Account user) {
            //TODO: Add all of the chats that a given user is in to the left panel
            ArrayList<Chat> userChats;
            userChats = user.getChats();
            for (Chat chat : userChats) {
                createIndividualChatPanel(chat);
            }
        }


        class AppGUIListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sendButton) {
                    Message newMessage = new Message(account, sendMessage.getText(), currentChat);
                    sendMessage(newMessage);
                }
                if (e.getSource() == settingsButton) {
                    createSettingsWindow();
                }
                if (e.getSource() == createChatPopupButton) {
                    createCreateChatPopUp();
                }
                if (e.getSource() == createChatButton) {
                    if (createChatNameTextField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a name", "Skullker",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        createChat();
                    }
                }
                if (e.getSource() == editAccountButton) {
                    //TODO: add edit account functionality
                    createEditAccountWindow();
                }
                if (e.getSource() == deleteAccountButton) {
                    int yes_no = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete " +
                            "your account?", "Skullker", JOptionPane.YES_NO_OPTION);
                    if (yes_no == JOptionPane.YES_OPTION) {
                        //TODO: delete the account from the server
                        userSettingsWindow.dispose();
                        app.dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                client.setVisible(true);
                            }
                        });

                    }
                }
                if (e.getSource() == cancelButton) {
                    userSettingsWindow.dispose();
                }
                if (e.getSource() == addUsersButton) {
                    createAddUsersWindow();
                }
                if (e.getSource() == addUserButton) {
                    //TODO: Add another user to the chat (It is checking the field "addUsernameTextField")
                }
            }
        }

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Message newMessage = new Message(account, sendMessage.getText(), currentChat);
                    sendMessage(newMessage);
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
