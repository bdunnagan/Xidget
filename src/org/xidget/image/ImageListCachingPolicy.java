/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.image;

import java.io.File;
import java.net.URI;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.external.CachingException;
import org.xmodel.external.ConfiguredCachingPolicy;
import org.xmodel.external.ICache;
import org.xmodel.external.IExternalReference;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ICachingPolicy which creates a stub for image files in a directory.
 * The second stage uses the ImageCachingPolicy class. The location of the directory is
 * specified by a URL.
 */
public class ImageListCachingPolicy extends ConfiguredCachingPolicy
{
  public ImageListCachingPolicy( ICache cache)
  {
    super( cache);
    defineNextStage( imagePath, new ImageCachingPolicy( cache), true);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.external.ConfiguredCachingPolicy#syncImpl(org.xmodel.external.IExternalReference)
   */
  @Override
  protected void syncImpl( IExternalReference reference) throws CachingException
  {
    try
    {
      IModelObject newList = getFactory().createObject( null, reference.getType());

      URI uri = new URI( Xlate.get( reference, "uri", "");
      
      File folder = uri.
      File[] files = folder.listFiles();
      for( File file: files)
      {
        IModelObject newImage = getFactory().createObject( newList, "image");
        newImage.setID( file.getName());
        newImage.setAttribute( "url", file.toURL());
        newList.addChild( newImage);
      }
      update( reference, newList);
    }
    catch( Exception e)
    {
      throw new CachingException( "Unable to sync reference: "+reference, e);
    }
  }
  
  private static IExpression imagePath = XPath.createExpression( "image");
}
