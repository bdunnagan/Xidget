/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Header;

/**
 * An interface for managing the column headers of a table.
 */
public interface IHeaderFeature
{
  /**
   * Returns the headers for the table.
   * @return Returns the headers for the table.
   */
  public List<Header> getHeaders();
}
