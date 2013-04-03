package al.aldi.tope.controller;

public class TopeAction implements ITopeAction {

    private int		itemId	= 0;
    private String	title	= null;

    ITopeExecutable	exec;
    ITopeAction		oppositeAction;

    public TopeAction() {

    }

    public TopeAction(int itemId) {
        this.itemId = itemId;
    }

    public TopeAction(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    @Override
    public boolean execute() {
        if (null == exec) {
            throw new ExceptionInInitializerError("ITopeExecutable exec not implemented");
        }
        return exec.run();
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public void setExecutable(ITopeExecutable exec) {
        this.exec = exec;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void switchAction() {


    }

    @Override
    public void setOppositeAction(ITopeAction opAction) {
        this.oppositeAction = opAction;
    }

    @Override
    public ITopeAction getOppositeAction() {
        return oppositeAction;
    }

    @Override
    public boolean hasOppositeAction() {
        return null != this.oppositeAction;
    }

}
