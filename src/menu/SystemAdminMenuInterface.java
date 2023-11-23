package menu;

import java.io.IOException;

public interface SystemAdminMenuInterface {
    void execute() throws IOException, ClassNotFoundException;
    void managePorts() throws IOException, ClassNotFoundException;
    void manageVehicles() throws IOException, ClassNotFoundException;
    void manageContainers() throws IOException, ClassNotFoundException;
    void managePortManagers() throws IOException, ClassNotFoundException;
    void calculateFuelInGivenDay() throws IOException, ClassNotFoundException;
    void viewAllTrips() throws IOException, ClassNotFoundException;
    void viewTripsInGivenDay() throws IOException, ClassNotFoundException;
    void viewTripsFromDayAToDayB() throws IOException, ClassNotFoundException;
}
