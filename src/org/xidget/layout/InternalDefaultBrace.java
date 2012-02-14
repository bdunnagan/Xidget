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
public class InternalDefaultBrace extends ComputeNode
{
  /**
   * Create an internal brace for the specified xidget. 
   * @param name The name.
   * @param xidget The xidget.
   * @param node The defined side of the xidget.
   * @param side1 The defined side of the xidget.
   * @param side2 The undefined side of the xidget.
   */
  public InternalDefaultBrace( String name, IXidget xidget, IComputeNode node, Side side1, Side side2)
  {
    this.name = name;
    this.xidget = xidget;
    this.side1 = side1;
    this.side2 = side2;
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
    switch( side2)
    {
      case top:     
      {
        if ( bounds.height >= 0) 
        {
          if ( side1 == Side.bottom) super.setValue( value - bounds.height); 
          if ( side1 == Side.vcenter) super.setValue( value - bounds.height / 2);
        }
        
        break;
      }
      
      case bottom:
      {
        if ( bounds.height >= 0) 
        {
          if ( side1 == Side.top) super.setValue( value + bounds.height); 
          if ( side1 == Side.vcenter) super.setValue( value + bounds.height / 2);
        }
        
        break;
      }
      
      case left:    
      {
        if ( bounds.width >= 0) 
        {
          if ( side1 == Side.right) super.setValue( value - bounds.width); 
          if ( side1 == Side.hcenter) super.setValue( value - bounds.width / 2);
        }
        
        break;
      }
        
      case right:
      {
        if ( bounds.width >= 0) 
        {
          if ( side1 == Side.left) super.setValue( value + bounds.width); 
          if ( side1 == Side.hcenter) super.setValue( value + bounds.width / 2);
        }
        
        break;
      }
      
      case hcenter: 
      {
        if ( bounds.width >= 0) 
        {
          if ( side1 == Side.left) super.setValue( value - bounds.width / 2);
          if ( side1 == Side.right) super.setValue( value + bounds.width / 2);
        }
        break;
      }
      
      case vcenter: 
      {
        if ( bounds.height >= 0) 
        {
          if ( side1 == Side.top) super.setValue( value - bounds.height / 2);
          if ( side1 == Side.bottom) super.setValue( value + bounds.height / 2);
        }
        break;
      }
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
  private Side side1;
  private Side side2;
}
