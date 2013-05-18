package al.aldi.tope.view.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ActionTouchListener implements OnTouchListener {
    public static int	ALPHA_ACTION_DOWN	= 100;
    public static int	ALPHA_ACTION_UP		= 255;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        ImageView actionImage = (ImageView) view;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            actionImage.setImageAlpha(ALPHA_ACTION_DOWN);

            return ((View) view.getParent()).onTouchEvent(event);
        }
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL: {
            actionImage.setImageAlpha(ALPHA_ACTION_UP);
            return ((View) view.getParent()).onTouchEvent(event);
        }
        }
        return false;
    }

}
