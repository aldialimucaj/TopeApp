package al.aldi.tope.view.adapter;

import java.util.HashMap;
import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.dialog.DynamicActionLongClickDialog;
import al.aldi.tope.view.listeners.ActionTouchAlphaListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconItemAdapter<E> extends BaseAdapter {

    public static final int      WIDTH_160    = 160;
    public static final int      HEIGHT_250   = 250;
    public static final int      HEIGHT_230   = 230;

    private Activity             activity;
    private Fragment             fragment;

    IconItemAdapter<ITopeAction> adapter      = null;
    TopeActionUtils              osActions    = null;
    Vector<ITopeAction>          actions      = null;
    HashMap<TopeAction, Integer> dbActionsMap = null;

    public IconItemAdapter() {
        osActions = TopeActionUtils.TopeActionUtilsManager.getOsActionUtil();
        actions = osActions.getActions();
    }

    public IconItemAdapter(Activity activity, Vector<ITopeAction> itmes) {
        this.activity = activity;
        this.actions = itmes;
    }

    public IconItemAdapter(Activity activity, HashMap<TopeAction, Integer> dbActionsMap) {
        this.activity = activity;
        this.dbActionsMap = dbActionsMap;
        this.actions = new Vector<ITopeAction>(dbActionsMap.keySet());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // if it's not recycled, initialize some attributes
            LayoutInflater li = activity.getLayoutInflater();
            LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

            v.setPadding(15, 15, 15, 15);

            final ITopeAction action = ((ITopeAction) getItem(position));
            TextView tv = (TextView) v.findViewById(R.id.gridActionText);
            tv.setText(action.getTitle());

            ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(action.getItemId());
            /* need to store the image id as tag in order to be able to read it once it fires a click event */
            imageView.setTag(action.getItemId());

            imageView.setOnTouchListener(new ActionTouchAlphaListener());

            if (null != dbActionsMap && null != action) {
                int actionOccurency = dbActionsMap.get(action);
                if (actionOccurency < dbActionsMap.size()) {
                    // the actions is not found IN all clients

                }
            }

            /* ON_LONG_CLICK */

            v.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

                    /* ****************** */
                    /* SHOWING DIALOG */
                    /* ****************** */
                    DynamicActionLongClickDialog td = new DynamicActionLongClickDialog();
                    Bundle args = new Bundle();
                    args.putParcelable(DynamicActionLongClickDialog.KEY_DYNAMIC_VIEW, action);
                    td.setArguments(args);
                    td.show(fragment.getChildFragmentManager(), "TAG");
                    return false;
                }
            });

            /* ON_CLICK */

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

                    /* ****************** */
                    /* EXECUTING ACTION */
                    /* ****************** */
                    ITopeAction action = ((ITopeAction) TopeUtils.getAction(actions, v));
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

                        int indexToReplace = actions.indexOf(action);

                        action = action.getOppositeAction();

                        /* Swapping item the with the opposite action in the rendering list */
                        actions.set(indexToReplace, action);
                    }

                }
            });

            return v;
        } else {
            return (LinearLayout) convertView;
        }
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int position) {
        return actions.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        ITopeAction a = actions.elementAt(position);
        return a.getItemId();
    }

    public Vector<ITopeAction> getActions() {
        return actions;
    }

    public void setActions(Vector<ITopeAction> actions) {
        this.actions = actions;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
