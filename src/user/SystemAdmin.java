package user;

import port.Port;

public class SystemAdmin extends User{
    public SystemAdmin() {
    }

    public SystemAdmin(String userID, String username, String password) {
        super(userID, username, password, "System Admin", "All Ports");
    }
}
