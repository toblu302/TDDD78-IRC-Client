package se.liu.ida.toblu302.tddd78.library;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Maintains a log of IRC messages.
 */
public class IRCLog
{
    private Collection<String> log = new ArrayList<>();

    public void add(String msg)
    {
        if(msg != null)
        {
            log.add(msg);
        }
    }

    @Override public String toString()
    {
        StringBuilder textLog = new StringBuilder();
        for (String o : log)
        {
            textLog.append(o).append("\n");
        }
        return textLog.toString();
    }
}