/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import java.text.ParseException;

/**
 * A class for formatting numbers using the Xidget formatting flags defined below. Formatting is divided
 * into an integer format specification and a fraction format specification. These are separated by one
 * of the following fraction separators: comma, period. The fraction separating character must differ
 * from the integer separating character used to delineate thousands, hundreds, etc.
 * 
 * Unlike %d, an integer specification may be applied to a floating point number. In this case, the 
 * rounding flags determine how the number is rounded to an integer.
 * 
 * The entire formatting specification is enclosed in braces like Xidget date/time formatting flags.
 * The specification has been defined so that number and date/time flags are a disjoint set.
 * 
 * Integer Formatting
 *  - Zero or more {*}
 *  - At least N, no padding {N}
 *  - At least N, space padding { N}
 *  - At least N, zero padding {0N}
 *  - Hex {Nx}
 *  - Oct {No}
 *  - Bin {Nb}
 *  - 36 {NZ}
 *  - 64 {Nz}
 *  - Round down, floor {Nx+}
 *  - Round up, ceil {Nz-}
 *  - Round half (default)
 *  - Minimum value {N>M} if value is <= M, returns "M" or "M-"
 *  - Minimum value {N>=M} if value is < M, returns "M-"
 *  - Maximum value {N<M} if value is >= M, return "M" or "M+"
 *  - Maximum value {N<=M} if value is > M, return "M+"
 *
 * Fraction Formatting
 *
 *  Integer part uses integer formatting.
 *
 *  Fractional part uses the following:
 *    - Number of decimal points
 *    - Round down 
 *    - Round up
 *    - Round half
 *    - Hex
 *    - Oct
 *    - Bin
 *    - 36
 *    - 64
 *    - Space padding
 *    - Zero padding
 *    - No padding
 *    All of the above, same as integer part.
 *
 *    - Scientific {Ne} always the last character in the format
 *    - Engineering {Ng} always the last character in the format
 *
 *    {03,*e} - scientific format, zero padded int with at least 3 places, arbitrary precision, comma-separated
 *    {*.*} - decimal format, arbitrary precision, no padding, period-seperated
 *    {3x.3x-} - hex format, round fraction down to 3 places
 *    {3x.3x-} - hex format, round fraction down to 3 places
 *     
 *  
 * Integer and fractional groupings are distinguished by the use of comma and period.
 * So, {N,03N.*} means thousands grouping of the integer and arbitrary precision. Whereas,
 * {N.02N,*} means hundreds grouping of the integer and arbitrary precision.  This can be
 * generalized by saying that if the last grouping character differs from the previous
 * grouping characters, then the last grouping character separates the fractional component.
 */
public final class NumberFormat
{
  /**
   * Create a NumberFormat with the specified format specification.
   * @param format The format specification.
   */
  public NumberFormat( String format) throws ParseException
  {
    this.format = parseFormat( format);
    
    if ( this.format.integer != null && this.format.fraction != null)
    {
      if ( this.format.integer.round != 0 && this.format.fraction.round != 0)
        throw new NumberFormatException( "Illegal rounding specifier in integer formatting.");
    }
  }
  
  /**
   * Format the specified number.
   * @param number The number to be formatted.
   * @return Returns the formatted number.
   */
  public String format( double number)
  {
    long integer = (long)number;
    double fraction = number - integer;
    StringBuilder sb = new StringBuilder();
    formatInteger( integer, sb);
    formatFraction( fraction, sb);
    return sb.toString();
  }

  /**
   * Format the specified integer.
   * @param integer The integer.
   * @param sb Returns the formatted string.
   */
  private void formatInteger( long integer, StringBuilder sb)
  {
    // min
    if ( format.integer.min != Long.MIN_VALUE && integer < format.integer.min)
    {
      integer = format.integer.min;
    }
    
    // max
    if ( format.integer.max != Long.MAX_VALUE && integer > format.integer.max)
    {
      integer = format.integer.max;
    }
    
    // convert to string
    String raw = Long.toString( integer, format.integer.base);
    if ( raw.charAt( 0) == '-')
    {
      sb.append( '-');
      raw.substring( 1);
    }
    
    // pad
    if ( format.integer.fill != 0)
    {
      for( int i=raw.length(); i<format.integer.length; i++)
        sb.append( format.integer.fill);
    }
    
    sb.append( raw);
  }
  
  /**
   * Format the specified fraction.
   * @param fraction The fraction.
   * @param sb Returns the formatted string.
   */
  private void formatFraction( double fraction, StringBuilder sb)
  {
  }
  
  /**
   * Parse the specified number.
   * @param number The number to be parsed.
   * @return Returns null or the parsed number.
   */
  public Number parse( String number)
  {
    return 0;
  }

  /**
   * Parse the format specification.
   * @param spec The format specification.
   * @return Returns the parsed format.
   */
  private Format parseFormat( String spec) throws ParseException
  {
    Format format = new Format();
    format.integer = new IntegerFormat();

    // parse integer common format
    parseCommonFormat( spec, format.integer);
    
    // integer min
    if ( parseIndex >= spec.length()) return format;
    if ( spec.charAt( parseIndex) == '>')
    {
      parseIndex++;
      format.integer.min = parseInteger( spec) + 1;
    }
    
    // integer max
    if ( parseIndex >= spec.length()) return format;
    if ( spec.charAt( parseIndex) == '<')
    {
      parseIndex++;
      format.integer.max = parseInteger( spec) - 1;
    }

    // no fraction formatting
    if ( parseIndex >= spec.length()) return format;
    format.fraction = new FractionFormat();
    
    // fraction separator char
    char c = spec.charAt( parseIndex);
    if ( c != '.' && c != ',') throw new ParseException( spec, parseIndex);
    format.fraction.separator = c;
    
    // parse fraction common format
    parseCommonFormat( spec, format.fraction);
    
    return format;
  }

  /**
   * Parse the next CommonFormat from the specification.
   * @param spec The format specification.
   * @param format The parsed CommonFormat.
   */
  private void parseCommonFormat( String spec, CommonFormat format) throws ParseException
  {
    // fill char
    parseIndex = 0;
    char c = spec.charAt( parseIndex);
    if ( c == '0' || c == ' ') { format.fill = c; parseIndex++;}
    
    // length
    if ( spec.charAt( parseIndex) == '*')
    {
      format.length = 0;
      parseIndex++;
    }
    else
    {
      format.length = parseInteger( spec);
    }
    
    // base (or possibly style)
    if ( parseIndex >= spec.length()) return;
    c = spec.charAt( parseIndex);
    if ( Character.isLetter(  c))
    {
      format.base = parseBase( c);
      if ( format.base < 0) 
      {
        if ( parseStyle( c) == null)
          throw new ParseException( spec, parseIndex);
      }
      parseIndex++;
    }
    
    // rounding
    if ( parseIndex >= spec.length()) return;
    c = spec.charAt( parseIndex);
    if ( c == '+' || c == '-') 
    {
      format.round = c;
      parseIndex++;
    }
    
    // parse style
    if ( parseIndex >= spec.length()) return;
    format.style = parseStyle( spec.charAt( parseIndex));
    if ( format.style != null) parseIndex++;
  }
  
  /**
   * Parse the next integer from the specified string.
   * @param s The string.
   * @return Returns the integer.
   */
  private int parseInteger( String s) throws ParseException
  {
    try 
    { 
      int start = parseIndex;
      for( ; parseIndex < s.length() && Character.isDigit( s.charAt( parseIndex)); parseIndex++);
      return Integer.parseInt( s.substring( start, parseIndex));
    } 
    catch( NumberFormatException e) 
    {
      throw new ParseException( s, parseIndex);
    }
  }
  
  /**
   * Parse the base-specifier character.
   * @param c The character.
   * @return Returns the base.
   */
  private int parseBase( char c)
  {
    switch( c)
    {
      case 'b': return 2;
      case 'o': return 8;
      case 'x': return 16;
      case 'Z': return 36;
      //case 'z': return 64;
    }
    return -1;
  }
  
  /**
   * Parse the style-specifier character.
   * @param c The character.
   * @return Returns null or the style.
   */
  private Style parseStyle( char c)
  {
    switch( c)
    {
      case 'e': return Style.engineering;
      case 'g': return Style.scientific;
    }
    return null;
  }

  private Format format;
  private int parseIndex;
  
  private final static class Format
  {
    public IntegerFormat integer;
    public FractionFormat fraction;
  }
  
  public static enum Style { floating, scientific, engineering};
  
  private static class CommonFormat
  {
    public int length;
    public char fill;
    public int base = 10;
    public char round;
    public Style style = Style.floating;
  }
  
  private final static class IntegerFormat extends CommonFormat
  {
    public long min = Long.MIN_VALUE;
    public long max = Long.MAX_VALUE;
  }
  
  public final static class FractionFormat extends CommonFormat
  {
    public char separator;
  }
  
  public static void main( String[] args) throws Exception
  {
    NumberFormat format = new NumberFormat( "*x");
    double[] tests = new double[] { 1, 36, 999, 1e-10, 1e+10};
    for( double test: tests)
    {
      System.out.printf( "|%s| %f\n", format.format( test), test);
    }
  }
}
