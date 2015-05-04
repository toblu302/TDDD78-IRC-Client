package se.liu.ida.toblu302.tddd78.client.components;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;


/**
 * A component which lets you select a channel in a tree.
 * Is a JTree inside of a JPanel. Uses GridBagLayout for scalability.
 *
 * This technically lets you select Queries as well, since they'll appear in the same list.
 */
public class ChannelSelectComponent extends JPanel
{
    private JTree channelSelector = null;
    private DefaultMutableTreeNode root = null;

    public ChannelSelectComponent(TreeSelectionListener selectionListener)
    {
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 1;

        this.setLayout(new GridBagLayout());

        root = new DefaultMutableTreeNode("IRC");

        channelSelector = new JTree(root);

        channelSelector.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        channelSelector.addTreeSelectionListener(selectionListener);

        this.add(channelSelector, gridConstraints);
    }

    public DefaultMutableTreeNode selectedNode()
    {
        return (DefaultMutableTreeNode)channelSelector.getLastSelectedPathComponent();
    }

    private boolean channelExists(String channel)
    {
        for (int i = 0; i < root.getChildCount(); ++i)
        {
            if (root.getChildAt(i).toString().equals(channel))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isRootSelected()
    {
        DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode) channelSelector.getModel().getRoot());
        if (this.selectedNode() != null)
        {
            return this.selectedNode().equals(firstLeaf);
        }
        return false;
    }

    public void newChannel(String str)
    {
        if (!channelExists(str))
        {
            root.add(new DefaultMutableTreeNode(str));
            DefaultTreeModel model = (DefaultTreeModel) channelSelector.getModel();
            model.reload(root);
        }
    }

    public void removeChannel(String str)
    {
        DefaultTreeModel model = (DefaultTreeModel) channelSelector.getModel();
        for (int i = 0; i < root.getChildCount(); ++i)
        {
            if (root.getChildAt(i).toString().equals(str))
            {
                root.remove(i);
                break;
            }
        }
        model.reload(root);
    }

    //I don't use this method yet,
    // but it could be useful if I were to add a functionality which leaves all the channels.
    public void removeAllChannels()
    {
        DefaultTreeModel model = (DefaultTreeModel) channelSelector.getModel();
        root.removeAllChildren();
        model.reload(root);
    }

}
