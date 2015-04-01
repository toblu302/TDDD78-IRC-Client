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

    public static int getNumericCode(String msg)
    {
	int returnNumber = -1;

	String[] parts = msg.split(" ");
	if(parts.length > 1 && parts[1].length()==3)
	{
	    returnNumber = Integer.parseInt(parts[1]);
	}

	return returnNumber;
    }

    public static MessageType getMessageType(String message)
    {
	if( message.contains("PRIVMSG #") )
	{
	    return MessageType.CHANNEL;
	}

	if( message.contains(" NICK :") )
	{
	    return MessageType.NAMECHANGE;
	}

	else if( message.contains("PRIVMSG ") )
	{
	    return MessageType.PRIVATE;
	}

    	else if( message.startsWith("PING :irc."))
	{
	    return MessageType.PING;
	}

	else
	{
	    String[] parts = message.split(" ");
	    if(parts.length > 1 && parts[1].length()==3)
	    {
		if(Character.isDigit(parts[1].charAt(0))
		   && Character.isDigit(parts[1].charAt(1))
		   && Character.isDigit(parts[1].charAt(2)))
		{
		    return MessageType.NUMERIC;
		}
	    }
	}

	return MessageType.OTHER;
    }
}
