package al.aldi.tope.model;

import al.aldi.tope.controller.ITopeExecutable;
import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.view.View;

/**
 * Interface declaring all necessary functions for tope actions.
 *
 * E is the return type of the executable. i.e. a Generics of {@link TopeResponse}
 * For example the class TopeResponse&lt;TestResponse&gt;
 *
 * @author Aldi Alimucaj
 *
 */
@SuppressLint("ParcelCreator")
public interface ITopeAction extends Parcelable, Comparable<ITopeAction> {

    /**
     * Execute the action's functionality on the client
     * @param client
     * @return response from the client
     */
    public Object execute(TopeClient client);

    /**
     * Set executable for this action. The executable is the one
     * which takes care of sending the request and expecting the response.
     * @param exec
     */
    public void setExecutable(ITopeExecutable exec);

    public long getActionId();

    public void setActionId(long actionId);

    public String getTitle();

    public int getItemId();

    public String getCommandFullPath();

    public void setCommandFullPath(String commandFullPath);

    public String getMethod();

    public void setMethod(String command);
    
    public void setOutputIgnored(boolean ignore);
    
    public boolean isOutputIgnored();

    public boolean isActive();

    public void setActive(boolean active);

    public ITopePayload getPayload();

    public boolean hasPayload();

    public void setPayload(ITopePayload payload);

    public View getContextView();

    public void setContextView(View contextView);

    public void switchAction();

    public void setOppositeAction(ITopeAction action);

    public ITopeAction getOppositeAction();

    public boolean hasOppositeAction();
    
    public boolean isConfirmationNeeded();

    /**
     * Specify if it is expecting a confirmation.
     *
     * @param confirmationNeeded false if the view does not need to know about the outcome of the action
     */
    public void setConfirmationNeeded(boolean confirmationNeeded);
}
