package se.liu.ida.toblu302.tddd78.library;

/**
 * The various numeric replies which an IRC server can send.
 * <p>
 * For full details, see the IRC protocol. It details the cases when the numeric replies are going to be sent.
 * <p>
 * (Not all of them are implemented here)
 */
public enum NumericReply
{
    /**
     * A numeric reply which hasn't been added here.
     */
    NOT_IMPLEMENTED(-1),

    /**
     * The server sends the user information upon a successful connection.
     */
    RPL_MYINFO(4),

    /**
     * Channel doesn't have a topic.
     */
    RPL_NOTOPIC(331),

    /**
     * The topic of a channel has been sent.
     */
    RPL_TOPIC(332),

    /**
     * A list of users in a channel has been sent.
     */
    RPL_NAMREPLY(353),

    /**
     * The channel doesn't exist.
     */
    ERR_NOSUCHCHANNEL(403),

    /**
     * Your nickname is in use.
     */
    ERR_NICKNAMEINUSE(433);


    private final int replyCode;
    private final static NumericReply[] REPLIES = NumericReply.values();

    NumericReply(int replyCode)
    {
        this.replyCode = replyCode;
    }

    public static NumericReply getNumericReply(int replyCode)
    {
        for (NumericReply reply : REPLIES)
        {
            if (reply.replyCode == replyCode)
            {
                return reply;
            }
        }

        return NOT_IMPLEMENTED;
    }
}
