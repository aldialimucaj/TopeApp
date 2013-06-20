package al.aldi.tope.model.responses;

import java.util.List;
import java.util.Vector;

import com.google.gson.annotations.Expose;

import al.aldi.tope.model.TopeAction;

public class ActionSynchResponse implements ITopeResponsePayload {
    @Expose
    List<TopeAction> actions;
    public ActionSynchResponse() {
        actions = new Vector<TopeAction>();
    }
    public List<TopeAction> getActions() {
        return actions;
    }
    public void setActions(List<TopeAction> actions) {
        this.actions = actions;
    }

}
