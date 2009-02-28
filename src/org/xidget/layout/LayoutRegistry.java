/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.layout.LayoutTagHandler.Layout;

/**
 * A class which stores the layout associations to xidgets.
 */
public class LayoutRegistry
{
  public LayoutRegistry()
  {
    layouts = new HashMap<String, Layout>();
    references = new HashMap<IXidget, String>();
  }

  /**
   * Returns the layout defined for the specified xidget.
   * @param xidget The xidget.
   * @return Returns the layout defined for the specified xidget.
   */
  public Layout getLayout( IXidget xidget)
  {
    return layouts.get( references.get( xidget));
  }
  
  /**
   * Define a layout in the registry.
   * @param name The name of the layout.
   * @param layout The layout definition.
   */
  public void defineLayout( String name, Layout layout)
  {
    layouts.put( name, layout);
  }
  
  /**
   * Associate a layout to a xidget.
   * @param xidget The xidget.
   * @param layout The name of the layout.
   */
  public void associateLayout( IXidget xidget, String layout)
  {
    references.put( xidget, layout);
  }
  
  private Map<IXidget, String> references;
  private Map<String, Layout> layouts;
}
