/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.caching;

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xmodel.external.ICache;

/**
 * An extension of JarCachingPolicy that uses the toolkit IFileAssociation for loading images.
 */
public class JarCachingPolicy extends org.xmodel.external.caching.JarCachingPolicy
{
  public JarCachingPolicy()
  {
    IToolkit toolkit = Creator.getInstance().getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
  }

  public JarCachingPolicy( ICache cache)
  {
    super( cache);
    IToolkit toolkit = Creator.getInstance().getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
  }
}
