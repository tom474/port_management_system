package container;

import port.Port;

public class OpenSideContainer extends Container {
    public OpenSideContainer() {
    }

    public OpenSideContainer(String containerID, double weight, String currentPort) {
        super(containerID, weight, "Open Side Container", 2.7, 3.2, currentPort);
    }
}

