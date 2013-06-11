package al.aldi.tope.view.dialog.fragment;

public interface ITopeActionDialog {


    /**
     * Set up the parameters before executing.
     */
    public void setUp();
    /**
     * Post execution method. If there is something the be cleaned up after performin action.
     * For example resetting the text.
     *
     */
    public void cleanUp();
}
