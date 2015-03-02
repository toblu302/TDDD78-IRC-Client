package se.liu.ida.toblu302.tddd78.library;

public final class IRCTest
{
    private IRCTest(){}

    public static void main(String[] args)
    {
	IRCConnection irc = new IRCConnection("irc.rizon.net", 6667, "toblu302", "Tobias Lundberg" );
	irc.joinChannel("#sdfff");
	irc.selectChannel("#sdfff");
	irc.talk("TEST TEST TEST TEST");
    }
}