package org.xidget.xaction;

import java.util.Collections;
import java.util.List;
import org.xmodel.IChangeSet;
import org.xmodel.IModelListener;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;
import org.xmodel.ModelObject;
import org.xmodel.Reference;
import org.xmodel.Xlate;
import org.xmodel.diff.RegularChangeSet;
import org.xmodel.external.NonSyncingListener;
import org.xmodel.listeners.UndoListener;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableScope;

/**
 * An XAction which executes a script and creates a record of the changes performed by the script.
 * The record represents the command, and optionally its undo script.  The command is stored in 
 * a user-defined place called the <i>command history</i>.  The command history is convenient for 
 * determining when a form is dirty, as well as for handling undo requests.  The UndoAction will
 * undo one command on a user-defined command history.  The user does not need to be aware of the 
 * schema of the command.
 * <p>
 * The action has three sections:
 * <ul>
 *   <li>do - A script executed when the command is created</li>
 *   <li>undo - A script executed when an undo request is issued</li>
 *   <li>redo - An optional script executed when a redo request is issued. If absent then the <i>do</i> script is called.</li>
 * </ul>
 * <p>
 * The context of each script (do, undo, redo) is preserved so that subsequent calls always receive the same
 * context with which they were originally invoked.  This insures that undo and redo are always consistent.
 * <p>
 * There is a second form of CommandAction in which only the do-script is provided, but one or two additional
 * expressions define elements on which listeners will be installed to create an invertible ChangeSet.  The
 * expressions are given by the <i>listenDeep</i> and/or <i>listenShallow</i> attributes (or children).  If
 * the change set created by the listener is empty after the do-script is executed, then the command will not
 * be put on the history.
 */
public class CommandAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    historyExpr = document.getExpression( "history", true);
    
    listenDeepExpr = document.getExpression( "listenDeep", true);
    listenShallowExpr = document.getExpression( "listenShallow", true);
    
    doScript = document.createChildScript( "do");
    undoScript = document.createChildScript( "undo");
    redoScript = document.createChildScript( "redo");
    
    // redo script is the same as do script unless change set is being used
    if ( listenDeepExpr == null && listenShallowExpr == null && redoScript == null) redoScript = doScript;
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    // get command history, if it doesn't exist then just execute do-script
    IModelObject history = historyExpr.queryFirst( context);
    if ( history == null) return doScript.run( context);
      
    // update index
    int index = Xlate.get( history, "index", 0);
    Xlate.set( history, "index", index+1);
    
    // remove trailing commands in history
    for( int i=index; i<history.getNumberOfChildren(); i++)
      history.removeChild( index);
    
    // create new command
    Command command = new Command();
    
    // create redo context and freeze leaf variables
    command.redoContext = new StatefulContext( context);
    copyVariables( context, command.redoContext);
    
    // install listeners on entities
    Object[] result = null;
    if ( listenShallowExpr != null || listenDeepExpr != null)
    {
      IChangeSet undoSet = new RegularChangeSet();
      IChangeSet redoSet = new RegularChangeSet();
      command.setChangeSets( undoSet, redoSet);
      undoListener = new UndoListener( undoSet, redoSet);

      List<IModelObject> deeps = (listenDeepExpr != null)? listenDeepExpr.query( context, null): Collections.<IModelObject>emptyList();
      List<IModelObject> shallows = (listenShallowExpr != null)? listenShallowExpr.query( context, null): Collections.<IModelObject>emptyList();
      for( IModelObject entity: deeps) deepListener.install( entity);
      for( IModelObject entity: shallows) entity.addModelListener( shallowListener);
      
      // execute do script
      result = doScript.run( context);
      
      // remove listeners on entities
      for( IModelObject entity: deeps) deepListener.uninstall( entity);
      for( IModelObject entity: shallows) entity.removeModelListener( shallowListener);
      
      // undo listener not needed now
      undoListener = null;
    }
    else
    {
      // execute do script
      result = doScript.run( context);
    }
    
    // create undo context and freeze leaf variables
    command.undoContext = new StatefulContext( context); 
    copyVariables( context, command.undoContext);
  
    // put command on stack (unless there is a change set and it is empty)
    if ( command.undoSet == null || command.undoSet.getSize() > 0)
    {
      IModelObject entry = new ModelObject( "command", Integer.toString( count));
      entry.setAttribute( "instance", command);
      entry.addChild( new Reference( getDocument().getRoot()));
      history.addChild( entry);
    }
    
    return result;
  }
  
  /**
   * Copy leaf variables in one context scope to another context scope.
   * @param source The source context.
   * @param target The target context.
   */
  private void copyVariables( IContext source, IContext target)
  {
    IVariableScope sourceScope = source.getScope();
    IVariableScope targetScope = target.getScope();
    targetScope.copyFrom( sourceScope);
  }

  private NonSyncingListener deepListener = new NonSyncingListener() {
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      super.notifyAddChild( parent, child, index);
      if ( undoListener != null) undoListener.notifyAddChild( parent, child, index);
    }
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      super.notifyRemoveChild( parent, child, index);
      if ( undoListener != null) undoListener.notifyRemoveChild( parent, child, index);
    }
    public void notifyChange( IModelObject object, String attrName, Object newValue, Object oldValue)
    {
      if ( undoListener != null) undoListener.notifyChange( object, attrName, newValue, oldValue);
    }
    public void notifyClear( IModelObject object, String attrName, Object oldValue)
    {
      if ( undoListener != null) undoListener.notifyClear( object, attrName, oldValue);
    }
  };
  
  private IModelListener shallowListener = new ModelListener() {
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      if ( undoListener != null) undoListener.notifyAddChild( parent, child, index);
    }
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      if ( undoListener != null) undoListener.notifyRemoveChild( parent, child, index);
    }
    public void notifyChange( IModelObject object, String attrName, Object newValue, Object oldValue)
    {
      if ( undoListener != null) undoListener.notifyChange( object, attrName, newValue, oldValue);
    }
    public void notifyClear( IModelObject object, String attrName, Object oldValue)
    {
      if ( undoListener != null) undoListener.notifyClear( object, attrName, oldValue);
    }
  };
  
  public class Command
  {
    public void setChangeSets( IChangeSet undoSet, IChangeSet redoSet)
    {
      this.undoSet = undoSet;
      this.redoSet = redoSet;
    }
    
    /**
     * Returns true if this command has an undo script.
     * @return Returns true if this command has an undo script.
     */
    public boolean canUndo()
    {
      return undoScript != null || undoSet != null;
    }
    
    /**
     * Run the undo script with the undo context.
     */
    public void undo()
    {
      if ( undoSet != null) undoSet.applyChanges();
      if ( undoScript != null) undoScript.run( undoContext);
    }
    
    /**
     * Run the do/redo script with the redo context.
     */
    public void redo()
    {
      if ( redoSet != null) redoSet.applyChanges();
      if ( redoScript != null) redoScript.run( redoContext);
    }
    
    private IContext redoContext;
    private IContext undoContext;
    private IChangeSet undoSet;
    private IChangeSet redoSet;
  }
  
  private static int count = 0;
  
  private IExpression historyExpr;
  private IExpression listenDeepExpr;
  private IExpression listenShallowExpr;
  private UndoListener undoListener;
  private ScriptAction doScript;
  private ScriptAction undoScript;
  private ScriptAction redoScript;
}
