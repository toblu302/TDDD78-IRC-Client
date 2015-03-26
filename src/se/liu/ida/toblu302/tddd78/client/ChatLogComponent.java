package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;

public class ChatLogComponent extends JPanel
{
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane;

    public ChatLogComponent()
    {
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 1;

	this.setLayout(new GridBagLayout());
	textArea.setLineWrap(true);
	textArea.setEditable(false);
	textArea.setVisible(true);

	scrollPane = new JScrollPane(textArea);

	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	this.add(scrollPane, c);
    }

    public void setText(String str)
    {
	textArea.setText(str);
	JScrollBar vertical = scrollPane.getVerticalScrollBar();
	vertical.setValue(vertical.getMaximum() );
    }
}
