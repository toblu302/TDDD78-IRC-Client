package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents something (channel, user) which can be talked to on IRC.
 */
public class Talkable
{
    private String name;
    private List<listedUser> currentUsers = new ArrayList<>();
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

    public void addUser(String name, char mode)
    {
        currentUsers.add( new listedUser(name, mode) );
    }

    public void changeUserName(String oldName, String newName)
    {
        for (listedUser user: currentUsers)
        {
            if( oldName.equals(user.getUserName()) )
            {
                StringBuilder sb = new StringBuilder();
                sb.append(oldName);
                sb.append(" is now known as ");
                sb.append(newName);
                addLog("", sb.toString() );
                user.changeName(newName);
                break;
            }
        }
    }

    public void removeUser(String name)
    {
        currentUsers.remove(name);
    }

    public ArrayList<String> getCurrentUsers()
    {
        ArrayList<String> returnList = new ArrayList<>();
        for (listedUser user: currentUsers)
        {
            returnList.add(user.toString());
        }
        return returnList;
    }
}