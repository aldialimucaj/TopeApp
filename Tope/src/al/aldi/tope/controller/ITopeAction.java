package al.aldi.tope.controller;

import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import android.os.Parcelable;

/**
 * Interface decalring all necessary functions for tope actions.
 * @author Aldi Alimucaj
 *
 */
public interface ITopeAction extends Parcelable {
    public TopeResponse execute(TopeClient client);

    public void setExecutable(ITopeExecutable exec);

    public String getTitle();

    public int getItemId();

    public String getCommand();

    public void setCommand(String command);

    public ITopePayload getPayload();

    public boolean hasPayload();

    public void setPayload(ITopePayload payload);

    public void switchAction();

    public void setOppositeAction(ITopeAction action);
    public ITopeAction getOppositeAction();
    public boolean hasOppositeAction();
}
