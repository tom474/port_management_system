package container;

import port.Port;

import java.io.Serializable;
import java.util.Formatter;

public abstract class Container implements Serializable {
    private String containerID;
    private double weight;
    private String containerType;
    private double shipFuelConsumption;
    private double truckFuelConsumption;
    private String currentPort;

    public Container() {
        this.containerID = "Default";
        this.weight = 0;
        this.containerType = "Default";
        this.shipFuelConsumption = 0;
        this.truckFuelConsumption = 0;
        this.currentPort = "Default";
    }

    public Container(String containerID, double weight, String containerType, double shipFuelConsumption, double truckFuelConsumption, String currentPort) {
        this.containerID = containerID;
        this.weight = weight;
        this.containerType = containerType;
        this.shipFuelConsumption = shipFuelConsumption;
        this.truckFuelConsumption = truckFuelConsumption;
        this.currentPort = currentPort;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public double getShipFuelConsumption() {
        return shipFuelConsumption;
    }

    public void setShipFuelConsumption(double shipFuelConsumption) {
        this.shipFuelConsumption = shipFuelConsumption;
    }

    public double getTruckFuelConsumption() {
        return truckFuelConsumption;
    }

    public void setTruckFuelConsumption(double truckFuelConsumption) {
        this.truckFuelConsumption = truckFuelConsumption;
    }

    public String getCurrentPort() {
        return currentPort;
    }

    public void setCurrentPort(String currentPort) {
        this.currentPort = currentPort;
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        return String.valueOf(formatter.format("| %-14s | %-24s | %-20s | %10s |", containerID, containerType, currentPort, weight));
    }
}

