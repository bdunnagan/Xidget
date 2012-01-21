/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.ifeature.IAsyncFeature;
import org.xmodel.xaction.IXAction;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionException;
import org.xmodel.xaction.debug.IDebugger;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that spawns a thread and executes its children in sequence using IAsyncFeature.run.
 * This implementation is useful for scripts that update a progress bar during execution.
 */
public class ProgressAction extends ScriptAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    ThreadRunnable runnable = new ThreadRunnable( context);
    
    Thread thread = new Thread( runnable, "Worker");
    thread.setDaemon( false);
    thread.start();
    
    return runnable.result;
  }

  private class ThreadRunnable implements Runnable
  {
    public ThreadRunnable( IContext context)
    {
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
      if ( !debugging)
      {
        for( IXAction action: getActions())
        {
          ActionRunnable runnable = new ActionRunnable( context, action);
          result = runnable.invokeAndWait();
          if ( result != null) return;
        }
      }
      else
      {
        IDebugger debugger = debuggers.get();
        try
        {
          debugger.push( context, ProgressAction.this);
          for( IXAction action: getActions())
          {
            ActionRunnable runnable = new ActionRunnable( context, action);
            result = runnable.invokeAndWait();
            if ( result != null) return;
          }
        }
        finally
        {
          debugger.pop();
        }
      }
    }
    
    private IContext context;
    public Object[] result;
  };

  private class ActionRunnable implements Runnable
  {
    public ActionRunnable( IContext context, IXAction action)
    {
      this.context = context;
      this.action = action;
    }
    
    /**
     * Invoke using SwingUtilities.invokeAndWait.
     * @return Returns the result.
     */
    public Object[] invokeAndWait()
    {
      try
      {
        result = null;
        IAsyncFeature asyncFeature = Creator.getToolkit().getFeature( IAsyncFeature.class);
        asyncFeature.runWait( this);
        return result;
      }
      catch( Exception e)
      {
        throw new XActionException( e);
      }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
      if ( !debugging)
      {
        result = action.run( context);
      }
      else
      {
        IDebugger debugger = debuggers.get();
        result = debugger.run( context, action);
      }
    }
    
    private IContext context;
    private IXAction action;
    public Object[] result;
  }
}
