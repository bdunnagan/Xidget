/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.util.Calendar;
import java.util.List;

import org.xmodel.xpath.expression.IContext;

/**
 * An implementation of IScale that creates one level of tick marks for values that represent dates.
 */
public class DateScale implements IScale
{
  /**
   * Create a scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   */
  public DateScale( double min, double max, int count)
  {
    String format = getFormat( getCalendar( min), getCalendar( max));
    computeTicks( format);
  }  

  /**
   * Returns the calendar for the specified absolute time.
   * @param time The absolute time.
   * @return The calendar.
   */
  private static Calendar getCalendar( double time)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis( (long)time);
    return calendar;
  }

  /**
   * Compute the tick marks.
   * @param min The earliest date.
   * @param max The latest date.
   * @return Returns the date format.
   */
  private static String getFormat( Calendar min, Calendar max)
  {
    if ( min.get( Calendar.YEAR) < max.get( Calendar.YEAR))
    {
      return "[MON]/[D]/[YY]";
    }
    else if ( min.get( Calendar.MONTH) < max.get( Calendar.MONTH))
    {
      return "[MON]/[D]";
    }
    else if ( min.get( Calendar.DAY_OF_MONTH) < max.get( Calendar.DAY_OF_MONTH))
    {
      return "[MON]/[D]";
    }
    else if ( min.get( Calendar.HOUR_OF_DAY) < max.get( Calendar.HOUR_OF_DAY))
    {
      return "[h]:[m]:[s]";
    }
    else if ( min.get( Calendar.MINUTE) < max.get( Calendar.MINUTE))
    {
      return "[h]:[m]:[s]";
    }
    else if ( min.get( Calendar.SECOND) < max.get( Calendar.SECOND))
    {
      return "[s].[SSS]";
    }
    else if ( min.get( Calendar.MILLISECOND) < max.get( Calendar.MILLISECOND))
    {
      return "[SSS]";
    }
    else
    {
      return "[MON]/[D]/[YY] [h]:[m]:[s].[SSS]";
    }
  }
  
  /**
   * Compute the tick labels.
   * @param format The date format.
   */
  private void computeTicks( String format)
  {
    
  }
  
  /* (non-Javadoc)
   * @see org.xidget.chart.IScale#getTicks()
   */
  @Override
  public List<Tick> getTicks()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.chart.IScale#getTickCounts()
   */
  @Override
  public List<Integer> getTickCounts()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.chart.IScale#plot(double)
   */
  @Override
  public double plot( double value)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see org.xidget.chart.IScale#value(double)
   */
  @Override
  public double value( double scale)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see org.xidget.chart.IScale#value(int, int)
   */
  @Override
  public double value( int index, int size)
  {
    // TODO Auto-generated method stub
    return 0;
  }
  
  private String format;
  private List<Tick> ticks;
}
