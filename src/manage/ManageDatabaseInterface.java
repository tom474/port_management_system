package manage;

import container.Container;
import port.Port;
import port.Trip;
import user.User;
import vehicle.Vehicle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ManageDatabaseInterface {
    List<Port> getPortDatabase() throws IOException, ClassNotFoundException;
    List<Vehicle> getVehicleDatabase() throws IOException, ClassNotFoundException;
    List<Container> getContainerDatabase() throws IOException, ClassNotFoundException;
    List<Trip> getTripDatabase() throws IOException, ClassNotFoundException;
    List<User> getUserDatabase() throws IOException, ClassNotFoundException;
    void updateTripDatabase() throws IOException, ClassNotFoundException;
    void initializeDatabase() throws IOException;
}
