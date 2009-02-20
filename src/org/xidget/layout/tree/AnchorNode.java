/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.tree;

import java.util.List;
import org.xidget.layout.IAnchor;
import org.xidget.layout.V2D;

/**
 * A node in an anchor tree.
 */
public final class AnchorNode
{  
  public IAnchor anchor;
  public V2D distance;
  public AnchorNode parent;
  public List<AnchorNode> children;
}
