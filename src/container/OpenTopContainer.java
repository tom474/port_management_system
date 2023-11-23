package container;

import port.Port;

public class OpenTopContainer extends Container {
    public OpenTopContainer() {
    }

    public OpenTopContainer(String containerID, double weight, String currentPort) {
        super(containerID, weight, "Open Top Container", 2.8, 3.2, currentPort);
    }
}
