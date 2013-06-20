package al.aldi.tope.model;

import al.aldi.tope.controller.ITopeExecutable;
import android.os.Parcelable;
import android.view.View;

/**
 * Interface decalring all necessary functions for tope actions.
 *
 * E is the return type of the executable. i.e. a Generics of TopeResponse
 * For example the class TopeResponse&lt;TestResponse&gt;
 *
 * @author Aldi Alimucaj
 *
 */
public interface ITopeAction extends Parcelable {
    public Object execute(TopeClient client);

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
}
