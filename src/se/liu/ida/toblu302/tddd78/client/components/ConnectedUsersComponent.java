package se.liu.ida.toblu302.tddd78.client.components;

import javax.swing.*;
import java.awt.*;

/**
 * A JComponent which keeps a list of all the users in a channel.
 * Is a JTree inside of a JPanel. Uses GridBagLayout for scalability.
 */
public class ConnectedUsersComponent extends JPanel
{
    private DefaultListModel<String> listModel;

    public ConnectedUsersComponent()
    {
        this.setLayout(new GridBagLayout());

        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 1;
        this.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scrollPane, gridConstraints);

        this.addUser("poop");
        removeAllUsers();
    }

    public void addMultipleUsers(Iterable<String> names)
    {
        for (String name : names)
        {
            this.addUser(name);
        }
    }

    public void removeAllUsers()
    {
        listModel.removeAllElements();
    }

    public void addUser(String name)
    {
        if (listModel.indexOf(name) == -1)
        {
            listModel.addElement(name);
        }
    }

    public void removeUser(String name)
    {
        int index = listModel.indexOf(name);
        if (index != -1)
        {
            listModel.removeElementAt(index);
        }
    }
}
