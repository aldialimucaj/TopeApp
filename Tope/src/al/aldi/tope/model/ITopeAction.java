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
public interface ITopeAction<E> extends Parcelable {
    public E execute(TopeClient client);

    public void setExecutable(ITopeExecutable<E> exec);

    public int getActionId();

    public void setActionId(int actionId);

    public String getTitle();

    public int getIconId();

    public String getCommand();

    public void setCommand(String command);

    public boolean isActive();

    public void setActive(boolean active);

    public ITopePayload getPayload();

    public boolean hasPayload();

    public void setPayload(ITopePayload payload);

    public View getContextView();

    public void setContextView(View contextView);

    public void switchAction();

    public void setOppositeAction(ITopeAction<E> action);

    public ITopeAction<E> getOppositeAction();

    public boolean hasOppositeAction();
}
