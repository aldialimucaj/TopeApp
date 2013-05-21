package al.aldi.tope.view.dialog.fragment;

public interface ITopeActionDialog {

    /**
     * Post execution method. If there is something the be cleaned up after performin action.
     * For example resetting the text.
     *
     */
    public void cleanUp();
}
