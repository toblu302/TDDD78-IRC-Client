package se.liu.ida.toblu302.tddd78.library;

/**
 * This enum represents varios different things which can happen on IRC.
 */
public enum IRCEventType
{
    /**
     * You joined a channel.
     */
    JOINEDCHANNEL,

    /**
     * You changed the currently selected channel.
     */
    CHANGEDCHANNEL,

    /**
     * You left a channel.
     */
    LEFTCHANNEL,

    /**
     * A new message.
     */
    NEWMESSAGE,

    /**
     * A new private dialog with a different user have been entered.
     */
    NEWQUERY,

    /**
     * A new private message.
     */
    NEWQUERYMESSAGE,

    /**
     * A nickname of a user have been changed.
     */
    CHANGEDNAME
}
