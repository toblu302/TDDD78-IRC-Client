package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents something (channel, user) which can be talked to on IRC.
 */
public class Talkable
{
    private String name;
    private List<ListedUser> currentUsers = new ArrayList<>();
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
        currentUsers.add(new ListedUser(name, mode));
    }

    public void changeUserName(String oldName, String newName)
    {
        for (ListedUser user : currentUsers)
        {
            if (oldName.equals(user.getUserName()))
            {
                addLog("", oldName + " is now known as " + newName);
                user.changeName(newName);
                break;
            }
        }
    }

    public void removeUser(String name)
    {
        for (ListedUser user : currentUsers)
        {
            if (user.getUserName().equals(name))
            {
                currentUsers.remove(user);
                break;
            }
        }
    }

    public ArrayList<String> getCurrentUsers()
    {
        ArrayList<String> returnList = new ArrayList<>();
        for (ListedUser user : currentUsers)
        {
            returnList.add(user.toString());
        }
        return returnList;
    }
}