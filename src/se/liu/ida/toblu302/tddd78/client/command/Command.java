package se.liu.ida.toblu302.tddd78.client.command;

/**
 * Represents a command which can be typed into the IRC client.
 */
public class Command
{
    private CommandType type;
    private int numberOfArguments;
    private String cmd;

    /**
     * @param type              The type of argument which the command represents.
     * @param numberOfArguments the number of arguments needed for the command to function.
     * @param cmd               the user input this command should represent.
     */
    public Command(final CommandType type, final String cmd, final int numberOfArguments)
    {
        this.type = type;
        this.cmd = cmd;
        this.numberOfArguments = numberOfArguments;
    }

    public CommandType getType()
    {
        return type;
    }

    public String getCmd()
    {
        return cmd;
    }

    public int getNumberOfArguments()
    {
        return numberOfArguments;
    }
}
