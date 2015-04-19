package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a connection to an IRC server.
 */

public class IRCConnection
{
    private Collection<IRCListener> listeners = new ArrayList<>();

    private boolean successfullyConnected = false;

    private Connection connection;
    private String userName;

    private Collection<Channel> channels = new ArrayList<>();
    private Collection<Query> queries = new ArrayList<>();

    private Talkable selectedTalkable = null;

    private Thread loggingThread = null;
    private boolean isLogging = false;

    private IRCLog log = new IRCLog();


    public IRCConnection(final String server, int port, String userName, String realName)
    {
        this.userName = userName;

        this.connection = new Connection(server, port);

        connection.write("NICK " + userName + "\r\n");
        connection.write("USER " + userName + " 0 * : " + realName + "\r\n");

        startLogging();
    }

    public void changeNick(String newNick)
    {
        //the "userName" variable doesn't get updated here, because the new NICK might not be accepted by the server
        connection.write("NICK " + newNick + "\r\n");
    }

    public void joinChannel(String channelName)
    {
        if (!successfullyConnected)
        {
            return;
        }

        Channel channel = new Channel(channelName, this.connection);
        channel.join();
        channels.add(channel);

        notifyListeners(new IRCEvent(IRCEventType.JOINEDCHANNEL, channelName));
    }

    private boolean inQuery(String userName)
    {
        Query q = getQueryFromName(userName);
        return (q != null);
    }

    public void joinQuery(String userName)
    {
        if (!successfullyConnected)
        {
            return;
        }

        Query q = new Query(userName, this.connection);
        queries.add(q);

        notifyListeners(new IRCEvent(IRCEventType.NEWQUERY, userName));
    }

    public void leaveChannel(String name)
    {
        Channel channel = this.getChannelFromName(name);

        if(channel == null)
        {
            return;
        }

        channel.leave();
        channels.remove(channel);

        notifyListeners(new IRCEvent(IRCEventType.LEFTCHANNEL, name));
    }

    public void selectTalkable(String talkableName)
    {
        Talkable t = getTalkableFromName(talkableName);

        if (t != null)
        {
            this.selectedTalkable = t;

            notifyListeners(new IRCEvent(IRCEventType.CHANGEDCHANNEL));
        }
    }

    public void selectRoot()
    {
        selectedTalkable = null;
        notifyListeners(new IRCEvent(IRCEventType.CHANGEDCHANNEL));
    }

    public void talk(String msg)
    {
        if (!successfullyConnected)
        {
            return;
        }

        if (this.selectedTalkable != null)
        {
            this.selectedTalkable.talk(msg);
            selectedTalkable.addLog(this.userName, msg);

            notifyListeners(new IRCEvent(IRCEventType.NEWMESSAGE));
        }
    }

    public String getLog()
    {
        if (selectedTalkable != null)
        {
            return selectedTalkable.getLog();
        }

        return log.toString();
    }

    public Iterable<String> getChannelUsers()
    {
        if (selectedTalkable == null)
        {
            return null;
        }

        // If the currently selected talkable exists in channel,
        // we can safely convert it to a Channel-object and use its getCurrentUsers()-method.
        // "selectedTalkable" can exist in "queries" as well as "channels", this code check which it is in.
        if( channels.contains(selectedTalkable) )
        {
            Channel channel = (Channel)selectedTalkable;
            return channel.getCurrentUsers();
        }

        return null;
    }

    public String getChannelTopic()
    {
        if (selectedTalkable == null)
        {
            return null;
        }
        if( channels.contains(selectedTalkable) )
        {
            Channel channel = (Channel)selectedTalkable;
            return channel.getTopic();
        }

        return null;
    }

    public void quitConnection()
    {
        isLogging = false;
        loggingThread.interrupt();

        connection.write("QUIT");
        connection.close();
    }

    private void handleMessage(String message)
    {
        log.add(message);

        Channel channel;

        String user = Message.getUserString(message);
        String channelName = Message.getChannelString(message);
        String userMessage = Message.getMessageString(message);

        switch (Message.getMessageType(message))
        {
            case CHANNELMESSAGE:
                channel = getChannelFromName(channelName);
                channel.addLog(user, userMessage);
                break;

            case USERJOINED:
                channel = getChannelFromName(userMessage);

                // We're going to be properly added (with the right mode) from a RPL_NAMEREPLY,
                // so ignore our own username if it shows up here
                if(!user.equals(this.userName))
                {
                    channel.addUser(user, ' ');
                }

                notifyListeners(new IRCEvent(IRCEventType.NEWUSER));
                break;

            case QUIT:
                for (Channel chan : channels)
                {
                    chan.removeUser(user);
                }
                notifyListeners(new IRCEvent(IRCEventType.USERQUIT, user));
                break;

            case PRIVATEMESSAGE:
                if (!inQuery(user))
                {
                    joinQuery(user);
                }
                Query q = getQueryFromName(user);
                q.addLog(user, userMessage);
                notifyListeners(new IRCEvent(IRCEventType.NEWQUERYMESSAGE));
                break;

            case NAMECHANGE:

                if (user.equals(this.userName))
                {
                    this.userName = userMessage;
                }

                for (Channel chan : channels)
                {
                    chan.changeUserName(user, userMessage);
                }
                notifyListeners(new IRCEvent(IRCEventType.CHANGEDNAME));
                break;

            case PING:
                connection.write("PONG");
                break;

            case NUMERIC:
                handleNumeric(Message.getNumericCode(message), message);
                break;

            case OTHER:
                break;

            default:
                break;
        }
        notifyListeners(new IRCEvent(IRCEventType.NEWMESSAGE));
    }

    private void handleNumeric(int numericCode, String message)
    {
        Channel channel;

        String user = Message.getUserString(message);
        String channelName = Message.getChannelString(message);
        String userMessage = Message.getMessageString(message);

        switch (NumericReply.getNumericReply(numericCode))
        {
            case RPL_MYINFO:
                successfullyConnected = true;
                break;

            case RPL_NOTOPIC:
                channel = this.getChannelFromName(channelName);
                channel.setTopic(null);
                break;

            case RPL_TOPIC:
                channel = this.getChannelFromName(channelName);
                channel.setTopic(userMessage);
                break;

            case ERR_NOSUCHCHANNEL:
                //invalid channel
                break;

            case ERR_NICKNAMEINUSE:
                //add a _ to the end of our userName if it is already taken
                this.userName += "_";
                this.changeNick(this.userName);
                break;

            case RPL_NAMREPLY:
                Channel t = this.getChannelFromName(channelName);

                int start = message.indexOf(channelName);
                String[] parts = message.substring(start + channelName.length() + 2).split(" ");
                for (String name : parts)
                {
                    if (name.charAt(0) == '+' || name.charAt(0) == '-' || name.charAt(0) == '@' || name.charAt(0) == '%')
                    {
                        t.addUser(name.substring(1), name.charAt(0));
                    } else
                    {
                        t.addUser(name, ' ');
                    }
                }
                break;

            case NOT_IMPLEMENTED:
                break;

            default:
                break;
        }
    }

    private Talkable getTalkableFromName(String talkableName)
    {
        Talkable t = getChannelFromName(talkableName);
        if( t == null)
        {
            t = getQueryFromName(talkableName);
        }

        return t;
    }

    private Query getQueryFromName(String queryName)
    {
        Query t = null;
        for (Query query : queries)
        {
            if (query.getName().equals(queryName))
            {
                t = query;
            }
        }

        return t;
    }

    private Channel getChannelFromName(String channelName)
    {
        Channel t = null;
        for (Channel chan : channels)
        {
            if (chan.getName().equals(channelName))
            {
                t = chan;
            }
        }

        return t;
    }

    private String read()
    {
        if(isLogging)
        {
            return connection.read();
        }
        return null;
    }

    private void startLogging()
    {
        class IRCThread implements Runnable
        {
            public void run()
            {
                while (!Thread.interrupted())
                {
                    String line = read();

                    if (line != null)
                    {
                        handleMessage(line);
                    }
                }
            }
        }
        loggingThread = new Thread(new IRCThread());
        loggingThread.start();
        isLogging = true;
    }

    public void addListener(IRCListener ircl)
    {
        this.listeners.add(ircl);
    }

    private void notifyListeners(IRCEvent e)
    {
        for (IRCListener listener : listeners)
        {
            listener.onIRCEvent(e);
        }
    }
}
