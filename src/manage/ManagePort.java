package manage;

import container.Container;
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
import java.util.Random;
import java.util.Scanner;

public class ManagePort implements ManagePortInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File vehicleDatabase = new File("src/model/vehicle.dat");
    private final File containerDatabase = new File("src/model/container.dat");
    private final File tripDatabase = new File("src/model/trip.dat");
    private final File userDatabase = new File("src/model/user.dat");

    @Override
    public void viewPorts() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();
        if (!portList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------- All Ports ---------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
            System.out.println();
            for (Port port : portList) {
                System.out.println(port);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no port in the database!");
        }
        System.out.println();
    }

    @Override
    public void addPort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<User> userList = manageDatabase.getUserDatabase();
        List<User> availablePortManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getRole().equals("Port Manager")) {
                if (user.getPort().equals("None")) {
                    availablePortManagerList.add(user);
                }
            }
        }

        // Generate random and unique port ID
        String portID = null;
        boolean isExistID = true;
        while (isExistID) {
            portID = "P" + random.nextInt(1000, 9999);
            for (Port port : portList) {
                isExistID = portID.equals(port.getPortID());
            }
        }

        // Prompt user
        System.out.print("Enter the port's name (ex. Nha Rong Port): ");
        String name = scanner.nextLine();
        System.out.print("Enter the port's latitude (ex. 10.7681): ");
        double latitude = scanner.nextDouble();
        System.out.print("Enter the port's longitude (ex. 106.7068): ");
        double longitude = scanner.nextDouble();
        System.out.print("Enter the port's storing capacity (kg) (ex. 3000000): ");
        double storingCapacity = scanner.nextDouble();
        scanner.nextLine();
        boolean isLanding;
        while (true) {
            System.out.print("Is this port on land? (yes/no): ");
            String onLand = scanner.nextLine();
            if (onLand.equals("yes")) {
                isLanding = true;
                break;
            } else if (onLand.equals("no")) {
                isLanding = false;
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }

        // Assign Port Manager
        String portManager = null;
        while (true) {
            System.out.print("Do you want to assign a port manager to this port now? (yes/no): ");
            String isAssign = scanner.nextLine();
            if (isAssign.equals("yes")) {
                if (!availablePortManagerList.isEmpty()) {
                    System.out.println("--------------------------------- Available Port Managers --------------------------------");
                    System.out.printf("| %-8s | %-15s | %-16s | %-15s | %-20s |", "User ID" , "Role", "Username", "Password", "Manage Port");
                    System.out.println();
                    for (User availablePortManager : availablePortManagerList) {
                        System.out.println(availablePortManager);
                    }
                    System.out.println("------------------------------------------------------------------------------------------");
                    while (true) {
                        System.out.print("Please enter a Port Manager's ID from the above list (ex. PM0001): ");
                        String assignedPortManagerID = scanner.nextLine();
                        for (User availablePortManager : availablePortManagerList) {
                            if (assignedPortManagerID.equals(availablePortManager.getUserID())) {
                                portManager = availablePortManager.getUserID();
                                break;
                            }
                        }
                        if (portManager == null) {
                            System.out.println("This port manager's id does not exist! Please enter another port manager's id.");
                        } else {
                            System.out.println("Assign port manager " + portManager + " to this port successfully!");
                            break;
                        }
                    }
                } else {
                    System.out.println("There is no available port manager! You can add a new port manager and assign to this port later.");
                    portManager = "None";
                }
                break;
            } else if (isAssign.equals("no")) {
                portManager = "None";
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }

        // Update Port Database
        Port newPort = new Port(portID, name, portManager, latitude, longitude, storingCapacity, isLanding);
        portList.add(newPort);
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(portList);
        oos1.close();

        // Update Port Manager Database
        List<User> newUserList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUserID().equals(portManager)) {
                user.setPort(newPort.getName());
            }
            newUserList.add(user);
        }
        FileOutputStream fos2 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newUserList);
        oos2.close();
        System.out.println("Add new port successfully!");
        System.out.println();
    }

    @Override
    public void updatePort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();
        List<User> userList = manageDatabase.getUserDatabase();
        List<Trip> tripList = manageDatabase.getTripDatabase();
        List<User> availablePortManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getRole().equals("Port Manager")) {
                if (user.getPort().equals("None")) {
                    availablePortManagerList.add(user);
                }
            }
        }


        // Prompt User
        Port oldPort = null;
        Port updatingPort = null;
        String updatingPortID;
        System.out.println("You can only update the port's name, latitude, longitude, and manager! Other port's details can not be changed to avoid unexpected errors.");
        while (true) {
            viewPorts();
            System.out.print("Enter the Port's ID that you want to update (ex. P0001): ");
            updatingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (updatingPortID.equals(port.getPortID())) {
                    oldPort = port;
                    updatingPort = port;
                }
            }
            if (updatingPort != null) {
                break;
            } else {
                System.out.println("This port's id does not exist! Please enter another port's id.");
            }
        }
        String updatingName;
        while (true) {
            System.out.print("Do you want to update the port's name? (yes, no): ");
            String isChangingName = scanner.nextLine();
            if (isChangingName.equals("yes")) {
                System.out.print("Enter the new name (ex. Nha Rong Port): ");
                updatingName = scanner.nextLine();
                updatingPort.setName(updatingName);
                break;
            } else if (isChangingName.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }
        double updatingLatitude;
        while (true) {
            System.out.print("Do you want to update the port's latitude? (yes, no): ");
            String isChangingLatitude = scanner.nextLine();
            if (isChangingLatitude.equals("yes")) {
                System.out.print("Enter the new latitude (ex. 10.7681): ");
                updatingLatitude = scanner.nextDouble();
                scanner.nextLine();
                updatingPort.setLatitude(updatingLatitude);
                break;
            } else if (isChangingLatitude.equals("no")) {
                System.out.println();
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }
        double updatingLongitude;
        while (true) {
            System.out.print("Do you want to update the port's longitude? (yes, no): ");
            String isChangingLatitude = scanner.nextLine();
            if (isChangingLatitude.equals("yes")) {
                System.out.print("Enter the new longitude (ex. 106.7068): ");
                updatingLongitude = scanner.nextDouble();
                scanner.nextLine();
                updatingPort.setLongitude(updatingLongitude);
                break;
            } else if (isChangingLatitude.equals("no")) {
                System.out.println();
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }
        String newPortManager = null;
        while (true) {
            System.out.print("Do you want to update this port's manager? (yes, no): ");
            String isChangingManager = scanner.nextLine();
            if (isChangingManager.equals("yes")) {
                if (updatingPort.getPortManager().equals("None")) {
                    System.out.println("Port " + updatingPort.getPortID() + " doesn't have a port manager!");
                    while (true) {
                        System.out.print("Do you want to assign port " + updatingPort.getPortID() + " to an existing port manager? (yes/no): ");
                        String isAssign = scanner.nextLine();
                        if (isAssign.equals("yes")) {
                            if (!availablePortManagerList.isEmpty()) {
                                System.out.println("--------------------------------- Available Port Managers --------------------------------");
                                System.out.printf("| %-8s | %-15s | %-16s | %-15s | %-20s |", "User ID" , "Role", "Username", "Password", "Manage Port");
                                System.out.println();
                                for (User availablePortManager : availablePortManagerList) {
                                    System.out.println(availablePortManager);
                                }
                                System.out.println("------------------------------------------------------------------------------------------");
                                while (true) {
                                    System.out.print("Please enter a Port Manager's ID from the above list (ex. PM0001): ");
                                    String assignedPortManagerID = scanner.nextLine();
                                    for (User availablePortManager : availablePortManagerList) {
                                        if (assignedPortManagerID.equals(availablePortManager.getUserID())) {
                                            newPortManager = availablePortManager.getUserID();
                                            break;
                                        }
                                    }
                                    if (newPortManager == null) {
                                        System.out.println("This port manager's id does not exist! Please enter another port manager's id.");
                                    } else {
                                        updatingPort.setPortManager(newPortManager);
                                        System.out.println("Assign Port Manager " + newPortManager + " successfully!");
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("There is no available port manager! You can add a new port manager and assign to this port later.");
                            }
                            break;
                        } else if (isAssign.equals("no")) {
                            System.out.println();
                            break;
                        } else {
                            System.out.println("Invalid input! Please enter yes or no!");
                        }
                    }
                } else {
                    while (true) {
                        System.out.print("Do you want to remove or change the current port manager? (remove/change): ");
                        String updateAction = scanner.nextLine();
                        if (updateAction.equals("remove")) {
                            updatingPort.setPortManager("None");
                        } else if (updateAction.equals("change")) {
                            if (!availablePortManagerList.isEmpty()) {
                                System.out.println("--------------------------------- Available Port Managers --------------------------------");
                                System.out.printf("| %-8s | %-15s | %-16s | %-15s | %-20s |", "User ID" , "Role", "Username", "Password", "Manage Port");
                                System.out.println();
                                for (User availablePortManager : availablePortManagerList) {
                                    System.out.println(availablePortManager);
                                }
                                System.out.println("------------------------------------------------------------------------------------------");
                                while (true) {
                                    System.out.print("Please enter a Port Manager's ID from the above list (ex. PM0001): ");
                                    String assignedPortManagerID = scanner.nextLine();
                                    for (User availablePortManager : availablePortManagerList) {
                                        if (assignedPortManagerID.equals(availablePortManager.getUserID())) {
                                            newPortManager = availablePortManager.getUserID();
                                            break;
                                        }
                                    }
                                    if (newPortManager == null) {
                                        System.out.println("This port manager's id does not exist! Please enter another port manager's id.");
                                    } else {
                                        updatingPort.setPortManager(newPortManager);
                                        System.out.println("Assign Port Manager " + newPortManager + " successfully!");
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("There is no available port manager! You can add a new port manager and assign to this port later.");
                            }
                            break;
                        } else {
                            System.out.println("Invalid input! Please enter 'remove' or 'change'!");
                        }
                    }
                }
                break;
            } else if (isChangingManager.equals("no")) {
                System.out.println();
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }

        // Update Port Database
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortID().equals(updatingPortID)) {
                port = updatingPort;
            }
            newPortList.add(port);
        }
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newPortList);
        oos1.close();

        // Update Vehicle Database
        List<Vehicle> newVehicleList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getPort().equals(oldPort.getName())) {
                vehicle.setPort(updatingPort.getName());
            }
            newVehicleList.add(vehicle);
        }
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newVehicleList);
        oos2.close();

        // Update Container Database
        List<Container> newContainerList = new ArrayList<>();
        for (Container container : containerList) {
            if (container.getCurrentPort().equals(oldPort.getName())) {
                container.setCurrentPort(updatingPort.getName());
            }
            newContainerList.add(container);
        }
        FileOutputStream fos3 = new FileOutputStream(containerDatabase);
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(newContainerList);
        oos3.close();

        // Update User Database
        List<User> newUserList = new ArrayList<>();
        if (!oldPort.getPortManager().equals("None")) {
            for (User user : userList) {
                if (user.getUserID().equals(oldPort.getPortManager())) {
                    user.setPort("None");
                }
                if (user.getUserID().equals(updatingPort.getPortManager())) {
                    user.setPort(updatingPort.getName());
                }
                newUserList.add(user);
            }
        } else {
            for (User user : userList) {
                if (user.getUserID().equals(updatingPort.getPortManager())) {
                    user.setPort(updatingPort.getName());
                }
                newUserList.add(user);
            }
        }
        FileOutputStream fos4 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos4 = new ObjectOutputStream(fos4);
        oos4.writeObject(newUserList);
        oos4.close();

        // Update Trip Database
        List<Trip> newTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.getDeparturePort().equals(oldPort.getName())) {
                trip.setDeparturePort(updatingPort.getName());
            }
            if (trip.getArrivalPort().equals(oldPort.getName())) {
                trip.setArrivalPort(updatingPort.getName());
            }
            newTripList.add(trip);
        }
        FileOutputStream fos5 = new FileOutputStream(tripDatabase);
        ObjectOutputStream oos5 = new ObjectOutputStream(fos5);
        oos5.writeObject(newTripList);
        oos5.close();
        System.out.println("Update port " + updatingPortID + "'s information successfully!");
        System.out.println();
    }

    @Override
    public void removePort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();
        List<User> userList = manageDatabase.getUserDatabase();

        Port removingPort = null;
        String removingPortID = null;
        boolean isExistID = false;
        // Prompt user
        while (!isExistID) {
            viewPorts();
            System.out.print("Enter the port's id that you want to remove(ex. P0001): ");
            removingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(removingPortID)) {
                    removingPort = port;
                    isExistID = true;
                    break;
                }
            }
            if (!isExistID) {
                System.out.println("This port's id does not exist! Please enter another port's id.");
            }
        }

        // Update Port Database
        portList.remove(removingPort);
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(portList);
        oos1.close();

        // Update Vehicle Database
        List<Vehicle> newVehicleList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getPort().equals(removingPort.getName())) {
                vehicle.setPort("None");
            }
            newVehicleList.add(vehicle);
        }
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newVehicleList);
        oos2.close();

        // Update Container Database
        List<Container> newContainerList = new ArrayList<>();
        for (Container container : containerList) {
            if (container.getCurrentPort().equals(removingPort.getName())) {
                container.setCurrentPort("None");
            }
            newContainerList.add(container);
        }
        FileOutputStream fos3 = new FileOutputStream(containerDatabase);
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(newContainerList);
        oos3.close();

        // Update User Database
        List<User> newUserList = new ArrayList<>();
        for (User user : userList) {
            if (user.getPort().equals(removingPort.getName())) {
                user.setPort("None");
            }
            newUserList.add(user);
        }
        FileOutputStream fos4 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos4 = new ObjectOutputStream(fos4);
        oos4.writeObject(newUserList);
        oos4.close();
        System.out.println("Remove port " + removingPortID + " successfully!");
        System.out.println();
    }

    @Override
    public void assignPortManagerToPort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<User> userList = manageDatabase.getUserDatabase();
        List<Port> availablePortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortManager().equals("None")) {
                availablePortList.add(port);
            }
        }
        List<User> availablePortManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getPort().equals("None")) {
                availablePortManagerList.add(user);
            }
        }

        // Prompt User
        String assigningPortID = null;
        String assigningPortManagerID = null;
        Port assigningPort = null;
        User assigningPortManager = null;
        if (!availablePortList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------- All Ports ---------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
            System.out.println();
            for (Port port : availablePortList) {
                System.out.println(port);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (true) {
                System.out.print("Enter the port's id that you want to assign (ex.P0001): ");
                assigningPortID = scanner.nextLine();
                for (Port port : availablePortList) {
                    if (port.getPortID().equals(assigningPortID)) {
                        assigningPort = port;
                        break;
                    }
                }
                if (assigningPort == null) {
                    System.out.println("Invalid Input! Please enter the port's id again.");
                } else {
                    break;
                }
            }
            if (!availablePortManagerList.isEmpty()) {
                System.out.println("--------------------------------- Available Port Managers --------------------------------");
                System.out.printf("| %-8s | %-15s | %-16s | %-15s | %-20s |", "User ID" , "Role", "Username", "Password", "Manage Port");
                System.out.println();
                for (User availablePortManager : availablePortManagerList) {
                    System.out.println(availablePortManager);
                }
                System.out.println("------------------------------------------------------------------------------------------");
                while (true) {
                    System.out.print("Enter the port manager's id that you want to assign (ex. PM0001): ");
                    assigningPortManagerID = scanner.nextLine();
                    for (User user : availablePortManagerList) {
                        if (user.getUserID().equals(assigningPortManagerID)) {
                            assigningPortManager = user;
                            break;
                        }
                    }
                    if (assigningPortManager == null) {
                        System.out.println("Invalid Input! Please enter the port's id again.");
                    } else {
                        break;
                    }
                }
                // Assign Port Manager to Port
                assigningPort.setPortManager(assigningPortManager.getUserID());
                assigningPortManager.setPort(assigningPort.getName());
            } else {
                System.out.println("There is no available port manager at the moment! You can create a new port manager and try again.");
            }
        } else {
            System.out.println("Every port is already assigned to a manager! You can create a new port and try again.");
        }

        // Update Port Database
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortID().equals(assigningPortID)) {
                port = assigningPort;
            }
            newPortList.add(port);
        }
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newPortList);
        oos1.close();

        // Update User Database
        List<User> newUserList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUserID().equals(assigningPortManagerID)) {
                user = assigningPortManager;
            }
            newUserList.add(user);
        }
        FileOutputStream fos2 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newUserList);
        oos2.close();

        System.out.println("Assign Port Manager " + assigningPortManagerID + " to Port " + assigningPortID + " successfully!");
        System.out.println();
    }

    @Override
    public void viewPortContainers() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        viewPorts();
        String selectedPortID;
        Port selectedPort = null;
        while (true) {
            System.out.print("Enter the port's id that you want to view its containers (ex. P0001): ");
            selectedPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortID)) {
                    selectedPort = port;
                    break;
                }
            }
            if (selectedPort != null) {
                if (!selectedPort.getContainerList().isEmpty()) {
                    System.out.println("----------------------------- Port's Container List -----------------------------");
                    System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
                    System.out.println();
                    for (Container container : selectedPort.getContainerList()) {
                        System.out.println(container);
                    }
                    System.out.println("---------------------------------------------------------------------------------");
                } else {
                    System.out.println(selectedPort.getName() + " doesn't have any container!");
                }
                break;
            } else {
                System.out.println("This port's id does not exist! Please enter another port's id.");
            }
        }
        System.out.println();
    }

    @Override
    public void addContainerToPort() throws IOException, ClassNotFoundException {
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

        String assigningPortID;
        Port assigningPort = null;
        while (true) {
            viewPorts();
            System.out.print("Enter the port's id that you want to add a container (ex. P0001): ");
            assigningPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(assigningPortID)) {
                    assigningPort = port;
                }
            }
            if (assigningPort != null) {
                System.out.println();
                break;
            } else {
                System.out.println("This port's id does not exist! Please enter another port's id.");
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

            List<Container> existingContainerList = assigningPort.getContainerList();
            double currentWeight = 0;
            for (Container container : existingContainerList) {
                currentWeight += container.getWeight();
            }
            if (currentWeight + assigningContainer.getWeight() >= assigningPort.getStoringCapacity()) {
                System.out.println(assigningPort.getName() + " is full! You can not add " + assigningContainer.getContainerID() + " to this port.");
            } else {
                assigningPort = assigningPort.addPortContainer(assigningContainer);
                assigningContainer.setCurrentPort(assigningPort.getName());
                // Update port database.
                List<Port> newPortList = new ArrayList<>();
                for (Port port : portList) {
                    if (port.getPortID().equals(assigningPort.getPortID())) {
                        port = assigningPort;
                    }
                    newPortList.add(port);
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
                System.out.println("Add container " + assigningContainer.getContainerID() + " to " + assigningPort.getName() + " successfully!");
            }
        } else {
            System.out.println("There is no available container! You can create a new container and assign to this port later!");
        }
        System.out.println();
    }

    @Override
    public void removeContainerFromPort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database.
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();

        String selectedPortId;
        Port selectedPort = null;
        while (true) {
            viewPorts();
            System.out.print("Enter the port's id that you want to remove a container (ex. P0001): ");
            selectedPortId = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortId)) {
                    selectedPort = port;
                }
            }
            if (selectedPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        String selectedContainerID;
        Container selectedContainer = null;
        List<Container> portContainerList = selectedPort.getContainerList();
        if (!portContainerList.isEmpty()) {
            System.out.println("----------------------------- Port's Container List -----------------------------");
            System.out.printf("| %-14s | %-24s | %-20s | %10s |", "Container ID", "Type", "Current Port" , "Weight");
            System.out.println();
            for (Container container : portContainerList) {
                System.out.println(container);
            }
            System.out.println("---------------------------------------------------------------------------------");
            while (true) {
                System.out.print("Enter the container's id that you want to remove from " + selectedPort.getName() +" (ex. C0001): ");
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
            selectedPort = selectedPort.removePortContainer(selectedContainer);
            selectedContainer.setCurrentPort("None");

            Vehicle updatingVehicle = null;
            for (Vehicle vehicle : selectedPort.getVehicleList()) {
                List<Container> vehicleContainerList = vehicle.getContainerList();
                for (Container container : vehicleContainerList) {
                    if (container.getContainerID().equals(selectedContainer.getContainerID())) {
                        vehicle = vehicle.unloadContainer(selectedContainer);
                        updatingVehicle = vehicle;
                        break;
                    }
                }
            }

            List<Port> newPortList = new ArrayList<>();
            if (updatingVehicle != null) {
                // Update port database
                for (Port port : portList) {
                    List<Vehicle> portVehicleList = port.getVehicleList();
                    List<Vehicle> newPortVehicleList = new ArrayList<>();
                    if (port.getPortID().equals(selectedPort.getPortID())) {
                        port = selectedPort;
                        for (Vehicle vehicle : portVehicleList) {
                            if (vehicle.getVehicleID().equals(updatingVehicle.getVehicleID())) {
                                vehicle = updatingVehicle;
                            }
                            newPortVehicleList.add(vehicle);
                        }
                        port.setVehicleList(newPortVehicleList);
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
                for (Port port : portList) {
                    if (port.getPortID().equals(selectedPort.getPortID())) {
                        port = port.removePortContainer(selectedContainer);
                    }
                    newPortList.add(port);
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
            System.out.println("Remove container " + selectedContainer.getContainerID() + " from " + selectedPort.getName() + " successfully!");
        } else {
            System.out.println(selectedPort.getName() + " doesn't have any containers to remove!");
        }
        System.out.println();
    }

    @Override
    public void viewPortVehicles() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        viewPorts();
        String selectedPortID;
        Port selectedPort = null;
        while (true) {
            System.out.print("Enter the port's id that you want to view its vehicles (ex. P0001): ");
            selectedPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortID)) {
                    selectedPort = port;
                    break;
                }
            }
            if (selectedPort != null) {
                if (!selectedPort.getVehicleList().isEmpty()) {
                    System.out.println("-------------------------------------------------------------------------- Port's Vehicle List -------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                    System.out.println();
                    for (Vehicle vehicle : selectedPort.getVehicleList()) {
                        System.out.println(vehicle);
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                } else {
                    System.out.println(selectedPort.getName() + " doesn't have any vehicle!");
                }
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }
        System.out.println();
    }

    @Override
    public void viewPortShips() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        viewPorts();
        String selectedPortID;
        Port selectedPort = null;
        while (true) {
            System.out.print("Enter the port's id that you want to view its vehicles (P0001): ");
            selectedPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortID)) {
                    selectedPort = port;
                    break;
                }
            }
            if (selectedPort != null) {
                if (!selectedPort.getVehicleList().isEmpty()) {
                    System.out.println("---------------------------------------------------------------------------- Port's Ship List ---------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                    System.out.println();
                    for (Vehicle vehicle : selectedPort.getVehicleList()) {
                        if (vehicle.getType().equals("Ship")) {
                            System.out.println(vehicle);
                        }
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                } else {
                    System.out.println(selectedPort.getName() + " doesn't have any vehicle!");
                }
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }
        System.out.println();
    }

    @Override
    public void viewPortTrucks() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        viewPorts();
        String selectedPortID;
        Port selectedPort = null;
        while (true) {
            System.out.print("Enter the port's id that you want to view its vehicles (P0001): ");
            selectedPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortID)) {
                    selectedPort = port;
                    break;
                }
            }
            if (selectedPort != null) {
                if (!selectedPort.getVehicleList().isEmpty()) {
                    System.out.println("---------------------------------------------------------------------------- Port's Truck List --------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                    System.out.println();
                    for (Vehicle vehicle : selectedPort.getVehicleList()) {
                        if (!vehicle.getType().equals("Ship")) {
                            System.out.println(vehicle);
                        }
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                } else {
                    System.out.println(selectedPort.getName() + " doesn't have any vehicle!");
                }
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }
        System.out.println();
    }

    @Override
    public void addVehicleToPort() throws IOException, ClassNotFoundException {
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

        String selectedPortID;
        Port selectedPort = null;
        while (true) {
            viewPorts();
            System.out.print("Enter the port's id that you want to add a vehicle (P0001): ");
            selectedPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortID)) {
                    selectedPort = port;
                    break;
                }
            }
            if (selectedPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        String selectedVehicleID;
        Vehicle selectedVehicle = null;
        if (!availableVehicleList.isEmpty()) {
            if (selectedPort.isLanding()) {
                System.out.println("---------------------------------------------------------------------------- Available Vehicles -------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
                System.out.println();
                for (Vehicle vehicle : availableVehicleList) {
                    System.out.println(vehicle);
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                while (true) {
                    System.out.print("Enter the vehicle's id that you want to add to " + selectedPort.getName() + " (ex. SH0001, TR0001): ");
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
                selectedPort = selectedPort.addPortVehicle(selectedVehicle);
                selectedVehicle.setPort(selectedPort.getName());

                // Update port database.
                List<Port> newPortList = new ArrayList<>();
                for (Port port : portList) {
                    if (port.getPortID().equals(selectedPort.getPortID())) {
                        port = selectedPort;
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
                    if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                        vehicle = selectedVehicle;
                    }
                    newVehicleList.add(vehicle);
                }
                FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                oos2.writeObject(newVehicleList);
                oos2.close();
                System.out.println("Add vehicle " + selectedVehicle.getVehicleID() + " to " + selectedPort.getName() + " successfully!");
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
                        System.out.print("Enter the vehicle's id that you want to add to" + selectedPort.getName() + " (ex. SH0001, TR0001): ");
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
                    selectedPort = selectedPort.addPortVehicle(selectedVehicle);
                    selectedVehicle.setPort(selectedPort.getName());

                    // Update port database.
                    List<Port> newPortList = new ArrayList<>();
                    for (Port port : portList) {
                        if (port.getPortID().equals(selectedPort.getPortID())) {
                            port = selectedPort;
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
                        if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                            vehicle = selectedVehicle;
                        }
                        newVehicleList.add(vehicle);
                    }
                    FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
                    ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                    oos2.writeObject(newVehicleList);
                    oos2.close();
                    System.out.println("Add vehicle " + selectedVehicle.getVehicleID() + " to " + selectedPort.getName() + " successfully!");
                }
            }
        } else {
            System.out.println("There is no available vehicle! You can create a new vehicle and assign to this port later!");
        }
        System.out.println();
    }

    @Override
    public void removeVehicleFromPort() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database.
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        String selectedPortId;
        Port selectedPort = null;
        while (true) {
            viewPorts();
            System.out.print("Enter the port's id that you want to remove a vehicle (ex. P0001): ");
            selectedPortId = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPortId)) {
                    selectedPort = port;
                }
            }
            if (selectedPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        String selectedVehicleId;
        Vehicle selectedVehicle = null;
        List<Vehicle> portVehicleList = selectedPort.getVehicleList();
        if (!portVehicleList.isEmpty()) {
            System.out.println("---------------------------------------------------------------------------- Port's Vehicle List ------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : portVehicleList) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (true) {
                System.out.print("Enter the vehicle's id that you want to remove from " + selectedPort.getName() + " (ex. SH0001, TR0001): ");
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
            selectedPort = selectedPort.removePortVehicle(selectedVehicle);
            selectedVehicle.setPort("None");

            // Update port database.
            List<Port> newPortList = new ArrayList<>();
            for (Port port : portList) {
                if (port.getPortID().equals(selectedPort.getPortID())) {
                    port = selectedPort;
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
                if (vehicle.getVehicleID().equals(selectedVehicle.getVehicleID())) {
                    vehicle = selectedVehicle;
                }
                newVehicleList.add(vehicle);
            }
            FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(newVehicleList);
            oos2.close();
            System.out.println("Remove vehicle " + selectedVehicle.getVehicleID() + " from " + selectedPort.getName() + " successfully!");
        } else {
            System.out.println(selectedPort.getName() + " doesn't have any vehicles to remove!");
        }
        System.out.println();
    }

    @Override
    public void loadPortContainer() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        Port selectingPort = null;
        while (true) {
            viewPorts();
            System.out.print("Choose a port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID)) {
                    selectingPort = port;
                }
            }
            if (selectingPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        Vehicle selectingVehicle = null;
        Container selectingContainer = null;
        List<Vehicle> portVehicleList = selectingPort.getVehicleList();
        List<Container> portContainerList = selectingPort.getContainerList();
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
                                        selectingPort.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port : portList) {
                                            if (port.getPortID().equals(selectingPort.getPortID())) {
                                                port = selectingPort;
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
                                        selectingPort.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port : portList) {
                                            if (port.getPortID().equals(selectingPort.getPortID())) {
                                                port = selectingPort;
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
                                        selectingPort.setVehicleList(newPortVehicleList);

                                        // Update port database
                                        List<Port> newPortList = new ArrayList<>();
                                        for (Port port : portList) {
                                            if (port.getPortID().equals(selectingPort.getPortID())) {
                                                port = selectingPort;
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
                                    selectingPort.setVehicleList(newPortVehicleList);

                                    // Update port database
                                    List<Port> newPortList = new ArrayList<>();
                                    for (Port port : portList) {
                                        if (port.getPortID().equals(selectingPort.getPortID())) {
                                            port = selectingPort;
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
    public void unloadPortContainer() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        Port selectingPort = null;
        while (true) {
            viewPorts();
            System.out.print("Choose a port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID)) {
                    selectingPort = port;
                }
            }
            if (selectingPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        List<Vehicle> portVehicleList = selectingPort.getVehicleList();
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
                        selectingPort.setVehicleList(newPortVehicleList);

                        // Update port database
                        List<Port> newPortList = new ArrayList<>();
                        for (Port port : portList) {
                            if (port.getPortID().equals(selectingPort.getPortID())) {
                                port = selectingPort;
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
    public void movePortVehicle() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Container> containerList = manageDatabase.getContainerDatabase();

        Port departurePort = null;
        Port arrivalPort = null;
        while (true) {
            viewPorts();
            System.out.print("Choose the departure port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID)) {
                    departurePort = port;
                }
            }
            if (departurePort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        List<Vehicle> portVehicleList = departurePort.getVehicleList();
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
                    for (Port port : portList) {
                        if (!port.getPortID().equals(departurePort.getPortID())) {
                            System.out.println(port);
                        }
                    }
                } else {
                    for (Port port : portList) {
                        if (!port.getPortID().equals(departurePort.getPortID()) && port.isLanding()) {
                            System.out.println(port);
                        }
                    }
                }
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Choose the arrival port's id from the above list (ex. P0001): ");
                String selectingPortID = scanner.nextLine();
                if (selectingVehicle.getType().equals("Ship")) {
                    for (Port port : portList) {
                        if (port.getPortID().equals(selectingPortID) && !port.getPortID().equals(departurePort.getPortID())) {
                            arrivalPort = port;
                        }
                    }
                } else {
                    for (Port port : portList) {
                        if (port.getPortID().equals(selectingPortID) && !port.getPortID().equals(departurePort.getPortID()) && port.isLanding()) {
                            arrivalPort = port;
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

                for (Port port : updatedPortList) {
                    if (port.getPortID().equals(departurePort.getPortID())) {
                        departurePort = port;
                    }
                    if (port.getPortID().equals(arrivalPort.getPortID())) {
                        arrivalPort = port;
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
                for (Port port : updatedPortList) {
                    if (port.getPortID().equals(arrivalPort.getPortID())) {
                        port = arrivalPort;
                    }
                    if (port.getPortID().equals(departurePort.getPortID())) {
                        port = departurePort;
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
            System.out.println(departurePort.getName() + " doesn't have any vehicle to move!");
        }
        System.out.println();
    }

    @Override
    public void calculateDistance() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        Port firstPort = null;
        while (true) {
            viewPorts();
            System.out.print("Choose the first port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID)) {
                    firstPort = port;
                }
            }
            if (firstPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        Port secondPort = null;
        while (true) {
            System.out.println("---------------------------------------------------------------------- All Ports ---------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
            System.out.println();
            for (Port port : portList) {
                if (!port.equals(firstPort)) {
                    System.out.println(port);
                }
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Choose the second port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID) && !port.getPortID().equals(firstPort.getPortID())) {
                    secondPort = port;
                }
            }
            if (secondPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }
        double distance = firstPort.calculateDistance(secondPort);
        System.out.println("The distance between " + firstPort.getName() +" and " + secondPort.getName() + "is: " + distance + "km.");
        System.out.println();
    }

    @Override
    public void viewPortTripHistory() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        Port selectingPort = null;
        while (true) {
            viewPorts();
            System.out.print("Choose a port's id from the above list (ex. P0001): ");
            String selectingPortID = scanner.nextLine();
            for (Port port : portList) {
                if (port.getPortID().equals(selectingPortID)) {
                    selectingPort = port;
                }
            }
            if (selectingPort != null) {
                break;
            } else {
                System.out.println("The port's id you entered does not exist! Please enter another port's id.");
            }
        }

        if (!selectingPort.getTripList().isEmpty()) {
            System.out.println("----------------------------------------------------------- Port's Trip History --------------------------------------------------------");
            System.out.printf("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", "Trip ID", "Vehicle", "Departure Port" , "Arrival Port", "Departure Date", "Arrival Date", "Status");
            System.out.println();
            for (Trip trip : selectingPort.getTripList()) {
                System.out.println(trip);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println(selectingPort.getName() + " doesn't have any trip!");
        }
        System.out.println();
    }
}
