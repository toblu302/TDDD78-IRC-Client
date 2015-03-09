package se.liu.ida.toblu302.tddd78.library;

public final class Message
{
    private Message(){}

    public static String getUserString(String msg)
    {
	int end = msg.indexOf('!');
	return msg.substring(1, end);
    }

    public static String getChannelString(String msg)
    {
	int start = msg.indexOf('#');
	int end = msg.indexOf(' ', start);
	return msg.substring(start, end);
    }

    public static String getMessageString(String msg)
    {
	int privMsgStart = msg.indexOf("PRIVMSG ");
	int start = msg.indexOf(" ", privMsgStart)+1;
	start = msg.indexOf(" ", start)+1;
	return msg.substring(start+1, msg.length());
    }

    public static MessageType getMessageType(String message)
    {
	if( message.contains("PRIVMSG #") )
	{
	    return MessageType.CHANNEL;
	}

	else if( message.contains("PRIVMSG ") )
	{
	    return MessageType.PRIVATE;
	}

	else if( message.substring(0, 10).equals("PING :irc.") )
	{
	    return MessageType.PING;
	}

	return MessageType.OTHER;
    }
}
