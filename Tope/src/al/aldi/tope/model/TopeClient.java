package al.aldi.tope.model;

public class TopeClient {
    private long	id;
    private String	name;
    private String	ip;
    private String	port;
    private String	user;
    private String	pass;
    private boolean	active;

    public TopeClient() {
        super();
    }

    public TopeClient(String name, String ip, String port) {
        super();
        this.name = name;
        this.ip = ip;
        this.port = port;
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

    @Override
    public String toString() {
        return "CLIENT: [" + id + "] " + name + " " + ip + " " + port + " " + active;
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

}
