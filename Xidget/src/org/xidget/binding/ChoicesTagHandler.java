/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IBindFeature;
import org.xidget.ifeature.IXidgetFeature;
import org.xidget.text.feature.IChoiceListFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.diff.AbstractListDiffer;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of ITagHandler for the <i>choices</i> element. This element
 * can specify choices in one of two ways: using an expression, or by the elements
 * children.
 */
public class ChoicesTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    if ( element.getNumberOfChildren() > 0)
    {
      IChoiceListFeature feature = xidget.getFeature( IChoiceListFeature.class);
      for( IModelObject child: element.getChildren())
        feature.addChoice( Xlate.get( child, ""));
    }
    else
    {
      // create expression
      String xpath = Xlate.get( element, "");
      if ( xpath.length() == 0)
        throw new TagException(
          "Empty expression in binding.");
      
      IExpression expression = XPath.createExpression( xpath);
      
      // create binding
      IExpressionListener listener = new Listener( xidget);
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      if ( bindFeature != null) bindFeature.add( binding);
    }
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#exit(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#filter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }
  
  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.feature = xidget.getFeature( IChoiceListFeature.class);
      this.differ = new ListDiffer();
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      updateChoices( nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      updateChoices( nodes);
    }
    
    private void updateChoices( List<IModelObject> nodes)
    {
      List<String> rhs = new ArrayList<String>();
      for( IModelObject node: nodes) rhs.add( Xlate.get( node, ""));
      
      // update choices
      List<String> lhs = feature.getChoices();
      differ.diff( lhs, rhs);
    }

    @SuppressWarnings("unchecked")
    private class ListDiffer extends AbstractListDiffer
    {
      /* (non-Javadoc)
       * @see org.xmodel.diff.IListDiffer#notifyInsert(java.util.List, int, int, java.util.List, int, int)
       */
      public void notifyInsert( List lhs, int lIndex, int lAdjust, List rhs, int rIndex, int rCount)
      {
        int index = lIndex + lAdjust;
        for( int i=0; i<rCount; i++, index++) feature.insertChoice( index, rhs.get( rIndex + i).toString());
      }

      /* (non-Javadoc)
       * @see org.xmodel.diff.IListDiffer#notifyRemove(java.util.List, int, int, java.util.List, int)
       */
      public void notifyRemove( List lhs, int lIndex, int lAdjust, List rhs, int rCount)
      {
        for( int i=0; i<rCount; i++) feature.removeChoice( lIndex + lAdjust);
      }
    }
    
    private ListDiffer differ;
    private IChoiceListFeature feature;
  }
}
