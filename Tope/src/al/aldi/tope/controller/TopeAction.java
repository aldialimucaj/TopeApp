package al.aldi.tope.controller;

import android.content.Context;
import android.widget.ImageView;

public class TopeAction implements ITopeAction {

    private ImageView	imageView;
    private int			itemId	= 0;
    private String		title	= null;

    ITopeExecutable		exec;

    public TopeAction() {

    }

    public TopeAction(Context context) {
        this.imageView = new ImageView(context);
    }

    public TopeAction(Context context, int itemId) {
        this.imageView = new ImageView(context);
        this.itemId = itemId;
    }

    public TopeAction(ImageView imageView, int itemId, String title) {
        this.imageView = imageView;
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
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
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
