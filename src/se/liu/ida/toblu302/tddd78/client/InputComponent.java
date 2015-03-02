package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class InputComponent extends JPanel
{
    private Collection<InputListener> listeners = new ArrayList<>();

    private JTextField inputBox = new JTextField();

    private final static int WIDTH = 720;
    private final static int HEIGHT = 20;

    public InputComponent()
    {
	Dimension dimension = new Dimension(WIDTH+10, HEIGHT+10);
	this.setPreferredSize(dimension);

	Dimension inputDimension = new Dimension(WIDTH, HEIGHT);
	inputBox.setPreferredSize(inputDimension);

	this.add(inputBox);

	ActionListener finishedInput = new AbstractAction()
    	{
    	    @Override public void actionPerformed(ActionEvent e)
	    {
		notifyListeners( inputBox.getText() );
		inputBox.setText("");
	    }
    	};

	inputBox.addActionListener(finishedInput);
    }

    public void addListener(InputListener il)
    {
	this.listeners.add(il);
    }

    private void notifyListeners(String str)
    {
	for (InputListener listener : listeners)
	{
	    listener.recievedInput(str);
	}
    }
}