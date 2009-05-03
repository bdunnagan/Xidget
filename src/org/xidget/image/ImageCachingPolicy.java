/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.image;

import org.xmodel.external.AbstractCachingPolicy;
import org.xmodel.external.CachingException;
import org.xmodel.external.IExternalReference;

/**
 * An implementation of ICachingPolicy which creates a stub for image files in a directory.
 * This caching policy uses an instance of IImageLoader, which is customized per platform,
 * to load the images.
 */
public class ImageCachingPolicy extends AbstractCachingPolicy
{
  /* (non-Javadoc)
   * @see org.xmodel.external.ICachingPolicy#sync(org.xmodel.external.IExternalReference)
   */
  public void sync( IExternalReference reference) throws CachingException
  {
    // TODO Auto-generated method stub

  }
}
