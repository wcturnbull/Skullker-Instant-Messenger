import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.activation.ActivationID;

public class AppGUI extends JFrame {
    private final JSplitPane splitPane;             //splits the window
    private final JPanel chatSelectorPanel;         //panel that holds all of the chats a user is in
    private final JPanel chatPanel;                 //panel that holds the content of a chat
    private final JScrollPane chatSelectorScroller; //scroller for the left panel
    private final JScrollPane chatScroller;         //scroller for the right panel
    private final JPanel messages;                  //main right panel
    private final JPanel currentChats;              //main left panel
    private final JPanel messagePanel;              //panel that allows a user to enter and send a message
    private final JTextField sendMessage;           //text field for a user to enter their message
    private final JButton sendButton;               //button that sends a message to the other user(s) in the chat
    private final JPanel settingsPanel;             //panel that holds the settings button
    private final JButton settingsButton;           //button that allows a user to edit/delete their account
    private final JButton createChatButton;         //button that allows a user to create a new chat

    public AppGUI(Account user) {
        setTitle("Messaging App");

        splitPane = new JSplitPane();

        chatSelectorPanel = new JPanel();
        chatSelectorScroller = new JScrollPane();
        settingsPanel = new JPanel();

        settingsButton = new JButton("User Settings");
        settingsButton.addActionListener(actionListener);

        createChatButton = new JButton("Create New Chat");
        createChatButton.addActionListener(actionListener);

        chatPanel = new JPanel();
        chatScroller = new JScrollPane();

        messages = new JPanel();
        currentChats = new JPanel();

        messagePanel = new JPanel();
        sendMessage = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(actionListener);

        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setLeftComponent(currentChats);
        splitPane.setRightComponent(messages);

        messages.setLayout(new BorderLayout());
        chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        messages.add(chatScroller, BorderLayout.EAST);
        messages.add(messagePanel, BorderLayout.SOUTH);
        messages.add(chatPanel);

        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.add(sendMessage);
        messagePanel.add(sendButton);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        currentChats.setLayout(new BorderLayout());
        chatSelectorScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        currentChats.add(chatSelectorScroller, BorderLayout.EAST);
        currentChats.add(settingsPanel, BorderLayout.NORTH);
        currentChats.add(createChatButton, BorderLayout.SOUTH);
        currentChats.add(chatSelectorPanel);
        settingsPanel.add(settingsButton);

        chatSelectorPanel.setLayout(new BoxLayout(chatSelectorPanel, BoxLayout.Y_AXIS));

        pack();
    }

    //TODO: Add actionListener Functionality
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {

            }
            if (e.getSource() == settingsButton) {

            }
            if (e.getSource() == createChatButton) {

            }
        }
    };

    public void addMessages(Account user) {
        //TODO: Add a the messages from a chat to the right panel

    }

    public void addChats(Account user) {
        //TODO: Add all of the chats that a given user is in to the left panel

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppGUI(new Account("Wes", "123")).setVisible(true);
            }
        });
    }
}
