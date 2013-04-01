package al.aldi.tope.controller;

import android.content.Context;
import android.widget.ImageView;

public class TopeAction implements ITopeAction {

    private int		itemId	= 0;
    private String	title	= null;

    ITopeExecutable	exec;

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

}
