package se.liu.ida.toblu302.tddd78.library;

public class ComplexEvent extends IRCEvent
{
    private ComplexEventType eventType;
    private String argument;

    public ComplexEvent(ComplexEventType eventType, String argument)
    {
	this.argument = argument;
	this.eventType = eventType;
    }

    public ComplexEventType getEventType()
    {
	return this.eventType;
    }

    public String getArgument()
    {
	return this.argument;
    }
}
