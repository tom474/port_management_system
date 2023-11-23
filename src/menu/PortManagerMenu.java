package menu;

import container.Container;
import manage.ManageDatabase;
import port.Port;
import port.Trip;
import user.User;
import vehicle.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PortManagerMenu implements PortManagerMenuInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File vehicleDatabase = new File("src/model/vehicle.dat");
    private final File containerDatabase = new File("src/model/container.dat");


    @Override
    public void execute(User portManager) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();
        String option;
        do {
            Port managingPort = null;
            // Get the database
            List<Port> portList = manageDatabase.getPortDatabase();

            for (Port port : portList) {
                if (portManager.getPort().equals(port.getName())) {
                    managingPort = port;
                }
            }
            assert managingPort != null;
            System.out.println("-------------------- Manage Ports --------------------");
            System.out.println("1. View Port's Information");
            System.out.println("2. View All Containers");
            System.out.println("3. Add A Container");
            System.out.println("4. Remove A Container");
            System.out.println("5. View All Vehicles");
            System.out.println("6. View All Ships");
            System.out.println("7. View All Trucks");
            System.out.println("8. Add A Vehicle");
            System.out.println("9. Remove A Vehicle");
            System.out.println("10. Load A Container To A Vehicle");
            System.out.println("11. Unload A Container From A Vehicle");
            System.out.println("12. Move A Vehicle To Another Port");
            System.out.println("13. Calculate The Distance Between 2 Ports");
            System.out.println("14. View Port's Trip History");
            System.out.println("0. Go Back To System Admin Menu");
            System.out.print("Choose an action (0-14): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> viewPort(managingPort);
                case "2" -> viewPortContainers(managingPort);
                case "3" -> addPortContainer(managingPort);
                case "4" -> removePortContainer(managingPort);
                case "5" -> viewPortVehicles(managingPort);
                case "6" -> viewPortShips(managingPort);
                case "7" -> viewPortTrucks(managingPort);
                case "8" -> addPortVehicle(managingPort);
                case "9" -> removePortVehicle(managingPort);
                case "10" -> loadContainer(managingPort);
                case "11" -> unloadContainer(managingPort);
                case "12" -> movePortVehicle(managingPort);
                case "13" -> calculateDistance(managingPort);
                case "14" -> viewTrips(managingPort);
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 14!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void viewPort(Port port) {
        System.out.println("----------------------------------------------------------------- Port's Information -----------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
        System.out.println();
        System.out.println(port);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    @Override
    public void viewPortContainers(Port port) {
        if (!port.getContainerList().isEmpty()) {
            System.out.println("----------------------------- Port's Container List -----------------------------");
            System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
            System.out.println();
            for (Container container : port.getContainerList()) {
                System.out.println(container);
            }
            System.out.println("---------------------------------------------------------------------------------");
        } else {
            System.out.println(port.getName() + " doesn't have any container!");
        }
        System.out.println();
    }

    @Override
    public void addPortContainer(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Container> containerList = manageDatabase.getContainerDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Container> availableContainerList = new ArrayList<>();
        for (Container container : containerList) {
            if (container.getCurrentPort().equals("None")) {
                availableContainerList.add(container);
            }
        }

        String assigningContainerID;
        Container assigningContainer = null;
        if (!availableContainerList.isEmpty()) {
            while (true) {
                System.out.println("----------------------------- Available Containers ------------------------------");
                System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                System.out.println();
                for (Container container : availableContainerList) {
                    System.out.println(container);
                }
                System.out.println("---------------------------------------------------------------------------------");
                System.out.print("Enter the container's id that you want to add to the port (ex. C0001): ");
                assigningContainerID = scanner.nextLine();
                for (Container container : availableContainerList) {
                    if (container.getContainerID().equals(assigningContainerID)) {
                        assigningContainer = container;
                    }
                }
                if (assigningContainer != null) {
                    break;
                } else {
                    System.out.println("This container's id does not exist! Please enter another container's id.");
                }
            }

            List<Container> existingContainerList = port.getContainerList();
            double currentWeight = 0;
            for (Container container : existingContainerList) {
                currentWeight += container.getWeight();
            }
            if (currentWeight + assigningContainer.getWeight() >= port.getStoringCapacity()) {
                System.out.println(port.getName() + " is full! You can not add " + assigningContainer.getContainerID() + " to this port.");
            } else {
                port = port.addPortContainer(assigningContainer);
                assigningContainer.setCurrentPort(port.getName());
                // Update port database.
                List<Port> newPortList = new ArrayList<>();
                for (Port port1 : portList) {
                    if (port1.getPortID().equals(port.getPortID())) {
                        port1 = port;
                    }
                    newPortList.add(port1);
                }
                FileOutputStream fos1 = new FileOutputStream(portDatabase);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(newPortList);
                oos1.close();

                // Update vehicle database
                List<Container> newContainerList = new ArrayList<>();
                for (Container container : containerList) {
                    if (container.getContainerID().equals(assigningContainer.getContainerID())) {
                        container = assigningContainer;
                    }
                    newContainerList.add(container);
                }
                FileOutputStream fos2 = new FileOutputStream(containerDatabase);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                oos2.writeObject(newContainerList);
                oos2.close();
                System.out.println("Add container " + assigningContainer.getContainerID() + " to " + port.getName() + " successfully!");
            }
        } else {
            System.out.println("There is no available container! You can create a new container and assign to this port later!");
        }
        System.out.println();
    }

    @Override
    public void removePortContainer(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database.
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();

        Port currentPort = port;
        String selectedContainerID;
        Container selectedContainer = null;
        List<Container> portContainerList = currentPort.getContainerList();
        if (!portContainerList.isEmpty()) {
            System.out.println("----------------------------- Port's Container List -----------------------------");
            System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
            System.out.println();
            for (Container container : portContainerList) {
                System.out.println(container);
            }
            System.out.println("---------------------------------------------------------------------------------");
            while (true) {
                System.out.print("Enter the container's id that you want to remove from " + currentPort.getName() +" (ex. C0001): ");
                selectedContainerID = scanner.nextLine();
                for (Container container : portContainerList) {
                    if (container.getContainerID().equals(selectedContainerID)) {
                        selectedContainer = container;
                    }
                }
                if (selectedContainer != null) {
                    break;
                } else {
                    System.out.println("This container's id does not exist! Please enter another container's id.");
                }
            }
            currentPort = currentPort.removePortContainer(selectedContainer);
            selectedContainer.setCurrentPort("None");

            Vehicle updatingVehicle = null;
            for (Vehicle vehicle : currentPort.getVehicleList()) {
                List<Container> vehicleContainerList = vehicle.getContainerList();
                for (Container container : vehicleContainerList) {
                    if (container.getContainerID().equals(selectedContainerID)) {
                        vehicle = vehicle.unloadContainer(selectedContainer);
                        updatingVehicle = vehicle;
                        break;
                    }
                }
            }

            List<Port> newPortList = new ArrayList<>();
            if (updatingVehicle != null) {
                // Update port database
                for (Port port1 : portList) {
                    List<Vehicle> portVehicleList = port1.getVehicleList();
                    List<Vehicle> newPortVehicleList = new ArrayList<>();
                    if (port1.getPortID().equals(currentPort.getPortID())) {
                        port1 = currentPort;
                        for (Vehicle vehicle : portVehicleList) {
                            if (vehicle.getVehicleID().equals(updatingVehicle.getVehicleID())) {
                                vehicle = updatingVehicle;
                            }
                            newPortVehicleList.add(vehicle);
                        }
                        port1.setVehicleList(newPortVehicleList);
                    }
                    newPortList.add(port1);
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
                List<Container> newContainerList = new ArrayList<>();
                for (Container container : containerList) {
                    if (container.getContainerID().equals(selectedContainer.getContainerID())) {
                        container = selectedContainer;
                    }
                    newContainerList.add(container);
                }
                FileOutputStream fos3 = new FileOutputStream(containerDatabase);
                ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
                oos3.writeObject(newContainerList);
                oos3.close();
            } else {
                // Update port database
                for (Port port1 : portList) {
                    if (port1.getPortID().equals(currentPort.getPortID())) {
                        port1 = port1.removePortContainer(selectedContainer);
                    }
                    newPortList.add(port1);
                }
                FileOutputStream fos1 = new FileOutputStream(portDatabase);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(newPortList);
                oos1.close();

                // Update container database
                List<Container> newContainerList = new ArrayList<>();
                for (Container container : containerList) {
                    if (container.getContainerID().equals(selectedContainer.getContainerID())) {
                        container = selectedContainer;
                    }
                    newContainerList.add(container);
                }
                FileOutputStream fos2 = new FileOutputStream(containerDatabase);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                oos2.writeObject(newContainerList);
                oos2.close();
            }
            System.out.println("Remove container " + selectedContainer.getContainerID() + " from " + currentPort.getName() + " successfully!");
        } else {
            System.out.println(currentPort.getName() + " doesn't have any containers to remove!");
        }
        System.out.println();
    }

    @Override
    public void viewPortVehicles(Port port) {
        if (!port.getVehicleList().isEmpty()) {
            System.out.println("---------------------------------------------------------------------------- Port's Ship List ---------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : port.getVehicleList()) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(port.getName() + " doesn't have any vehicle!");
        }
        System.out.println();
    }

    @Override
    public void viewPortShips(Port port) {
        List<Vehicle> shipList = new ArrayList<>();
        for (Vehicle vehicle : port.getVehicleList()) {
            if (vehicle.getType().equals("Ship")) {
                shipList.add(vehicle);
            }
        }
        if (!shipList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------------- Port's Ship List ---------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : port.getVehicleList()) {
                if (vehicle.getType().equals("Ship")) {
                    System.out.println(vehicle);
                }
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(port.getName() + " doesn't have any vehicle!");
        }
        System.out.println();
    }

    @Override
    public void viewPortTrucks(Port port) {
        List<Vehicle> truckList = new ArrayList<>();
        for (Vehicle vehicle : port.getVehicleList()) {
            if (!vehicle.getType().equals("Ship")) {
                truckList.add(vehicle);
            }
        }
        if (!truckList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------------- Port's Ship List ---------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : port.getVehicleList()) {
                if (!vehicle.getType().equals("Ship")) {
                    System.out.println(vehicle);
                }
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(port.getName() + " doesn't have any vehicle!");
        }
        System.out.println();
    }

    @Override
    public void addPortVehicle(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Vehicle> availableVehicleList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getPort().equals("None")) {
                availableVehicleList.add(vehicle);
            }
        }
        List<Vehicle> avalableShipList = new ArrayList<>();
        for (Vehicle vehicle : availableVehicleList) {
            if (vehicle.getType().equals("Ship")) {
                avalableShipList.add(vehicle);
            }
        }

        String selectedVehicleID;
        Vehicle selectedVehicle = null;
        if (!availableVehicleList.isEmpty()) {
            if (port.isLanding()) {
                System.out.println("---------------------------------------------------------------------------- Available Vehicles -------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                System.out.println();
                for (Vehicle vehicle : availableVehicleList) {
                    System.out.println(vehicle);
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                while (true) {
                    System.out.print("Enter the vehicle's id that you want to add to" + " " + port.getName() + " (ex. SH0001, TR0001): ");
                    selectedVehicleID = scanner.nextLine();
                    for (Vehicle vehicle : availableVehicleList) {
                        if (vehicle.getVehicleID().equals(selectedVehicleID)) {
                            selectedVehicle = vehicle;
                        }
                    }
                    if (selectedVehicle != null) {
                        break;
                    } else {
                        System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                    }
                }
                port = port.addPortVehicle(selectedVehicle);
                selectedVehicle.setPort(port.getName());

                // Update port database.
                List<Port> newPortList = new ArrayList<>();
                for (Port port1 : portList) {
                    if (port1.getPortID().equals(port.getPortID())) {
                        port1 = port;
                    }
                    newPortList.add(port1);
                }
                FileOutputStream fos1 = new FileOutputStream(portDatabase);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(newPortList);
                oos1.close();

                // Update vehicle database
                List<Vehicle> newVehicleList = new ArrayList<>();
                for (Vehicle vehicle : vehicleList) {
                    if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                        vehicle = selectedVehicle;
                    }
                    newVehicleList.add(vehicle);
                }
                FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                oos2.writeObject(newVehicleList);
                oos2.close();
                System.out.println("Add vehicle " + selectedVehicle.getVehicleID() + " to " + port.getName() + " successfully!");
            } else {
                if (!avalableShipList.isEmpty()) {
                    System.out.println("---------------------------------------------------------------------------- Available Vehicles -------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                    System.out.println();
                    for (Vehicle vehicle : avalableShipList) {
                        System.out.println(vehicle);
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    while (true) {
                        System.out.print("Enter the vehicle's id that you want to add to" + port.getName() + " (ex. SH0001, TR0001): ");
                        selectedVehicleID = scanner.nextLine();
                        for (Vehicle vehicle : avalableShipList) {
                            if (vehicle.getVehicleID().equals(selectedVehicleID)) {
                                selectedVehicle = vehicle;
                            }
                        }
                        if (selectedVehicle != null) {
                            break;
                        } else {
                            System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                        }
                    }
                    port = port.addPortVehicle(selectedVehicle);
                    selectedVehicle.setPort(port.getName());

                    // Update port database.
                    List<Port> newPortList = new ArrayList<>();
                    for (Port port1 : portList) {
                        if (port1.getPortID().equals(port.getPortID())) {
                            port1 = port;
                        }
                        newPortList.add(port1);
                    }
                    FileOutputStream fos1 = new FileOutputStream(portDatabase);
                    ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                    oos1.writeObject(newPortList);
                    oos1.close();

                    // Update vehicle database
                    List<Vehicle> newVehicleList = new ArrayList<>();
                    for (Vehicle vehicle : vehicleList) {
                        if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                            vehicle = selectedVehicle;
                        }
                        newVehicleList.add(vehicle);
                    }
                    FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                    ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                    oos2.writeObject(newVehicleList);
                    oos2.close();
                    System.out.println("Add vehicle " + selectedVehicle.getVehicleID() + " to " + port.getName() + " successfully!");
                } else {
                    System.out.println("There is no available vehicle! You can create a new vehicle and assign to this port later!");
                }
            }
        } else {
            System.out.println("There is no available vehicle! You can create a new vehicle and assign to this port later!");
        }
        System.out.println();
    }

    @Override
    public void removePortVehicle(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database.
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        String selectedVehicleId;
        Vehicle selectedVehicle = null;
        Port newPort;
        List<Vehicle> portVehicleList = port.getVehicleList();
        if (!portVehicleList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------------- Port's Vehicle List ------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : portVehicleList) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (true) {
                System.out.print("Enter the vehicle's id that you want to remove from " + port.getName() + " (ex. SH0001, TR0001): ");
                selectedVehicleId = scanner.nextLine();
                for (Vehicle vehicle : portVehicleList) {
                    if (vehicle.getVehicleID().equals(selectedVehicleId)) {
                        selectedVehicle = vehicle;
                    }
                }
                if (selectedVehicle != null) {
                    System.out.println();
                    break;
                } else {
                    System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                }
            }
            newPort = port.removePortVehicle(selectedVehicle);
            selectedVehicle.setPort("None");

            // Update port database.
            List<Port> newPortList = new ArrayList<>();
            for (Port port1 : portList) {
                if (port1.getPortID().equals(newPort.getPortID())) {
                    port1 = newPort;
                }
                newPortList.add(port1);
            }
            FileOutputStream fos1 = new FileOutputStream(portDatabase);
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(newPortList);
            oos1.close();

            // Update vehicle database
            List<Vehicle> newVehicleList = new ArrayList<>();
            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                    vehicle = selectedVehicle;
                }
                newVehicleList.add(vehicle);
            }
            FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(newVehicleList);
            oos2.close();
            System.out.println("Remove vehicle " + selectedVehicle.getVehicleID() + " from " + newPort.getName() + " successfully!");
        } else {
            System.out.println(port.getName() + " doesn't have any vehicles to remove!");
        }
        System.out.println();
    }

    @Override
    public void loadContainer(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        Vehicle selectingVehicle = null;
        Container selectingContainer = null;
        List<Vehicle> portVehicleList = port.getVehicleList();
        List<Container> portContainerList = port.getContainerList();
        List<Container> unavailableContainerList = new ArrayList<>();
        List<Container> availableContainerList = new ArrayList<>();
        List<Container> availableBasicTruckContainerList = new ArrayList<>();
        List<Container> availableTankerTruckContainerList = new ArrayList<>();
        List<Container> availableReeferTruckContainerList = new ArrayList<>();
        for (Vehicle vehicle : portVehicleList) {
            for (Container container : portContainerList) {
                if (vehicle.getContainerList().contains(container)) {
                    unavailableContainerList.add(container);
                }
            }
        }
        for (Container container : portContainerList) {
            if (!unavailableContainerList.contains(container)) {
                availableContainerList.add(container);
            }
        }
        for (Container container : availableContainerList) {
            if (container.getContainerType().equals("Dry Storage Container") || container.getContainerType().equals("Open Top Container") || container.getContainerType().equals("Open Side Container")) {
                availableBasicTruckContainerList.add(container);
            }
        }
        for (Container container : availableContainerList) {
            if (container.getContainerType().equals("Refrigerated Container")) {
                availableReeferTruckContainerList.add(container);
            }
        }
        for (Container container : availableContainerList) {
            if (container.getContainerType().equals("Liquid Container")) {
                availableTankerTruckContainerList.add(container);
            }
        }
        if (!portVehicleList.isEmpty()) {
            if (!availableContainerList.isEmpty()) {
                while (true) {
                    System.out.println("-------------------------------------------------------------------------- Port's Vehicle List -------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                    System.out.println();
                    for (Vehicle vehicle : portVehicleList) {
                        System.out.println(vehicle);
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.print("Choose a vehicle's id from the above list (ex. SH0001, TR0001): ");
                    String selectingVehicleID = scanner.nextLine();
                    for (Vehicle vehicle : portVehicleList) {
                        if (vehicle.getVehicleID().equals(selectingVehicleID)) {
                            selectingVehicle = vehicle;
                        }
                    }
                    if (selectingVehicle != null) {
                        break;
                    } else {
                        System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                    }
                }
                switch (selectingVehicle.getType()) {
                    case "Basic Truck" -> {
                        System.out.println("A Basic Truck can only load dry storage, open top, or open side containers.");
                        if (!availableBasicTruckContainerList.isEmpty()) {
                            while (true) {
                                System.out.println("----------------------------- Available Containers ------------------------------");
                                System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                                System.out.println();
                                for (Container container : availableBasicTruckContainerList) {
                                    System.out.println(container);
                                }
                                System.out.println("---------------------------------------------------------------------------------");
                                System.out.print("Choose a container's id from the above list (ex. C0001): ");
                                String selectingContainerID = scanner.nextLine();
                                for (Container container : availableBasicTruckContainerList) {
                                    if (container.getContainerID().equals(selectingContainerID)) {
                                        selectingContainer = container;
                                    }
                                }
                                if (selectingContainer != null) {
                                    if (selectingContainer.getWeight() + selectingVehicle.getCurrentCarryingCapacity() < selectingVehicle.getCarryingCapacity()) {
                                        selectingVehicle = selectingVehicle.loadContainer(selectingContainer);
                                        List<Vehicle> newPortVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : portVehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newPortVehicleList.add(vehicle);
                                        }
                                        port.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port1 : portList) {
                                            if (port1.getPortID().equals(port.getPortID())) {
                                                port1 = port;
                                            }
                                            newPortList.add(port1);
                                        }
                                        FileOutputStream fos1 = new FileOutputStream(portDatabase);
                                        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                                        oos1.writeObject(newPortList);
                                        oos1.close();

                                        // Update vehicle database
                                        List<Vehicle> newVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : vehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newVehicleList.add(vehicle);
                                        }
                                        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                                        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                                        oos2.writeObject(newVehicleList);
                                        oos2.close();
                                    } else {
                                        System.out.println(selectingVehicle.getName() + " does not have enough carrying capacity to load container " + selectingContainer.getContainerID() + "!");
                                    }
                                    break;
                                } else {
                                    System.out.println("The container's id you entered does not exist! Please enter another container's id.");
                                }
                            }
                        } else {
                            System.out.println("This port doesn't have any dry storage, open top, or open side container to load!");
                        }
                    }
                    case "Reefer Truck" -> {
                        System.out.println("A Reefer Truck can only load refrigerated containers.");
                        if (!availableReeferTruckContainerList.isEmpty()) {
                            while (true) {
                                System.out.println("----------------------------- Available Containers ------------------------------");
                                System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                                System.out.println();
                                for (Container container : availableReeferTruckContainerList) {
                                    System.out.println(container);
                                }
                                System.out.println("---------------------------------------------------------------------------------");
                                System.out.print("Choose a container's id from the above list (ex. C0001): ");
                                String selectingContainerID = scanner.nextLine();
                                for (Container container : availableReeferTruckContainerList) {
                                    if (container.getContainerID().equals(selectingContainerID)) {
                                        selectingContainer = container;
                                    }
                                }
                                if (selectingContainer != null) {
                                    if (selectingContainer.getWeight() + selectingVehicle.getCurrentCarryingCapacity() < selectingVehicle.getCarryingCapacity()) {
                                        selectingVehicle = selectingVehicle.loadContainer(selectingContainer);
                                        List<Vehicle> newPortVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : portVehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newPortVehicleList.add(vehicle);
                                        }
                                        port.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port1 : portList) {
                                            if (port1.getPortID().equals(port.getPortID())) {
                                                port1 = port;
                                            }
                                            newPortList.add(port1);
                                        }
                                        FileOutputStream fos1 = new FileOutputStream(portDatabase);
                                        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                                        oos1.writeObject(newPortList);
                                        oos1.close();

                                        // Update vehicle database
                                        List<Vehicle> newVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : vehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newVehicleList.add(vehicle);
                                        }
                                        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                                        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                                        oos2.writeObject(newVehicleList);
                                        oos2.close();
                                    } else {
                                        System.out.println(selectingVehicle.getName() + " does not have enough carrying capacity to load container " + selectingContainer.getContainerID() + "!");
                                    }
                                    break;
                                } else {
                                    System.out.println("The container's id you entered does not exist! Please enter another container's id.");
                                }
                            }
                        } else {
                            System.out.println("This port doesn't have any refrigerated container to load!");
                        }
                    }
                    case "Tanker Truck" -> {
                        System.out.println("A Tanker Truck can only load refrigerated containers.");
                        if (!availableTankerTruckContainerList.isEmpty()) {
                            while (true) {
                                System.out.println("----------------------------- Available Containers ------------------------------");
                                System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                                System.out.println();
                                for (Container container : availableTankerTruckContainerList) {
                                    System.out.println(container);
                                }
                                System.out.println("---------------------------------------------------------------------------------");
                                System.out.print("Choose a container's id from the above list (ex. C0001): ");
                                String selectingContainerID = scanner.nextLine();
                                for (Container container : availableTankerTruckContainerList) {
                                    if (container.getContainerID().equals(selectingContainerID)) {
                                        selectingContainer = container;
                                    }
                                }
                                if (selectingContainer != null) {
                                    if (selectingContainer.getWeight() + selectingVehicle.getCurrentCarryingCapacity() < selectingVehicle.getCarryingCapacity()) {
                                        selectingVehicle = selectingVehicle.loadContainer(selectingContainer);
                                        List<Vehicle> newPortVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : portVehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newPortVehicleList.add(vehicle);
                                        }
                                        port.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port1 : portList) {
                                            if (port1.getPortID().equals(port.getPortID())) {
                                                port1 = port;
                                            }
                                            newPortList.add(port1);
                                        }
                                        FileOutputStream fos1 = new FileOutputStream(portDatabase);
                                        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                                        oos1.writeObject(newPortList);
                                        oos1.close();

                                        // Update vehicle database
                                        List<Vehicle> newVehicleList = new ArrayList<>();
                                        for (Vehicle vehicle : vehicleList) {
                                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                                vehicle = selectingVehicle;
                                            }
                                            newVehicleList.add(vehicle);
                                        }
                                        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                                        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                                        oos2.writeObject(newVehicleList);
                                        oos2.close();
                                    } else {
                                        System.out.println(selectingVehicle.getName() + " does not have enough carrying capacity to load container " + selectingContainer.getContainerID() + "!");
                                    }
                                    break;
                                } else {
                                    System.out.println("The container's id you entered does not exist! Please enter another container's id.");
                                }
                            }
                        } else {
                            System.out.println("This port doesn't have any liquid container to load!");
                        }
                    }
                    default -> {
                        System.out.println("A Ship can load any type of containers.");
                        while (true) {
                            System.out.println("----------------------------- Available Containers ------------------------------");
                            System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                            System.out.println();
                            for (Container container : availableContainerList) {
                                System.out.println(container);
                            }
                            System.out.println("---------------------------------------------------------------------------------");
                            System.out.print("Choose a container's id from the above list (ex. C0001): ");
                            String selectingContainerID = scanner.nextLine();
                            for (Container container : availableContainerList) {
                                if (container.getContainerID().equals(selectingContainerID)) {
                                    selectingContainer = container;
                                }
                            }
                            if (selectingContainer != null) {
                                if (selectingContainer.getWeight() + selectingVehicle.getCurrentCarryingCapacity() < selectingVehicle.getCarryingCapacity()) {
                                    selectingVehicle = selectingVehicle.loadContainer(selectingContainer);
                                    List<Vehicle> newPortVehicleList = new ArrayList<>();
                                    for (Vehicle vehicle : portVehicleList) {
                                        if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                            vehicle = selectingVehicle;
                                        }
                                        newPortVehicleList.add(vehicle);
                                    }
                                    port.setVehicleList(newPortVehicleList);

                                    // Update port database
                                    List<Port> newPortList = new ArrayList<>();
                                    for (Port port1 : portList) {
                                        if (port1.getPortID().equals(port.getPortID())) {
                                            port1 = port;
                                        }
                                        newPortList.add(port1);
                                    }
                                    FileOutputStream fos1 = new FileOutputStream(portDatabase);
                                    ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                                    oos1.writeObject(newPortList);
                                    oos1.close();

                                    // Update vehicle database
                                    List<Vehicle> newVehicleList = new ArrayList<>();
                                    for (Vehicle vehicle : vehicleList) {
                                        if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                            vehicle = selectingVehicle;
                                        }
                                        newVehicleList.add(vehicle);
                                    }
                                    FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                                    ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                                    oos2.writeObject(newVehicleList);
                                    oos2.close();
                                    System.out.println("Load container " + selectingContainer.getContainerID() + " to " + selectingVehicle.getName() + " successfully!");
                                } else {
                                    System.out.println(selectingVehicle.getName() + " does not have enough carrying capacity to load container " + selectingContainer.getContainerID() + "!");
                                }
                                break;
                            } else {
                                System.out.println("The container's id you entered does not exist! Please enter another container's id.");
                            }
                        }
                    }
                }
            } else {
                System.out.println("This port doesn't have any container to load!");
            }
        } else {
            System.out.println("This port doesn't have any vehicle to load container!");
        }
        System.out.println();
    }

    @Override
    public void unloadContainer(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        List<Vehicle> portVehicleList = port.getVehicleList();
        Vehicle selectingVehicle = null;
        if (!portVehicleList.isEmpty()) {
            while (true) {
                System.out.println("-------------------------------------------------------------------------- Port's Vehicle List -------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                System.out.println();
                for (Vehicle vehicle : portVehicleList) {
                    System.out.println(vehicle);
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Choose a vehicle's id from the above list (ex. SH0001, TR0001): ");
                String selectingVehicleID = scanner.nextLine();
                for (Vehicle vehicle : portVehicleList) {
                    if (vehicle.getVehicleID().equals(selectingVehicleID)) {
                        selectingVehicle = vehicle;
                    }
                }
                if (selectingVehicle != null) {
                    break;
                } else {
                    System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                }
            }

            List<Container> vehicleContainerList = selectingVehicle.getContainerList();
            Container selectingContainer = null;
            if (!vehicleContainerList.isEmpty()) {
                while (true) {
                    System.out.println("-------------------------------- All Containers ---------------------------------");
                    System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                    System.out.println();
                    for (Container container : vehicleContainerList) {
                        System.out.println(container);
                    }
                    System.out.println("---------------------------------------------------------------------------------");
                    System.out.print("Choose a container's id from the above list (ex. C0001): ");
                    String selectingContainerID = scanner.nextLine();
                    for (Container container : vehicleContainerList) {
                        if (container.getContainerID().equals(selectingContainerID)) {
                            selectingContainer = container;
                        }
                    }
                    if (selectingContainer != null) {
                        selectingVehicle = selectingVehicle.unloadContainer(selectingContainer);
                        List<Vehicle> newPortVehicleList = new ArrayList<>();
                        for (Vehicle vehicle : portVehicleList) {
                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                vehicle = selectingVehicle;
                            }
                            newPortVehicleList.add(vehicle);
                        }
                        port.setVehicleList(newPortVehicleList);

                        // Update port database
                        List<Port> newPortList = new ArrayList<>();
                        for (Port port1 : portList) {
                            if (port1.getPortID().equals(port.getPortID())) {
                                port1 = port;
                            }
                            newPortList.add(port1);
                        }
                        FileOutputStream fos1 = new FileOutputStream(portDatabase);
                        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                        oos1.writeObject(newPortList);
                        oos1.close();

                        // Update vehicle database
                        List<Vehicle> newVehicleList = new ArrayList<>();
                        for (Vehicle vehicle : vehicleList) {
                            if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                                vehicle = selectingVehicle;
                            }
                            newVehicleList.add(vehicle);
                        }
                        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                        oos2.writeObject(newVehicleList);
                        oos2.close();
                        System.out.println("Unload container " + selectingContainer.getContainerID() + " from " + selectingVehicle.getName() + " successfully!");
                        break;
                    } else {
                        System.out.println("The container's id you entered does not exist! Please enter another container's id.");
                    }
                }
            } else {
                System.out.println("This port doesn't have any container to unload!");
            }
        } else {
            System.out.println("This port doesn't have any vehicle to unload container!");
        }
        System.out.println();
    }

    @Override
    public void viewTrips(Port port) {
        if (!port.getTripList().isEmpty()) {
            System.out.println("----------------------------------------------------------- Port's Trip History --------------------------------------------------------");
            System.out.printf("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", "Trip ID", "Vehicle", "Departure Port" , "Arrival Port", "Departure Date", "Arrival Date", "Status");
            System.out.println();
            for (Trip trip : port.getTripList()) {
                System.out.println(trip);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(port.getName() + " doesn't have any trip!");
        }
        System.out.println();
    }

    @Override
    public void movePortVehicle(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();

        Port departurePort = port;
        Port arrivalPort = null;
        List<Vehicle> portVehicleList = port.getVehicleList();
        Vehicle selectingVehicle = null;
        if (!portVehicleList.isEmpty()) {
            while (true) {
                System.out.println("-------------------------------------------------------------------------- Port's Vehicle List -------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity", "Current Capacity", "Fuel Capacity", "Current Fuel", "Containers");
                System.out.println();
                for (Vehicle vehicle : portVehicleList) {
                    System.out.println(vehicle);
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Choose a vehicle's id from the above list (ex. SH0001, TR0001): ");
                String selectingVehicleID = scanner.nextLine();
                for (Vehicle vehicle : portVehicleList) {
                    if (vehicle.getVehicleID().equals(selectingVehicleID)) {
                        selectingVehicle = vehicle;
                    }
                }
                if (selectingVehicle != null) {
                    break;
                } else {
                    System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
                }
            }

            while (true) {
                System.out.println("---------------------------------------------------------------------- All Ports ---------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
                System.out.println();
                if (selectingVehicle.getType().equals("Ship")) {
                    for (Port port1 : portList) {
                        if (!port1.getPortID().equals(departurePort.getPortID())) {
                            System.out.println(port1);
                        }
                    }
                } else {
                    for (Port port1 : portList) {
                        if (!port1.getPortID().equals(departurePort.getPortID()) && port1.isLanding()) {
                            System.out.println(port1);
                        }
                    }
                }
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Choose the arrival port's id from the above list (ex. P0001): ");
                String selectingPortID = scanner.nextLine();
                if (selectingVehicle.getType().equals("Ship")) {
                    for (Port port1 : portList) {
                        if (port1.getPortID().equals(selectingPortID) && !port1.getPortID().equals(departurePort.getPortID())) {
                            arrivalPort = port1;
                        }
                    }
                } else {
                    for (Port port1 : portList) {
                        if (port1.getPortID().equals(selectingPortID) && !port1.getPortID().equals(departurePort.getPortID()) && port1.isLanding()) {
                            arrivalPort = port1;
                        }
                    }
                }
                if (arrivalPort != null) {
                    break;
                } else {
                    System.out.println("The port's id you entered does not exist! Please enter another port's id.");
                }
            }

            if (selectingVehicle.canMoveToPort(arrivalPort)) {
                selectingVehicle = selectingVehicle.moveToPort(arrivalPort);
                List<Port> updatedPortList = manageDatabase.getPortDatabase();

                for (Port port1 : updatedPortList) {
                    if (port1.getPortID().equals(departurePort.getPortID())) {
                        departurePort = port1;
                    }
                    if (port1.getPortID().equals(arrivalPort.getPortID())) {
                        arrivalPort = port1;
                    }
                }

                // Update vehicleList in ports
                List<Vehicle> oldDeparturePortVehicleList = departurePort.getVehicleList();
                List<Vehicle> newDeparturePortVehicleList = new ArrayList<>();
                for (Vehicle vehicle : oldDeparturePortVehicleList) {
                    if (!vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                        newDeparturePortVehicleList.add(vehicle);
                    }
                }
                departurePort.setVehicleList(newDeparturePortVehicleList);
                arrivalPort = arrivalPort.addPortVehicle(selectingVehicle);

                // Update containerList in ports
                if (!selectingVehicle.getContainerList().isEmpty()) {
                    List<Container> oldDeparturePortContainerList = departurePort.getContainerList();
                    List<Container> newDeparturePortContainerList = new ArrayList<>();
                    for (Container container : oldDeparturePortContainerList) {
                        boolean isContainerMove = false;
                        for (Container container1 : selectingVehicle.getContainerList()) {
                            if (container.getContainerID().equals(container1.getContainerID())) {
                                isContainerMove = true;
                                break;
                            }
                        }
                        if (!isContainerMove) {
                            newDeparturePortContainerList.add(container);
                        }
                    }
                    departurePort.setContainerList(newDeparturePortContainerList);

                    for (Container container : selectingVehicle.getContainerList()) {
                        arrivalPort = arrivalPort.addPortContainer(container);
                    }
                }

                // Update port database
                List<Port> newPortList = new ArrayList<>();
                for (Port port1 : updatedPortList) {
                    if (port1.getPortID().equals(arrivalPort.getPortID())) {
                        port1 = arrivalPort;
                    }
                    if (port1.getPortID().equals(departurePort.getPortID())) {
                        port1 = departurePort;
                    }
                    newPortList.add(port1);
                }
                FileOutputStream fos1 = new FileOutputStream(portDatabase);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(newPortList);
                oos1.close();

                // Update vehicle database
                List<Vehicle> newVehicleList = new ArrayList<>();
                for (Vehicle vehicle : vehicleList) {
                    if (vehicle.getVehicleID().equals(selectingVehicle.getVehicleID())) {
                        vehicle = selectingVehicle;
                    }
                    newVehicleList.add(vehicle);
                }
                FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                oos2.writeObject(newVehicleList);
                oos2.close();

                // Update container database
                List<Container> newContainerList = new ArrayList<>();
                for (Container container: containerList) {
                    for (Container vehicleContainer : selectingVehicle.getContainerList()) {
                        if (container.getContainerID().equals(vehicleContainer.getContainerID())) {
                            container.setCurrentPort(arrivalPort.getName());
                        }
                    }
                    newContainerList.add(container);
                }
                FileOutputStream fos3 = new FileOutputStream(containerDatabase);
                ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
                oos3.writeObject(newContainerList);
                oos3.close();
                System.out.println("Move " + selectingVehicle.getName() + " to " + arrivalPort.getName() + " successfully!");
            } else {
                System.out.println(selectingVehicle.getVehicleID() + " can not move to " + arrivalPort + "!");
                System.out.println("One of the following issues may occur: ");
                System.out.println("1. The vehicle doesn't have enough fuel to move.");
                System.out.println("2. The arrival port doesn't have enough storage.");
                System.out.println("3. Trucks can not move to the port that is not on land.");
            }
        } else {
            System.out.println(port.getName() + " doesn't have any vehicle to move!");
        }
        System.out.println();
    }

    @Override
    public void calculateDistance(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        Port secondPort = null;
        while (true) {
            System.out.println("---------------------------------------------------------------------- All Ports ---------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
            System.out.println();
            for (Port port1 : portList) {
                if (!port1.getPortID().equals(port.getPortID())) {
                    System.out.println(port1);
                }
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Choose the second port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port1 : portList) {
                if (port1.getPortID().equals(selectingPortID) && !port1.getPortID().equals(port.getPortID())) {
                    secondPort = port1;
                }
            }
            if (secondPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }
        double distance = port.calculateDistance(secondPort);
        System.out.println(" The distance between " + port.getName() +" and " + secondPort.getName() + "is: " + distance + "km.");
        System.out.println();
    }
}
