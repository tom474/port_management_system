package manage;

import java.io.IOException;

public interface ManageVehicleInterface {
    void viewVehicles() throws IOException, ClassNotFoundException;
    void viewShips() throws IOException, ClassNotFoundException;
    void viewTrucks() throws IOException, ClassNotFoundException;
    void addVehicle() throws IOException, ClassNotFoundException;
    void updateVehicle() throws IOException, ClassNotFoundException;
    void removeVehicle() throws IOException, ClassNotFoundException;
    void refuelVehicle() throws IOException, ClassNotFoundException;
}
