package al.aldi.tope.model;

import al.aldi.tope.controller.ITopeExecutable;
import android.os.Parcelable;

/**
 * Interface decalring all necessary functions for tope actions.
 * @author Aldi Alimucaj
 *
 */
public interface ITopeAction extends Parcelable {
    public TopeResponse execute(TopeClient client);

    public void setExecutable(ITopeExecutable exec);

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

    public void switchAction();

    public void setOppositeAction(ITopeAction action);
    public ITopeAction getOppositeAction();
    public boolean hasOppositeAction();
}
