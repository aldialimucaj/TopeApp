package al.aldi.tope.view.adapter;

import al.aldi.andorid.utils.PreferencesUtil;
import al.aldi.android.view.ImageUtils;
import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.dialog.DynamicActionLongClickDialog;
import al.aldi.tope.view.fragments.GeneralSectionFragment;
import al.aldi.tope.view.fragments.GeneralSectionFragment.ActionClickBehaviour;
import al.aldi.tope.view.listeners.ActionTouchAlphaListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Adapter for the Actions.
 *
 * @param <E>
 */
public class IconItemAdapter<E> extends BaseAdapter {

	public static final String TAG = "al.aldi.tope.view.adapter.IconItemAdapter";

	public static final int WIDTH_160 = 160;
	public static final int HEIGHT_250 = 250;
	public static final int HEIGHT_230 = 230;

	private Activity activity;
	private Fragment fragment;

	Vector<ITopeAction> actions = null;
	HashMap<TopeAction, Integer> dbActionsMap = null;

	private int maxEntryOccurency = 0;

	Vibrator vibrator = null;

	public IconItemAdapter(Activity activity, Vector<ITopeAction> items, HashMap<TopeAction, Integer> dbActionsMap) {
		this.activity = activity;
		this.dbActionsMap = dbActionsMap;
		this.actions = items;
		Collections.sort(actions);// sort the action by their id. Maybe this is
									// going to change in the future.
		this.maxEntryOccurency = getMaxClientOccurencies(dbActionsMap);
		vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater li = activity.getLayoutInflater();
			LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

			v.setPadding(15, 15, 15, 15);

			ITopeAction action = ((ITopeAction) getItem(position));
			TextView tv = (TextView) v.findViewById(R.id.gridActionText);

			if (action.hasOppositeAction()) {
				ITopeAction opTmpAction = action.getOppositeAction();
				if (PreferencesUtil.readPrefBool(getActivity(), TAG, opTmpAction.getMethod())) {
					Log.i(TAG, "Rendering " + opTmpAction.getMethod() + " instead of " + action.getMethod() + " ...");
					
					// we need to remember the index of this action in the array in order to swap it for the opposite
					int indexToReplace = actions.indexOf(action);
					action = action.getOppositeAction();
					actions.set(indexToReplace, action);
				}
			}

			String title = action.getTitle();
			tv.setText(title);

			ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(action.getItemId());
			/*
			 * need to store the image id as tag in order to be able to read it
			 * once it fires a click event
			 */
			imageView.setTag(action.getItemId());

			imageView.setOnTouchListener(new ActionTouchAlphaListener());

			/*
			 * making the image look dim if the action is not supported by all
			 * clients
			 */
			if (null != dbActionsMap && null != action) {
				int actionOccurency = getClientOccurency(dbActionsMap, (TopeAction) action);
				if (actionOccurency < maxEntryOccurency) {
					imageView.setOnTouchListener(new ActionTouchAlphaListener(150));
					ImageUtils.setImageAlpha(imageView, 150);
				}
			}

			/* ON_LONG_CLICK */

			AllClickListeners onLongClickListener = new AllClickListeners(action) {

				@Override
				public boolean onLongClick(View v) {
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

				public void onClick(View v) {
					onLongClick(v);
				}
			};
			v.setOnLongClickListener(onLongClickListener);

			/* ON_CLICK */

			AllClickListeners onClickListener = new AllClickListeners() {
				@Override
				public void onClick(final View v) {

					vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

					/* ****************** */
					/* EXECUTING ACTION */
					/* ****************** */
					final ITopeAction action = ((ITopeAction) TopeUtils.getAction(actions, v));
					/* CONFIRMATION */
					if (action.isConfirmationNeeded()) {
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									executeAction(action, v);
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									return;
								}
							}
						};

						AlertDialog.Builder ab = new AlertDialog.Builder(activity);
						ab.setMessage("Execute \"" + action.getTitle() + "\"?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
					} else {
						executeAction(action, v);
					}

				}

				public boolean onLongClick(View v) {
					onLongClick(v);
					return false;
				}
			};
			v.setOnClickListener(onClickListener);

			ActionClickBehaviour clickBehaviour = getActionBehaviour((TopeAction) action);
			switch (clickBehaviour) {
			case BEHAVE_BOTH_LONG_CLICK:
				v.setOnClickListener((OnClickListener) onLongClickListener);
				break;
			case BEHAVE_BOTH_CLICK:
				v.setOnLongClickListener((OnLongClickListener) onClickListener);
				break;

			default:
				break;
			}

			return v;
		} else {
			return (LinearLayout) convertView;
		}
	}

	private void executeAction(ITopeAction action, View parentView) {

        ActionCareTaker act = null;
        try {
            act = new ActionCareTaker(action, getActivity());
            act.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

		swapActions(action, parentView);
	}

	/**
	 * Checks whether the action has an opposite action and if so it will swap
	 * the current action for the opposite action and display it.
	 * 
	 * @param action
	 * @param parentView
	 */
	private void swapActions(ITopeAction action, View parentView) {
		if (null != action && action.hasOppositeAction()) {

			ImageView actionImage = (ImageView) parentView.findViewById(R.id.gridActionImage);
			actionImage.setImageResource(action.getOppositeAction().getItemId());
			actionImage.setTag(action.getOppositeAction().getItemId());

			/* setting the alpha changer as the action image is touched */
			actionImage.setOnTouchListener(new ActionTouchAlphaListener());

			TextView descriptionText = (TextView) parentView.findViewById(R.id.gridActionText);
			descriptionText.setText(action.getOppositeAction().getTitle());
			String title = action.getOppositeAction().getTitle();
			descriptionText.setText(title);

			int indexToReplace = actions.indexOf(action);

			/* need to deactivate this view */
			PreferencesUtil.writeToPrefs(getActivity(), TAG, action.getMethod(), false);
			Log.i(TAG, "Set " + action.getMethod() + " to INactive ");

			action = action.getOppositeAction();

			/* Swapping item the with the opposite action in the rendering list */
			actions.set(indexToReplace, action);

			/* Setting cache to remember the item when fragments change */
			PreferencesUtil.writeToPrefs(getActivity(), TAG, action.getMethod(), true);
			Log.i(TAG, "Set " + action.getMethod() + " to Active ");
		}
	}

	private int getMaxClientOccurencies(HashMap<TopeAction, Integer> dbActionsMap) {
		int i = 0;
		for (Map.Entry<TopeAction, Integer> entry : dbActionsMap.entrySet()) {
			if (entry.getValue() > i) {
				i = entry.getValue();
			}
		}

		return i;
	}

	private int getClientOccurency(HashMap<TopeAction, Integer> dbActionsMap, TopeAction action) {
		for (Map.Entry<TopeAction, Integer> entry : dbActionsMap.entrySet()) {
			if (entry.getKey().getCommandFullPath().equals(action.getCommandFullPath())) {
				return entry.getValue();
			}
		}

		return -1;
	}

	private GeneralSectionFragment.ActionClickBehaviour getActionBehaviour(TopeAction action) {
		GeneralSectionFragment gsf = (GeneralSectionFragment) fragment;
		HashMap<String, ActionClickBehaviour> clickBehaviourMap = gsf.getClickBehaviourMap();
		for (Map.Entry<String, ActionClickBehaviour> entry : clickBehaviourMap.entrySet()) {
			if (entry.getKey().equals(action.getCommandFullPath())) {
				return entry.getValue();
			}
		}
		return ActionClickBehaviour.NORMAL;
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

	abstract class AllClickListeners implements View.OnLongClickListener, View.OnClickListener {
		ITopeAction action;

		public AllClickListeners() {
		}

		public AllClickListeners(ITopeAction action) {
			this.action = action;
		}
	}

}
