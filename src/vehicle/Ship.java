package vehicle;

import container.Container;
import port.Port;

public class Ship extends Vehicle {
    public Ship() {
    }

    public Ship(String vehicleID, String name, double carryingCapacity, double fuelCapacity, String port) {
        super(vehicleID, name, "Ship", carryingCapacity, fuelCapacity, port);
    }
}
