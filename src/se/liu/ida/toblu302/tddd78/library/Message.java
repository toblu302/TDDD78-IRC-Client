package se.liu.ida.toblu302.tddd78.library;


/**
 * Utility class to interpret the new incoming messages from the IRC server.
 */

public final class Message
{
    private Message()
    {
    }

    public static String getUserString(String msg)
    {
        // the "user" part of a message is from the first ':' (usually first character) to the first "!" or the first space
        // Exampel: ":toblu![blahblah]" and ":toblu [blahblah]" both has the userstring "toblu"

        if (msg.charAt(0) != ':')
        {
            return null;
        }

        int firstExclamation = msg.indexOf('!');
        int firstSpace = msg.indexOf(' ');

        // set the "end" of the user string to the first "!"/space, depending on which comes first
        int end = Math.min(firstExclamation, firstSpace);

        // indexOf returns -1 if the char does not exist, so we need fix that by getting the other value
        if (end == -1)
        {
            end = Math.max(firstExclamation, firstSpace);
        }

        // if end still equals -1, neither a ! or a space exists, so we return null
        if (end == -1)
        {
            return null;
        }

        return msg.substring(1, end);
    }

    public static String getChannelString(String msg)
    {
        if (!msg.contains("#"))
        {
            return null;
        }

        int start = msg.indexOf('#');
        int end = msg.indexOf(' ', start);
        if (end < start)
        {
            end = msg.length();
        }
        return msg.substring(start, end);
    }

    public static String getMessageString(String msg)
    {
        //the "message" of a string is from the second : and forward (the first ":" is the very first character)
        int secondColon = msg.indexOf(':', 1);
        if (secondColon == -1)
        {
            return null;
        }
        return msg.substring(secondColon + 1, msg.length());
    }

    public static int getNumericCode(String msg)
    {
        int returnNumber = -1;

        String[] parts = msg.split(" ");
        if (parts.length > 1 && parts[1].length() == 3)
        {
            returnNumber = Integer.parseInt(parts[1]);
        }

        return returnNumber;
    }

    public static MessageType getMessageType(String message)
    {
        if (message.contains("PRIVMSG #"))
        {
            return MessageType.CHANNELMESSAGE;
        }

        else if (message.contains("PRIVMSG "))
        {
            return MessageType.PRIVATEMESSAGE;
        }

        else if (message.contains(" NICK :"))
        {
            return MessageType.NICK;
        }

        else if (message.contains(" JOIN :"))
        {
            return MessageType.JOIN;
        }

        else if (message.contains(" TOPIC #"))
        {
            return MessageType.TOPIC;
        }

        else if (message.contains(" PART #"))
        {
            return MessageType.PART;
        }

        else if (message.contains(" QUIT "))
        {
            return MessageType.QUIT;
        }

        else if (message.startsWith("PING :irc."))
        {
            return MessageType.PING;
        }

        else
        {
            String[] parts = message.split(" ");
            if (parts.length > 1 && parts[1].length() == 3)
            {
                if (Character.isDigit(parts[1].charAt(0))
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
