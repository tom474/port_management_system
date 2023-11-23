package manage;

import java.io.IOException;

public interface ManageUserInterface {
    void viewPortManagers() throws IOException, ClassNotFoundException;
    void addPortManager() throws IOException, ClassNotFoundException;
    void updatePortManager() throws IOException, ClassNotFoundException;
    void removePortManager() throws IOException, ClassNotFoundException;
}
