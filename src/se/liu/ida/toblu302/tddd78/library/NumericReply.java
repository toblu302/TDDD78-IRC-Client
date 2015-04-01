package se.liu.ida.toblu302.tddd78.library;

public enum NumericReply
{
    NOT_IMPLEMENTED(-1), RPL_MYINFO(004), RPL_NOTOPIC(331), RPL_TOPIC(332), RPL_NAMREPLY(353), ERR_NOSUCHCHANNEL(403), ERR_NICKNAMEINUSE(433);


    private final int replyCode;
    private final static NumericReply[] REPLIES = NumericReply.values();

    private NumericReply(int replyCode)
    {
	this.replyCode = replyCode;
    }

    public static NumericReply getNumericReply(int replyCode)
    {
	for (NumericReply reply : REPLIES)
	{
	    if(reply.replyCode == replyCode)
	    {
		return reply;
	    }
	}

	return NOT_IMPLEMENTED;
    }
}
