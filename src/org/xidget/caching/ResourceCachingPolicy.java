/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ResourceCachingPolicy.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.caching;

import java.io.File;
import org.xmodel.Xlate;
import org.xmodel.external.CachingException;
import org.xmodel.external.ConfiguredCachingPolicy;
import org.xmodel.external.ICache;
import org.xmodel.external.IExternalReference;
import org.xmodel.external.UnboundedCache;

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
    zipCachingPolicy = new ZipCachingPolicy( cache);
    
    String[] static1 = fileCachingPolicy.getStaticAttributes();
    String[] static2 = zipCachingPolicy.getStaticAttributes();
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
      zipCachingPolicy.sync( reference);
    }
    else
    {
      fileCachingPolicy.sync( reference);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.external.AbstractCachingPolicy#clear(org.xmodel.external.IExternalReference)
   */
  @Override
  public void clear( IExternalReference reference) throws CachingException
  {
    if ( useJar( reference)) zipCachingPolicy.clear( reference);
    else fileCachingPolicy.clear( reference);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.ConfiguredCachingPolicy#flushImpl(org.xmodel.external.IExternalReference)
   */
  @Override
  public void flushImpl( IExternalReference reference) throws CachingException
  {
    if ( useJar( reference)) zipCachingPolicy.flush( reference);
    else fileCachingPolicy.flush( reference);
  }

  /**
   * Returns true if the zip caching policy should be used.
   * @return Returns true if the zip caching policy should be used.
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
  private ZipCachingPolicy zipCachingPolicy;
  private Boolean useJar;
}