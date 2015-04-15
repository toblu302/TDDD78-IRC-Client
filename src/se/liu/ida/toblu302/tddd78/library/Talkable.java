package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents something which can be "talked" to in IRC.
 */
public interface Talkable
{
    public void talk(String msg);
    public String getName();
    public void addLog(String username, String message);
    public String getLog();
}
