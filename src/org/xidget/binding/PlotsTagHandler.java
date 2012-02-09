/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.chart.PlotsListener;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ITagHandler that configures a set of plots, as defined by org.xidget.chart.Plot.
 * An expression defines the set of plots.  For each plot, an ordered list defines the points in the plot.
 * Coordinate expressions define the coordinates of each point in the plot, and detail expressions defined
 * the display characteristics of those points.
 */
public class PlotsTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    IExpression plotsExpr = Xlate.get( element, "source", Xlate.childGet( element, "source", (IExpression)null));
    if ( plotsExpr != null)
    {
      PlotsListener plotsListener = new PlotsListener( xidget);

      // plot expressions
      plotsListener.setPlotForegroundExpression( Xlate.get( element, "fcolor", Xlate.childGet( element, "fcolor", (IExpression)null)));
      plotsListener.setPlotBackgroundExpression( Xlate.get( element, "bcolor", Xlate.childGet( element, "bcolor", (IExpression)null)));
      plotsListener.setPlotStrokeExpression( Xlate.get( element, "stroke", Xlate.childGet( element, "stroke", (IExpression)null)));

      // point expressions
      IModelObject pointsElement = element.getFirstChild( "points");
      plotsListener.setPointsExpression( Xlate.get( pointsElement, "source", Xlate.childGet( pointsElement, "source", (IExpression)null)));
      plotsListener.setCoordsExpression( Xlate.get( pointsElement, "coords", Xlate.childGet( pointsElement, "coords", (IExpression)null)));
      plotsListener.setPointLabelExpression( Xlate.get( pointsElement, "label", Xlate.childGet( pointsElement, "label", (IExpression)null)));
      plotsListener.setPointForegroundExpression( Xlate.get( pointsElement, "fcolor", Xlate.childGet( pointsElement, "fcolor", (IExpression)null)));
      plotsListener.setPointBackgroundExpression( Xlate.get( pointsElement, "bcolor", Xlate.childGet( pointsElement, "bcolor", (IExpression)null)));
      
      // alternative coord expressions (since sequences are not implemented yet)
      IExpression[] coordExprs = getCoordinateExpressions( pointsElement);
      plotsListener.setCoordExpressions( coordExprs);
      
      XidgetBinding binding = new XidgetBinding( plotsExpr, plotsListener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    
    return false;
  }
  
  /**
   * Returns the coordinate expressions from the specified points element.
   * @param pointsElement The points element.
   * @return Returns the coordinate expressions from the specified points element.
   */
  private IExpression[] getCoordinateExpressions( IModelObject pointsElement)
  {
    List<IModelObject> coordNodes = pointsElement.getChildren( "coord");
    IExpression[] coordExprs = new IExpression[ coordNodes.size()];
    for( int i=0; i<coordExprs.length; i++)
    {
      coordExprs[ i] = Xlate.get( coordNodes.get( i), (IExpression)null);
    }
    return coordExprs;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    return null;
  }
}