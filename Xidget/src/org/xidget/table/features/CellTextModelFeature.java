/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.feature.IErrorFeature;
import org.xidget.text.ITextValidator;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ITextModelFeature which delegates to an instance of TextModelFeature for each 
 * editable cell. This implementation only supports one channel.
 */
public class CellTextModelFeature implements ITextModelFeature
{
  public CellTextModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.modelFeature = xidget.getParent().getFeature( ITableModelFeature.class);
    this.rowSetFeature = xidget.getParent().getFeature( IRowSetFeature.class);

    // determine column index from config element
    IModelObject element = xidget.getConfig();
    this.column = element.getParent().getChildren( element.getType()).indexOf( element);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextModelFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    if ( channel.equals( TextModelFeature.allChannel))
      return modelFeature.getSource( rowSetFeature.getCurrentRow(), column, channel);
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextModelFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( String channel, IModelObject node)
  {
    if ( channel.equals( TextModelFeature.allChannel))
      modelFeature.setSource( rowSetFeature.getCurrentRow(), column, channel, node);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextModelFeature#setText(java.lang.String, java.lang.String)
   */
  public void setText( String channel, String text)
  {
    if ( channel.equals( TextModelFeature.allChannel) && !updating)
    {
      // transform
      if ( transform != null) 
        text = transform.transform( text);
      
      // validate
      IErrorFeature errorAdapter = xidget.getFeature( IErrorFeature.class);
      if ( errorAdapter != null && validator != null)
      {
        String error = validator.validate( text);
        if ( error != null) errorAdapter.valueError( error);
      }
      
      // commit
      try
      {
        updating = true;
        modelFeature.setText( rowSetFeature.getCurrentRow(), column, channel, text);
      }
      finally
      {
        updating = false;
      }
    }    
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextModelFeature#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    if ( channel.equals( TextModelFeature.allChannel))
      transform = new TextTransform( expression);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextModelFeature#setValidator(java.lang.String, org.xidget.text.ITextValidator)
   */
  public void setValidator( String channel, ITextValidator validator)
  {
    if ( channel.equals( TextModelFeature.allChannel))
      this.validator = validator;
  }
  
  private IXidget xidget;
  private ITableModelFeature modelFeature;
  private IRowSetFeature rowSetFeature;
  private TextTransform transform;
  private ITextValidator validator;
  private boolean updating;
  private int column;
}

