/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.expression.IExpression.ResultType;

/**
 * A conditional xidget binding system. The system is configured with one or more xidgets with
 * corresponding boolean expressions which define when the xidget should be bound.  The boolean
 * expressions are bound to the same context as the xidgets.
 */
public class ConfigurationSwitch<T> extends ExpressionListener
{
  public ConfigurationSwitch( IListener<T> listener)
  {
    this.listener = listener;
    cases = new HashMap<IExpression, T>();
    ignore = false;
  }

  /**
   * Set the default configuration handler.
   * @param handler Null or the default configuration handler.
   */
  public void setDefaultHandler( T handler)
  {
    defaultHandler = handler;
  }
  
  /**
   * Add a handler to the switch.
   * @param condition A boolean expression indicating when the handler will be used.
   * @param handler The handler associated with the configuration.
   */
  public void addCase( IExpression condition, T handler)
  {
    if ( condition.getType() != ResultType.BOOLEAN) 
      throw new IllegalArgumentException( "Expression is not boolean: "+condition);
    cases.put( condition, handler);
  }
  
  /**
   * Remove the case corresponding to the specified expression.
   * @param condition The boolean expression registered with the <code>addCase</code> method.
   */
  public void removeCase( IExpression condition)
  {
    cases.remove( condition);
  }

  /**
   * Returns the number of cases.
   * @return Returns the number of cases.
   */
  public int getCaseCount()
  {
    return cases.size();
  }
  
  /**
   * Returns the handlers associated with all cases.
   * @return Returns the handlers associated with all cases.
   */
  public List<T> getHandlers()
  {
    if ( defaultHandler != null)
    {
      List<T> result = new ArrayList<T>( cases.values());
      result.add( 0, defaultHandler);
      return result;
    }
    else
    {
      return new ArrayList<T>( cases.values());
    }
  }

  /**
   * Bind the switch and notify listeners of the matching case.
   * @param context The context.
   */
  public void bind( StatefulContext context)
  {
    try
    {
      IExpression condition = findMatchingCase( context);
      if ( condition == null)
      {
        bindAllCases( context);
        if ( defaultHandler != null)
        {
          listener.notifyMatch( context, defaultHandler);
        }
      }
      else
      {
        T handler = cases.get( condition);
        listener.notifyMatch( context, handler);
        bindCase( context, condition);
      }
    }
    catch( ExpressionException e)
    {
      Log.printf( "handler", "Failed to evaluate condition for ConditionalSwitch: %s\n", e);
    }
  }

  /**
   * Unbind the switch.
   * @param context The context.
   */
  public void unbind( StatefulContext context)
  {
    try
    {
      IExpression condition = findMatchingCase( context);
      if ( condition == null)
      {
        unbindAllCases( context);
        if ( defaultHandler != null)
        {
          listener.notifyMismatch( context, defaultHandler);
        }
      }
      else
      {
        T handler = cases.get( condition);
        listener.notifyMismatch( context, handler);
        unbindCase( context, condition);
      }
    }
    catch( ExpressionException e)
    {
      Log.printf( "handler", "Failed to evaluate condition for ConditionalSwitch: %s\n", e);
    }
  }
  
  /**
   * Returns the currently selected case.
   * @param context THe context.
   * @return Returns the currently selected case.
   */
  public T getSelectedCase( IContext context)
  {
    try
    {
      IExpression condition = findMatchingCase( context);
      if ( condition != null) return cases.get( condition);
    }
    catch( ExpressionException e)
    {
      Log.printf( "handler", "Failed to evaluate condition for ConditionalSwitch: %s\n", e);
    }
    return null;
  }
  
  /**
   * Find the condition which matches the specified object.
   * @param context The context to be tested.
   * @return Returns the condition which matches the specified object or null.
   */
  private IExpression findMatchingCase( IContext context) throws ExpressionException
  {
    for( IExpression condition: cases.keySet())
      if ( condition.evaluateBoolean( context)) return condition;
    return null;
  }
  
  /**
   * Bind all the conditions to the specified object because none of them currently match.
   * @param context The context object to which the conditions will be bound.
   */
  private void bindAllCases( IContext context)
  {
    for( IExpression condition: cases.keySet())
      bindCase( context, condition);
  }
  
  /**
   * Unbind all the conditions to the specified object because none of them currently match.
   * @param context The context object to which the conditions will be bound.
   */
  private void unbindAllCases( IContext context)
  {
    for( IExpression condition: cases.keySet())
      unbindCase( context, condition);
  }
  
  /**
   * Bind a single condition to the specified context.
   * @param context The context to which the condition will be bound.
   * @param condition The condition to be bound.
   */
  private void bindCase( IContext context, IExpression condition)
  {
    condition.addListener( context, this);
  }
  
  /**
   * Unbind a single condition from the specified context.
   * @param context The context from which the condition will be unbound.
   * @param condition The condition to be unbound.
   */
  private void unbindCase( IContext context, IExpression condition)
  {
    condition.removeListener( context, this);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.IExpressionListener#notifyChange(
   * org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, boolean)
   */
  public void notifyChange( IExpression expression, IContext context, boolean newValue)
  {
    if ( ignore) return;
    
    if ( newValue)
    {
      // all conditions are bound and non-matching conditions need to be unbound
      for( IExpression otherCondition: cases.keySet())
        if ( otherCondition != expression)
          unbindCase( context, otherCondition);
      
      // notify mismatch of default model
      if ( defaultHandler != null)
      {
        ignore = true;
        listener.notifyMismatch( (StatefulContext)context, defaultHandler);
        ignore = false;
      }
      
      // notify match
      T handler = cases.get( expression);
      if ( handler != null)
      {
        ignore = true;
        listener.notifyMatch( (StatefulContext)context, handler);
        ignore = false;
      }
    }
    else
    {
      // notify mismatch 
      T handler = cases.get( expression);
      if ( handler != null) 
      {
        ignore = true;
        listener.notifyMismatch( (StatefulContext)context, handler);
        ignore = false;
      }
      
      // exactly one condition was matching but no longer matches
      try
      {
        IExpression condition = findMatchingCase( context);
        if ( condition == null)
        {
          // bind all conditions except the one which is already bound
          for ( IExpression otherCondition: cases.keySet())
            if ( otherCondition != expression)
              bindCase( context, otherCondition);
          
          // notify match of default model
          if ( defaultHandler != null)
          {
            ignore = true;
            listener.notifyMatch( (StatefulContext)context, defaultHandler);
            ignore = false;
          }
        }
        else
        {
          // bind the condition which was found
          bindCase( context, condition);
          
          // notify match
          ignore = true;
          T selectedHandler = cases.get( condition);
          if ( selectedHandler != null) 
          {
            listener.notifyMatch( (StatefulContext)context, selectedHandler);
          }
          ignore = false;
        }
      }
      catch( ExpressionException e)
      {
        Log.printf( "handler", "Unable to evaluate conditional model expression: %s\n", e);
      }
    }
  }

  /**
   * An interface for receiving configuration switch notifications.
   */
  public interface IListener<T>
  {
    /**
     * Called when the specified handler's case is matched.
     * @param context The context.
     * @param handler The handler.
     */
    public void notifyMatch( StatefulContext context, T handler);
    
    /**
     * Called when the specified handler's case does not match.
     * @param context The context.
     * @param handler The handler.
     */
    public void notifyMismatch( StatefulContext context, T handler);
  }

  private IListener<T> listener;
  private T defaultHandler;
  private Map<IExpression, T> cases;
  private boolean ignore;
}
