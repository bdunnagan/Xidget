/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.ILayoutFeature.Side;

/**
 * An IComputeNode that braces one dimension of a widget based on its default bounds.
 * The node only has a value if the dimension of the default bounds is non-negative,
 * and one side of the widget is defined.
 */
public class InternalBrace extends ComputeNode
{
  /**
   * Create an internal brace for the specified xidget. 
   * @param name The name.
   * @param node The defined side of the xidget.
   * @param xidget The xidget.
   * @param side The undefined side of the xidget.
   */
  public InternalBrace( String name, IXidget xidget, IComputeNode node, Side side)
  {
    this.name = name;
    this.xidget = xidget;
    this.side = side;
    addDependency( node);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#setValue(float)
   */
  @Override
  public void setValue( float value)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getDefaultBounds();
    switch( side)
    {
      case top:    super.setValue( value - bounds.height); break;
      case bottom: super.setValue( value + bounds.height); break;
      case left:   super.setValue( value - bounds.width); break;
      case right:  super.setValue( value + bounds.width); break;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%s[%d] = %s <- %s, %s", name, getID(), printValue(), printDependencies(), xidget);
  }
  
  private String name;
  private IXidget xidget;
  private Side side;
}
