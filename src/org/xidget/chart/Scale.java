/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

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
    public double scale; 
    public String label;
  }
  
  /**
   * Create a linear scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param context The parent context.
   */
  public Scale( double min, double max, int count, IContext context)
  {
    this( min, max, count, 0, context);
  }
  
  /**
   * Create a scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param log 0 or the log base for logarithmic scales.
   * @param context The parent context.
   */
  public Scale( double min, double max, int count, double log, IContext context)
  {
    this.context = (context != null)? new StatefulContext( context): null;
    this.log = log;
    this.count = count;
    
    if ( min > max)
    {
      double tmp = min; 
      min = max;
      max = tmp;
    }
    
    if ( log != 0)
    {
      logq = Math.log( log);
      if ( min != 0) min = Math.log( min) / logq;
      if ( max != 0) max = Math.log( max) / logq;
    }
    
    ticks = new ArrayList<Tick>();
    computeMajorTicks( min, max, ticks);
    
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
    
    // create labels
    for( Tick tick: ticks)
    {
      createLabel( tick);
    }
  }
  
  /**
   * Returns the most significant digit (base10).
   * @param value The value.
   * @return Returns the most significant digit.
   */
  private int msd( double value)
  {
    double e = Math.floor( Math.log10( value));
    double p = Math.pow( 10, e);
    double norm = value / p;
    
    // will only be 1, 2, 4, 5, 6, or 8
    return (int)Math.round( norm);
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
   * Returns the value of the specified position on the scale. This method is
   * the inverse of the plot( double) method.
   * @param scale A value between 0 and 1, inclusive.
   * @return Returns the value of the specified position on the scale.
   */
  public double value( double scale)
  {
    double value = scaleRange * scale + scaleMin;    
    if ( log != 0) value = Math.pow( value, log);
    return value;
  }
  
  /**
   * Interpolates the value represented by the specified index on a grid of 
   * size equally spaced points.  This method compensates for aliasing that
   * is caused by rounding tick values to the grid.  
   * @param index A value between 0 (inclusive), and size (exclusive).
   * @param size The size of the grid.
   * @return Returns the nearest dealiased/interpolated value.
   */
  public double value( int index, int size)
  {
    double s = (double)index / size * ticks.size();
    
    int k = (int)s;
    Tick t0 = ticks.get( k);
    if ( k == ticks.size() - 1) return t0.value;
    
    Tick t1 = ticks.get( k+1);
    
    int p0 = (int)Math.round( t0.scale * size);
    int p1 = (int)Math.round( t1.scale * size);
    if ( p0 == p1) return t0.value;
    
    double m = (t1.value - t0.value) / (p1 - p0);
    return m * (index - p0) + t0.value;
  }
  
  /**
   * Compute the major tick marks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param ticks Returns the list of major tick marks.
   */
  private void computeMajorTicks( double min, double max, List<Tick> ticks)
  {
    double maxExpFloor = findExponent( min, max);
    maxPow = Math.pow( 10, maxExpFloor);
    
    // note that scale min and max are exponents for logarithmic scales
    if ( min > 0) scaleMin = roundTowardZero( min / maxPow) * maxPow;
    else scaleMin = roundAwayFromZero( min / maxPow) * maxPow;
    if ( max > 0) scaleMax = roundAwayFromZero( max / maxPow) * maxPow;
    else scaleMax = roundTowardZero( max / maxPow) * maxPow;
    
    if ( scaleMax < scaleMin)
    {
      double t = scaleMin;
      scaleMin = scaleMax;
      scaleMax = t;
    }
    
    scaleRange = (scaleMax - scaleMin);

    int divs = getMajorDivisions( scaleRange);
    for( int i=0; i<=divs; i++)
    {
      double value = scaleMin + (scaleRange * i / divs);
      Tick tick = new Tick();
      tick.depth = 0;
      tick.value = value;
      tick.scale = plot( value);
      ticks.add( tick);
    }
  }
  
  /**
   * Returns the maximum number of digits represented by the specified range.
   * @param min The minimum value.
   * @param max The maximum value.
   * @return Returns the maximum number of digits represented by the specified range.
   */
  private double findExponent( double min, double max)
  {
    double absMin = Math.abs( min);
    double absMax = Math.abs( max);
    double abs = (absMin < absMax)? absMax: absMin;
    return Math.floor( Math.log10( abs));
  }
  
  /**
   * Calculate major divisions within the specified range.
   * @param range The range.
   * @return Returns the number of major divisions.
   */
  private int getMajorDivisions( double range)
  {
    double norm = range / maxPow;
    if ( norm > 10) return (int)norm; 
    
    int msd = msd( range);
    switch( msd)
    {
      case 1:  return 10;
      case 2:  return 4;
      case 3:  return 6;
      default: return msd;
    }
  }
  
  /**
   * Round the specified value toward zero.
   * @param value The value.
   * @return Returns the rounded value.
   */
  private static double roundTowardZero( double value)
  {
    if ( value < 0)
    {
      return Math.ceil( value);
    }
    else
    {
      return Math.floor( value);
    }
  }
  
  /**
   * Round the specified value away from zero.
   * @param value The value.
   * @return Returns the rounded value.
   */
  private static double roundAwayFromZero( double value)
  {
    if ( value < 0)
    {
      return Math.floor( value);
    }
    else
    {
      return Math.ceil( value);
    }
  }
  
  /**
   * Subdivide the ticks.
   * @param subdivisions The number of subdivisions.
   */
  private void subdivide( int subdivisions)
  {
    //System.out.printf( "count=%d, q=%d, t=%g\n", ticks.size(), subdivisions+1, ticks.get( 1).value);
    
    counts.add( ticks.size() + (ticks.size() - 1) * subdivisions);
    
    BigDecimal v0 = BigDecimal.valueOf( ticks.get( 0).value);
    BigDecimal v1 = BigDecimal.valueOf( ticks.get( 1).value);
    BigDecimal q = BigDecimal.valueOf( subdivisions + 1);
    double dt = v1.subtract( v0).divide( q).doubleValue();
    
    int depth = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size()-1; i++)
    {
      double value = ticks.get( i).value;
      for( int j=1; j<=subdivisions; j++)
      {
        Tick tick = new Tick();
        tick.depth = depth;
        tick.value = value + (dt * j);
        tick.scale = plot( tick.value);
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
    while( true)
    {
      int n = count / ticks.size();
      double range = ticks.get( 1).value - ticks.get( 0).value;
      int msd = msd( range);
      
      //System.out.printf( "n=%d, r=%g, msd=%d, ", n, range, msd);
      
      if ( msd == 1 && n >= 10)
      {
        subdivide( 1);
      }
      else if ( msd == 5 && n >= 5)
      {
        subdivide( 4);
      }
      else if ( n >= 10)
      {
        subdivide( 9);
      }
      else if ( n >= 2)
      {
        subdivide( 1);
      }
      else
      {
        break;
      }
    }
  }
  
  /**
   * Creete a label for the specified tick.
   * @param tick The tick.
   */
  private void createLabel( Tick tick)
  {
    if ( labelExpr != null)
    {
      context.set( "value", tick.value);
      tick.label = labelExpr.evaluateString( context);
    }
    else
    {
      tick.label = String.format( "%f", tick.value);
      tick.label = trimZeros( tick.label);
    }
  }
  
  /**
   * Trim the trailing zeros in the fraction.
   * @param label The label.
   * @return Returns the trimmed label.
   */
  private String trimZeros( String label)
  {
    if ( symbols == null) symbols = new DecimalFormatSymbols();
    
    int index = label.indexOf( symbols.getDecimalSeparator());
    for( int i=label.length() - 1; i >= index; i--)
    {
      if ( label.charAt( i) != '0') 
      {
        if ( label.charAt( i) != symbols.getDecimalSeparator()) i++; 
        return label.substring( 0, i);
      }
    }
    
    return label.substring( 0, index);
  }
  
  private static DecimalFormatSymbols symbols;

  private List<Tick> ticks;
  private List<Integer> counts;
  private int count;
  private double maxPow;
  private double scaleMin;
  private double scaleMax;
  private double scaleRange;
  private double log;
  private double logq;
  private StatefulContext context;
  private IExpression labelExpr;
}
