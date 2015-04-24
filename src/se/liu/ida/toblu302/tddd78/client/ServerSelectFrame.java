package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;
import java.net.UnknownHostException;

/**
 * A JFrame for an IRC user to enter a server, portnumer, username, and a real name.
 */

public class ServerSelectFrame extends JFrame
{
    private JTextField server = new JTextField("irc.rizon.net");
    private JTextField port = new JTextField("6667");
    private JTextField username = new JTextField("tobleu");
    private JTextField realName = new JTextField("Sod Jed");

    final static int WIDTH = 240;
    final static int HEIGHT = 160;

    public ServerSelectFrame()
    {
        super("IRC!");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(0, 2, 10, 10));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JButton connect = new JButton("Connect!");
        connect.addActionListener(e -> this.onConnect());

        this.add(new JLabel("Server: "));
        this.add(server);

        this.add(new JLabel("Port: "));
        this.add(port);

        this.add(new JLabel("Real name: "));
        this.add(realName);

        this.add(new JLabel("Username: "));
        this.add(username);

        this.add(connect);

        this.pack();
        this.setVisible(true);
    }

    private void onConnect()
    {
        int portNumber;
        try
        {
            portNumber = Integer.parseInt(port.getText());
        }
        catch( NumberFormatException e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid port number.");
            return;
        }

        try
        {
            //No need to use the window after it has been created.
            IRCFrame ircWindow = new IRCFrame(server.getText(), portNumber, username.getText(), realName.getText());
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid server, try again.");
            return;
        }
        this.dispose();
    }

    public static void main(String[] args)
    {
        //No need to use the window after this point
        ServerSelectFrame window = new ServerSelectFrame();
    }
}


