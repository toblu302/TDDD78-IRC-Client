package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConnectedUsersComponent extends JPanel
{
    private JList list;
    private DefaultListModel listModel;
    private JScrollPane scrollPane;

    public ConnectedUsersComponent()
    {
	this.setLayout(new GridBagLayout());

	listModel = new DefaultListModel();
	list = new JList(listModel);

	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 1;
	this.setLayout(new GridBagLayout());

	scrollPane = new JScrollPane(list);

	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	this.add(scrollPane, c);

	this.addUser("poop");
	removeAllUsers();
    }

    public void addMultipleUsers(ArrayList<String> names)
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
	if(listModel.indexOf(name) == -1)
	{
	    listModel.addElement(name);
	}
    }

    public void removeUser(String name)
    {
	int index = listModel.indexOf(name);
	if(index != -1)
	{
	    listModel.removeElementAt(index);
	}
    }
}
