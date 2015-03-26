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
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 1;
	c.weighty = 1;

	this.setLayout(new GridBagLayout());

	root = new DefaultMutableTreeNode("IRC");

	channelSelector = new JTree(root);

	channelSelector.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	channelSelector.addTreeSelectionListener(selectionListener);


	this.add(channelSelector, c);
    }

    public DefaultMutableTreeNode selectedNode()
    {
	return (DefaultMutableTreeNode)channelSelector.getLastSelectedPathComponent();
    }

    private boolean channelExists(String channel)
    {
	for(int i = 0; i < root.getChildCount(); ++i)
	{
	    if(root.getChildAt(i).toString().equals(channel))
	    {
		return true;
	    }
	}

	return false;
    }

    public boolean isRootSelected()
    {
	DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode)channelSelector.getModel().getRoot());
	if(this.selectedNode() != null)
	{
	    return this.selectedNode().equals(firstLeaf);
	}
	return false;
    }

    public void newChannel(String str)
    {
	if(!channelExists(str))
	{
	    root.add(new DefaultMutableTreeNode(str));
	    DefaultTreeModel model = (DefaultTreeModel) channelSelector.getModel();
	    model.reload(root);
	}
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

    public void removeAllChannels()
    {
	DefaultTreeModel model = (DefaultTreeModel)channelSelector.getModel();
	root.removeAllChildren();
	model.reload(root);
    }

}
