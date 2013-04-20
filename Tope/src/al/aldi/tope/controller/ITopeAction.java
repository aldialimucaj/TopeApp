package al.aldi.tope.controller;

import al.aldi.tope.model.ITopePayload;

public interface ITopeAction {
    public boolean execute();

    public void setExecutable(ITopeExecutable exec);

    public String getTitle();

    public int getItemId();

    public ITopePayload getPayload();

    public boolean hasPayload();

    public void setPayload(ITopePayload payload);

    public void switchAction();

    public void setOppositeAction(ITopeAction action);
    public ITopeAction getOppositeAction();
    public boolean hasOppositeAction();
}
