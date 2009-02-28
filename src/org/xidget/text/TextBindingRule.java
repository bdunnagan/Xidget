/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import java.util.List;
import org.xidget.IBindingRule;
import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the text of a text widget.
 */
public class TextBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( (ITextWidgetAdapter)xidget.getAdapter( ITextWidgetAdapter.class)); 
  }

  final class Listener extends ExpressionListener
  {
    Listener( ITextWidgetAdapter adapter)
    {
      this.adapter = adapter;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( nodes.contains( source)) return;
      source = nodes.get( 0);
      adapter.setText( Xlate.get( source, ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( !nodes.contains( source)) return;
      source = expression.queryFirst( context);
      adapter.setText( Xlate.get( source, ""));      
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      adapter.setText( Boolean.toString( newValue));
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      adapter.setText( Double.toString( newValue));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      adapter.setText( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      adapter.setText( Xlate.get( object, ""));
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private ITextWidgetAdapter adapter;
    private IModelObject source;
  }
}
