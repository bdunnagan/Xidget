/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * This class implements the generic xidget application startup.
 */
public abstract class Startup
{
  protected Startup()
  {
  }
  
  /**
   * Start the application.
   * @param mainPath The path to the startup script.
   */
  public void start( IExpression path) throws Exception
  {
    // configure the xidget toolkit
    Creator.setToolkitClass( getToolkitClass());

    // get resource root
    StatefulContext resources = getResourceRoot();
    
    // locate main script
    IModelObject main = path.queryFirst( resources);
    
    // load and execute the startup script
    XActionDocument document = new XActionDocument( main);
    document.addPackage( "org.xidget.xaction");
    
    ScriptAction script = document.createScript();
    script.run( resources);
  }
  
  /**
   * @return Returns the platform toolkit.
   */
  protected abstract Class<? extends IToolkit> getToolkitClass();

  /**
   * @return Returns the root of the resources.
   */
  protected abstract StatefulContext getResourceRoot();
}
