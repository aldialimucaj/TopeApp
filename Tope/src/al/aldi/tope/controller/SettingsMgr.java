package al.aldi.tope.controller;

public class SettingsMgr {
    private static SettingsMgr	instance	= null;

    private String				serverName;
    private int					port		= 0;

    private SettingsMgr() {
    }

    public static SettingsMgr getInstance() {
        if (null == instance) {
            instance = new SettingsMgr();
        }
        return instance;
    }

    public String getURL() {
        return "http://" + serverName + ":" + port;
    }

    public String getURL(String command) {
        return getURL() + command;
    }

    /*
     * GENERATED
     */

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
