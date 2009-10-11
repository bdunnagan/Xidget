/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.caching;

import java.io.File;
import org.xmodel.Xlate;
import org.xmodel.external.CachingException;
import org.xmodel.external.ConfiguredCachingPolicy;
import org.xmodel.external.ICache;
import org.xmodel.external.IExternalReference;
import org.xmodel.external.UnboundedCache;
import org.xmodel.external.caching.JarCachingPolicy;

/**
 * A caching policy that provides access to resources associated with a bean-sprayer
 */
public class ResourceCachingPolicy extends ConfiguredCachingPolicy
{
  public ResourceCachingPolicy()
  {
    this( new UnboundedCache());
  }
  
  public ResourceCachingPolicy( ICache cache)
  {
    super( cache);
    
    fileCachingPolicy = new FileSystemCachingPolicy( cache);
    jarCachingPolicy = new JarCachingPolicy( cache);
    
    String[] static1 = fileCachingPolicy.getStaticAttributes();
    String[] static2 = jarCachingPolicy.getStaticAttributes();
    String[] static3 = new String[ static1.length + static2.length];
    System.arraycopy( static1, 0, static3, 0, static1.length);
    System.arraycopy( static2, 0, static3, static1.length, static2.length);
    setStaticAttributes( static3);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.ConfiguredCachingPolicy#syncImpl(org.xmodel.external.IExternalReference)
   */
  @Override
  protected void syncImpl( IExternalReference reference) throws CachingException
  {
    if ( useJar( reference))
    {
      jarCachingPolicy.sync( reference);
    }
    else
    {
      fileCachingPolicy.sync( reference);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.external.AbstractCachingPolicy#checkin(org.xmodel.external.IExternalReference)
   */
  @Override
  public void checkin( IExternalReference reference)
  {
    if ( useJar( reference)) jarCachingPolicy.checkin( reference);
    else fileCachingPolicy.checkin( reference);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.AbstractCachingPolicy#checkout(org.xmodel.external.IExternalReference)
   */
  @Override
  public void checkout( IExternalReference reference)
  {
    if ( useJar( reference)) jarCachingPolicy.checkout( reference);
    else fileCachingPolicy.checkout( reference);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.AbstractCachingPolicy#clear(org.xmodel.external.IExternalReference)
   */
  @Override
  public void clear( IExternalReference reference) throws CachingException
  {
    if ( useJar( reference)) jarCachingPolicy.clear( reference);
    else fileCachingPolicy.clear( reference);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.AbstractCachingPolicy#flush(org.xmodel.external.IExternalReference)
   */
  @Override
  public void flush( IExternalReference reference) throws CachingException
  {
    if ( useJar( reference)) jarCachingPolicy.flush( reference);
    else fileCachingPolicy.flush( reference);
  }

  /**
   * Returns true if the jar caching policy should be used.
   * @return Returns true if the jar caching policy should be used.
   */
  private boolean useJar( IExternalReference reference)
  {
    if ( useJar == null)
    {
      File file = new File( Xlate.get( reference, "path", "."));
      System.out.println( "file="+file);
      useJar = !file.exists();
    }
    return useJar;
  }

  private FileSystemCachingPolicy fileCachingPolicy;
  private JarCachingPolicy jarCachingPolicy;
  private Boolean useJar;
}