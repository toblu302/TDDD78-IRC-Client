package se.liu.ida.toblu302.tddd78.client;

import se.liu.ida.toblu302.tddd78.library.IRCEvent;
import se.liu.ida.toblu302.tddd78.library.IRCListener;
import se.liu.ida.toblu302.tddd78.library.IRCConnection;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class IRCFrame extends JFrame implements IRCListener, InputListener, TreeSelectionListener
{
    private IRCConnection irc;

    private CommandExecuter commandHandler;

    private InputComponent textInput = new InputComponent();
    private ChatLogComponent chatLog = new ChatLogComponent();
    private ChannelSelectComponent channelSelect = new ChannelSelectComponent(this);

    private final int WIDTH = 640;
    private final int HEIGHT = 480;

    public IRCFrame() throws HeadlessException
    {
	super("IRC!");

        this.setJMenuBar(this.getIRCMenuBar());
	textInput.addListener(this);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        channelSelect.setPreferredSize( new Dimension( (int)(WIDTH * (2/10)), HEIGHT-20) );
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.5;
        c.gridwidth = 1;
        this.add(channelSelect, c);

        chatLog.setPreferredSize( new Dimension( (int)(WIDTH * (8/10)), HEIGHT-20) );
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.9;
        c.weighty = 0.5;
        c.gridwidth = 13;
        this.add(chatLog, c);

        textInput.setPreferredSize(new Dimension(WIDTH, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(textInput, c);


	irc = new IRCConnection("irc.rizon.net", 6667, "tobleu", "Sodjwe  Dofigijrt");
        irc.addListener(this);
	irc.joinChannel("#sdfff");
	irc.selectChannel("#sdfff");

        commandHandler = new CommandExecuter(irc);

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

        if(optionChosen == 0)
        {
            irc.quitConnection();
            System.exit(0);
        }
    }

    private void joinChannel()
    {
        String channelName = JOptionPane.showInputDialog(null, "Join channel:");
        this.irc.joinChannel(channelName);
        this.irc.selectChannel(channelName);
    }

    public void onIRCEvent(IRCEvent e)
    {
        switch( e.getEventType() )
        {
            case NEWMESSAGE:
            case NEWQUERYMESSAGE:
            case CHANGEDCHANNEL:
                updateChatLog();
                break;

            case JOINEDCHANNEL:
                channelSelect.newChannel( e.getArgument() );
                break;

            case LEFTCHANNEL:
                channelSelect.removeChannel( e.getArgument() );
                break;

            case NEWQUERY:
                channelSelect.newChannel( e.getArgument() );
                break;

            default:
                break;
        }
    }

    private void joinServer()
    {
        String serverAdress = JOptionPane.showInputDialog(null, "Join server (port 6667):");
        if (serverAdress != null)
        {
            channelSelect.removeAllChannels();
            irc.quitConnection();
            irc = new IRCConnection(serverAdress, 6667, "tobleu", "Sodjwe  Dofigijrt");
            irc.addListener(this);
        }
    }

    //InputListener - user entered some input
    public void recievedInput(String str)
    {
        if(str.startsWith("/"))
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
        chatLog.setText( irc.getLog() );
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        if(channelSelect.isRootSelected())
        {
            irc.selectRoot();
        }

        DefaultMutableTreeNode channel = channelSelect.selectedNode();

        if(channel != null)
        {
            irc.selectChannel( channel.toString() );
        }
    }

    public static void main(String[] args)
    {
	IRCFrame window = new IRCFrame();
    }

}