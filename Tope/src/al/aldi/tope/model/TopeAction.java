package al.aldi.tope.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;

import al.aldi.tope.controller.ITopeExecutable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Implementation of the tope action which represent a user action
 * that is going to execute a command into to server. Clients contain
 * payload that represent the model or data they are going to deliver.
 * But the main command ist represented by the parameter <b>command</b>
 * which is the sub URL address accecption this call.
 * Actioncions may also contain opposite actions which are called to replace
 * the original action. An action executable object is the object that
 * takes care of the execution of the HTTP Request to the active clients.
 *
 * @author Aldi Alimucaj
 *
 */
public class TopeAction implements ITopeAction {
    @Expose
    private long    actionId         = -1;
    @Expose
    private long    clientId         = -1;
    @Expose
    private int     itemId           = 0;
    @Expose
    private String  module           = null;
    @Expose
    private String  method           = null;
    @Expose
    private String  commandFullPath  = null;
    @Expose
    private String  title            = null;
    @Expose
    private boolean active           = true;
    @Expose
    private int     revisionId       = 0;
    @Expose
    private long    oppositeActionId = -1;
    @Expose
    private boolean outputIgnored           = true;

    /* context view which will be shown if long click for example */
    protected View            contextView      = null;

    protected ITopeExecutable exec;
    
    protected ITopeAction     oppositeAction;
    
    @Expose
    protected ITopePayload    payload          = new TopePayload();

    public TopeAction() {
    }

    public TopeAction(Parcel in) {
        readFromParcel(in);
    }

    public TopeAction(int itemId) {
        this.itemId = itemId;
    }

    public TopeAction(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    public TopeAction(int itemId, String title, ITopePayload payload) {
        super();
        this.itemId = itemId;
        this.title = title;
        this.payload = payload;
    }

    public TopeAction(int itemId, String title, String command) {
        super();
        this.itemId = itemId;
        this.title = title;
        this.method = command;
    }

    public TopeAction(String command, int itemId, String title) {
        super();
        this.itemId = itemId;
        this.title = title;
        this.method = command;
        this.commandFullPath = command;
    }

    public TopeAction(int itemId, String title, String command, ITopePayload payload) {
        super();
        this.itemId = itemId;
        this.title = title;
        this.method = command;
        this.payload = payload;
    }

    @Override
    public Object execute(TopeClient client) {
        if (null == exec) {
            throw new ExceptionInInitializerError("ITopeExecutable exec not implemented");
        }
        return exec.run(client);
    }

    @Override
    public String toString() {
        return "[" + actionId + "] " + method;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        long result = 1;
        result = actionId;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + itemId;
        result = prime * result + revisionId;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return (int) result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TopeAction other = (TopeAction) obj;
        if (actionId != other.actionId)
            return false;
        if (active != other.active)
            return false;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (itemId != other.itemId)
            return false;
        if (revisionId != other.revisionId)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setItemId(int iconId) {
        this.itemId = iconId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String command) {
        this.method = command;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCommandFullPath() {
        return commandFullPath;
    }

    public void setCommandFullPath(String commandFullPath) {
        this.commandFullPath = commandFullPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ITopePayload getPayload() {
        return payload;
    }

    public void setPayload(ITopePayload payload) {
        this.payload = payload;
    }

    public View getContextView() {
        return contextView;
    }

    public void setContextView(View contextView) {
        this.contextView = contextView;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public void setExecutable(ITopeExecutable exec) {
        this.exec = exec;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public long getOppositeActionId() {
        return oppositeActionId;
    }

    public void setOppositeActionId(long oppositeActionId) {
        this.oppositeActionId = oppositeActionId;
    }

    @Override
    public void switchAction() {

    }

    @Override
    public void setOppositeAction(ITopeAction opAction) {
        this.oppositeAction = opAction;
    }

    @Override
    public ITopeAction getOppositeAction() {
        return oppositeAction;
    }

    @Override
    public boolean hasOppositeAction() {
        return null != this.oppositeAction;
    }

    @Override
    public boolean hasPayload() {
        return null != getPayload();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void setOutputIgnored(boolean ignore) {
        outputIgnored = ignore;
        
    }

    @Override
    public boolean isOutputIgnored() {
        return outputIgnored;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemId);
        dest.writeString(title);
        dest.writeString(method);
    }

    private void readFromParcel(Parcel in) {
        itemId = (int) in.readLong();
        title = in.readString();
        method = in.readString();
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
                                                       public TopeAction createFromParcel(Parcel in) {
                                                           return new TopeAction(in);
                                                       }

                                                       public TopeAction[] newArray(int size) {
                                                           return new TopeAction[size];
                                                       }
                                                   };

   
}
