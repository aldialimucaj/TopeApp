package al.aldi.tope.controller;

import android.content.Context;
import android.widget.ImageView;

public class TopeAction implements ITopeAction {

    ImageView		imageView;

    int				itemId	= 0;

    ITopeExecutable	exec;

    public TopeAction() {

    }

    public TopeAction(Context context) {
        super();
        this.imageView = new ImageView(context);
    }

    public TopeAction(Context context, int itemId) {
        super();
        this.imageView = new ImageView(context);
        this.itemId = itemId;
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

}
