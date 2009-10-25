/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.layout.Margins;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * A base implementation of XAction for use with the various layout actions defined in this package.
 */
public abstract class AbstractLayoutAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    xidgetsExpr = document.getExpression( "xidgets", true);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    // get parent xidget
    IModelObject element = context.getObject();
    IXidget parent = (IXidget)element.getAttribute( "instance");
    
    // get parameters
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    Margins margins = layoutFeature.getMargins();
    int spacing = layoutFeature.getSpacing();
    
    // layout
    if ( xidgetsExpr == null)
    {
      if ( parent.getChildren().size() > 0)
        layout( context, parent, parent.getChildren(), margins, spacing);
    }
    else
    {
      List<IModelObject> elements = xidgetsExpr.evaluateNodes( context);
      List<IXidget> xidgets = new ArrayList<IXidget>( elements.size());
      for( IModelObject node: elements)
      {
        IXidget xidget = (IXidget)node.getAttribute( "instance");
        if ( xidget != null) xidgets.add( xidget);
      }
      layout( context, parent, xidgets, margins, spacing);
    }
    
    return null;
  }
  
  /**
   * Create the attachments for the children of the specified xidget.
   * @param context The context of the action.
   * @param parent The parent xidget.
   * @param children The xidget to be arranged.
   * @param margins The margins of the inside of the form.
   * @param spacing The spacing between xidgets.
   */
  protected abstract void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing);

  private IExpression xidgetsExpr;
}
