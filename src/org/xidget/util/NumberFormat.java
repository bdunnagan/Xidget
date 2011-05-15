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
public class NumberFormat
{
  /**
   * Create a NumberFormat with the specified format specification.
   * @param format The format specification.
   */
  public NumberFormat( String format) throws ParseException
  {
    this.format = parseFormat( format);
  }
  
  /**
   * Format the specified number.
   * @param number The number to be formatted.
   * @return Returns the formatted number.
   */
  public String format( double number)
  {
  }
  
  /**
   * Parse the specified number.
   * @param number The number to be parsed.
   * @return Returns null or the parsed number.
   */
  public Number parse( String number)
  {
  }

  /**
   * Parse the format specification.
   * @param spec The format specification.
   * @return Returns the parsed format.
   */
  private Format parseFormat( String spec) throws ParseException
  {
    Format format = new Format();

    // parse integer common format
    parseCommonFormat( spec, format.integer);
    
    // integer min
    if ( spec.charAt( parseIndex) == '>')
    {
      parseIndex++;
      if ( spec.charAt( parseIndex) == '=') { format.integer.minInclusive = true; parseIndex++;}
      format.integer.min = parseInteger( spec);
    }
    
    // integer max
    if ( spec.charAt( parseIndex) == '<')
    {
      parseIndex++;
      if ( spec.charAt( parseIndex) == '=') { format.integer.maxInclusive = true; parseIndex++;}
      format.integer.max = parseInteger( spec);
    }

    // no fraction specification
    if ( parseIndex >= spec.length()) return format;
    
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
    c = spec.charAt( parseIndex);
    if ( c == '+' || c == '-') 
    {
      format.round = c;
      parseIndex++;
    }
    
    // parse style
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
      case 'z': return 64;
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
  
  private static class CommonFormat
  {
    public int length;
    public char fill;
    public int base;
    public char round;
    public Style style;
  }
  
  private final static class IntegerFormat extends CommonFormat
  {
    public int min;
    public boolean minInclusive;
    public int max;
    public boolean maxInclusive;
  }
  
  public static enum Style { floating, scientific, engineering};
  
  public final static class FractionFormat extends CommonFormat
  {
    public char separator;
  }
}
