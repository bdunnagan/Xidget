/*
 * Stonewall Networks, Inc.
 *
 *   Project: XXidgetPlugin
 *   Author:  bdunnagan
 *   Date:    May 5, 2005
 *
 * Copyright 2005.  Stonewall Networks, Inc.
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
public class XidgetSwitch extends ExpressionListener
{
  public XidgetSwitch()
  {
    cases = new HashMap<IExpression, IXidget>();
    ignore = false;
  }
  
  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.model.IConditionalXidget#setDefaultXidget(dunnagan.bob.xmodel.ui.model.ViewXidget)
   */
  public void setDefaultXidget( IXidget xidget)
  {
    defaultXidget = xidget;
  }

  /**
   * Add a xidget to the switch.
   * @param condition A boolean expression indicating when the xidget should be bound.
   * @param xidget The xidget to be bound.
   */
  public void addCase( IExpression condition, IXidget xidget)
  {
    if ( condition.getType() != ResultType.BOOLEAN) 
      throw new IllegalArgumentException( "XidgetSwitch expression is not boolean: "+condition);
    cases.put( condition, xidget);
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
   * Returns the xidgets associated with all cases.
   * @return Returns the xidgets associated with all cases.
   */
  public List<IXidget> getXidgets()
  {
    if ( defaultXidget != null)
    {
      List<IXidget> result = new ArrayList<IXidget>( cases.values());
      result.add( 0, defaultXidget);
      return result;
    }
    else
    {
      return new ArrayList<IXidget>( cases.values());
    }
  }

  /**
   * Bind the switch. This will determine the matching case and bind it.
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
        if ( defaultXidget != null) defaultXidget.bind( context);
      }
      else
      {
        IXidget xidget = cases.get( condition);
        xidget.bind( context);
        bindCase( context, condition);
      }
    }
    catch( ExpressionException e)
    {
      Log.printf( "xidget", "Failed to evaluate condition for XidgetSwitch: %s\n", e);
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
        if ( defaultXidget != null) defaultXidget.unbind( context);
      }
      else
      {
        IXidget xidget = cases.get( condition);
        xidget.unbind( context);
        unbindCase( context, condition);
      }
    }
    catch( ExpressionException e)
    {
      Log.printf( "xidget", "Failed to evaluate condition for XidgetSwitch: %s\n", e);
    }
  }
  
  /**
   * Returns the currently selected case.
   * @param context THe context.
   * @return Returns the currently selected case.
   */
  public IXidget getSelectedCase( IContext context)
  {
    try
    {
      IExpression condition = findMatchingCase( context);
      if ( condition != null) return cases.get( condition);
    }
    catch( ExpressionException e)
    {
      Log.printf( "xidget", "Failed to evaluate condition for XidgetSwitch: %s\n", e);
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
      if ( defaultXidget != null)
      {
        ignore = true;
        defaultXidget.unbind( (StatefulContext)context);
        ignore = false;
      }
      
      // notify match
      IXidget xidget = cases.get( expression);
      if ( xidget != null)
      {
        ignore = true;
        xidget.bind( (StatefulContext)context);
        ignore = false;
      }
    }
    else
    {
      // notify mismatch 
      IXidget xidget = cases.get( expression);
      if ( xidget != null) 
      {
        ignore = true;
        xidget.unbind( (StatefulContext)context);
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
          if ( defaultXidget != null)
          {
            ignore = true;
            defaultXidget.bind( (StatefulContext)context);
            ignore = false;
          }
        }
        else
        {
          // bind the condition which was found
          bindCase( context, condition);
          
          // notify match
          ignore = true;
          IXidget selectedXidget = cases.get( condition);
          if ( selectedXidget != null) selectedXidget.bind( (StatefulContext)context);
          ignore = false;
        }
      }
      catch( ExpressionException e)
      {
        Log.printf( "xidget", "Unable to evaluate conditional model expression: %s\n", e);
      }
    }
  }

  private IXidget defaultXidget;
  private Map<IExpression, IXidget> cases;
  private boolean ignore;
}
