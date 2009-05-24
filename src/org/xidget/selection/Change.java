/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.selection;

/**
 * A change record calculated by one of the differs defined in this package. The change
 * record is used to update the selection.
 */
public class Change
{
  public int lIndex;
  public int rIndex;
  public int count;
}
