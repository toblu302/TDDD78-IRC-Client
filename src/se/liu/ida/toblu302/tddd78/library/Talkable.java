package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;

public class Talkable
{
    private String name;
    private ArrayList<String> currentUsers = new ArrayList<>();
    private Connection connection;
    private IRCLog log = new IRCLog();

    public Talkable(final String name, Connection connection)
    {
        this.connection = connection;
        this.name = name;
    }

    public void join()
    {
        connection.write("JOIN " + name + "\r\n");
    }

    public void leave()
    {
        connection.write("PART " + name + "\r\n");
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

    public void addUser(String name)
    {
        currentUsers.add(name);
    }

    public ArrayList<String> getCurrentUsers()
    {
        return currentUsers;
    }
}