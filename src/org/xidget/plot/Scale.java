/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.plot;

import java.util.ArrayList;
import java.util.List;

/**
 * @par A class for calculating the values of tick marks on a scale. The values are
 * calculated so that the tick values are multiples of a user-specified set of
 * divisors.
 * @par Additional instances of this class may be used to create tick sub-divisions.
 * The finest sub-divisions are calculated with the most number of divisors. Each 
 * successively gross sub-division can be calculated by removing one divisor.
 */
public class Scale
{
  public static class Tick
  {
    public double value;
    public int depth;
  }
  
  public Scale( double min, double max, int count)
  {
    ticks = computeMajorTicks( min, max);
    for( int i=0; i<divisions.length; i++)
    {
      if ( ticks.size() * (divisions[ i] + 1) > count) break;
      subdivide( ticks, divisions[ i]);
    }
  }
  
  /**
   * Compute the major tick marks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @return Returns the list of major tick marks.
   */
  private List<Tick> computeMajorTicks( double min, double max)
  {
    //double minExpFloor = Math.floor( Math.log10( min));
    double maxExpFloor = Math.floor( Math.log10( max));
    
    //double minPow = Math.pow( 10, minExpFloor);
    double maxPow = Math.pow( 10, maxExpFloor);
    //double minNorm = min / minPow;
    //double maxNorm = max / maxPow;
    //double scaleMin = Math.floor( minNorm) * minPow;
    //double scaleMax = Math.ceil( maxNorm) * maxPow;

    double v0 = Math.floor( min / maxPow) * maxPow;
    double v1 = Math.ceil( max / maxPow) * maxPow;
    
    List<Tick> ticks = new ArrayList<Tick>();
    for( double value = v0; value <= v1; value += maxPow)
    {
      Tick tick = new Tick();
      tick.value = value;
      tick.depth = 0;
      ticks.add( tick);
    }
    
    return ticks;
  }
  
  /**
   * Subdivide the specified list of tick marks.
   * @param ticks The list of tick marks.
   * @param subdivisions The number of subdivisions.
   */
  private void subdivide( List<Tick> ticks, int subdivisions)
  {
    double v0 = ticks.get( 0).value;
    double v1 = ticks.get( 1).value;
    double dt = (v1 - v0) / (subdivisions + 1);
    
    int depth = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size()-1; i++)
    {
      double v = ticks.get( i).value;
      for( int j=0; j<subdivisions; j++)
      {
        v += dt;
        Tick tick = new Tick();
        tick.value = v;
        tick.depth = depth;
        i++; ticks.add( i, tick);
      }
    }
  }
  
  private int[] divisions = { 1, 1, 1, 1, 1};
  private List<Tick> ticks;
  
  public static void main( String[] args) throws Exception
  {
    int count = 20;
    double min = .25; double max = .28;
    Scale scale = new Scale( min, max, count);
    for( Tick tick: scale.ticks)
    {
      for( int j=0; j<tick.depth; j++)
        System.out.printf( "    ");
      System.out.printf( "%e\n", tick.value);
    }
  }
}
