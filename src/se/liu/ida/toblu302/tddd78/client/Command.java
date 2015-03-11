package se.liu.ida.toblu302.tddd78.client;

public class Command
{
    private CommandType type;
    private int numberOfArguments;
    private String cmd;

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
