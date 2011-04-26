/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.plot;

/**
 * @par A class for calculating the values of tick marks on a scale. The values are
 * calculated so that the tick values are multiples of a user-specified set of
 * divisors.
 * @par Additional instances of this class may be used to create tick sub-divisions.
 * The finest sub-divisions are calculated with the most number of divisors. Each 
 * successively gross sub-division can be calculated by removing one divisor.
 */
public final class Scale
{
  /**
   * Create a scale over the specified range that uses the default list of divisors. 
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param ticks The number of ticks on the scale.
   */
  public Scale( double min, double max, int ticks)
  {
    this( defaultDivisors, min, max, ticks);
  }
  
  /**
   * Create a scale over the specified range with the specified divisors.
   * @param divisors The divisors.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param ticks The number of ticks on the scale.
   */
  public Scale( int[] divisors, double min, double max, int count)
  {
    this.divisors = divisors;
    delta = getResolution( count-1, min, max);
    tick = Math.floor( min / delta) * delta - delta;
    range = Math.pow( 10.0, Math.floor( Math.log10( max - min)) + 1);
  }
  
  /**
   * @return Returns the value of the next tick.
   */
  public final double nextTick()
  {
    return (tick += delta);
  }
  
  /**
   * Returns an integer representing the courseness of the last tick value returned
   * by the nextTick() method. The result is in the range [1, 4] where 1 is the 
   * most course and 4 is the finest.
   */
  public final int tickLevel()
  {
    double a = tick / range;
    for( int i=0; i<divisors.length; i++)
    {
      double v = a * divisors[ i];
      double f = v - Math.floor( v);
      if ( f == 0) return (i+1);
    }
    return 0;
  }
  
  /**
   * Returns the finest nice tick resolution for the specified range of values.
   * @param ticks The number of tick marks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @return Returns one of the defined nice tick resolutions.
   */
  private final double getResolution( int ticks, double min, double max)
  {
    double delta = (max - min) / ticks;
    double log = Math.log10( delta);
    double power = Math.pow( 10.0, Math.ceil( log));
    double norm = delta / power;
    
    System.out.printf( "res=%f, log=%f, power=%f, norm=%f, range=%f, ", 
      delta, log, power, norm, Math.floor( Math.log10( max - min)));
    
    for( int i=0; i<divisors.length; i++)
    {
      if ( (norm * divisors[ i]) > 1) 
      {
        if ( i == 0) i = 1;
        return power / divisors[ i-1];
      }
    }
    
    return power / divisors[ divisors.length];
  }
  
  private final static int[] defaultDivisors = new int[] { 1, 2, 4, 5, 8, 10};
  private int[] divisors;
  private double delta;
  private double tick;
  private double range;
  
  public static void main( String[] args) throws Exception
  {
    double min = 19.3;
    double max = 90.4;
    Scale scale = new Scale( min, max, 10);
    double tick = scale.nextTick();
    for( int i=0; i<30; i++)
    {
      System.out.printf( "%d | %f\n", scale.tickLevel(), tick);
      tick = scale.nextTick();
    }
  }
}
