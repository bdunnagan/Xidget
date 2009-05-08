/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for getting and setting the selection for a xidget. Selection is always represented
 * as a node-set with one node for each selected item. Each selection node points back to an element
 * representing the selected item.  For example, in a table xidget, the selection node would point 
 * to a row object in the table.  The selection node <i>points</i> in one of three ways: using an
 * instance of Reference, or by creating a new element and storing the <i>id</i> of the selected
 * element in either the <i>id</i> or value of the new element.  The second two options are provided
 * to simplify the creation of xml fragments which use one of these two referencing schemes. 
 */
public interface ISelectionFeature
{
}
