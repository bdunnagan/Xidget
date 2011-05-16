/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.graph;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
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
    public double scale; 
    public String label;
  }
  
  public enum Format { decimal, normalized, scientific, engineering};
  
  /**
   * Create a linear scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param format The formatting style.
   */
  public Scale( double min, double max, int count, Format format)
  {
    this( min, max, count, 0, format);
  }
  
  /**
   * Create a scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param log 0 or the log base for logarithmic scales.
   * @param format The formatting style.
   */
  public Scale( double min, double max, int count, double log, Format format)
  {
    this.format = format;
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
   * @return Returns the list of major tick marks.
   */
  private List<Tick> computeMajorTicks( double min, double max)
  {
    double minAbs = Math.abs( min);
    double maxAbs = Math.abs( max);
    double maxExpFloor = Math.floor( Math.log10( (maxAbs > minAbs)? maxAbs: minAbs));
    maxPow = Math.pow( 10, maxExpFloor);
    
    // note that scale min and max are exponents for logarithmic scales
    scaleMin = roundTowardZero( min / maxPow) * maxPow;
    scaleMax = roundAwayFromZero( max / maxPow) * maxPow;
    if ( scaleMax < scaleMin)
    {
      double t = scaleMin;
      scaleMin = scaleMax;
      scaleMax = t;
    }
    scaleRange = (scaleMax - scaleMin);
    
    int divs = 0;
    int msd = msd( scaleRange);
    switch( msd)
    {
      case 1:  divs = 10; break;
      case 2:  divs = 4; break;
      case 3:  divs = 6; break;
      default: divs = msd; break;
    }
    
    List<Tick> ticks = new ArrayList<Tick>();
    for( int i=0; i<=divs; i++)
    {
      double value = scaleMin + (scaleRange * i / divs);
      Tick tick = new Tick();
      tick.depth = 0;
      tick.value = value;
      tick.scale = plot( value);
      ticks.add( tick);
    }
    
    return ticks;
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
    double norm = normalize( tick.value);
    
    switch( format)
    {
      case decimal:
      {
        tick.label = String.format( "%f", tick.value);
        tick.label = trimZeros( tick.label);
      }
      break;
      
      case normalized:
      {
        tick.label = String.format( "%f", norm);
        tick.label = trimZeros( tick.label);
      }
      break;
      
      case scientific:
      {
        tick.label = String.format( "%1.2e", tick.value);
        tick.label = trimZeros( tick.label);
      }
      break;
      
      case engineering:
      {
        if ( tick.value == 0) 
        {
          tick.label = "0";
          return;
        }
        
        int e = (int)Math.floor( Math.log10( tick.value));
        if ( e < -2)
        {
          int i = (-e / 3) - 1;
          int r = -e % 3;
          if ( r == 1) norm *= 10;
          if ( r == 2) norm *= 100;
          String label = trimZeros( String.format( "%1.2f", norm));
          if ( i < negativeExponentSymbols.length())
          {
            tick.label = label + negativeExponentSymbols.charAt( i);
          }
          else
          {
            tick.label = norm + "10E-" + i;
          }
        }
        else if ( e > 2)
        {
          int i = (e / 3) - 1;
          int r = e % 3;
          if ( r == 1) norm *= 10;
          if ( r == 2) norm *= 100;
          String label = trimZeros( String.format( "%1.2f", norm));
          if ( i < positiveExponentSymbols.length())
          {
            tick.label = label + positiveExponentSymbols.charAt( i);
          }
          else
          {
            tick.label = label + "10E" + i;
          }
        }
        else
        {
          tick.label = trimZeros( String.format( "%1.2f", tick.value));
        }
      }
      break;
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
  
  /**
   * Normalize the specified value.
   * @param value The value.
   * @return Returns a value less than 10 and greater than -10.
   */
  private double normalize( double value)
  {
    int e = (int)Math.floor( Math.log10( Math.abs( value)));
    double p = Math.pow( 10, e);
    return value / p;
  }
  
  private final static String negativeExponentSymbols = "munpfazy";
  private final static String positiveExponentSymbols = "KMGTPEZY";
  private static DecimalFormatSymbols symbols;
  
  private Format format;
  private List<Tick> ticks;
  private List<Integer> counts;
  private int count;
  private double maxPow;
  private double scaleMin;
  private double scaleMax;
  private double scaleRange;
  private double log;
  private double logq;
  
  @SuppressWarnings("unused")
  public static void main( String[] args) throws Exception
  {
    for( int i=0; i<1; i++)
    {
      long t0 = System.nanoTime();
      
      int count = 100000;
      Scale scale = new Scale( -3.9329, -4.359, count, 0, Format.engineering);
      
      long t1 = System.nanoTime();
      System.out.printf( "%gms\n", (t1 - t0) / 1e6);
    }
    
//    for( Tick tick: scale.ticks)
//    {
//      for( int j=0; j<tick.depth; j++)
//        System.out.printf( "    ");
//      System.out.printf( "%f\n", tick.value);
//    }
//    
//    for( double i=min; i<=max; i += 1)
//    {
//      System.out.printf( "%f -> %f\n", i, scale.plot( i));
//      //if ( i == 0) i = 10; else i *= 10;
//    }
  }
}
