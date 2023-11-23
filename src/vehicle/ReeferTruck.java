package vehicle;

import container.Container;
import port.Port;

public class ReeferTruck extends Vehicle{
    public ReeferTruck() {
    }

    public ReeferTruck(String vehicleID, String name, double carryingCapacity, double fuelCapacity, String port) {
        super(vehicleID, name, "Reefer Truck", carryingCapacity, fuelCapacity, port);
    }
}
