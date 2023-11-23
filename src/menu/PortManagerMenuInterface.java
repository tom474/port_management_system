package menu;

import port.Port;
import user.User;

import java.io.IOException;

public interface PortManagerMenuInterface {
    void execute(User portManager) throws IOException, ClassNotFoundException;
    void viewPort(Port port);
    void viewPortContainers(Port port);
    void addPortContainer(Port port) throws IOException, ClassNotFoundException;
    void removePortContainer(Port port) throws IOException, ClassNotFoundException;
    void viewPortVehicles(Port port);
    void viewPortShips(Port port);
    void viewPortTrucks(Port port);
    void addPortVehicle(Port port) throws IOException, ClassNotFoundException;
    void removePortVehicle(Port port) throws IOException, ClassNotFoundException;
    void loadContainer(Port port) throws IOException, ClassNotFoundException;
    void unloadContainer(Port port) throws IOException, ClassNotFoundException;
    void viewTrips(Port port) throws IOException, ClassNotFoundException;
    void movePortVehicle(Port port) throws IOException, ClassNotFoundException;
    void calculateDistance(Port port) throws IOException, ClassNotFoundException;
}
