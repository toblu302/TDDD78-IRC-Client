package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ServerSelectFrame extends JFrame
{
    private JLabel serverLabel = new JLabel("Server: ");
    private JTextField server = new JTextField();

    private JLabel portLabel = new JLabel("Port: ");
    private JTextField port = new JTextField();

    private JLabel usernameLabel = new JLabel("Username: ");
    private JTextField username = new JTextField();

    private JLabel realNameLabel = new JLabel("Real name: ");
    private JTextField realName = new JTextField();

    private JButton connect = new JButton("Connect!");

    final static int WIDTH = 240;
    final static int HEIGHT = 160;

    public ServerSelectFrame()
    {
	super("IRC!");
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new GridLayout(0,2,10,10));
	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

	connect.addActionListener(e -> this.onConnect());
	this.add(serverLabel);
	this.add(server);

	this.add(portLabel);
	this.add(port);

	this.add(realNameLabel);
	this.add(realName);

	this.add(usernameLabel);
	this.add(username);

	this.add(connect);

	this.pack();
	this.setVisible(true);
    }

    private void onConnect()
    {
	int portNumber = Integer.parseInt(port.getText());
	IRCFrame ircWindow = new IRCFrame(server.getText(), portNumber, username.getText(), realName.getText());
	this.dispose();
    }

    public static void main(String[] args)
    {
	ServerSelectFrame window = new ServerSelectFrame();
    }
}


