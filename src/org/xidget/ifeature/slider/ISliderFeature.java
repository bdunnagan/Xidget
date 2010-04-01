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
   * Set the number of decimal points of precision.
   * @param precision The precision.
   */
  public void setPrecision( int precision);
  
  /**
   * Set the value of the slider.
   * @param value The value.
   */
  public void setValue( double value);
  
  /**
   * Returns the value of the slider.
   * @return Returns the value of the slider.
   */
  public double getValue();
  
  /**
   * Set the minimum value.
   * @param value The minimum value.
   */
  public void setMinimum( double value);
  
  /**
   * Returns the minimum value.
   * @return Returns the minimum value.
   */
  public double getMinimum();
  
  /**
   * Set the maximum value.
   * @param value The maximum value.
   */
  public void setMaximum( double value);
  
  /**
   * Returns the maximum value.
   * @return Returns the maximum value.
   */
  public double getMaximum();
}
