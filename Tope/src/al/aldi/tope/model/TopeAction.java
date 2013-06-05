package al.aldi.tope.model;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.utils.TopeActionUtils;
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
public class TopeAction<E> implements ITopeAction<E> {

    private int     actionId         = -1;
    private int     iconId           = 0;
    private String  title            = null;
    private String  command          = null;
    private int     revisionId       = 0;
    private boolean active           = true;
    private int     oppositeActionId = -1;

    /* context view which will be shown if long click for example */
    View            contextView      = null;

    ITopeExecutable<E> exec;
    ITopeAction<E>     oppositeAction;
    ITopePayload    payload          = new TopePayload();

    public TopeAction() {
    }

    public TopeAction(Parcel in) {
        readFromParcel(in);
    }

    public TopeAction(int itemId) {
        this.iconId = itemId;
    }

    public TopeAction(int itemId, String title) {
        this.iconId = itemId;
        this.title = title;
    }

    public TopeAction(int itemId, String title, ITopePayload payload) {
        super();
        this.iconId = itemId;
        this.title = title;
        this.payload = payload;
    }

    public TopeAction(int itemId, String title, String command) {
        super();
        this.iconId = itemId;
        this.title = title;
        this.command = command;
    }

    public TopeAction(String command, int itemId, String title) {
        super();
        this.iconId = itemId;
        this.title = title;
        this.command = command;
    }

    public TopeAction(int itemId, String title, String command, ITopePayload payload) {
        super();
        this.iconId = itemId;
        this.title = title;
        this.command = command;
        this.payload = payload;
    }

    @Override
    public E execute(TopeClient client) {
        if (null == exec) {
            throw new ExceptionInInitializerError("ITopeExecutable exec not implemented");
        }
        return exec.run(client);
    }

    @Override
    public String toString() {
        return "[" + iconId + "] " + command;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + actionId;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + iconId;
        result = prime * result + revisionId;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        TopeAction other = (TopeAction) obj;
        if (actionId != other.actionId)
            return false;
        if (active != other.active)
            return false;
        if (command == null) {
            if (other.command != null)
                return false;
        } else if (!command.equals(other.command))
            return false;
        if (iconId != other.iconId)
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

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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
    public int getIconId() {
        return iconId;
    }

    @Override
    public void setExecutable(ITopeExecutable<E> exec) {
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

    public int getOppositeActionId() {
        return oppositeActionId;
    }

    public void setOppositeActionId(int oppositeActionId) {
        this.oppositeActionId = oppositeActionId;
    }

    @Override
    public void switchAction() {

    }

    @Override
    public void setOppositeAction(ITopeAction<E> opAction) {
        this.oppositeAction = opAction;
    }

    @Override
    public ITopeAction<E> getOppositeAction() {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(iconId);
        dest.writeString(title);
        dest.writeString(command);
    }

    private void readFromParcel(Parcel in) {
        iconId = (int) in.readLong();
        title = in.readString();
        command = in.readString();
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
