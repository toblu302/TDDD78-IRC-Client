package se.liu.ida.toblu302.tddd78.library;

public abstract class IRCEvent
{
    private SimpleEventType simpleEventType = null;
    private ComplexEventType complexEventType = null;

    public IRCEvent(final SimpleEventType simpleEventType)
    {
	this.simpleEventType = simpleEventType;
    }

    public IRCEvent(final ComplexEventType complexEventType)
    {
	this.complexEventType = complexEventType;
    }


}
