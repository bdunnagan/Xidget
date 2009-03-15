/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.XidgetMap;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IWidgetFeature;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.ILayoutFeature;
import org.xidget.layout.WidgetBottomNode;
import org.xidget.layout.WidgetHeightNode;
import org.xidget.layout.WidgetLeftNode;
import org.xidget.layout.WidgetRightNode;
import org.xidget.layout.WidgetTopNode;
import org.xidget.layout.WidgetWidthNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Convenient base class for xidgets.
 */
public abstract class AbstractXidget implements IXidget
{
  protected AbstractXidget()
  {
    nodes = new IComputeNode[ 6];
  }

  /**
   * Set the parent xidget.
   * @param parent The parent.
   */
  private void setParent( IXidget parent)
  {
    if ( this.parent != null) this.parent.getChildren().remove( this);
    this.parent = parent;
    if ( parent != null) parent.getChildren().add( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getParent()
   */
  public IXidget getParent()
  {
    return parent;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getChildren()
   */
  public List<IXidget> getChildren()
  {
    if ( children == null) children = new ArrayList<IXidget>();
    return children;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setContext(org.xmodel.xpath.expression.StatefulContext)
   */
  public void setContext( StatefulContext context)
  {
    this.context = context;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getContext()
   */
  public StatefulContext getContext()
  {
    return context;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#addBinding(org.xidget.IXidgetBinding)
   */
  public void addBinding( IXidgetBinding binding)
  {
    if ( bindings == null) bindings = new ArrayList<IXidgetBinding>( 3);
    bindings.add( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#removeBinding(org.xidget.IXidgetBinding)
   */
  public void removeBinding( IXidgetBinding binding)
  {
    if ( bindings != null) bindings.remove( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#bind()
   */
  public void bind()
  {
    // children
    if ( children != null)
      for( IXidget child: children)
      {
        child.setContext( getContext());
        child.bind();
      }
    
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#unbind()
   */
  public void unbind()
  {
    // internal bindings
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.unbind( context);

    // children
    if ( children != null)
      for( IXidget child: children)
        child.unbind();
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAnchor(java.lang.String)
   */
  public IComputeNode getAnchor( String type)
  {
    IWidgetFeature widget = getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    char c0 = type.charAt( 0);
    char c1 = type.charAt( 1);
    if ( c0 == 'x')
    {
      if ( c1 == '0')
      {
        if ( nodes[ 0] == null) nodes[ 0] = new WidgetLeftNode( widget);
        return nodes[ 0];
      }
      else
      {
        if ( nodes[ 2] == null) nodes[ 2] = new WidgetRightNode( widget);
        return nodes[ 2];
      }
    }
    else if ( c0 == 'y')
    {
      if ( c1 == '0')
      {
        if ( nodes[ 1] == null) nodes[ 1] = new WidgetTopNode( widget);
        return nodes[ 1];
      }
      else
      {
        if ( nodes[ 3] == null) nodes[ 3] = new WidgetBottomNode( widget);
        return nodes[ 3];
      }
    }
    else if ( c0 == 'w')
    {
      if ( nodes[ 4] == null) nodes[ 4] = new WidgetWidthNode( widget);
      return nodes[ 4];
    }
    else 
    {
      if ( nodes[ 5] == null) nodes[ 5] = new WidgetHeightNode( widget);
      return nodes[ 5];
    }    
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param parent Null or the parent of this xidget.
   * @param element The configuration element.
   * @return Returns true.
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    setParent( parent);
    
    // add xidget to config map
    XidgetMap map = processor.getFeature( XidgetMap.class);
    if ( map == null) throw new TagException( "Tag processor must have a XidgetMap feature.");
    map.add( this, element);
    
    // load xidget layout
    IModelObject layout = element.getFirstChild( "layout");
    IExpression layoutExpr = Xlate.childGet( element, "layout", Xlate.get( element, "layout", (IExpression)null));
    
    // if declared in-place
    if ( layout != null && layoutExpr == null)
      setLayout( processor, layout);
    

    // reference to layout
    if ( layoutExpr != null)
    {
      IModelObject declaration = layoutExpr.queryFirst( (layout != null)? layout: element);
      if ( declaration == null) throw new TagException( "Declaration not found for layout: "+element);
      setLayout( processor, declaration);      
    }
    
    return true;
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param element The element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }

  /**
   * Set the layout on the xidget associated with the specified tag handler.
   * @param processor The processor.
   * @param declaration The layout declaration.
   */
  private void setLayout( TagProcessor processor, IModelObject declaration)
  {
    ILayoutFeature feature = getFeature( ILayoutFeature.class);
    if ( feature == null) feature = getParent().getFeature( ILayoutFeature.class);
    if ( feature != null) feature.configure( processor, declaration);      
  }
  
  private IXidget parent;
  private List<IXidget> children;
  private StatefulContext context;
  private List<IXidgetBinding> bindings;
  private IComputeNode[] nodes;
}
