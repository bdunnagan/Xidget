/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * A class for formatting and parsing dates using the Xidget formatting flags defined below.
 * Each formatting flag must be enclosed in braces {} to separate it from surround text.
 * 
 * empty-string => default format for current locale
 * 
 * year
 *   YY   - 2 digits
 *   YEAR - 1 to 4 digits
   * 
 * month
 *   M     - 1 or 2 digits
 *   MM    - 2 digits padded
 *   MON   - Abbreviation
 *   MONTH - Full name
 *   
 * week
 *   YW    - 1 digit week of year
 *   
 * day
 *   D       - 1 or 2 digits
 *   DD      - 2 digits padded
 *   DAY     - abbreviated name of day of the week
 *   DAYFULL - full name of the day of of week
 *   DDD     - 1 to 3 digit day of year
 * 
 * hour
 *   h     - 1 digit AM/PM
 *   hh    - 2 digits 24 hour padded
 * 
 * min
 *   m     - 1 or 2 digits
 *   mm    - 2 digits padded
 * 
 * sec
 *   s     - 1 or 2 digits
 *   ss    - 2 digits padded
 *   
 * msec
 *   S     - 1 to 3 digits
 *   SSS   - 3 digits padded
 *   
 * Time Zone
 *   Z
 * 
 * AM/PM
 *   AM
 *  
 * AD/BC
 *   AD
 */
public class DateUtil
{
  public enum Field
  {
    NONE,
    YY,
    YEAR,
    M,
    MM,
    MON,
    MONTH,
    YW,
    D,
    DD,
    DAY,
    DAYDAY,
    DDD,
    h,
    hh,
    m,
    mm,
    s,
    ss,
    S,
    SSS,
    Z,
    AM,
    AD
  }
  
  /**
   * Format the specified numeric date using the specified xidget date formatting string.
   * The encoding of the date into the numeric representation is platform-specific.
   * @param format The format.
   * @param date The number of milliseconds since January 1, 1970.
   * @return Returns the formatted string.
   */
  public String format( String format, long date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis( date);
    
    formatIndex = 0;
    StringBuilder sb = new StringBuilder();
    Field field = nextField( format, sb);
    while( field != Field.NONE)
    {
      switch( field)
      {
        case YY:     twoDigitPadding( cal.get( Calendar.YEAR) % 1000, sb); break;
        case YEAR:   sb.append( cal.get( Calendar.YEAR)); break;
        case M:      sb.append( cal.get( Calendar.MONTH) + 1); break;
        case MM:     twoDigitPadding( cal.get( Calendar.MONTH) + 1, sb); break;
        case MON:    sb.append( cal.getDisplayName( Calendar.MONTH, Calendar.SHORT, Locale.getDefault())); break;
        case MONTH:  sb.append( cal.getDisplayName( Calendar.MONTH, Calendar.LONG, Locale.getDefault())); break;
        case YW:     sb.append( cal.get( Calendar.WEEK_OF_YEAR)); break;
        case D:      sb.append( cal.get( Calendar.DAY_OF_MONTH)); break;
        case DD:     twoDigitPadding( cal.get( Calendar.DAY_OF_MONTH), sb); break;
        case DAY:    sb.append( cal.getDisplayName( Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())); break;
        case DAYDAY: sb.append( cal.getDisplayName( Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())); break;
        case DDD:    sb.append( cal.get( Calendar.DAY_OF_YEAR)); break;
        case h:      sb.append( cal.get( Calendar.HOUR) + 1); break;
        case hh:     twoDigitPadding( cal.get( Calendar.HOUR_OF_DAY), sb); break;
        case m:      sb.append( cal.get( Calendar.MINUTE)); break;
        case mm:     twoDigitPadding( cal.get( Calendar.MINUTE), sb); break;
        case s:      sb.append( cal.get( Calendar.SECOND)); break;
        case ss:     twoDigitPadding( cal.get( Calendar.SECOND), sb); break;
        case S:      sb.append( cal.get( Calendar.MILLISECOND)); break;
        case SSS:    threeDigitPadding( cal.get( Calendar.MILLISECOND), sb); break;
        case Z:      sb.append( cal.getTimeZone().getDisplayName( true, TimeZone.SHORT)); break;
        case AM:     sb.append( cal.getDisplayName( Calendar.AM_PM, Calendar.SHORT, Locale.getDefault())); break;
        case AD:     sb.append( (cal.get( Calendar.ERA) == GregorianCalendar.AD)? "AD": "BC"); break;
      }
      
      field = nextField( format, sb);
    }
    
    
    return sb.toString();
  }
  
  /**
   * Parse the specified date string using the specified xidget date formatting string.
   * The encoding of the returned value is a platform-specific numeric encoding representing
   * milliseconds since some time in the past.
   * @param format The format.
   * @param date The formatted date string.
   * @return Returns the number of milliseconds since January 1, 1970.
   */
  public long parse( String format, String date)
  {
    Calendar cal = Calendar.getInstance();

    formatIndex = 0;
    parseIndex = 0;
    
    StringBuilder sb = new StringBuilder();
    Field field = nextField( format, sb);
    while( field != Field.NONE)
    {
      String stuff = date.substring( parseIndex, parseIndex + sb.length());
      if ( !stuff.equals( sb.toString())) return Long.MIN_VALUE;
      
      switch( field)
      {
        case YY:     if ( !parseYY( date, cal)) return Long.MIN_VALUE; break;
        case YEAR:   if ( !parseNumber( date, cal, Calendar.YEAR, 0)) return Long.MIN_VALUE; break;
        
        case M:
        case MM:
          if ( !parseNumber( date, cal, Calendar.MONTH, 0)) return Long.MIN_VALUE; 
          break;
        
        case MON:    if ( !parseString( date, cal, Calendar.MONTH, Calendar.SHORT, 3)) return Long.MIN_VALUE; break;
        case MONTH:  if ( !parseString( date, cal, Calendar.MONTH, Calendar.SHORT)) return Long.MIN_VALUE; break;
        
        case YW:     if ( !parseNumber( date, cal, Calendar.WEEK_OF_MONTH, 0)) return Long.MIN_VALUE; break;
        
        case D:      
        case DD:
          if ( !parseNumber( date, cal, Calendar.DAY_OF_MONTH, 0)) return Long.MIN_VALUE; 
          break;
          
        case DAY:    if ( !parseString( date, cal, Calendar.DAY_OF_WEEK, Calendar.SHORT, 3)) return Long.MIN_VALUE; break;
        case DAYDAY: if ( !parseString( date, cal, Calendar.DAY_OF_WEEK, Calendar.LONG)) return Long.MIN_VALUE; break;
        case DDD:    if ( !parseNumber( date, cal, Calendar.DAY_OF_YEAR, 0)) return Long.MIN_VALUE; break;
        
        case h:
          if ( !parseNumber( date, cal, Calendar.HOUR, -1)) return Long.MIN_VALUE; 
          break;
          
        case hh:
          if ( !parseNumber( date, cal, Calendar.HOUR, 0)) return Long.MIN_VALUE; 
          break;
          
        case m:
        case mm:
          if ( !parseNumber( date, cal, Calendar.MINUTE, 0)) return Long.MIN_VALUE; 
          break;
        
        case s:
        case ss:
          if ( !parseNumber( date, cal, Calendar.SECOND, 0)) return Long.MIN_VALUE; 
          break;
          
        case S:
        case SSS:
          if ( !parseNumber( date, cal, Calendar.MILLISECOND, 0)) return Long.MIN_VALUE; 
          break;
        
        case Z:
          parseIndex += 3;
          if ( parseIndex >= date.length()) return Long.MIN_VALUE;
          Calendar zoneCal = Calendar.getInstance();
          try
          {
            zoneCal.setTime( timeZoneFormat.parse( date.substring( parseIndex-3, parseIndex)));
          }
          catch( Exception e)
          {
            return Long.MIN_VALUE;
          }
          break;
          
        case AM:
          parseIndex += 2;
          if ( parseIndex >= date.length()) return Long.MIN_VALUE;
          String am = date.substring( parseIndex-2, parseIndex);
          cal.set( Calendar.AM_PM, am.equals( "AM")? Calendar.AM: Calendar.PM);
          break;
          
        case AD:
          parseIndex += 2;
          if ( parseIndex >= date.length()) return Long.MIN_VALUE;
          String ad = date.substring( parseIndex-2, parseIndex);
          cal.set( Calendar.ERA, ad.equals( "AD")? GregorianCalendar.AD: GregorianCalendar.BC);
          break;
      }
      
      sb.setLength( 0);
      field = nextField( format, sb);
    }
    
    return cal.getTimeInMillis();
  }
  
  /**
   * Return the next field from the specified format string.
   * @param format The format.
   * @return Return the next field from the specified format string.
   */
  private Field nextField( String format, StringBuilder sb)
  {
    int start = format.indexOf( '{', formatIndex);
    if ( start < 0)
    {
      sb.append( format.substring( formatIndex));
      return Field.NONE;
    }
    
    int end = format.indexOf( '}', start+1);
    if ( end < 0)
    {
      sb.append( format.substring( formatIndex));
      return Field.NONE;
    }
    
    try
    {
      String spec = format.substring( start+1, end);
      Field field = Field.valueOf( spec);
      if ( start > formatIndex) sb.append( format.substring( formatIndex, start));
      formatIndex = end+1;
      return field;
    }
    catch( IllegalArgumentException e)
    {
    }

    sb.append( format.substring( formatIndex));
    formatIndex = end+1;
    
    return Field.NONE;
  }
  
  /**
   * Returns the two-digit padded value.
   * @param value The value.
   * @param result Returns the result.
   */
  private void twoDigitPadding( int value, StringBuilder result)
  {
    if ( value < 10) 
    {
      result.append( '0');
      result.append( value);
    }
    else
    {
      result.append( value);
    }
  }
  
  /**
   * Returns the three-digit padded value.
   * @param value The value.
   * @param result Returns the result.
   */
  private void threeDigitPadding( int value, StringBuilder result)
  {
    if ( value < 10) 
    {
      result.append( "00");
      result.append( value);
    }
    else if ( value < 100)
    {
      result.append( '0');
      result.append( value);
    }
    else
    {
      result.append( value);
    }
  }
  
  /**
   * Parse a number from the specified date string and use the result to set a calendar field.
   * @param date The date string.
   * @param calendar The calendar.
   * @param field The calendar field.
   * @param delta The amount to add (subtract) from the parsed number.
   * @return Returns true if the parse was successful.
   */
  private boolean parseNumber( String date, Calendar calendar, int field, int delta)
  {
    int start = parseIndex;
    
    char c = date.charAt( parseIndex++);
    while( Character.isDigit( c))
      c = date.charAt( parseIndex++);
    
    try
    {
      int value = Integer.parseInt( date.substring( start, parseIndex));
      calendar.set( field, value + delta);
      return true;
    }
    catch( NumberFormatException e)
    {
      return false;
    }
  }
    
  /**
   * Parse a string field from the specified date and populate the calendar.
   * @param date The date string.
   * @param calendar The calendar.
   * @param field The calendar field.
   * @param style The calendar field style.
   * @param length The length of the string to parse.
   * @param delta The amount to add (subtract) from the parsed number.
   * @return Returns true if the parse was successful.
   */
  private boolean parseString( String date, Calendar calendar, int field, int style, int length)
  {
    Map<String, Integer> map = calendar.getDisplayNames( field, style, Locale.getDefault());
    String s = date.substring( parseIndex, parseIndex + length);
    Integer value = map.get( s);
    if ( value != null)
    {
      calendar.set( field, value);
      return true;
    }
    return false;
  }
    
  /**
   * Parse a string field from the specified date and populate the calendar.
   * @param date The date string.
   * @param calendar The calendar.
   * @param field The calendar field.
   * @param style The calendar field style.
   * @return Returns true if the parse was successful.
   */
  private boolean parseString( String date, Calendar calendar, int field, int style)
  {
    Map<String, Integer> map = calendar.getDisplayNames( field, style, Locale.getDefault());
    int start = parseIndex;
    while( parseIndex < date.length())
    {
      String s = date.substring( start, ++parseIndex);
      Integer value = map.get( s);
      if ( value != null)
      {
        calendar.set( field, value);
        return true;
      }
    }
    return false;
  }
    
  /**
   * Parse a number from the specified date string and use the result to set a calendar field.
   * @param date The date string.
   * @param calendar The calendar.
   * @param field The calendar field.
   * @return Returns true if the parse was successful.
   */
  private boolean parseYY( String date, Calendar calendar)
  {
    int start = parseIndex;
    
    char c = date.charAt( parseIndex++);
    while( Character.isDigit( c))
      c = date.charAt( parseIndex++);
    
    try
    {
      int value = Integer.parseInt( date.substring( start, parseIndex));
      calendar.set( Calendar.YEAR, (value < 71)? (value + 1900): (value + 2000));
      return true;
    }
    catch( NumberFormatException e)
    {
      return false;
    }
  }

  private final static DateFormat timeZoneFormat = new SimpleDateFormat( "z");
  
  private int formatIndex;
  private int parseIndex;

  public static void main( String[] args) throws Exception
  {
//    DateUtil util = new DateUtil();
//    String s = util.format( "{YEAR}/{MONTH}/{DAYDAY} {hh}:{mm}:{ss} {Z}", System.currentTimeMillis());
//    long t = util.parse( "{YEAR}/{MONTH}/{DAYDAY} {hh}:{mm}:{ss} {Z}", s);
//    System.out.println( s);
    
    DateFormat format = new SimpleDateFormat( "z");
    Date d = format.parse( "EST");
    Calendar cal = Calendar.getInstance();
    cal.setTime( d);
    System.out.println( cal.getTimeZone().getDisplayName());
   }
}
