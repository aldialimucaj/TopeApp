package al.aldi.tope.controller;

import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import android.os.Parcel;
import android.os.Parcelable;

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

    private int					itemId				= 0;
    private String				title				= null;
    private String				command				= null;

    ITopeExecutable				exec;
    ITopeAction					oppositeAction;
    ITopePayload				payload;

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
        this.command = command;
    }

    public TopeAction(int itemId, String title, String command, ITopePayload payload) {
        super();
        this.itemId = itemId;
        this.title = title;
        this.command = command;
        this.payload = payload;
    }

    @Override
    public TopeResponse execute(TopeClient client) {
        if (null == exec) {
            throw new ExceptionInInitializerError("ITopeExecutable exec not implemented");
        }
        return exec.run(client);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ITopePayload getPayload() {
        return payload;
    }

    public void setPayload(ITopePayload payload) {
        this.payload = payload;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemId);
        dest.writeString(title);
        dest.writeString(command);
    }

    private void readFromParcel(Parcel in) {
        itemId = (int) in.readLong();
        title = in.readString();
        command = in.readString();
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator	CREATOR	= new Parcelable.Creator() {
                                                        public TopeAction createFromParcel(Parcel in) {
                                                            return new TopeAction(in);
                                                        }

                                                        public TopeAction[] newArray(int size) {
                                                            return new TopeAction[size];
                                                        }
                                                    };



}
