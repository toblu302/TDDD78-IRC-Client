package se.liu.ida.toblu302.tddd78.library;

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

    public void changeNick(String newNick)
    {
	connection.write("NICK " + newNick + "\r\n");
    }

    public void joinChannel(String channelName)
    {
	Talkable t = new Talkable(channelName, this.connection);
	t.join();
	talkables.add(t);

	notifyListeners(new IRCEvent(IRCEventType.JOINEDCHANNEL, channelName));
    }

    private boolean inQuery(String userName)
    {
	Talkable t = getTalkableFromName(userName);
	return( t != null);
    }

    public void joinQuery(String userName)
    {
	Talkable t = new Talkable(userName, this.connection);
	talkables.add(t);

	notifyListeners(new IRCEvent(IRCEventType.NEWQUERY, userName));
    }

    public void leaveChannel(String name)
    {
	Talkable t = this.getTalkableFromName(name);
	t.leave();
	talkables.remove(t);

	notifyListeners(new IRCEvent(IRCEventType.LEFTCHANNEL, name));
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

	    notifyListeners( new IRCEvent(IRCEventType.CHANGEDCHANNEL) );
	}
    }

    public void selectRoot()
    {
	selectedTalkable = null;
	notifyListeners(new IRCEvent(IRCEventType.CHANGEDCHANNEL));
    }

    public void talk(String msg)
    {
	if( this.selectedTalkable != null )
	{
	    this.selectedTalkable.talk(msg);
	    selectedTalkable.addLog(this.userName, msg);

	    notifyListeners(new IRCEvent(IRCEventType.NEWMESSAGE));
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

    public ArrayList<String> getChannelUsers()
    {
	if(selectedTalkable == null)
	{
	    return null;
	}
	return selectedTalkable.getCurrentUsers();
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

	String channelName, userName, messageString;
	Talkable t;

	switch(Message.getMessageType(message))
	{
	    case CHANNEL:
		channelName = Message.getChannelString(message);
		userName = Message.getUserString(message);
		messageString = Message.getMessageString(message);
		t = getTalkableFromName(channelName);
		t.addLog(userName, messageString);
		break;

	    case PRIVATE:
		userName = Message.getUserString(message);
		messageString = Message.getMessageString(message);
		if(!inQuery(userName))
		{
		    joinQuery(userName);
		}
		t = getTalkableFromName(userName);
		t.addLog(userName, messageString);
		notifyListeners(new IRCEvent(IRCEventType.NEWQUERYMESSAGE));
		break;

	    case PING:
		connection.write("PONG");
		break;

	    case NUMERIC:
		handleNumeric(Message.getNumericCode(message), message);
		System.out.println(Message.getNumericCode(message));
		break;

	    case OTHER:
		break;

	    default: break;
	}
	notifyListeners(new IRCEvent(IRCEventType.NEWMESSAGE));
    }

    private void handleNumeric(int numericCode, String message)
    {
	switch( NumericReply.getNumericReply(numericCode) )
	{
	    case RPL_NOTOPIC:
		//channel doesn't have a topic
		break;

	    case RPL_TOPIC:
		//channel got a topic
		break;

	    case RPL_NAMREPLY:
		String channelName = Message.getChannelString(message);
		Talkable t = this.getTalkableFromName(channelName);

		int start = message.indexOf(channelName);
		String[] parts = message.substring(start+channelName.length()+2).split(" ");
		for (String name : parts)
		{
		    t.addUser(name);
		}
		break;

	    case RPL_ENDOFNAMES:
		//
		break;

	    default:
		break;
	}
    }

    private Talkable getTalkableFromName(String channelName)
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

    private void notifyListeners(IRCEvent e)
    {
    	for (IRCListener listener : listeners)
    	{
    	    listener.onIRCEvent(e);
    	}
    }
}
