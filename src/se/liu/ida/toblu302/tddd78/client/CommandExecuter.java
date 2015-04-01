package se.liu.ida.toblu302.tddd78.client;

import se.liu.ida.toblu302.tddd78.library.IRCConnection;

/**
 * This class executes with user-entered commands on an IRCConnection.
 */
public class CommandExecuter
{
    private IRCConnection irc;
    private CommandMaker maker = new CommandMaker();

    public CommandExecuter(final IRCConnection irc)
    {
	this.irc = irc;
    }

    public void executeCommand(String command)
    {
	if(!command.startsWith("/"))
	{
	    return;
	}

	String[] parts = command.substring(1).split(" ");

	Command c = maker.getCommand(parts);

	if(c == null)
	{
	    return;
	}

	switch(c.getType())
	{
	    case QUIT:
		irc.quitConnection();
		System.exit(0);
		break;

	    case NICK:
		irc.changeNick( parts[1] );
		break;

	    case CHANNEL:
		irc.joinChannel( parts[1] );
		break;

	    case MSG:
		irc.joinQuery( parts[1] );
		break;

	    case PART:
		irc.leaveChannel( parts[1] );
		break;

	    default:
		break;
	}

    }
}
