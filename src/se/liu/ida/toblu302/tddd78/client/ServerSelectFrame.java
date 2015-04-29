package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;
import java.net.UnknownHostException;

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

        JButton save = new JButton("Save!");
        save.addActionListener(e -> this.onSave());

        this.add(new JLabel("Server: "));
        this.add(server);

        this.add(new JLabel("Port: "));
        this.add(port);

        this.add(new JLabel("Real name: "));
        this.add(realName);

        this.add(new JLabel("Username: "));
        this.add(username);

        this.add(connect);
        this.add(save);

        this.pack();
        this.setVisible(true);

        this.loadSettings();
    }

    private void onConnect()
    {
        try
        {
            int portNumber = Integer.parseInt(port.getText());

            //No need to use the window after it has been created.
            IRCFrame ircWindow = new IRCFrame(server.getText(), portNumber, username.getText(), realName.getText());
        }
        catch( NumberFormatException e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid port number.");
            return;
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid server, try again.");
            return;
        }

        this.dispose();
    }

    private void onSave()
    {
        SettingsManager.getInstance().setServer( server.getText() );
        SettingsManager.getInstance().setPort( port.getText() );
        SettingsManager.getInstance().setRealname( realName.getText() );
        SettingsManager.getInstance().setUsername( username.getText() );
    }

    private void loadSettings()
    {
        this.server.setText( SettingsManager.getInstance().getServer() );
        this.port.setText( SettingsManager.getInstance().getPort() );
        this.realName.setText(SettingsManager.getInstance().getRealname());
        this.username.setText( SettingsManager.getInstance().getUsername() );
    }

    public static void main(String[] args)
    {
        //No need to use the window after this point
        ServerSelectFrame window = new ServerSelectFrame();
    }
}


