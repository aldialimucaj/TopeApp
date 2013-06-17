package al.aldi.tope.view;

import al.aldi.tope.R;
import al.aldi.tope.utils.TopeActionUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class UtilsSectionFragment extends GeneralSectionFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public static final String ACTION_PREFIX      = "/utils/";

    int                        fragmentId         = R.layout.gridview_fragment;
    int                        fragmentGridId     = R.id.fragmentGridView;

    /* ******************* ITopeActions ******************** */

    public UtilsSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getUtilsActionUtil();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;
    }

    protected void fillTitlesMap() {

    }

    protected void setExecutorsMap() {
    }

    protected void setOppositeActionsMap() {

    }

    protected void fillIconMap() {

    }
}