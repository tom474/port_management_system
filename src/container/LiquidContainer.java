package container;

import port.Port;

public class LiquidContainer extends Container {
    public LiquidContainer() {
    }

    public LiquidContainer(String containerID, double weight, String currentPort) {
        super(containerID, weight, "Liquid Container", 4.8, 5.3, currentPort);
    }
}
