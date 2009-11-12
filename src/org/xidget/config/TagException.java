/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TagException.java
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
package org.xidget.config;

/**
 * An exception thrown during tag processing by a tag handler which causes
 * tag processing to stop.
 */
public class TagException extends Exception
{
  private static final long serialVersionUID = 1L;

  public TagException()
  {
    super();
  }

  public TagException( String message, Throwable cause)
  {
    super( message, cause);
  }

  public TagException( String message)
  {
    super( message);
  }

  public TagException( Throwable cause)
  {
    super( cause);
  }
}
