/**
 *
 */
package al.aldi.tope.controller.executables;

import al.aldi.tope.model.ITopeAction;
import android.support.v4.app.Fragment;

/**
 * @author Aldi Alimucaj
 *
 */
public class DefaultExecutor<TopeResponse> extends MainExecutor<TopeResponse> {

    ITopeAction<TopeResponse> action       = null;
    TopeResponse              topeResponse = null;
    Fragment                  fragment     = null;

    public DefaultExecutor(ITopeAction<TopeResponse> defaultAction, Fragment fragment) {
        super(defaultAction, fragment);
        this.action = defaultAction;
        this.fragment = fragment;
    }

    @Override
    public void postRun(TopeResponse response) {
        // TODO Auto-generated method stub

    }

}
