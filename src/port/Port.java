package port;

import container.Container;
import vehicle.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Port implements Serializable {
    private String portID;
    private String name;
    private String portManager;
    private double latitude;
    private double longitude;
    private double storingCapacity;
    private boolean isLanding;
    private List<Container> containerList;
    private List<Vehicle> vehicleList;
    private List<Trip> tripList;

    public Port() {
        this.portID = "Default";
        this.name = "Default";
        this.portManager = null;
        this.latitude = 0;
        this.longitude = 0;
        this.storingCapacity = 0;
        this.isLanding = false;
        this.containerList = new ArrayList<>();
        this.vehicleList = new ArrayList<>();
        this.tripList = new ArrayList<>();
    }

    public Port(String portID, String name, String portManager , double latitude, double longitude, double storingCapacity, boolean isLanding) {
        this.portID = portID;
        this.name = name;
        this.portManager = portManager;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storingCapacity = storingCapacity;
        this.isLanding = isLanding;
        this.containerList = new ArrayList<>();
        this.vehicleList = new ArrayList<>();
        this.tripList = new ArrayList<>();
    }

    public String getPortID() {
        return portID;
    }

    public void setPortID(String portID) {
        this.portID = portID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortManager() {
        return portManager;
    }

    public void setPortManager(String portManager) {
        this.portManager = portManager;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getStoringCapacity() {
        return storingCapacity;
    }

    public void setStoringCapacity(double storingCapacity) {
        this.storingCapacity = storingCapacity;
    }

    public boolean isLanding() {
        return isLanding;
    }

    public void setLanding(boolean landing) {
        isLanding = landing;
    }

    public List<Container> getContainerList() {
        return containerList;
    }

    public void setContainerList(List<Container> containerList) {
        this.containerList = containerList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }

    public Port addPortContainer(Container container) {
        this.containerList.add(container);
        return this;
    }

    public Port removePortContainer(Container container) {
        this.containerList.remove(container);
        return this;
    }

    public Port addPortVehicle(Vehicle vehicle) {
        this.vehicleList.add(vehicle);
        return this;
    }

    public Port removePortVehicle(Vehicle vehicle) {
        this.vehicleList.remove(vehicle);
        return this;
    }

    public Port addPortTrip(Trip trip) {
        this.tripList.add(trip);
        return this;
    }

    public Port removePortTrip(Trip trip) {
        this.tripList.remove(trip);
        return this;
    }

    public double calculateDistance(Port anotherPort) {
        double radiusOfEarth = 6371;

        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(this.latitude);
        double lon1Rad = Math.toRadians(this.longitude);
        double lat2Rad = Math.toRadians(anotherPort.getLatitude());
        double lon2Rad = Math.toRadians(anotherPort.getLongitude());

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        return Math.round(radiusOfEarth * c);
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        return String.valueOf(formatter.format("| %-10s | %-20s | %-20s | %10.4f | %10.4f | %15.2f | %-10b | %10d | %8d | %6d |", portID, name, portManager , latitude, longitude, storingCapacity, isLanding, containerList.size(), vehicleList.size(), tripList.size()));
    }
}
