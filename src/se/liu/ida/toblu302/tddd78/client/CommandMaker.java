package se.liu.ida.toblu302.tddd78.client;

import java.util.ArrayList;
import java.util.List;

public class CommandMaker
{
    private List<Command> commands = new ArrayList<>();

    public CommandMaker()
    {
	commands.add(new Command(CommandType.QUIT, "quit", 0));
	commands.add(new Command(CommandType.NICK, "nick", 1));
	commands.add(new Command(CommandType.CHANNEL, "join", 1));
	commands.add(new Command(CommandType.MSG, "msg", 1));
	commands.add(new Command(CommandType.PART, "leave", 1));
    }

    public Command getCommand(String[] parts)
    {
	String cmd = parts[0];
	int arguments = parts.length-1;
	for (Command command : commands)
	{
	    if( command.getCmd().equals(cmd) && arguments==command.getNumberOfArguments() )
	    {
		return command;
	    }
	}

	return null;
    }
}
