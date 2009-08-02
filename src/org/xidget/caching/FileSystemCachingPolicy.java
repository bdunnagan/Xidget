/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.caching;

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xmodel.external.ICache;

/**
 * An extension of FileSystemCachingPolicy that uses the toolkit IFileAssociation for loading images.
 */
public class FileSystemCachingPolicy extends org.xmodel.external.caching.FileSystemCachingPolicy
{
  public FileSystemCachingPolicy()
  {
    IToolkit toolkit = Creator.getInstance().getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
  }

  public FileSystemCachingPolicy( ICache cache)
  {
    super( cache);
    IToolkit toolkit = Creator.getInstance().getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
  }
}
