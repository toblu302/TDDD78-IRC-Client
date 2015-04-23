package se.liu.ida.toblu302.tddd78.client.command;

/**
 * This represents the various different types commands a user can enter.
 *
 * @see se.liu.ida.toblu302.tddd78.client.command.Command
 */
public enum CommandType
{
    /**
     * Exit the connection.
     */
    QUIT,

    /**
     * Change the nick.
     */
    NICK,

    /**
     * Join a channel.
     */
    JOIN,

    /**
     * Send a private message to a user
     */
    MSG,

    /**
     * Leave a channel.
     */
    PART
}
