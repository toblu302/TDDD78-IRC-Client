package se.liu.ida.toblu302.tddd78.client.command;

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

        //the first character is the "/" (see if-statement above), so it's ignored here
        String[] parts = commandString.substring(1).split(" ");

        Command command = maker.getCommand(parts);

        //the command is invalid
        if (command == null)
        {
            System.out.println("You've entered an invalid command.\nHere is a list of valid commands:");
            System.out.println(maker.getValidCommands());
            return;
        }

        switch (command.getType())
        {
            case QUIT:
                irc.quitConnection();
                System.exit(0);
                break;

            case NICK:
                irc.changeNick( parts[1] );
                break;

            case JOIN:
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
