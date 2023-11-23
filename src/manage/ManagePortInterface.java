package manage;

import java.io.IOException;

public interface ManagePortInterface {
    void viewPorts() throws IOException, ClassNotFoundException;
    void addPort() throws IOException, ClassNotFoundException;
    void updatePort() throws IOException, ClassNotFoundException;
    void removePort() throws IOException, ClassNotFoundException;
    void assignPortManagerToPort() throws IOException, ClassNotFoundException;
    void viewPortContainers() throws IOException, ClassNotFoundException;
    void addContainerToPort() throws IOException, ClassNotFoundException;
    void removeContainerFromPort() throws IOException, ClassNotFoundException;
    void viewPortVehicles() throws IOException, ClassNotFoundException;
    void viewPortShips() throws IOException, ClassNotFoundException;
    void viewPortTrucks() throws IOException, ClassNotFoundException;
    void addVehicleToPort() throws IOException, ClassNotFoundException;
    void removeVehicleFromPort() throws IOException, ClassNotFoundException;
    void loadPortContainer() throws IOException, ClassNotFoundException;
    void unloadPortContainer() throws IOException, ClassNotFoundException;
    void movePortVehicle() throws IOException, ClassNotFoundException;
    void calculateDistance() throws IOException, ClassNotFoundException;
    void viewPortTripHistory() throws IOException, ClassNotFoundException;

}
