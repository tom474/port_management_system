package container;

import port.Port;

public class RefrigeratedContainer extends Container {
    public RefrigeratedContainer() {
    }

    public RefrigeratedContainer(String containerID, double weight, String currentPort) {
        super(containerID, weight, "Refrigerated Container", 4.5, 5.4, currentPort);
    }
}
