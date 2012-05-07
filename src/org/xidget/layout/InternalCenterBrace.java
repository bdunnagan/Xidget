/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature.Side;

/**
 * An IComputeNode that braces one edge based on the other edge and the center, or braces the center based on the two edges.
 */
public class InternalCenterBrace extends ComputeNode
{
  /**
   * Create an internal brace for the specified xidget. 
   * @param name The name.
   * @param xidget The xidget.
   * @param node1 The first node that is defined.
   * @param side1 The first side that is defined.
   * @param node2 The other node that is defined.
   * @param side2 The other side that is defined.
   */
  public InternalCenterBrace( String name, IXidget xidget, IComputeNode node1, Side side1, IComputeNode node2, Side side2)
  {
    this.name = name;
    
    this.node1 = node1;
    this.side1 = side1;
    
    this.node2 = node2;
    this.side2 = side2;
    
    addDependency( node1);
    addDependency( node2);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#setValue(float)
   */
  @Override
  public void setValue( float value)
  {
    if ( !node1.hasValue() || !node2.hasValue()) return;
      
    switch( side1)
    {
      case top:
      {
        if ( side2 == Side.vcenter)
        {
          float t = node1.getValue();
          float c = node2.getValue();
          float b = t + (c - t) * 2;
          super.setValue( b);
        }
        
        if ( side2 == Side.bottom)
        {
          float t = node1.getValue();
          float b = node2.getValue();
          float c = t + (b - t) / 2;
          super.setValue( c);
        }
        
        break;
      }
      
      case bottom:
      {
        if ( side2 == Side.vcenter)
        {
          float b = node1.getValue();
          float c = node2.getValue();
          float t = b - (b - c) * 2;
          super.setValue( t);
        }
        
        if ( side2 == Side.top)
        {
          float b = node1.getValue();
          float t = node2.getValue();
          float c = t + (b - t) / 2;
          super.setValue( c);
        }
        
        break;
      }
      
      case left:
      {
        if ( side2 == Side.hcenter)
        {
          float l = node1.getValue();
          float c = node2.getValue();
          float r = l + (c - l) * 2;
          super.setValue( r);
        }
        
        if ( side2 == Side.right)
        {
          float l = node1.getValue();
          float r = node2.getValue();
          float c = l + (r - l) / 2;
          super.setValue( c);
        }
        
        break;
      }
      
      case right:
      {
        if ( side2 == Side.hcenter)
        {
          float r = node1.getValue();
          float c = node2.getValue();
          float l = r - (r - c) * 2;
          super.setValue( l);
        }
        
        if ( side2 == Side.left)
        {
          float r = node1.getValue();
          float l = node2.getValue();
          float c = l + (r - l) / 2;
          super.setValue( c);
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
    return String.format( "%s[%d] = %s <- %s", name, getID(), printValue(), printDependencies());
  }
  
  private String name;
  private IComputeNode node1;
  private Side side1;
  private IComputeNode node2;
  private Side side2;
}
