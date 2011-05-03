/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that calculates tick marks over a user-specified range of values. The scale is
 * guaranteed to generate tick marks that encompass all values in the range.  Tick marks
 * may be calculated for a linear or logarithmic scale.  This class also provides a method
 * to translate coordinates into scale space.
 */
public class Scale
{
  public static class Tick
  {
    public int depth;
    public double value;
    public double normed;
    public double scale; 
    public String text;
  }
  
  /**
   * Create a linear scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   */
  public Scale( double min, double max, int count)
  {
    this( min, max, count, 0);
  }
  
  /**
   * Create a scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param log 0 or the log base for logarithmic scales.
   */
  public Scale( double min, double max, int count, double log)
  {
    this.log = log;
    this.count = count;
    
    if ( log != 0)
    {
      logq = Math.log( log);
      if ( min != 0) min = Math.log( min) / logq;
      if ( max != 0) max = Math.log( max) / logq;
    }
    
    ticks = computeMajorTicks( min, max);
    counts = new ArrayList<Integer>();
    counts.add( ticks.size());
    subdivide();
    
    // convert tick values
    if ( log > 0)
    {
      for( Tick tick: ticks)
      {
        tick.value = Math.pow( log, tick.value);
      }
    }
  }
  
  /**
   * Returns the most significant digit of the specified base.
   * @param value The value.
   * @param base The base (e.g. 10).
   * @return Returns the most significant digit.
   */
  private int msd( double value, int base)
  {
    if ( value >= base)
    {
      while( value >= base) value /= base;
      return (int)value;
    }
    else
    {
      while( value < base) value *= base;
      return (int)(value / base);
    }
  }
  
  /**
   * @return Returns the order of magnitude of the maximum value.
   */
  public double getExponent()
  {
    return maxPow;
  }
  
  /**
   * @return Returns the tick marks.
   */
  public List<Tick> getTicks()
  {
    return ticks;
  }
  
  /**
   * @return Returns the tick counts by depth.
   */
  public List<Integer> getTickCounts()
  {
    return counts;
  }
  
  /**
   * Returns the location of the specified value relative to the scale where
   * a value of 0 is the minimum tick value and a value of 1 is the maximum
   * tick value.  
   * @param value The value to be converted.
   * @return Returns the location of the specified value in the scale space.
   */
  public double plot( double value)
  {
    if ( log != 0 && value != 0)
      value = Math.log( value) / logq;
    return (value - scaleMin) / scaleRange;
  }
  
  /**
   * Compute the major tick marks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @return Returns the list of major tick marks.
   */
  private List<Tick> computeMajorTicks( double min, double max)
  {
    double maxExpFloor = Math.floor( Math.log10( max));
    maxPow = Math.pow( 10, maxExpFloor);
    
    // note that scale min and max are exponents for logarithmic scales
    scaleMin = Math.floor( min / maxPow) * maxPow;
    scaleMax = Math.ceil( max / maxPow) * maxPow;
    scaleRange = (scaleMax - scaleMin);
    
    List<Tick> ticks = new ArrayList<Tick>();
    for( double value = scaleMin; value <= scaleMax; value += maxPow)
    {
      Tick tick = new Tick();
      tick.depth = 0;
      tick.value = value;
      tick.normed = value / maxPow;
      tick.scale = plot( value);
      ticks.add( tick);
    }
    
    return ticks;
  }
  
  /**
   * Subdivide the ticks.
   * @param subdivisions The number of subdivisions.
   */
  private void subdivide( int subdivisions)
  {
    counts.add( ticks.size() + (ticks.size() - 1) * subdivisions);
    
    double v0 = ticks.get( 0).value;
    double v1 = ticks.get( 1).value;
    double dt = (v1 - v0) / (subdivisions + 1);
    
    int depth = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size()-1; i++)
    {
      double value = ticks.get( i).value;
      for( int j=0; j<subdivisions; j++)
      {
        value += dt;
        Tick tick = new Tick();
        tick.depth = depth;
        tick.value = value;
        tick.normed = value / maxPow;
        tick.scale = plot( value);
        i++; ticks.add( i, tick);
      }
    }
  }
  
  /**
   * Smartly subdivide the ticks to create nicely rounded tick values.
   * This method assumes that major ticks have already been created.
   */
  private void subdivide()
  {
    while( count >= 2)
    {
      double range = ticks.get( 1).value - ticks.get( 0).value;
      int msd = msd( range, 10);
      if ( msd == 1 && count >= 10)
      {
        subdivide( 1);
        count /= 2;
      }
      else if ( msd == 5 && count >= 5)
      {
        subdivide( 4);
        count /= 5;
      }
      else if ( count >= 10)
      {
        subdivide( 9);
        count /= 10;
      }
      else if ( count >= 2)
      {
        subdivide( 1);
        count /= 2;
      }
      else
      {
        break;
      }
    }
  }
  
  private List<Tick> ticks;
  private List<Integer> counts;
  private int count;
  private double maxPow;
  private double scaleMin;
  private double scaleMax;
  private double scaleRange;
  private double log;
  private double logq;
  
  public static void main( String[] args) throws Exception
  {
    int count = 100;
    double min = 3.5; double max = 8.4;
    Scale scale = new Scale( min, max, count, 0);
    for( Tick tick: scale.ticks)
    {
      for( int j=0; j<tick.depth; j++)
        System.out.printf( "    ");
      System.out.printf( "%f\n", tick.value);
    }
    
    for( double i=min; i<=max; i += 1)
    {
      System.out.printf( "%f -> %f\n", i, scale.plot( i));
      //if ( i == 0) i = 10; else i *= 10;
    }
  }
}
