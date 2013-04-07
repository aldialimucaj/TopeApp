package al.aldi.tope.model;

import java.util.Iterator;
import java.util.List;

import al.aldi.tope.model.db.ClientDataSource;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class TopeClient implements Parcelable {

    private static final String	LOG_TAG	= "TopeClient";

    private long				id;
    private String				name;
    private String				ip;
    private String				port;
    private String				user;
    private String				pass;
    private boolean				active;
    private Context				context;

    public TopeClient() {
        super();
    }

    public TopeClient(Parcel in) {
        readFromParcel(in);
    }

    public TopeClient(String name, String ip, String port) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public TopeClient(String name, String ip, String port, Context context) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.context = context;
    }

    public TopeClient(String name, String ip, String port, String user, String pass, boolean active) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.active = active;
    }

    public void insertDb() {
        if (null != context) {
            ClientDataSource source = new ClientDataSource(context);
            source.open();
            source.create(this);
            source.close();
        } else {
            Log.e(LOG_TAG, "Cannot insert client without a context! Please set it up.");
        }

    }

    public void updateDb() {
        if (null != context) {
            ClientDataSource source = new ClientDataSource(context);
            source.open();
            source.updateClient(this);
            source.close();
        } else {
            Log.e(LOG_TAG, "Cannot update client without a context! Please set it up.");
        }

    }

    public String getURL() {
        return "http://" + ip + ":" + port;
    }

    public String getURL(String command) {
        return getURL() + command;
    }

    public void delete() {
        if (null != context) {
            ClientDataSource source = new ClientDataSource(context);
            source.open();
            source.deleteClient(this);
            source.close();
        } else {
            Log.e(LOG_TAG, "Cannot update client without a context! Please set it up.");
        }
    }

    public void printFromDb() {
        ClientDataSource source = new ClientDataSource(context);
        source.open();
        System.out.println(source.getClient(id));
        source.close();
    }

    public void printAll() {
        ClientDataSource source = new ClientDataSource(context);
        source.open();

        List<TopeClient> clients = source.getAll();
        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
            TopeClient topeClient = (TopeClient) iterator.next();
            System.out.println(topeClient);
        }
        source.close();

    }

    @Override
    public String toString() {
        return "CLIENT: [" + id + "] " + name + " " + ip + " " + port + " " + active;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(ip);
        dest.writeString(port);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(user);
        dest.writeString(pass);

    }

    private void readFromParcel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ip = in.readString();
        port = in.readString();
        active = in.readByte() == 1;
        user = in.readString();
        pass = in.readString();
    }

    public static final Parcelable.Creator	CREATOR	= new Parcelable.Creator() {
                                                        public TopeClient createFromParcel(Parcel in) {
                                                            return new TopeClient(in);
                                                        }

                                                        public TopeClient[] newArray(int size) {
                                                            return new TopeClient[size];
                                                        }
                                                    };
}
