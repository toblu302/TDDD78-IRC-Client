package se.liu.ida.toblu302.tddd78.client;

import se.liu.ida.toblu302.tddd78.client.command.CommandExecuter;
import se.liu.ida.toblu302.tddd78.client.components.ChannelSelectComponent;
import se.liu.ida.toblu302.tddd78.client.components.ChatLogComponent;
import se.liu.ida.toblu302.tddd78.client.components.ConnectedUsersComponent;
import se.liu.ida.toblu302.tddd78.client.components.InputComponent;
import se.liu.ida.toblu302.tddd78.library.IRCEvent;
import se.liu.ida.toblu302.tddd78.library.IRCListener;
import se.liu.ida.toblu302.tddd78.library.IRCConnection;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;

/**
 * A JFrame which contains and manages everything needed to connect to (and use) an IRC server.
 */
public class IRCFrame extends JFrame implements IRCListener, InputListener, TreeSelectionListener
{
    private IRCConnection irc;

    private CommandExecuter commandHandler;

    private ChannelSelectComponent channelSelect = new ChannelSelectComponent(this);
    private ChatLogComponent chatLog = new ChatLogComponent();
    private ConnectedUsersComponent connectedUsers = new ConnectedUsersComponent();
    private InputComponent textInput = new InputComponent(); //might be useful from other contexts as well, which is why
                                                             // it isn't local

    private final static int WIDTH = 720;
    private final static int HEIGHT = 640;

    private final static int CHATBOX_HEIGHT = 20;

    private final static double CHANNEL_LIST_WEIGHT = 0.2;
    private final static double USER_LIST_WEIGHT = 0.2;
    private final static double CHATLOG_WEIGHT = 0.8;

    public IRCFrame(String server, int port, String username, String realName) throws HeadlessException, UnknownHostException
    {
        super("IRC!");

        this.setJMenuBar(this.getIRCMenuBar());
        textInput.addListener(this);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();

        channelSelect.setPreferredSize(new Dimension((int) (WIDTH * CHANNEL_LIST_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 0;
        gridConstraints.weightx = CHANNEL_LIST_WEIGHT;
        gridConstraints.weighty = 5;
        gridConstraints.gridwidth = 1;
        this.add(channelSelect, gridConstraints);

        chatLog.setPreferredSize(new Dimension((int) (WIDTH * CHATLOG_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 1;
        gridConstraints.weightx = CHATLOG_WEIGHT;
        gridConstraints.weighty = 5;
        gridConstraints.gridwidth = 1;
        this.add(chatLog, gridConstraints);

        connectedUsers.setPreferredSize(new Dimension((int) (WIDTH * USER_LIST_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 2;
        gridConstraints.weightx = USER_LIST_WEIGHT;
        gridConstraints.weighty = 5;
        gridConstraints.gridwidth = 1;
        this.add(connectedUsers, gridConstraints);

        textInput.setPreferredSize(new Dimension(WIDTH, CHATBOX_HEIGHT));
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        gridConstraints.weightx = 5;
        gridConstraints.weighty = 0;
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        this.add(textInput, gridConstraints);


        irc = new IRCConnection(server, port, username, realName);
        irc.addListener(this);

        commandHandler = new CommandExecuter(irc);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                irc.quitConnection();
                System.exit(0);
            }
        });


        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setVisible(true);
    }

    private JMenuBar getIRCMenuBar()
    {
        JMenu file = new JMenu("File");

        JMenuItem quit = new JMenuItem("Quit", 'Q');
        JMenuItem newChannel = new JMenuItem("Join Channel", 'C');

        quit.addActionListener(e -> quit());
        newChannel.addActionListener(e -> joinChannel());

        file.add(quit);
        file.add(newChannel);

        final JMenuBar menubar = new JMenuBar();
        menubar.add(file);

        return menubar;
    }



    private void quit()
    {
        //ask the user if they want to quit: exit the program if they do
        int optionChosen = JOptionPane.showOptionDialog(null, "Do you want to quit?",
                "Quit?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (optionChosen == 0)
        {
            irc.quitConnection();
            System.exit(0);
        }
    }

    private void joinChannel()
    {
        String channelName = JOptionPane.showInputDialog(null, "Join channel:");
        this.irc.joinChannel(channelName);
        this.irc.selectTalkable(channelName);
    }

    public void onIRCEvent(IRCEvent e)
    {
        switch (e.getEventType())
        {
            case USERQUIT:
                connectedUsers.removeUser( e.getArgument() );
                break;

            case NEWUSER:
            case CHANGEDNAME:
            case CHANGEDCHANNEL:
                //for all these cases:
                //remove all users from the list and re-add them
                connectedUsers.removeAllUsers();
                if (irc.getChannelUsers() != null)
                {
                    connectedUsers.addMultipleUsers(irc.getChannelUsers());
                }

                //update the chatlog and the topic
                updateChatLog();
                this.setTitle( irc.getChannelTopic() );
                break;

            case NEWMESSAGE:
            case NEWQUERYMESSAGE:
                //update the chatlog, regardless if we're in the channel or not
                updateChatLog();
                break;

            case NEWTOPIC:
                this.setTitle( irc.getChannelTopic() );
                break;

            case JOINEDCHANNEL:
                channelSelect.newChannel(e.getArgument());
                break;

            case LEFTCHANNEL:
                channelSelect.removeChannel(e.getArgument());
                break;

            case NEWQUERY:
                channelSelect.newChannel(e.getArgument());
                break;

            default:
                break;
        }
    }

    //InputListener - user entered some input
    public void recievedInput(String str)
    {
        if (str.startsWith("/"))
        {
            //let our CommandExecuter deal with the commands
            commandHandler.executeCommand(str);
        }
        else
        {
            irc.talk(str);
        }
    }

    public void updateChatLog()
    {
        chatLog.setText(irc.getLog());
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        if (channelSelect.isRootSelected())
        {
            //if we select the "root" of the tree, we want to select some corresponding "Talkable".
            //let the IRCConnection decide what "root" means
            irc.selectRoot();
        }
        else //otherwise, try to convert the node we selected to a Talkable
        {
            DefaultMutableTreeNode channelNode = channelSelect.selectedNode();

            if (channelNode != null)
            {
                irc.selectTalkable(channelNode.toString());
            }
        }
    }

}