/*
 * Xidget - XML Widgets based on JAHM
 * 
 * FileSystemCachingPolicy.java
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

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xmodel.external.ICache;

/**
 * An extension of FileSystemCachingPolicy that uses the toolkit IFileAssociation for loading images.
 */
public class FileSystemCachingPolicy extends org.xmodel.caching.FileSystemCachingPolicy
{
  public FileSystemCachingPolicy()
  {
    IToolkit toolkit = Creator.getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
    addAssociation( new XipAssociation());
  }

  public FileSystemCachingPolicy( ICache cache)
  {
    super( cache);
    IToolkit toolkit = Creator.getToolkit();
    addAssociation( toolkit.getImageFileAssociation());
    addAssociation( new XipAssociation());
  }
}
