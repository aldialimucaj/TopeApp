package al.aldi.tope.view.listeners;

import al.aldi.tope.R;
import android.app.Activity;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * This represents the long lick menues on the header.
 *
 * @author Aldi Alimucaj
 *
 */
public class ActionLongClickListener implements OnLongClickListener {

    Activity	mActivity			= null;
    ActionMode	mActionMode			= null;
    Callback	mActionModeCallback	= null;

    public ActionLongClickListener(Activity mActivity, Callback mActionModeCallback) {
        super();
        this.mActivity = mActivity;
        this.mActionModeCallback = mActionModeCallback;
    }

    public ActionLongClickListener(Activity mActivity) {
        super();
        this.mActivity = mActivity;
        mActionModeCallback = new ActionModeCallback();
    }

    @Override
    public boolean onLongClick(View view) {
        if (mActionMode != null) {
            return false;
        }
        System.out.println("ActionLongClickListener.onLongClick() "+"Pretty long click");
        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = mActivity.startActionMode(mActionModeCallback);
        view.setSelected(true);
        return true;
    }

    class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Here is where i can inflate the dynamic menu
            MenuInflater inflater = mode.getMenuInflater();
            menu.addSubMenu("Submenu1");
            menu.addSubMenu("Submenu2");
            inflater.inflate(R.menu.action_dynamic_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case 0:
                System.out.println(item.getItemId());
                mode.finish();
                return true;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }

    }
}
