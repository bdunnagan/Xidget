/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.selection;

import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;

/**
 * A shallow list differ for use with foreign key references. The class can be customized
 * with the name of an attribute which contains the foreign key. This implementation does
 * not support foreign keys defined in child elements since that is a bad idea anyway.
 */
public class ForeignKeyListDiffer extends AbstractSelectionDiffer
{
  public ForeignKeyListDiffer()
  {
    this( "id", "id");
  }
  
  /**
   * Create an instance which will create foreign keys by transferring the value of the given
   * <i>fromAttribute</i> on the referent to the <i>toAttribute</i> on the reference. 
   * @param fromAttribute The attribute that defines the identity of the referent (null means value).
   * @param toAttribute The attribute that holds the foreign key (null means value).
   */
  public ForeignKeyListDiffer( String fromAttribute, String toAttribute)
  {
    this.fromAttribute = (fromAttribute != null)? fromAttribute: "id";
    this.toAttribute = (toAttribute != null)? toAttribute: "id";
  }
  
  /* (non-Javadoc)
   * @see org.xidget.selection.AbstractSelectionDiffer#createReference(org.xmodel.IModelObject)
   */
  @Override
  public IModelObject createReference( IModelObject node)
  {
    Object id = node.getAttribute( fromAttribute);
    IModelObject result = new ModelObject( node.getType());
    result.setAttribute( toAttribute, id);
    return result;
  }

  /* (non-Javadoc)
   * @see org.xidget.selection.AbstractSelectionDiffer#getIdentity(org.xmodel.IModelObject)
   */
  @Override
  public Object getIdentity( IModelObject node)
  {
    return Xlate.get( node, fromAttribute, (String)null);
  }

  /* (non-Javadoc)
   * @see org.xmodel.diff.AbstractListDiffer#isMatch(java.lang.Object, java.lang.Object)
   */
  @Override
  public boolean isMatch( Object object1, Object object2)
  {
    IModelObject node1 = (IModelObject)object1;
    IModelObject node2 = (IModelObject)object2;
  
    Object value1 = node1.getAttribute( toAttribute);
    Object value2 = node2.getAttribute( toAttribute);
    if ( value1 == null || value2 == null) return value1 == value2;
    return value1.equals( value2);
  }

  private String fromAttribute;
  private String toAttribute;
}
