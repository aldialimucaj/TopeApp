package al.aldi.tope.view.listeners;

import al.aldi.android.view.ImageUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * TouchListener which changes the alpha of the view (ImageView).
 *
 * @author Aldi Alimucaj
 *
 */
public class ActionTouchAlphaListener implements OnTouchListener {
    public static int	ALPHA_ACTION_DOWN	= 100;
    public static int	ALPHA_ACTION_UP		= 255;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view instanceof ImageView) {
            ImageView actionImage = (ImageView) view;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageUtils.setImageAlpha(actionImage, ALPHA_ACTION_DOWN);

                return ((View) view.getParent()).onTouchEvent(event);
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageUtils.setImageAlpha(actionImage, ALPHA_ACTION_UP);
                return ((View) view.getParent()).onTouchEvent(event);
            }
            }
        }
        return false;
    }

}
