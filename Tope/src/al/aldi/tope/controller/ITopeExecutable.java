package al.aldi.tope.controller;

import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import android.content.Context;

/**
 * An executable is a class that takes a single client runs the commands on that client
 * and returns a tope response with the necessary data about the outcome.
 *
 * @author Aldi Alimucaj
 *
 */
public interface ITopeExecutable {

    /**
     * Runs the command. This is the first step of calling the server.
     * The response contains the general response messages such as success statusCode etc. and
     * the payload which in many cases needs to be consulted. The type of the payload depends
     * on the generic class assigned to the TopeResponse.
     *
     * @param client
     * @return the TopeResponse&lt;E&gt;
     */
    public Object run(TopeClient client);

    /**
     * Actions that are needed to be taken before the request is sent.
     *
     * @param response
     * @return
     */
    public abstract boolean preRun(Object response);

    /**
     * Actinos that are going to be executed after the request has arrived and before the view
     * has be alerted.
     *
     * @param response
     */
    public abstract void postRun(Object response);

    /**
     * Mandatory method for every executor to be able to add an action.
     * @param action
     */
    public void setAction(ITopeAction action);

    /**
     * Returns the registered action for execution
     * @return
     */
    public ITopeAction getAction();

    /**
     * Set the context in order to achieve interaction with views.
     * @param context
     */
    public void setContext(Context context);
}
