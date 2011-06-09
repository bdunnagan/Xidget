/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IValueFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An abstract implementation of IValueFeature that implements validation and transformation via
 * the onDisplay, onCommit and onValidate scripts. Subclasses override an abstract method to 
 * get and set the value from the widget.
 */
public abstract class AbstractValueFeature implements IValueFeature
{
  protected AbstractValueFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }

  /**
   * Set the transformed value to be directly displayed by the xidget.
   * @param value The transformed value.
   */
  protected abstract void setValue( Object value);
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#setValue(java.lang.Object)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void display( Object value)
  {
    if ( updating) return;
    updating = true;

    //System.out.printf( "%s display %s\n", xidget, value);
    
    try
    {
      IScriptFeature feature = xidget.getFeature( IScriptFeature.class);
      if ( feature != null)
      {
        StatefulContext context = getValueContext( value);
        if ( context == null) return;
        
        Object[] result = feature.runScript( "onDisplay", context);
        if ( result == null)
        {
          // use raw value or new value of $v
          setValue( context.get( "v"));
        }
        else
        {
          // use returned value
          Object object = result[ 0];
          if ( object instanceof List)
          {
            List<IModelObject> list = (List<IModelObject>)object;
            if ( list.size() > 0)
            {
              setValue( list.get( 0).getValue());
            }
            else
            {
              setValue( "");
            }
          }
          else
          {
            setValue( object);
          }
        }
      }
      else
      {
        setValue( value);
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#commit()
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean commit()
  {
    if ( updating) return true;
    updating = true;
    
    try
    {
      ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
      if ( sourceFeature == null) return false;
  
      IModelObject node = sourceFeature.getSource();
      if ( node == null) return false;
      
      Object value = getValue();
      //System.out.printf( "%s commit %s\n", xidget, value);      
      
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( scriptFeature != null)
      {
        StatefulContext context = getValueContext( value);
        if ( context == null) return false;
        
        // validate
        Object[] result = scriptFeature.runScript( "onValidate", context);
        if ( result == null || ((Boolean)result[ 0]))
        {
          // commit
          result = scriptFeature.runScript( "onCommit", context);
          if ( result == null)
          {
            // use raw value or new value of $v
            node.setValue( context.get( "v"));
            return true;
          }
          else
          {
            // use returned value
            Object object = result[ 0];
            if ( object instanceof List)
            {
              List<IModelObject> list = (List<IModelObject>)object;
              if ( list.size() > 0)
              {
                setValue( list.get( 0).getValue());
              }
              else
              {
                setValue( "");
              }
            }
            else
            {
              setValue( object);
            }
            return true;
          }
        }
      }
      else
      {
        node.setValue( value);
        return true;
      }
      
      return false;
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#validate(java.lang.Object)
   */
  @Override
  public boolean validate( Object value)
  {
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    if ( scriptFeature != null)
    {
      StatefulContext context = getValueContext( value);
      if ( context == null) return false;
      
      Object[] result = scriptFeature.runScript( "onValidate", context);
      if ( result == null) return true;
      if ( !((Boolean)result[ 0])) return false;
    }
    return true;
  }

  /**
   * @return Returns the bound context.
   */
  private StatefulContext getContext()
  {
    IBindFeature feature = xidget.getFeature( IBindFeature.class);
    return feature.getBoundContext();
  }

  /**
   * Returns a nested context with the $v variable set to the specified value.
   * @param value The value.
   * @return Returns the nested context.
   */
  private StatefulContext getValueContext( Object value)
  {
    StatefulContext parent = getContext();
    if ( parent == null) return null;
    
    StatefulContext context = new StatefulContext( parent);
    if ( value instanceof Number) context.set( "v", (Number)value);
    else if ( value instanceof Boolean) context.set( "v", (Boolean)value);
    else context.set( "v", (value != null)? value.toString(): "");
    
    return context;
  }
  
  protected IXidget xidget;
  private boolean updating;
}
