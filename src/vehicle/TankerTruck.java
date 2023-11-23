package vehicle;

import container.Container;
import port.Port;

public class TankerTruck extends Vehicle {
    public TankerTruck() {
    }

    public TankerTruck(String vehicleID, String name, double carryingCapacity, double fuelCapacity, String port) {
        super(vehicleID, name, "Tanker Truck", carryingCapacity, fuelCapacity, port);
    }
}
