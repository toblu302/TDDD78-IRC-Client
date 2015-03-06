package se.liu.ida.toblu302.tddd78.library;

public class SimpleEvent extends IRCEvent
{
    private SimpleEventType eventType;

    public SimpleEvent(final SimpleEventType eventType)
    {
	super();
	this.eventType = eventType;
    }

    public SimpleEventType getEventType()
    {
	return this.eventType;
    }
}
