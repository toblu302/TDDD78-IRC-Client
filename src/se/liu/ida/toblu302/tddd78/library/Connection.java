package se.liu.ida.toblu302.tddd78.library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Represents a connection to a server, which are read/written to using BufferedWriter and BufferedReader.
 */
public class Connection
{
    private String server = null;
    private Socket socket = null;

    private BufferedWriter writer = null;
    private BufferedReader reader = null;

    public Connection(String server, int port)
    {
        this.server = server;
        try
        {
            this.socket = new Socket(server, port);

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            System.out.println("Unknown server.");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String read()
    {
        String line = "";
        try
        {
            line = reader.readLine();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return line;
    }

    public void write(String message)
    {
        try
        {
            writer.write(message);
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            socket.shutdownInput();
            socket.shutdownOutput();
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
