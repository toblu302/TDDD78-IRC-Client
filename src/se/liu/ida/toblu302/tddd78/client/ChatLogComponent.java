package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import java.awt.*;

public class ChatLogComponent extends JPanel
{
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane;

    private final static int WIDTH = 640;
    private final static int HEIGHT = 720;


    public ChatLogComponent()
    {
	Dimension dimension = new Dimension(WIDTH+10, HEIGHT+10);
	this.setPreferredSize(dimension);

	textArea.setLineWrap(true);
	textArea.setEditable(false);
	textArea.setVisible(true);

	scrollPane = new JScrollPane(textArea);

	Dimension scrollDimension = new Dimension(WIDTH+10, HEIGHT+10);
	scrollPane.setPreferredSize(scrollDimension);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	this.add(scrollPane);
    }

    public void setText(String str)
    {
	textArea.setText(str);
	JScrollBar vertical = scrollPane.getVerticalScrollBar();
	vertical.setValue(vertical.getMaximum() );
    }
}
