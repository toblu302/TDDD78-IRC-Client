package se.liu.ida.toblu302.tddd78.client;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;

public class ChannelSelectComponent extends JPanel
{
    private JTree channelSelector = null;
    private DefaultMutableTreeNode root = null;

    public ChannelSelectComponent(TreeSelectionListener selectionListener)
    {
	Dimension dimension = new Dimension(100,640);
	this.setPreferredSize(dimension);

	root = new DefaultMutableTreeNode("IRC");

	channelSelector = new JTree(root);

	channelSelector.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	channelSelector.addTreeSelectionListener(selectionListener);


	this.add(channelSelector);
    }

    public DefaultMutableTreeNode selectedNode()
    {
	return (DefaultMutableTreeNode)channelSelector.getLastSelectedPathComponent();
    }

    public void newChannel(String str)
    {
	root.add(new DefaultMutableTreeNode(str));
	DefaultTreeModel model = (DefaultTreeModel)channelSelector.getModel();
	model.reload(root);
    }

    public void removeChannel(String str)
    {
	DefaultTreeModel model = (DefaultTreeModel)channelSelector.getModel();
	for(int i = 0; i < root.getChildCount(); ++i)
	{
	    if(root.getChildAt(i).toString().equals(str))
	    {
		root.remove(i);
		break;
	    }
	}
	model.reload(root);
    }

    public void removeAll()
    {
	DefaultTreeModel model = (DefaultTreeModel)channelSelector.getModel();
	root.removeAllChildren();
	model.reload(root);
    }

}
