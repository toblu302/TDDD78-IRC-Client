package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents something (channel, user) which can be talked to on IRC.
 */
public class AbstractTalkable implements Talkable
{
    protected String name;
    protected Connection connection;
    protected IRCLog log = new IRCLog();

    public AbstractTalkable(final String name, Connection connection)
    {
        this.connection = connection;
        this.name = name;
    }

    public void talk(String msg)
    {
        connection.write("PRIVMSG " + this.name + " :" + msg + "\r\n");
    }

    public String getName()
    {
        return this.name;
    }

    public String getLog()
    {
        return log.toString();
    }

    public void addLog(String username, String message)
    {
        log.add("<" + username + "> " + message);
    }

}