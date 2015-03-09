package se.liu.ida.toblu302.tddd78.client;

import se.liu.ida.toblu302.tddd78.library.IRCConnection;

public class CommandExecuter
{
    private IRCConnection irc;

    public CommandExecuter(final IRCConnection irc)
    {
	this.irc = irc;
    }

    private void executeCommand(String command)
    {
	if( command.equals("quit") )
	{
	    irc.quitConnection();
	}
    }

    private void executeCommand(String command, String arg1)
    {
	if( command.equals("nick") )
	{
	    irc.changeNick(arg1);
	}
    }

    public void doCommand(String command)
    {
	if(!command.startsWith("/"))
	{
	    return;
	}

	executeCommand(command.substring(1, command.length()));
    }

}
