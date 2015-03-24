package se.liu.ida.toblu302.tddd78.library;

public enum NumericReply
{
    NOT_IMPLEMENTED(-1), RPL_NOTOPIC(331), RPL_TOPIC(332), RPL_NAMREPLY(353), RPL_ENDOFNAMES(366);

    private final int ID;
    private static NumericReply[] replies = NumericReply.values();

    private NumericReply(int ID)
    {
	this.ID = ID;
    }

    public static NumericReply getNumericReply(int ID)
    {
	for (NumericReply reply : replies)
	{
	    if(reply.ID == ID)
	    {
		return reply;
	    }
	}

	return NOT_IMPLEMENTED;
    }
}
