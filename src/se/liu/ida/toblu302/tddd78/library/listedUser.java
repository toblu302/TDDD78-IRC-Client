package se.liu.ida.toblu302.tddd78.library;

public class listedUser
{
    private String userName;
    private char mode;

    public listedUser(final String userName, final char mode)
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

    @Override public String toString()
    {
        return (this.mode + this.userName);
    }
}
