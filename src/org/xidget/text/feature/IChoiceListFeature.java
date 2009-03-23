/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.feature;

import java.util.List;

/**
 * An interface for xidgets which provide a list of choices from which the
 * client can choose (i.e. a combo box).
 */
public interface IChoiceListFeature
{
  /**
   * Returns a copy of the list of choices.
   * @return Returns a copy of the list of choices.
   */
  public List<String> getChoices();

  /**
   * Add a choice to the end of the list.
   * @param choice The choice.
   */
  public void addChoice( String choice);
  
  /**
   * Insert a choice in the middle of the list.
   * @param index The insert index.
   * @param choice The choice.
   */
  public void insertChoice( int index, String choice);
  
  /**
   * Remove a choice.
   * @param choice The choice.
   */
  public void removeChoice( String choice);
  
  /**
   * Remove the choice at the specified index.
   * @param index The index.
   */
  public void removeChoice( int index);
  
  /**
   * Remove all the choices.
   */
  public void removeAllChoices();
}
