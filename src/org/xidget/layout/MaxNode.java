/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.Log;

/**
 * An implementation of IComputeNode whose value is the maximum value of all of its dependent nodes.
 */
public class MaxNode extends ComputeNode
{
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    if ( !hasValue())
    {
      float max = Float.NEGATIVE_INFINITY;
      for( IComputeNode dependency: getDependencies())
      {
        if ( dependency.hasValue())
        {
          if ( max < dependency.getValue())
            max = dependency.getValue();
        }
      }
      
      if ( max != Float.NEGATIVE_INFINITY) 
        setValue( max);
      
      Log.printf( "layout", "update: %s\n", toString()); 
    }
  }
}
