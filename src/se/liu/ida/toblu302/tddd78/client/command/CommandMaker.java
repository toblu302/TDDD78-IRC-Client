package se.liu.ida.toblu302.tddd78.client.command;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class creates and stores the various different Commands which a user can enter.
 *
 * @see se.liu.ida.toblu302.tddd78.client.command.Command
 */
public class CommandMaker
{
    private Collection<Command> commands = new ArrayList<>();

    public CommandMaker()
    {
        commands.add(new Command(CommandType.QUIT, "quit", 0));
        commands.add(new Command(CommandType.NICK, "nick", 1));
        commands.add(new Command(CommandType.JOIN, "join", 1));
        commands.add(new Command(CommandType.MSG, "msg", 1));
        commands.add(new Command(CommandType.PART, "leave", 1));
    }

    public Command getCommand(String[] parts)
    {
        String cmd = parts[0];
        int arguments = parts.length - 1;
        for (Command command : commands)
        {
            if (command.getCmd().equals(cmd) && arguments == command.getNumberOfArguments())
            {
                return command;
            }
        }

        return null;
    }

    public String getValidCommands()
    {
        StringBuilder sb = new StringBuilder();

        for (Command command : commands)
        {
            sb.append(command);
        }

        return sb.toString();
    }
}
