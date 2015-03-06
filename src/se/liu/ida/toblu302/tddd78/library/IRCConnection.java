package se.liu.ida.toblu302.tddd78.library;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a connection to an IRC server.
 */

public class IRCConnection
{
    private Collection<IRCListener> listeners = new ArrayList<>();

    private Connection connection;
    private String userName;
    private String realName;

    private Collection<Talkable> talkables = new ArrayList<>();
    private Talkable selectedTalkable = null;

    private Thread loggingThread = null;

    private IRCLog log = new IRCLog();


    public IRCConnection(final String server, int port, String userName, String realName)
    {
	this.userName = userName;
	this.realName = realName;

	this.connection = new Connection(server, port);

	connection.write("NICK " + userName + "\r\n");
	connection.write("USER " + userName + " 0 * : " + realName + "\r\n");

	startLogging();
    }

    public void joinChannel(String name)
    {
	Talkable t = new Talkable(name, this.connection);
	t.join();
	talkables.add(t);

	notifyListeners(IRCEvent.JOINEDCHANNEL, name);
    }

    public void leaveChannel(String name)
    {
	Talkable t = this.getChannelFromName(name);
	t.leave();
	talkables.remove(t);

	notifyListeners(IRCEvent.LEFTCHANNEL, name);
    }

    public void selectChannel(String channelName)
    {
	Talkable t = null;
	for (Talkable talkable : talkables)
	{
	    if( talkable.getName().equals(channelName) )
	    {
		t = talkable;
	    }
	}

	if( t != null)
	{
	    this.selectedTalkable = t;
	    this.notifyListeners(IRCEvent.CHANGEDCHANNEL);
	}
    }

    public void talk(String msg)
    {
	if( this.selectedTalkable != null )
	{
	    this.selectedTalkable.talk(msg);
	    selectedTalkable.addLog(this.userName, msg);

	    notifyListeners(IRCEvent.NEWMESSAGE);
	}
    }

    public String getLog()
    {
	if(selectedTalkable != null)
	{
	    return selectedTalkable.getLog();
	}
	return log.toString();
    }

    public void quitConnection()
    {
	loggingThread.interrupt();
	connection.write("QUIT");
    	connection.close();
    }

    private void handleMessage(String message)
    {
	log.add(message);
	switch(Message.getMessageType(message))
	{
	    case CHANNEL:
		String channelName = Message.getChannelString(message);
		String userName = Message.getUserString(message);
		String messageString = Message.getMessageString(message);
		Talkable t = getChannelFromName(channelName);
		t.addLog(userName, messageString);
		break;

	    case PING:
		connection.write("PONG");
		break;

	    case OTHER:
		break;

	    default: break;
	}

	notifyListeners(IRCEvent.NEWMESSAGE);
    }

    private Talkable getChannelFromName(String channelName)
    {
	Talkable t = null;
	for (Talkable talkable : talkables)
	{
	    if( talkable.getName().equals(channelName) )
	    {
		t = talkable;
	    }
	}

	return t;
    }

    private String read()
    {
    	return connection.read();
    }

    private void startLogging()
    {
	class IRCThread implements Runnable
	{
	    public void run()
	    {
		while (!Thread.interrupted())
		{
		    String line = read();

		    if(line != null)
		    {
			handleMessage(line);
		    }
		}
	    }
	}
	loggingThread = new Thread(new IRCThread());
	loggingThread.start();
    }

    public void addListener(IRCListener ircl)
    {
	this.listeners.add(ircl);
    }

    private void notifyListeners(IRCEvent e, String argument)
    {
	for (IRCListener listener : listeners)
	{
	    listener.onIRCEvent(e, argument);
	}
    }

    private void notifyListeners(IRCEvent e)
    {
    	for (IRCListener listener : listeners)
    	{
    	    listener.onIRCEvent(e, null);
    	}
    }
}
