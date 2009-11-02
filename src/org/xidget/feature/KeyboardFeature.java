/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xidget.ifeature.IKeyboardFeature;
import org.xmodel.xaction.IXAction;

/**
 * A base implementation of the IKeyboardFeature. The translation method is left to the platform-specific sub-class.
 */
public abstract class KeyboardFeature implements IKeyboardFeature
{
  public KeyboardFeature()
  {
    root = new TreeNode();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#addKeyPressedBinding(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void addKeyPressedBinding( String keys, IXAction script)
  {
    TreeNode node = getCreateNode( keys);
    node.addPressedAction( script);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#removeKeyPressedBinding(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void removeKeyPressedBinding( String keys, IXAction script)
  {
    TreeNode node = getCreateNode( keys);
    node.removePressedAction( script);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#addKeyReleasedBinding(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void addKeyReleasedBinding( String keys, IXAction script)
  {
    TreeNode node = getCreateNode( keys);
    node.addReleasedAction( script);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#removeKeyReleasedBinding(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void removeKeyReleasedBinding( String keys, IXAction script)
  {
    TreeNode node = getCreateNode( keys);
    node.removeReleasedAction( script);
  }

  /**
   * Returns the node associated with the specified key sequence.
   * @param keys The key configuration sequence.
   * @return Returns the node associated with the specified key sequence.
   */
  private TreeNode getCreateNode( String keys)
  {
    TreeNode node = root;
    int[] codes = translate( keys);
    for( int i=0; i<codes.length; i++)
    {
      TreeNode child = node.getChild( codes[ i]);
      if ( child == null)
      {
        child = new TreeNode();
        node.addChild( codes[ i], child);
      }
      node = child;
    }
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#keyPressed(int)
   */
  public void keyPressed( int code)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#keyReleased(int)
   */
  public void keyReleased( int code)
  {
  }
  
  private class TreeNode
  {
    public TreeNode()
    {
    }

    /**
     * Add a key-pressed action to this node.
     * @param action The action.
     */
    public void addPressedAction( IXAction action)
    {
      if ( pressedActions == null) pressedActions = new ArrayList<IXAction>();
      pressedActions.add( action);
    }
    
    /**
     * Remove a key-pressed action from this node.
     * @param action The action.
     */
    public void removePressedAction( IXAction action)
    {
      if ( pressedActions != null) pressedActions.remove( action);
    }
    
    /**
     * Returns the actions associated with this node when the key is pressed.
     * @return Returns the actions associated with this node when the key is pressed.
     */
    private List<IXAction> getKeyPressedActions()
    {
      if ( pressedActions == null) return Collections.emptyList();
      return pressedActions;
    }
    
    /**
     * Add a key-released action to this node.
     * @param action The action.
     */
    public void addReleasedAction( IXAction action)
    {
      if ( releasedActions == null) releasedActions = new ArrayList<IXAction>();
      releasedActions.add( action);
    }
    
    /**
     * Remove a key-released action from this node.
     * @param action The action.
     */
    public void removeReleasedAction( IXAction action)
    {
      if ( releasedActions != null) releasedActions.remove( action);
    }
    
    /**
     * Returns the actions associated with this node when the key is released.
     * @return Returns the actions associated with this node when the key is released.
     */
    private List<IXAction> getKeyReleasedActions()
    {
      if ( releasedActions == null) return Collections.emptyList();
      return releasedActions;
    }
    
    /**
     * Add a child to the tree.
     * @param code The key-code.
     * @param child The child.
     */
    public void addChild( int code, TreeNode child)
    {
      if ( children == null) children = new HashMap<Integer, TreeNode>();
      children.put( code, child);
    }
    
    /**
     * Remove a child from the tree.
     * @param code The key-code.
     */
    public void removeChild( int code)
    {
      children.remove( code);
    }

    /**
     * Returns the specified child.
     * @param code The key-code.
     * @return Returns null or the specified child.
     */
    public TreeNode getChild( int code)
    {
      if ( children != null) return children.get( code);
      return null;
    }

    private Map<Integer, TreeNode> children;
    private List<IXAction> pressedActions;
    private List<IXAction> releasedActions;
  }
  
  private TreeNode root;
  private TreeNode current;
}
