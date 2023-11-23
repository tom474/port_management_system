package manage;

import java.io.IOException;

public interface ManageContainerInterface {
    void viewContainers() throws IOException, ClassNotFoundException;
    void addContainer() throws IOException, ClassNotFoundException;
    void removeContainer() throws IOException, ClassNotFoundException;
    void calculateTotalWeightOfEachType() throws IOException, ClassNotFoundException;
}
