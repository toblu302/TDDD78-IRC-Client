package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents a user for listing in a channel.
 * This is needed because the mode and the username of a user can be updated separately.
 */
public class ListedUser
{
    private String userName;
    private char mode; //different usermodes hasn't been fully implemented yet

    public ListedUser(final String userName, final char mode)
    {
        this.userName = userName;
        this.mode = mode;
    }

    public void changeName(String newName)
    {
        this.userName = newName;
    }

    public String getUserName()
    {
        return this.userName;
    }

    @Override
    public String toString()
    {
        return (this.userName);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListedUser that = (ListedUser) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return userName != null ? userName.hashCode() : 0;
    }
}
