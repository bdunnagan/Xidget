/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IScale that creates one level of tick marks for values that represent dates.
 */
@SuppressWarnings("unused")
public class DateScale implements IScale
{
  /**
   * Create a scale over the specified range with at most the specified number of ticks.
   * @param min The minimum value in the range.
   * @param max The maximum value in the range.
   * @param count The maximum number of ticks in the scale.
   * @param context The parent context.
   * @param labelExpr The label expression;
   */
  public DateScale( double min, double max, int count, IContext context, IExpression labelExpr)
  {
    long range = (long)(max - min);
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

  private void computeTicks()
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
}
