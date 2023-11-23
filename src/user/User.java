package user;

import port.Port;

import java.io.Serializable;
import java.util.Formatter;

public abstract class User implements Serializable {
    private String userID;
    private String username;
    private String password;
    private String role;
    private String port;

    public User() {
        this.userID = "Default";
        this.username = "Default";
        this.password = "Default";
        this.role = "Default";
        this.port = null;
    }

    public User(String userID, String username, String password, String role, String port) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.port = port;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        return String.valueOf(formatter.format("| %-8s | %-15s | %-16s | %-15s | %-20s |", userID, role, username, password, port));
    }
}
