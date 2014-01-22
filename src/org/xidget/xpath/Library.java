/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import org.xmodel.xpath.function.FunctionFactory;

/**
 * Register the XPath functions in this package.
 */
public class Library
{
  public static void register()
  {
    FunctionFactory.getInstance().register( CapitalizeFunction.name, CapitalizeFunction.class);
    FunctionFactory.getInstance().register( FileExistsFunction.name, FileExistsFunction.class);
    FunctionFactory.getInstance().register( FontsFunction.name, FontsFunction.class);
    FunctionFactory.getInstance().register( IsFolderFunction.name, IsFolderFunction.class);
    FunctionFactory.getInstance().register( ValidateXPathFunction.name, ValidateXPathFunction.class);
    FunctionFactory.getInstance().register( DateFormatFunction.name, DateFormatFunction.class);
    FunctionFactory.getInstance().register( DateParseFunction.name, DateParseFunction.class);
    FunctionFactory.getInstance().register( DateAddFunction.name, DateAddFunction.class);
    FunctionFactory.getInstance().register( DateSetFunction.name, DateSetFunction.class);
    FunctionFactory.getInstance().register( DateGetFunction.name, DateSetFunction.class);
    FunctionFactory.getInstance().register( DateNowFunction.name, DateNowFunction.class);
  }
}
