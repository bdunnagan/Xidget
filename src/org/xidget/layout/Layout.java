/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.ArrayList;
import java.util.List;
import org.xidget.layout.tree.AnchorTree;

/**
 * Layout mediator which detects and stops attachment cycles. The cycle analysis
 * does not prevent the layout from running. Information about cycles is available
 * for debugging purposes.
 */
public class Layout
{
  public Layout()
  {
    attachments = new ArrayList<Attachment>();
    tree = new AnchorTree();
  }
  
  /**
   * Add an attachment.
   * @param attachment The attachment.
   */
  public void addAttachment( Attachment attachment)
  {
    attachments.add( attachment);
    attachment.anchors[ 0].addListener( anchorListener);
    attachment.anchors[ 1].addListener( anchorListener);
  }
  
  /**
   * Remove an attachment.
   * @param attachment The attachment.
   */
  public void removeAttachment( Attachment attachment)
  {
    attachments.remove( attachment);
    attachment.anchors[ 0].removeListener( anchorListener);
    attachment.anchors[ 1].removeListener( anchorListener);
  }
  
  /**
   * Initiate the layout.
   */
  public void layout()
  {
    tree.compute();
  }
    
  private IAnchor.IListener anchorListener = new IAnchor.IListener() {
    public void onMove( IAnchor anchor)
    {
      tree.anchorMoved( anchor);
    }
  };

  private List<Attachment> attachments;
  private AnchorTree tree;
}
