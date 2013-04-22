package al.aldi.tope.controller;

import al.aldi.tope.model.ITopePayload;
import android.os.Parcelable;

public interface ITopeAction extends Parcelable {
    public boolean execute();

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
