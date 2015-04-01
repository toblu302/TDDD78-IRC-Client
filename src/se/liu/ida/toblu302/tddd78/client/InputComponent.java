package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Lets the user enter some input, which is then sent to the InputListeners in listeners.
 * @see InputListener
 */
public class InputComponent extends JTextField
{
    private Collection<InputListener> listeners = new ArrayList<>();

    private JTextField inputBox = this;

    public InputComponent()
    {
	ActionListener finishedInput = new AbstractAction()
    	{
    	    @Override public void actionPerformed(ActionEvent e)
	    {
		notifyListeners( inputBox.getText() );
		inputBox.setText("");
	    }
    	};

	this.addActionListener(finishedInput);
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