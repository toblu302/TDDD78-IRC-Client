package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents an IRC channel
 */
public class Channel extends AbstractTalkable
{
    private Set<ListedUser> currentUsers = new HashSet<>();
    private String topic = null;

    public Channel(final String name, Connection connection)
    {
        super(name, connection);
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public void addUser(String name, char mode)
    {
        currentUsers.add( new ListedUser(name, mode) );
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
                addLog("", name + " has left the channel.");
                currentUsers.remove(user);
                break;
            }
        }
    }

    public Iterable<String> getCurrentUsers()
    {
        Collection<String> returnList = new ArrayList<>();
        for (ListedUser user : currentUsers)
        {
            returnList.add(user.toString());
        }
        return returnList;
    }

    public void join()
    {
        connection.write("JOIN " + name + "\r\n");
    }

    public void leave()
    {
        connection.write("PART " + name + "\r\n");
    }
}
