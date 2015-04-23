package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents something which can be "talked" to in IRC.
 */
public interface Talkable
{
    /**
     * Sends a message to the Talkable.
     * @param msg the message to be sent.
     */
    public void talk(String msg);

    /**
     * Get the name of the Talkable.
     * @return the name of the Talkable.
     */
    public String getName();

    /**
     * Adds a message to the log of the talkable.
     * @param username the user who sent the message.
     * @param message the message the user sent.
     */
    public void addLog(String username, String message);

    /**
     * Get a log of all the messages in the Talkable.
     * @return all the messages in Talkable.
     */
    public String getLog();
}
