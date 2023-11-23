package vehicle;

import container.Container;
import port.Port;

public class BasicTruck extends Vehicle{
    public BasicTruck() {
    }

    public BasicTruck(String vehicleID, String name, double carryingCapacity, double fuelCapacity, String port) {
        super(vehicleID, name, "Basic Truck", carryingCapacity, fuelCapacity, port);
    }
}
