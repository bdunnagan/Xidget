/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;
import org.xmodel.listeners.ListDetailListener;
import org.xmodel.listeners.NodeValueListener;
import org.xmodel.listeners.SetDetailListener;
import org.xmodel.log.Log;
import org.xmodel.xpath.expression.ExactExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Listener for nodes that contain an ordered list of points to be plotted.
 */
public class PlotsListener extends SetDetailListener
{
  public PlotsListener( IXidget xidget)
  {
    this.xidget = xidget;
    this.plotMap = new HashMap<IModelObject, Plot>();
    this.pointMap = new HashMap<IModelObject, Point>();
    this.pointsListener = new PointsListener();
  }

  /**
   * Set the expression that returns the nodes representing the points in the plot.
   * @param expression The point nodes expression.
   */
  public void setPointsExpression( IExpression expression)
  {
    this.pointsExpr = expression;
  }
  
  /**
   * Set the expression that returns the nodes representing the point coordinates.
   * @param expression The expression.
   */
  public void setCoordsExpression( IExpression expression)
  {
    if ( expression != null) pointsListener.addDetail( expression, new CoordsListener());
  }
  
  /**
   * Set the expressions that specify the coordinates of the point.
   * @param expressions The expressions in order of coordinate.
   */
  public void setCoordExpressions( IExpression[] expressions)
  {
    pointsListener.dim = expressions.length;
    for( int i=0; i<expressions.length; i++)
    {
      pointsListener.addDetail( expressions[ i], new CoordListener2( i));
    }
  }

  /**
   * Set the expression that returns the plot stroke line width.
   * @param expression The expression.
   */
  public void setPlotStrokeExpression( IExpression expression)
  {
    if ( expression != null) addDetail( expression, new PlotStrokeListener());
  }
  
  /**
   * Set the expression that returns the foreground color for a plot.
   * @param expression The expression.
   */
  public void setPlotForegroundExpression( IExpression expression)
  {
    if ( expression != null) addDetail( expression, new PlotForegroundListener());
  }
  
  /**
   * Set the expression that returns the background color for a plot.
   * @param expression The expression.
   */
  public void setPlotBackgroundExpression( IExpression expression)
  {
    if ( expression != null) addDetail( expression, new PlotBackgroundListener());
  }
  
  /**
   * Set the expression that returns the foreground color for a point.
   * @param expression The expression.
   */
  public void setPointForegroundExpression( IExpression expression)
  {
    if ( expression != null) pointsListener.addDetail( expression, new PointForegroundListener());
  }
  
  /**
   * Set the expression that returns the background color for a point.
   * @param expression The expression.
   */
  public void setPointBackgroundExpression( IExpression expression)
  {
    if ( expression != null) pointsListener.addDetail( expression, new PointBackgroundListener());
  }

  /**
   * Set the expression that returns the label for a point.
   * @param expression The expression.
   */
  public void setPointLabelExpression( IExpression expression)
  {
    if ( expression != null) pointsListener.addDetail( expression, new LabelListener());
  }
  
  /**
   * Returns the specified value object as a double.
   * @param value The value object.
   * @return Returns the specified value object as a double.
   */
  private static double getDoubleValue( Object value)
  {
    if ( value instanceof Number)
    {
      return ((Number)value).doubleValue();
    }
    else if ( value instanceof Boolean)
    {
      return ((Boolean)value)? 1: 0;
    }
    else if ( value != null)
    {
      try { return Double.parseDouble( value.toString());} catch( Exception e) {}
    }
      
    return 0;
  }
  
  /**
   * @return Returns the IPlotFeature.
   */
  private final IPlotFeature getPlotFeature()
  {
    if ( plotFeature == null) plotFeature = xidget.getFeature( IPlotFeature.class);
    return plotFeature;
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    for( IModelObject node: nodes)
    {
      Plot plot = new Plot();
      plotMap.put( node, plot);

      binding = true;
      pointsExpr.addNotifyListener( new StatefulContext( context, node), pointsListener);
      binding = false;

      getPlotFeature().addPlot( plot);
    }
    
    super.notifyAdd( expression, context, nodes);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    super.notifyRemove( expression, context, nodes);
    
    for( IModelObject node: nodes)
    {
      Plot plot = plotMap.remove( node);
      if ( plot != null) getPlotFeature().removePlot( plot); else log.error( "Duplicate call to remove nodes: "+nodes);

      pointsExpr.removeListener( new StatefulContext( context, node), pointsListener);
    }
  }
  
  public class PointsListener extends ListDetailListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.ListDetailListener#notifyInsert(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyInsert( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      Point[] points = new Point[ count];
      
      Plot plot = plotMap.get( context.getObject());
      if ( plot == null) return;
      
      for( int i=0; i<count; i++)
      {
        IModelObject node = nodes.get( start + i);
        Point point = points[ i] = new Point( plot);
        pointMap.put( node, point);
        
        //
        // When individual coordinate expressions are used, the dimension is known before binding,
        // and this is the most convenient place to create the coordinate array.
        //
        if ( dim != 0) point.coords = new double[ dim];
      }
      
      super.notifyInsert( expression, context, nodes, start, count);
      
      for( int i=0; i<count; i++) plot.add( start + i, points[ i]);
    }

    /* (non-Javadoc)
     * @see org.xmodel.listeners.ListDetailListener#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      super.notifyRemove( expression, context, nodes, start, count);
      
      Plot plot = plotMap.get( context.getObject());
      if ( plot == null) return;
      
      for( int i=0; i<count; i++)
      {
        IModelObject node = nodes.get( start + i);
        pointMap.remove( node);
        plot.remove( i);
      }
    }
    
    public int dim;
  }

  public class CoordsListener extends ExactExpressionListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExactExpressionListener#notifyInsert(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyInsert( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      Point point = pointMap.get( context.getObject());
      
      if ( coordListeners != null)
      {
        for( int i=0; i<nodes.size(); i++)
          nodes.get( i).removeModelListener( coordListeners[ i]);
      }
      
      coordListeners = new CoordListener1[ nodes.size()];
      for( int i=0; i<nodes.size(); i++)
      {
        coordListeners[ i] = new CoordListener1( point, i);
        nodes.get( i).addModelListener( coordListeners[ i]);
      }

      update( point, nodes);      
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExactExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      if ( coordListeners != null)
      {
        for( int i=0; i<nodes.size(); i++)
          nodes.get( i).removeModelListener( coordListeners[ i]);
      }
      
      coordListeners = null;
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      Point point = pointMap.get( context.getObject());
      double[] coords = new double[] { newValue};
      if ( binding)
      {
        point.coords = coords;
      }
      else
      {
        getPlotFeature().updateCoords( point, coords);
        point.coords = coords;
      }
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newString, String oldString)
    {
      try
      {
        double newValue = Double.parseDouble( newString);
        double oldValue = Double.parseDouble( oldString);
        notifyChange( expression, context, newValue, oldValue);
      }
      catch( Exception e)
      {
        log.error( String.format( "Unable to parse coordinate string expression: %s", expression));
      }
    }

    /**
     * Update the coordinates of a point.
     * @param point The point.
     * @param nodes The coordinate nodes.
     */
    private void update( Point point, List<IModelObject> nodes)
    {
      double[] coords = new double[ nodes.size()];
      for( int i=0; i<coords.length; i++)
        coords[ i] = getDoubleValue( nodes.get( i).getValue());

      if ( binding) 
      {
        point.coords = coords;
      }
      else 
      {
        getPlotFeature().updateCoords( point, coords);
        point.coords = coords;
      }
    }
    
    private CoordListener1[] coordListeners;
  }
  
  public class CoordListener1 extends ModelListener
  {
    public CoordListener1( Point point, int coordinate)
    {
      this.point = point;
      this.coordinate = coordinate;
    }

    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyChange(org.xmodel.IModelObject, java.lang.String, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyChange( IModelObject object, String attrName, Object newValue, Object oldValue)
    {
      if ( attrName.length() == 0)
      {
        double value = getDoubleValue( newValue);
        getPlotFeature().updateCoord( point, coordinate, value);
        point.coords[ coordinate] = value;
      }
    }
    
    private Point point;
    private int coordinate;
  }
  
  public class CoordListener2 extends NodeValueListener
  {
    public CoordListener2( int coordinate)
    {
      this.coordinate = coordinate;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newObject, Object oldObject)
    {
      if ( newObject != null)
      {
        try
        {
          double newValue = (newObject instanceof Number)? ((Number)newObject).doubleValue(): Double.parseDouble( newObject.toString());
          Point point = pointMap.get( context.getObject());
          if ( !binding) getPlotFeature().updateCoord( point, coordinate, newValue);
          point.coords[ coordinate] = newValue;
        }
        catch( Exception e)
        {
          log.error( String.format( "Unable to parse coordinate string expression."));
        }
      }
    }
    
    private int coordinate;
  }
  
  public class LabelListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      String label = (newValue != null)? newValue.toString(): "";
      Point point = pointMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateLabel( point, label);
      point.label = label;
    }
  }
  
  public class PlotStrokeListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      double value = getDoubleValue( newValue);
      Plot plot = plotMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateStrokeWidth( plot, value);
      plot.setStrokeWidth( value);
    }
  }
  
  public class PlotForegroundListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      String color = (newValue != null)? newValue.toString(): "";
      Plot plot = plotMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateForeground( plot, color);
      plot.setForeground( color);
    }
  }
  
  public class PlotBackgroundListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      String color = (newValue != null)? newValue.toString(): "";
      Plot plot = plotMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateBackground( plot, color);
      plot.setBackground( color);
    }
  }
  
  public class PointForegroundListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      String color = (newValue != null)? newValue.toString(): "";
      Point point = pointMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateForeground( point, color);
      point.fcolor = color;
    }
  }
  
  public class PointBackgroundListener extends NodeValueListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      String color = (newValue != null)? newValue.toString(): "";
      Point point = pointMap.get( context.getObject());
      if ( !binding) getPlotFeature().updateBackground( point, color);
      point.bcolor = color;
    }
  }

  private Log log = Log.getLog( PlotsListener.class);
  
  private IXidget xidget;
  private IPlotFeature plotFeature;
  private Map<IModelObject, Plot> plotMap;
  private Map<IModelObject, Point> pointMap;
  private PointsListener pointsListener;
  private IExpression pointsExpr;
  private boolean binding;
}
