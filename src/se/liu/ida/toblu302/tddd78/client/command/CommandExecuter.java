package se.liu.ida.toblu302.tddd78.client.command;

import se.liu.ida.toblu302.tddd78.client.command.Command;
import se.liu.ida.toblu302.tddd78.client.command.CommandMaker;
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

    public void executeCommand(String commandString)
    {
        if (!commandString.startsWith("/"))
        {
            return;
        }

        String[] parts = commandString.substring(1).split(" ");

        Command command = maker.getCommand(parts);

        if (command == null)
        {
            return;
        }

        switch (command.getType())
        {
            case QUIT:
                irc.quitConnection();
                System.exit(0);
                break;

            case NICK:
                irc.changeNick(parts[1]);
                break;

            case JOIN:
                irc.joinChannel(parts[1]);
                break;

            case MSG:
                irc.joinQuery(parts[1]);
                break;

            case PART:
                irc.leaveChannel(parts[1]);
                break;

            default:
                break;
        }

    }
}
