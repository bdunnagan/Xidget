/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xidget.ifeature.IWidgetFeature;

/**
 * An IComputeNode that braces one dimension of a widget based on its default bounds.
 * The node only has a value if the dimension of the default bounds is defined and the specified
 * side of the xidget is constrained.
 */
public class InternalDimensionBrace extends ComputeNode
{
  /**
   * Create an internal brace for the specified xidget. 
   * @param name The name.
   * @param xidget The xidget.
   * @param node The defined side of the xidget.
   * @param side1 The defined side of the xidget.
   * @param side2 The undefined side of the xidget.
   */
  public InternalDimensionBrace( String name, IXidget xidget, IComputeNode node, Side side1, Side side2)
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
    Bounds bounds = widgetFeature.getComputedBounds();
    
    switch( side2)
    {
      case top:     
      {
        if ( !bounds.isHeightDefined()) bounds = layoutHeight();
        if ( side1 == Side.bottom) super.setValue( value - bounds.height); 
        if ( side1 == Side.vcenter) super.setValue( value - bounds.height / 2);
        break;
      }
      
      case bottom:
      {
        if ( !bounds.isHeightDefined()) bounds = layoutHeight();
        if ( side1 == Side.top) super.setValue( value + bounds.height); 
        if ( side1 == Side.vcenter) super.setValue( value + bounds.height / 2);
        break;
      }
      
      case left:    
      {
        if ( !bounds.isWidthDefined()) bounds = layoutWidth();
        if ( side1 == Side.right) super.setValue( value - bounds.width); 
        if ( side1 == Side.hcenter) super.setValue( value - bounds.width / 2);
        break;
      }
        
      case right:
      {
        if ( !bounds.isWidthDefined()) bounds = layoutWidth();
        if ( side1 == Side.left) super.setValue( value + bounds.width); 
        if ( side1 == Side.hcenter) super.setValue( value + bounds.width / 2);
        break;
      }
      
      case hcenter: 
      {
        if ( !bounds.isWidthDefined()) bounds = layoutWidth();
        if ( side1 == Side.left) super.setValue( value - bounds.width / 2);
        if ( side1 == Side.right) super.setValue( value + bounds.width / 2);
        break;
      }
      
      case vcenter: 
      {
        if ( !bounds.isHeightDefined()) bounds = layoutHeight();
        if ( side1 == Side.top) super.setValue( value - bounds.height / 2);
        if ( side1 == Side.bottom) super.setValue( value + bounds.height / 2);
        break;
      }
    }
  }
  
  /**
   * Layout the specified dimension of the xidget.
   * @return Returns the computed bounds after layout.
   */
  private Bounds layoutWidth()
  {
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    layoutFeature.layout();
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();
    if ( !bounds.isWidthDefined()) throw new LayoutException( String.format( "%s: Width is not constrained!", xidget));
    
    return bounds;
  }
  
  /**
   * Layout the specified dimension of the xidget.
   * @return Returns the computed bounds after layout.
   */
  private Bounds layoutHeight()
  {
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    layoutFeature.layout();
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();
    if ( !bounds.isHeightDefined()) throw new LayoutException( String.format( "%s: Height is not constrained!", xidget));
    
    return bounds;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    String left = String.format( "%8s[%d] = %6s", name, getID(), printValue());
    return String.format( "%10s | %s", left, printDependencies());
  }
  
  private String name;
  private IXidget xidget;
  private Side side1;
  private Side side2;
}
