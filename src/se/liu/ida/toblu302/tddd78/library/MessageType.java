package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents the various different messages which can be sent from an IRC server.
 */
public enum MessageType
{
    /**
     * New message in a channel.
     */
    CHANNELMESSAGE,

    /**
     * New private message.
     */
    PRIVATEMESSAGE,

    /**
     * Someone joined a channel.
     */
    JOIN,

    /**
     * Someone exits the IRC server.
     */
    QUIT,

    /**
     * Someone left the channel.
     */
    PART,

    /**
     * Someone changed their name.
     */
    NICK,

    /**
     * IRC-server pinged you (to check if you're still there).
     */
    PING,

    /**
     * Numeric reply. For details, see the IRC protocol.
     */
    NUMERIC,

    /**
     * Everything else.
     */
    OTHER
}
