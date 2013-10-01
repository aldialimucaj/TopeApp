package al.aldi.tope.view.listeners;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main listeners for Tope Actions
 *
 * @author Aldi Alimucaj
 *
 */
//TODO: Use this class instead of inline code in IconItemAdapter
public class ActionClickListener implements OnItemClickListener {
    Vector<ITopeAction>	items;
    Activity			activity;
    Vibrator vibrator = null;

    public ActionClickListener(Vector<ITopeAction> items, Activity activity) {
        super();
        this.items = items;
        this.activity = activity;
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ITopeAction action = ((ITopeAction) items.elementAt(position));

        vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

        /* ****************** */
        /* EXECUTING ACTION */
        /* ****************** */
        ActionCareTaker act = new ActionCareTaker(action, getActivity());
        act.execute();

        if (null != action && action.hasOppositeAction()) {

            ImageView actionImage = (ImageView) v.findViewById(R.id.gridActionImage);
            actionImage.setImageResource(action.getOppositeAction().getItemId());
            actionImage.setTag(action.getOppositeAction().getItemId());

            /* setting the alpha changer as the action image is touched */
            actionImage.setOnTouchListener(new ActionTouchAlphaListener());

            TextView descriptionText = (TextView) v.findViewById(R.id.gridActionText);
            descriptionText.setText(action.getOppositeAction().getTitle());

            int indexToReplace = items.indexOf(action);

            action = action.getOppositeAction();

            /* Swapping item the with the opposite action in the rendering list */
            items.set(indexToReplace, action);
        }
    }



    public Vector<ITopeAction> getItems() {
        return items;
    }



    public void setItems(Vector<ITopeAction> items) {
        this.items = items;
    }



    public Activity getActivity() {
        return activity;
    }



    public void setActivity(Activity activity) {
        this.activity = activity;
    }



}
