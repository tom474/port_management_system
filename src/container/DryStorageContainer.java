package container;

import port.Port;

public class DryStorageContainer extends Container {
    public DryStorageContainer() {
    }

    public DryStorageContainer(String containerID, double weight, String currentPort) {
        super(containerID, weight, "Dry Storage Container", 3.5, 4.6, currentPort);
    }
}
