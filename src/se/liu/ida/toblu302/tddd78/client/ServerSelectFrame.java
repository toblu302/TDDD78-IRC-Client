package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;

/**
 * A JFrame for an IRC user to enter a server, portnumer, username, and a real name.
 */

public class ServerSelectFrame extends JFrame
{
    private JTextField server = new JTextField();
    private JTextField port = new JTextField();
    private JTextField username = new JTextField();
    private JTextField realName = new JTextField();

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
        int portNumber = Integer.parseInt(port.getText());
        //No need to use the window after it has been created.
        IRCFrame ircWindow = new IRCFrame(server.getText(), portNumber, username.getText(), realName.getText());
        this.dispose();
    }

    public static void main(String[] args)
    {
        //No need to use the window after this point
        ServerSelectFrame window = new ServerSelectFrame();
    }
}


