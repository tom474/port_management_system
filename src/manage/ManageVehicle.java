package manage;

import port.Port;
import port.Trip;
import vehicle.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class ManageVehicle implements ManageVehicleInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File vehicleDatabase = new File("src/model/vehicle.dat");
    private final File tripDatabase = new File("src/model/trip.dat");

    @Override
    public void viewVehicles() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        if (!vehicleList.isEmpty()) {
            System.out.println("------------------------------------------------------------------------------ All Vehicles -----------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s | + \n", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : vehicleList) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no vehicle in the database!");
        }
        System.out.println();
    }

    @Override
    public void viewShips() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Vehicle> shipList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getType().equals("Ship")) {
                shipList.add(vehicle);
            }
        }
        if (!shipList.isEmpty()) {
            System.out.println("------------------------------------------------------------------------------- All Ships -------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : shipList) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no ship in the database!");
        }
        System.out.println();
    }

    @Override
    public void viewTrucks() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Vehicle> truckList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            if (!vehicle.getType().equals("Ship")) {
                truckList.add(vehicle);
            }
        }
        if (!truckList.isEmpty()) {
            System.out.println("------------------------------------------------------------------------------- All Trucks ------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %-10s |", "Vehicle ID", "Name", "Type", "Port", "Carrying Capacity","Current Capacity", "Fuel Capacity","Current Fuel", "Containers");
            System.out.println();
            for (Vehicle vehicle : truckList) {
                System.out.println(vehicle);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no truck in the database!");
        }
        System.out.println();
    }

    @Override
    public void addVehicle() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ManageDatabase manageDatabase = new ManageDatabase();
        ManagePort managePort = new ManagePort();


        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Port> landingPortlist = new ArrayList<>();
        for (Port port : portList) {
            if (port.isLanding()) {
                landingPortlist.add(port);
            }
        }

        // Get user's input
        String vehicleType;
        String selectVehicleType;
        label:
        while (true){
            System.out.println("Choose the vehicle's type: ");
            System.out.println("1. Ship");
            System.out.println("2. Basic Truck");
            System.out.println("3. Tanker Truck");
            System.out.println("4. Reefer Truck");
            System.out.print("Enter a number from 1 to 4: ");
            selectVehicleType = scanner.nextLine();
            switch (selectVehicleType) {
                case "1":
                    vehicleType = "Ship";
                    break label;
                case "2":
                    vehicleType = "Basic Truck";
                    break label;
                case "3":
                    vehicleType = "Tanker Truck";
                    break label;
                case "4":
                    vehicleType = "Reefer Truck";
                    break label;
                default:
                    System.out.println("Invalid Input! Please enter an integer number from 1 to 4.");
                    break;
            }
        }

        System.out.print("Enter vehicle name (ex. Ship 1, Truck 1): ");
        String vehicleName = scanner.nextLine();
        System.out.print("Enter vehicle carrying capacity (kg) (ex. 15000): ");
        double carryingCapacity = scanner.nextDouble();
        System.out.print("Enter vehicle fuel capacity (gallons) (ex. 100): ");
        double fuelCapacity = scanner.nextDouble();
        scanner.nextLine();
        String portName = null;
        if (vehicleType.equals("Ship")) {
            while (true) {
                managePort.viewPorts();
                System.out.print("Enter a the port's id that you want to assign this vehicle (ex. P0001): ");
                String portID = scanner.nextLine();
                for (Port port : portList) {
                    if (port.getPortID().equals(portID)) {
                        portName = port.getName();
                    }
                }
                if (portName != null) {
                    break;
                } else {
                    System.out.println("The port's id you entered does not exist! Please enter another port's id.");
                }
            }
        } else {
            while (true) {
                System.out.println("-------------------------------------------------------------------- Landing Ports -------------------------------------------------------------------");
                System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
                System.out.println();
                for (Port port : landingPortlist) {
                    System.out.println(port);
                }
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.print("Enter a the port's id that you want to assign this vehicle (ex. P0001): ");
                String portID = scanner.nextLine();
                for (Port port : landingPortlist) {
                    if (port.getPortID().equals(portID)) {
                        portName = port.getName();
                    }
                }
                if (portName != null) {
                    System.out.println();
                    break;
                } else {
                    System.out.println("The port's id you entered does not exist! Please enter another port's id.");
                }
            }
        }

        // Create new vehicle
        String vehicleID = null;
        Vehicle newVehicle;
        switch (vehicleType) {
            case "Ship" -> {
                boolean isExistID = true;
                while (isExistID) {
                    vehicleID = "SH" + random.nextInt(1000, 9999);
                    for (Vehicle vehicle : vehicleList) {
                        isExistID = vehicleID.equals(vehicle.getVehicleID());
                    }
                }
                newVehicle = new Ship(vehicleID, vehicleName, carryingCapacity, fuelCapacity, portName);
            }
            case "Basic Truck" -> {
                boolean isExistID = true;
                while (isExistID) {
                    vehicleID = "TR" + random.nextInt(1000, 9999);
                    for (Vehicle vehicle : vehicleList) {
                        isExistID = vehicleID.equals(vehicle.getVehicleID());
                    }
                }
                newVehicle = new BasicTruck(vehicleID, vehicleName, carryingCapacity, fuelCapacity, portName);
            }
            case "Tanker Truck" -> {
                boolean isExistID = true;
                while (isExistID) {
                    vehicleID = "TR" + random.nextInt(1000, 9999);
                    for (Vehicle vehicle : vehicleList) {
                        isExistID = vehicleID.equals(vehicle.getVehicleID());
                    }
                }
                newVehicle = new TankerTruck(vehicleID, vehicleName, carryingCapacity, fuelCapacity, portName);
            }
            default -> {
                boolean isExistID = true;
                while (isExistID) {
                    vehicleID = "TR" + random.nextInt(1000, 9999);
                    for (Vehicle vehicle : vehicleList) {
                        isExistID = vehicleID.equals(vehicle.getVehicleID());
                    }
                }
                newVehicle = new ReeferTruck(vehicleID, vehicleName, carryingCapacity, fuelCapacity, portName);
            }
        }

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port: portList) {
            if (port.getName().equals(portName)) {
                port = port.addPortVehicle(newVehicle);
            }
            newPortList.add(port);
        }
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newPortList);
        oos1.close();

        // Update vehicle database
        vehicleList.add(newVehicle);
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(vehicleList);
        oos2.close();
        System.out.println("Add vehicle " + vehicleName + " successfully!");
        System.out.println();
    }

    @Override
    public void updateVehicle() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();
        List<Trip> tripList = manageDatabase.getTripDatabase();

        System.out.println("You can only update the vehicle's name! Other vehicle's details can not be changed to avoid unexpected errors.");
        System.out.println("Note: If you want to change the vehicle's current port, you can use the function 'Move A Vehicle From A Port To Another Port' in the 'Manage Port Database' menu.");
        Vehicle oldVehicle = null;
        String updatingVehicleID;
        Vehicle updatingVehicle = null;
        while (true) {
            viewVehicles();
            System.out.print("Enter a vehicle's id that you want to update (ex. SH0001, TR0001): ");
            updatingVehicleID = scanner.nextLine();
            for (Vehicle vehicle: vehicleList) {
                if (vehicle.getVehicleID().equals(updatingVehicleID)) {
                    oldVehicle = vehicle;
                    updatingVehicle = vehicle;
                }
            }
            if (updatingVehicle != null) {
                break;
            } else {
                System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id. ");
            }
        }

        System.out.print("Enter the new name (ex. Ship 1): ");
        String updatingName = scanner.nextLine();
        updatingVehicle.setName(updatingName);

        // Updating port database.
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            List<Vehicle> portVehicleList = port.getVehicleList();
            if (port.getName().equals(updatingVehicle.getPort())) {
                List<Vehicle> newPortVehicleList = new ArrayList<>();
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

        // Update vehicle database.
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

        // Update trip database.
        List<Trip> newTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.getVehicle().equals(oldVehicle.getName())) {
                trip.setVehicle(updatingVehicle.getName());
            }
            newTripList.add(trip);
        }
        FileOutputStream fos3 = new FileOutputStream(tripDatabase);
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(newTripList);
        oos3.close();
        System.out.println("Update vehicle " + updatingVehicleID + "'s information successfully!");
        System.out.println();
    }

    @Override
    public void removeVehicle() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        Vehicle deletingVehicle = null;
        String deletedVehicleID;
        while (true) {
            viewVehicles();
            System.out.print("Enter a vehicle's id that you want to delete (ex. SH0001, TR0001): ");
            deletedVehicleID = scanner.nextLine();
            for (Vehicle vehicle: vehicleList) {
                if (vehicle.getVehicleID().equals(deletedVehicleID)) {
                    deletingVehicle = vehicle;
                }
            }

            if (deletingVehicle != null) {
                System.out.println();
                break;
            } else {
                System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id.");
            }
        }

        // Update port database.
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            List<Vehicle> portVehicleList = port.getVehicleList();
            if (port.getName().equals(deletingVehicle.getPort())) {
                List<Vehicle> newPortVehicleList = new ArrayList<>();
                for (Vehicle vehicle : portVehicleList) {
                    if (!vehicle.getVehicleID().equals(deletingVehicle.getVehicleID())) {
                        newPortVehicleList.add(vehicle);
                    }
                }
                port.setVehicleList(newPortVehicleList);
            }
            newPortList.add(port);
        }
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newPortList);
        oos1.close();

        // Update vehicle database.
        vehicleList.remove(deletingVehicle);
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(vehicleList);
        oos2.close();
        System.out.println("Delete vehicle " + deletedVehicleID + " successfully!");
        System.out.println();
    }

    @Override
    public void refuelVehicle() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Vehicle> vehicleList = manageDatabase.getVehicleDatabase();

        Vehicle refuelingVehicle = null;
        String refuelingVehicleID;
        while (true) {
            viewVehicles();
            System.out.print("Enter a vehicle ID that you want to refuel (ex. SH0001, TR0001): ");
            refuelingVehicleID = scanner.nextLine();
            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getVehicleID().equals(refuelingVehicleID)) {
                    refuelingVehicle = vehicle;
                }
            }
            if (refuelingVehicle != null) {
                refuelingVehicle = refuelingVehicle.refuel();
                break;
            } else {
                System.out.println("The vehicle's id you entered does not exist! Please enter another vehicle's id. ");
            }
        }

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port: portList) {
            List<Vehicle> portVehicleList = port.getVehicleList();
            if (port.getName().equals(refuelingVehicle.getPort())) {
                List<Vehicle> newPortVehicleList = new ArrayList<>();
                for (Vehicle vehicle : portVehicleList) {
                    if (vehicle.getVehicleID().equals(refuelingVehicle.getVehicleID())) {
                        vehicle = refuelingVehicle;
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
            if (vehicle.getVehicleID().equals(refuelingVehicle.getVehicleID())) {
                vehicle = refuelingVehicle;
            }
            newVehicleList.add(vehicle);
        }
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newVehicleList);
        oos2.close();
        System.out.println();
    }
}
