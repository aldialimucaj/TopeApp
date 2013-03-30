package al.aldi.tope.controller;

import android.widget.ImageView;

public interface ITopeAction {
    public boolean execute();

    public void setExecutable(ITopeExecutable exec);

    public ImageView getImageView();

    public int getItemId();
}
