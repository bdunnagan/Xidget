/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.slider;

/**
 * An interface for setting slider parameters.
 */
public interface ISliderFeature
{
  /**
   * Specify whether the scale is linear or logarithmic. If the log argument is
   * less than or equal to 1, the scale is linear. Otherwise, the scale is logarithmic
   * with the specified base.
   * @param log The logarithm base.
   */
  public void setLogarithmic( double log);
  
  /**
   * Returns the logarithmic scale where 1 means linear.
   * @return Returns the logarithmic scale where 1 means linear.
   */
  public double getLogarithmic(); 
  
  /**
   * Set the minimum value.
   * @param value The minimum value.
   */
  public void setMinimum( int value);
  
  /**
   * Returns the minimum value.
   * @return Returns the minimum value.
   */
  public int getMinimum();
  
  /**
   * Set the maximum value.
   * @param value The maximum value.
   */
  public void setMaximum( int value);
  
  /**
   * Returns the maximum value.
   * @return Returns the maximum value.
   */
  public int getMaximum();
  
  /**
   * Specify whether labels should be generated for the slider.
   * @param auto True if labels should be generated.
   */
  public void setAutoLabel( boolean auto);
  
  /**
   * Returns true if auto-labelling should be used.
   * @return Returns true if auto-labelling should be used.
   */
  public boolean getAutoLabel();
}
