package al.aldi.tope.controller;

public interface ITopeAction {
    public boolean execute();

    public void setExecutable(ITopeExecutable exec);

    public String getTitle();

    public int getItemId();

    public void switchAction();

    public void setOppositeAction(ITopeAction action);
    public ITopeAction getOppositeAction();
    public boolean hasOppositeAction();
}
