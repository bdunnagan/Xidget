/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IOptionalNodeFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.button.IButtonWidgetFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of ITagHandler for the <i>source</i> element.
 */
public class SourceTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature feature = parent.getFeature( IXidgetFeature.class);
    if ( feature == null) return false;
    
    IXidget xidget = feature.getXidget();
    return xidget != null && xidget.getFeature( ISourceFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    IXidget xidget = xidgetFeature.getXidget();
    
    String channel = Xlate.get( element, "channel", TextModelFeature.allChannel);
    
    // create expression
    String xpath = Xlate.get( element, "");
    if ( xpath.length() == 0) throw new TagException( "Empty expression in binding.");
    IExpression expression = XPath.createExpression( xpath);
    
    // configure optional node feature
    if ( Xlate.get( element, "optional", true))
    {
      IOptionalNodeFeature optionalNodeFeature = xidget.getFeature( IOptionalNodeFeature.class);
      if ( optionalNodeFeature != null) optionalNodeFeature.setSourceExpression( channel, expression);
    }

    // create binding
    IExpressionListener listener = new Listener( xidget, channel);
    XidgetBinding binding = new XidgetBinding( expression, listener);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.addBindingAfterChildren( binding);
    
    return true;
  }
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      this.xidget = xidget;
      this.channel = channel;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
      IModelObject source = expression.queryFirst( context);
      if ( source == sourceFeature.getSource( channel)) return;      
      
      sourceFeature.setSource( channel, source);
      
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, Xlate.get( source, ""));
      
      IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      if ( buttonWidgetFeature != null) buttonWidgetFeature.setState( Xlate.get( source, false));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
      IModelObject source = expression.queryFirst( context);
      if ( source == sourceFeature.getSource( channel)) return;      
      
      sourceFeature.setSource( channel, source);
      
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, Xlate.get( source, ""));
      
      IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      if ( buttonWidgetFeature != null) buttonWidgetFeature.setState( Xlate.get( source, false));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, Boolean.toString( newValue));
      
      IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      if ( buttonWidgetFeature != null) buttonWidgetFeature.setState( newValue);
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, Double.toString( newValue));
      
      IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      if ( buttonWidgetFeature != null)
      {
        boolean newState = (newValue != 0);
        boolean oldState = (oldValue != 0);
        if ( newState != oldState) buttonWidgetFeature.setState( newState);
      }
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, newValue);
      
      try
      {
        IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
        if ( buttonWidgetFeature != null)
        {
          boolean newState = Boolean.parseBoolean( newValue);
          boolean oldState = Boolean.parseBoolean( oldValue);
          if ( newState != oldState) buttonWidgetFeature.setState( newState);
        }
      }
      catch( Exception e)
      {
      }
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      ITextWidgetFeature textWidgetFeature = xidget.getFeature( ITextWidgetFeature.class);
      if ( textWidgetFeature != null) textWidgetFeature.setText( channel, Xlate.get( object, ""));
      
      try
      {
        IButtonWidgetFeature buttonWidgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
        if ( buttonWidgetFeature != null)
        {
          boolean newState = Boolean.parseBoolean( newValue.toString());
          boolean oldState = Boolean.parseBoolean( oldValue.toString());
          if ( newState != oldState) buttonWidgetFeature.setState( newState);
        }
      }
      catch( Exception e)
      {
      }
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private String channel;
    private IXidget xidget;
  }
}
