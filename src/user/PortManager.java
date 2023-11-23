package user;

import port.Port;

public class PortManager extends User{

    public PortManager() {
    }

    public PortManager(String userID, String username, String password, String port) {
        super(userID, username, password, "Port Manager", port);
    }
}
