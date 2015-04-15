package se.liu.ida.toblu302.tddd78.library;

/**
 * Represents a thing that happened on IRC.
 *
 * @see IRCEventType
 */
public class IRCEvent
{
    private IRCEventType eventType;
    private String argument;

    public IRCEvent(final IRCEventType eventType)
    {
        this.eventType = eventType;
        this.argument = null;
    }

    public IRCEvent(final IRCEventType eventType, final String argument)
    {
        this.eventType = eventType;
        this.argument = argument;
    }

    public IRCEventType getEventType()
    {
        return eventType;
    }

    public String getArgument()
    {
        return argument;
    }
}
