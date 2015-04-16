package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an IRC channel
 */
public class Channel extends AbstractTalkable
{
    private List<ListedUser> currentUsers = new ArrayList<>();

    public Channel(final String name, Connection connection)
    {
        super(name, connection);
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

    public void join()
    {
        connection.write("JOIN " + name + "\r\n");
    }

    public void leave()
    {
        connection.write("PART " + name + "\r\n");
    }
}