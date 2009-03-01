/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.IXidget;
import org.xidget.adapter.IErrorAdapter;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A class which manages the transformation of text between a widget and its datamodel.
 * This class is also responsible for validating the text and publishing errors.
 */
final class TextChannel implements IWidgetTextChannel.IListener
{
  /**
   * Create a text processor for the specified xidget and widget channel.
   * @param xidget The xidget.
   * @param widgetChannel The widget.
   */
  TextChannel( IXidget xidget, IWidgetTextChannel widgetChannel)
  {
    this.widgetChannel = widgetChannel;
    this.errorAdapter = (IErrorAdapter)xidget.getAdapter( IErrorAdapter.class);
    this.context = new StatefulContext();
  }
  
  /**
   * Returns the widget text channel.
   * @return Returns the widget text channel.
   */
  IWidgetTextChannel getWidgetChannel()
  {
    return widgetChannel;
  }
  
  /**
   * Set the datamodel node.
   * @param source The datamodel node.
   */
  void setSource( IModelObject source)
  {
    this.source = source;
    setTextFromModel( Xlate.get( source, ""));
  }
  
  /**
   * Returns the datamodel node.
   * @return Returns the datamodel node.
   */
  IModelObject getSource()
  {
    return source;
  }
  
  /**
   * Process text entered from a widget.
   * @param text The text.
   */
  void setTextFromWidget( String text)
  {
    if ( updating || source == null) return;
    
    // transform
    if ( xoutExpr != null)
    {
      context.set( "value", text);
      text = xoutExpr.evaluateString( context);
    }
    
    // validate
    if ( validator != null)
    {
      String error = validator.validate( text);
      if ( error != null) errorAdapter.valueError( error);
    }
    
    // write to model
    try
    {
      updating = true;
      source.setValue( text);
    }
    finally
    {
      updating = false;
    }
  }
  
  /**
   * Process text changed in the datamodel.
   * @param text The text.
   */
  void setTextFromModel( String text)
  {
    if ( updating) return;
    
    // validate
    if ( validator != null)
    {
      String error = validator.validate( text);
      if ( error != null) errorAdapter.valueError( error);
    }
    
    // transform
    if ( xinExpr != null)
    {
      context.set( "value", text);
      text = xinExpr.evaluateString( context);
    }
    
    // write to widget
    try
    {
      updating = true;
      widgetChannel.setText( text);
    }
    finally
    {
      updating = false;
    }
  }
  
  /**
   * Set the transform from widget text to datamodel text.
   * @param transform The transform.
   */
  void setInputTransform( IExpression transform)
  {
    xinExpr = transform;
  }
  
  /**
   * Set the transform from datamodel text to widget text.
   * @param transform The transform.
   */
  void setOutputTransform( IExpression transform)
  {
    xoutExpr = transform;
  }
  
  /**
   * Set the text validator.
   * @param validator The text validator.
   */
  void setValidator( ITextValidator validator)
  {
    this.validator = validator;
  }
    
  /* (non-Javadoc)
   * @see org.xidget.text.IWidgetTextChannel.IListener#onTextChanged(java.lang.String)
   */
  public void onTextChanged( String text)
  {
    setTextFromWidget( text);
  }

  private IWidgetTextChannel widgetChannel;
  private IModelObject source;
  private StatefulContext context;
  private IExpression xinExpr;
  private IExpression xoutExpr;
  private ITextValidator validator;
  private IErrorAdapter errorAdapter;
  private boolean updating;
}
