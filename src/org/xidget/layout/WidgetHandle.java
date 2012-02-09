/*
 * Xidget - XML Widgets based on JAHM
 * 
 * WidgetHandle.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature.Side;

/**
 * An implementation of IComputeNode that functions as the handle for one side of a widget.
 * This handle may be the outside of any widget or the inside of a container.
 */
public class WidgetHandle extends ComputeNode
{
  /**
   * Create a new WidgeHandle with the specified offset.
   * @param xidget The xidget (for debugging).
   * @param side The side (for debugging).
   * @param offset The offset from the side of the widget.
   */
  public WidgetHandle( IXidget xidget, Side side, int offset)
  {
    this.xidget = xidget;
    this.side = side;
    this.offset = offset;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#setValue(float)
   */
  public void setValue( float value)
  {
    super.setValue( value + offset);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "Handle-%s[%d] = %s (%d) <- %s, %s", side, getID(), printValue(), offset, printDependencies(), xidget);
  }
  
  private IXidget xidget;
  private Side side;
  private int offset;
}
