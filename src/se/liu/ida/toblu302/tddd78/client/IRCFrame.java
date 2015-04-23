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
    private InputComponent textInput = new InputComponent();

    private final static int WIDTH = 720;
    private final static int HEIGHT = 640;

    private final static int CHATBOX_HEIGHT = 20;

    private final static double CHANNEL_LIST_WEIGHT = 0.2;
    private final static double USER_LIST_WEIGHT = 0.2;
    private final static double CHATLOG_WEIGHT = 0.8;

    private String username;
    private String realName;
    private final static int DEFAULTPORT = 6667;

    public IRCFrame(String server, int port, String username, String realName) throws HeadlessException, UnknownHostException
    {
        super("IRC!");

        this.username = username;
        this.realName = realName;

        this.setJMenuBar(this.getIRCMenuBar());
        textInput.addListener(this);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();

        channelSelect.setPreferredSize(new Dimension((int) (WIDTH * CHANNEL_LIST_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.weightx = CHANNEL_LIST_WEIGHT;
        gridConstraints.weighty = 5;
        gridConstraints.gridwidth = 1;
        this.add(channelSelect, gridConstraints);

        chatLog.setPreferredSize(new Dimension((int) (WIDTH * CHATLOG_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 0;
        gridConstraints.weightx = CHATLOG_WEIGHT;
        gridConstraints.weighty = 5;
        gridConstraints.gridwidth = 1;
        this.add(chatLog, gridConstraints);

        connectedUsers.setPreferredSize(new Dimension((int) (WIDTH * USER_LIST_WEIGHT), HEIGHT));
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 0;
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
        JMenuItem newServer = new JMenuItem("Join Server", 'S');

        quit.addActionListener(e -> quit());
        newChannel.addActionListener(e -> joinChannel());
        newServer.addActionListener(e -> joinServer());

        file.add(quit);
        file.add(newChannel);
        file.add(newServer);


        final JMenuBar menubar = new JMenuBar();
        menubar.add(file);

        return menubar;
    }



    private void quit()
    {
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
                connectedUsers.removeAllUsers();
                if (irc.getChannelUsers() != null)
                {
                    connectedUsers.addMultipleUsers(irc.getChannelUsers());
                }
                updateChatLog();
                this.setTitle(irc.getChannelTopic());
                break;

            case NEWMESSAGE:
            case NEWQUERYMESSAGE:
                updateChatLog();
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

    private void joinServer()
    {
        String serverAdress = JOptionPane.showInputDialog(null, "Join server (port " + DEFAULTPORT + "): ");
        try
        {
            //bug here, can't use the new connection to talk to a channel/user
            IRCConnection newConnection = new IRCConnection(serverAdress, DEFAULTPORT, username, realName);
            channelSelect.removeAllChannels();
            irc.quitConnection();
            newConnection.addListener(this);
            irc = newConnection;
        }
        catch(UnknownHostException e)
        {
            JOptionPane.showMessageDialog(null, "Invalid server, try again.");
        }
    }

    //InputListener - user entered some input
    public void recievedInput(String str)
    {
        if (str.startsWith("/"))
        {
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
            irc.selectRoot();
        }

        DefaultMutableTreeNode channel = channelSelect.selectedNode();

        if (channel != null)
        {
            irc.selectTalkable(channel.toString());
        }
    }

}