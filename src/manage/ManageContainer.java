package manage;

import container.*;
import port.Port;
import vehicle.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ManageContainer implements ManageContainerInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File vehicleDatabase = new File("src/model/vehicle.dat");
    private final File containerDatabase = new File("src/model/container.dat");

    @Override
    public void viewContainers() throws IOException, ClassNotFoundException {
        // Get the database
        ManageDatabase manageDatabase = new ManageDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();

        // Display Containers
        if (!containerList.isEmpty()) {
            System.out.println("--------------------------------- All Containers --------------------------------");
            System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
            System.out.println();
            for (Container container : containerList) {
                System.out.println(container);
            }
            System.out.println("---------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no container in the database!");
        }
        System.out.println();
    }

    @Override
    public void addContainer() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ManageDatabase manageDatabase = new ManageDatabase();
        ManagePort managePort = new ManagePort();

        // Get the database
        List<Container> containerList = manageDatabase.getContainerDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();

        // Get user input
        String containerType;
        String selectType;
        label:
        while (true) {
            System.out.println("Choose the container's type: ");
            System.out.println("1. Dry Storage Container");
            System.out.println("2. Liquid Container");
            System.out.println("3. Open Side Container");
            System.out.println("4. Open Top Container");
            System.out.println("5. Refrigerated Container");
            System.out.print("Enter a number from 1 to 5: ");
            selectType = scanner.nextLine();
            switch (selectType) {
                case "1":
                    containerType = "Dry Storage Container";
                    break label;
                case "2":
                    containerType = "Liquid Container";
                    break label;
                case "3":
                    containerType = "Open Side Container";
                    break label;
                case "4":
                    containerType = "Open Top Container";
                    break label;
                case "5":
                    containerType = "Refrigerated Container";
                    break label;
                default:
                    System.out.println("Invalid Input! Please enter an integer number from 1 to 5.");
                    break;
            }
        }

        System.out.print("Enter the container's weight (kg) (ex. 1500): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();
        String portName = null;
        String portID;
        Port assigningPort = null;
        boolean isStore = false;
        while (!isStore) {
            managePort.viewPorts();
            System.out.print("Enter a the port's id that you want to assign this container (ex. P0001): ");
            portID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(portID)) {
                    portName = port.getName();
                    assigningPort = port;
                }
            }
            if (assigningPort != null) {
                List<Container> existingContainerList = assigningPort.getContainerList();
                double currentWeight = 0;
                for (Container container : existingContainerList) {
                    currentWeight += container.getWeight();
                }
                if (currentWeight + weight >= assigningPort.getStoringCapacity()) {
                    System.out.println(assigningPort.getName() + " is full! Please choose another port.");
                } else {
                    isStore = true;
                }
            } else {
                System.out.println("The port's id you entered is not exist! Please enter another port's id.");
            }
        }

        String containerID = null;
        boolean isExistID = true;
        while (isExistID) {
            containerID = "C" + random.nextInt(1000, 9999);
            for (Container container : containerList) {
                isExistID = containerID.equals(container.getContainerID());
            }
        }

        // Create new container
        Container newContainer;
        switch (containerType) {
            case "Dry Storage Container" -> newContainer = new DryStorageContainer(containerID, weight, portName);
            case "Liquid Container" -> newContainer = new LiquidContainer(containerID, weight, portName);
            case "Open Side Container" -> newContainer = new OpenSideContainer(containerID, weight, portName);
            case "Open Top Container" -> newContainer = new OpenTopContainer(containerID, weight, portName);
            default -> newContainer = new RefrigeratedContainer(containerID, weight, portName);
        }

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port: portList) {
            if (port.getName().equals(portName)) {
                port = port.addPortContainer(newContainer);
            }
            newPortList.add(port);
        }
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newPortList);
        oos1.close();

        // Update container database
        containerList.add(newContainer);
        FileOutputStream fos2 = new FileOutputStream(containerDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(containerList);
        oos2.close();
        System.out.println("Add container " + containerID + " successfully!");
    }

    @Override
    public void removeContainer() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Container> containerList = manageDatabase.getContainerDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();

        // Get user input
        Container removingContainer = null;
        String removingContainerID = null;
        viewContainers();
        boolean isExistID = false;
        while (!isExistID) {
            System.out.print("Enter the container's id that you want to remove (ex. C0001): ");
            removingContainerID = scanner.nextLine();
            for (Container container : containerList) {
                if (removingContainerID.equals(container.getContainerID())) {
                    removingContainer = container;
                    isExistID = true;
                    break;
                }
            }
            if (!isExistID) {
                System.out.println("This container's id is not exist! Please enter another container's id.");
            }
        }

        Vehicle updatingVehicle = null;
        for (Vehicle vehicle : vehicleList) {
            List<Container> vehicleContainerList = vehicle.getContainerList();
            for (Container container : vehicleContainerList) {
                if (container.getContainerID().equals(removingContainerID)) {
                    updatingVehicle = vehicle;
                    updatingVehicle = updatingVehicle.unloadContainer(removingContainer);
                    break;
                }
            }
        }

        List<Port> newPortList = new ArrayList<>();
        if (updatingVehicle != null) {
            // Update port database
            for (Port port : portList) {
                List<Vehicle> portVehicleList = port.getVehicleList();
                List<Vehicle> newPortVehicleList = port.getVehicleList();
                List<Container> portContainerList = port.getContainerList();
                for (Container container : portContainerList) {
                    if (container.getContainerID().equals(removingContainerID)) {
                        port = port.removePortContainer(removingContainer);
                        for (Vehicle vehicle : portVehicleList) {
                            if (vehicle.getVehicleID().equals(updatingVehicle.getVehicleID())) {
                                vehicle = updatingVehicle;
                            }
                            newPortVehicleList.add(vehicle);
                        }
                    }

                }
                newPortList.add(port);
            }
            FileOutputStream fos1 = new FileOutputStream(portDatabase);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(newPortList);
            oos1.close();

            // Update vehicle database
            List<Vehicle> newVehicleList = new ArrayList<>();
            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getVehicleID().equals(updatingVehicle.getVehicleID())) {
                    vehicle = updatingVehicle;
                }
                newVehicleList.add(vehicle);
            }
            FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(newVehicleList);
            oos2.close();

            // Update container database
            containerList.remove(removingContainer);
            FileOutputStream fos3 = new FileOutputStream(containerDatabase);
            ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
            oos3.writeObject(containerList);
            oos3.close();
        } else {
            // Update port database
            for (Port port : portList) {
                List<Container> portContainerList = port.getContainerList();
                for (Container container : portContainerList) {
                    if (container.getContainerID().equals(removingContainerID)) {
                        port = port.removePortContainer(removingContainer);
                    }
                }
                newPortList.add(port);
            }
            FileOutputStream fos1 = new FileOutputStream(portDatabase);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(newPortList);
            oos1.close();

            // Update container database
            containerList.remove(removingContainer);
            FileOutputStream fos2 = new FileOutputStream(containerDatabase);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(containerList);
            oos2.close();
        }

        System.out.println("Remove container " + removingContainerID + " successfully!");
        System.out.println();
    }

    @Override
    public void calculateTotalWeightOfEachType() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Container> containerList = manageDatabase.getContainerDatabase();

        // Get user input
        String selectType;
        label:
        while (true) {
            System.out.println("Which type of container do you want to calculate the total weight?");
            System.out.println("1. Dry Storage Container");
            System.out.println("2. Liquid Container");
            System.out.println("3. Open Side Container");
            System.out.println("4. Open Top Container");
            System.out.println("5. Refrigerated Container");
            System.out.print("Enter a number from 1 to 5: ");
            selectType = scanner.nextLine();
            double totalWeight = 0;

            switch (selectType) {
                case "1":
                    for (Container container : containerList) {
                        if (container.getContainerType().equals("Dry Storage Container")) {
                            totalWeight += container.getWeight();
                        }
                    }
                    System.out.println("The total weight of all Dry Storage Containers: " + totalWeight);
                    break label;
                case "2":
                    for (Container container : containerList) {
                        if (container.getContainerType().equals("Liquid Container")) {
                            totalWeight += container.getWeight();
                        }
                    }
                    System.out.println("The total weight of all Liquid Containers: " + totalWeight);
                    break label;
                case "3":
                    for (Container container : containerList) {
                        if (container.getContainerType().equals("Open Side Container")) {
                            totalWeight += container.getWeight();
                        }
                    }
                    System.out.println("The total weight of all Open Side Containers: " + totalWeight);
                    break label;
                case "4":
                    for (Container container : containerList) {
                        if (container.getContainerType().equals("Open Top Container")) {
                            totalWeight += container.getWeight();
                        }
                    }
                    System.out.println("The total weight of all Open Top Containers: " + totalWeight);
                    break label;
                case "5":
                    for (Container container : containerList) {
                        if (container.getContainerType().equals("Refrigerated Container")) {
                            totalWeight += container.getWeight();
                        }
                    }
                    System.out.println("The total weight of all Refrigerated Containers: " + totalWeight + "kg.");
                    break label;
                default:
                    System.out.println("Invalid Input! Please enter an integer number from 1 to 5.");
                    break;
            }
        }
        System.out.println();
    }
}
