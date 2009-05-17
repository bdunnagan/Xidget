/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for accessing the context associated with a widget. Widgets are created by the
 * IWidgetCreationFeature.  They are created before the xidget is bound via the IBindFeature.
 * Therefore, there is usually one widget per context.
 */
public interface IWidgetContextFeature
{
  /**
   * Set the context associated with the specified widget. 
   * @param widget The platform-specific widget object.
   * @param context The context.
   */
  public void createAssociation( Object widget, StatefulContext context);
  
  /**
   * Remove the specified widget-context association.
   * @param widget The platform-specific widget object.
   */
  public void removeAssociation( Object widget);
    
  /**
   * Returns the context associated with the specified widget.
   * @param widget The platform-specific widget object.
   * @return Returns the context associated with the specified widget.
   */
  public StatefulContext getContext( Object widget);
}
