package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents the various different messages which can be sent from an IRC server.
 */
public enum MessageType
{
    /**
     * New message in a channel.
     */
    CHANNEL,

    /**
     * Someone joined a channel.
     */
    JOIN,

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
    PRIVATE,

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
