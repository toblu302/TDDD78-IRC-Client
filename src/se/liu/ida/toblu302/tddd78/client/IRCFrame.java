package se.liu.ida.toblu302.tddd78.client;

import se.liu.ida.toblu302.tddd78.library.IRCEvent;
import se.liu.ida.toblu302.tddd78.library.IRCListener;
import se.liu.ida.toblu302.tddd78.library.IRCConnection;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class IRCFrame extends JFrame implements IRCListener, InputListener, TreeSelectionListener
{
    private IRCConnection irc;

    private InputComponent textInput = new InputComponent();
    private ChatLogComponent chatLog = new ChatLogComponent();
    private ChannelSelectComponent channelSelect = new ChannelSelectComponent(this);

    public IRCFrame() throws HeadlessException
    {
	super("IRC!");

        this.setJMenuBar(this.getIRCMenuBar());

	textInput.addListener(this);
	this.add(textInput, BorderLayout.SOUTH);
        this.add(chatLog, BorderLayout.CENTER);
        this.add(channelSelect, BorderLayout.WEST);

	irc = new IRCConnection("irc.rizon.net", 6667, "tobleu", "Sodjwe  Dofigijrt");
        irc.addListener(this);
	irc.joinChannel("#sdfff");
	irc.selectChannel("#sdfff");

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

    public void onIRCEvent(IRCEvent e, String argument)
    {
        switch(e)
        {
            case NEWMESSAGE:
                updateChatLog();
                break;

            case JOINEDCHANNEL:
                channelSelect.newChannel(argument);
                break;

            case CHANGEDCHANNEL:
                updateChatLog();
                break;

            case LEFTCHANNEL:
                channelSelect.removeChannel(argument);
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
            channelSelect.removeAll();
            irc.quitConnection();
            irc = new IRCConnection(serverAdress, 6667, "tobleu", "Sodjwe  Dofigijrt");
            irc.addListener(this);
        }
    }

    //InputListener - user entered some input
    public void recievedInput(String str)
    {
	irc.talk(str);
    }

    public void updateChatLog()
    {
        chatLog.setText( irc.getLog() );
    }

    public void valueChanged(TreeSelectionEvent e)
    {
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