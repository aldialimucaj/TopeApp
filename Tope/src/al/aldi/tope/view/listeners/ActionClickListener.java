package al.aldi.tope.view.listeners;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.ITopeAction;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Main listeners for Tope Actions
 *
 * @author Aldi Alimucaj
 *
 */
public class ActionClickListener implements OnItemClickListener {
    Vector<ITopeAction>	items;
    Activity			activity;


    public ActionClickListener(Vector<ITopeAction> items, Activity activity) {
        super();
        this.items = items;
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ITopeAction action = ((ITopeAction) items.elementAt(position));

        ActionCareTaker act = new ActionCareTaker(action, getActivity());
        act.execute();

        /* check if there is an opposite action, if so then swap the actions */
        if (action.hasOppositeAction()) {

            ImageView actionImage = (ImageView) v.findViewById(R.id.gridActionImage);
            actionImage.setImageResource(action.getOppositeAction().getItemId());

            TextView descriptionText = (TextView) v.findViewById(R.id.gridActionText);
            descriptionText.setText(action.getOppositeAction().getTitle());

            /* set the action to the opposite, so it will rotate the next time */
            action = action.getOppositeAction();

            items.set(position, action);
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
