/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.ifeature.IOptionalNodeFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xmodel.ChangeSet;
import org.xmodel.IModelObject;
import org.xmodel.IPath;
import org.xmodel.ModelAlgorithms;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.Context;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.expression.IExpression.ResultType;
import org.xmodel.xsd.Schema;
import org.xmodel.xsd.SchemaTrace;

/**
 * An implementation of IOptionalNodeFeature.
 */
public class OptionalNodeFeature implements IOptionalNodeFeature
{
  public OptionalNodeFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.channels = new HashMap<String, Channel>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IOptionalNodeFeature#setSourceExpression(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setSourceExpression( String channelName, IExpression expression)
  {
    Channel channel = new Channel();
    channel.sourceExpr = expression;
    channels.put( channelName, channel); 
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IOptionalNodeFeature#createOptionalNodes(java.lang.String, org.xmodel.xpath.expression.StatefulContext)
   */
  public void createOptionalNodes( String channelName, StatefulContext context)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null)
    {
      if ( channel.sourceExpr != null && channel.sourceExpr.getType() == ResultType.NODES)
      {
        channel.schemaRoot = schemaExpr.queryFirst( context);
        if ( channel.schemaRoot != null)
        {
          IModelObject node = channel.sourceExpr.queryFirst( context);
          if ( node == null)
          {
            findSchemaFromGeneratedFragment( context, channel);
          }
          else
          {
            findSchemaFromSourceNode( context.getObject(), node, channel);
          }
        }
      }
      
      if ( channel.sourcePath != null)
      {
        ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
        IModelObject source = sourceFeature.getSource( channelName);
        if ( source == null)
        {
          channel.undo = new ChangeSet();
          ModelAlgorithms.createPathSubtree( context.getObject(), channel.sourcePath, null, channel.undo);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IOptionalNodeFeature#deleteOptionalNodes(java.lang.String, org.xmodel.xpath.expression.StatefulContext)
   */
  public void deleteOptionalNodes( String channelName, StatefulContext context)
  {
    //
    // The source schema can change at any time. If the new source node is in the same schema fragment
    // of the context as the previous source node, then we need to recalculate whether the node new
    // source node is optional.
    //
    Channel channel = channels.get( channelName);
    if ( channel != null)
    {
      // undo optional nodes if they were generated
      if ( channel.undo != null)
      {
        channel.undo.applyChanges();
        channel.undo = null;
      }
      else
      {
        ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
        IModelObject source = sourceFeature.getSource( channelName);
        if ( source != null) removeOptionals( channel, source);
      }
    }
  }
  
  /**
   * Remove optional node and optional ancestors that don't have a child or other attributes.
   * @param channel The channel.
   * @param source The source.
   */
  private void removeOptionals( Channel channel, IModelObject source)
  {
    IModelObject element = source;
    for( int i = channel.trace.getLength() - 1; i >= 0; i--)
    {
      if ( !channel.trace.isOptional( channel.trace.getElement( i)) || element.getNumberOfChildren() > 0 || element.getAttributeNames().size() > 0) break;
      
      IModelObject parent = element.getParent();
      element.removeFromParent();
      element = parent;
    }
  }
  
  /**
   * Populates the source schema by using a schema trace to generate the document fragment.
   * @param context The context.
   * @param channel The channel.
   */
  private void findSchemaFromGeneratedFragment( IContext context, Channel channel)
  {
    // trace schema of context node
    SchemaTrace trace = SchemaTrace.getInstance( channel.schemaRoot, context.getObject());
    if ( trace != null)
    {
      IModelObject generated = Schema.createDocumentBranch( trace, true);
      IModelObject node = channel.sourceExpr.queryFirst( new Context( context, generated));
      if ( node != null) findSchemaFromSourceNode( generated, node, channel);
    }
  }

  /**
   * Lookup the source schema for the specified source node.
   * @param schemaRoot The schema root.
   * @param root The root of the fragment (the context node).
   * @param node The source node.
   * @param channel The channel.
   */
  private void findSchemaFromSourceNode( IModelObject root, IModelObject node, Channel channel)
  {
    // trace schema of source node
    channel.trace = SchemaTrace.getInstance( channel.schemaRoot, node);
    if ( channel.trace != null)
    {
      channel.sourcePath = ModelAlgorithms.createRelativePath( root, node);
      channel.schema = channel.trace.getLeaf();
      channel.optional = channel.trace.isOptional();
    }
  }
  
  private class Channel
  {
    IModelObject schemaRoot;
    IExpression sourceExpr;
    IPath sourcePath;
    SchemaTrace trace;
    IModelObject schema;
    ChangeSet undo;
    boolean optional;
  }
  
  private IExpression schemaExpr = XPath.createExpression( "$schema");
  private IXidget xidget;
  private Map<String, Channel> channels;
}
