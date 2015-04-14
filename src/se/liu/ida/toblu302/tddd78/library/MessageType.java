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
     * Someone joined a channel.
     */
    USERJOINED,

    /**
     * Someone quit a channel.
     */
    QUIT,

    /**
     * Someone changed their name.
     */
    NAMECHANGE,

    /**
     * New private message.
     */
    PRIVATEMESSAGE,

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
