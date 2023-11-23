package vehicle;

import container.Container;
import manage.ManageDatabase;
import port.Port;
import port.Trip;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Vehicle implements Serializable {
    private String vehicleID, name, type, port;
    private double currentFuel, currentCarryingCapacity, carryingCapacity, fuelCapacity;
    private List<Container> containerList;
    private int dryStorageContainer, liquidContainer, openSideContainer, openTopContainer, refrigeratedContainer;

    public Vehicle() {
        this.vehicleID = "Default";
        this.name = "Default";
        this.type = "Default";
        this.carryingCapacity = 0;
        this.fuelCapacity = 0;
        this.currentCarryingCapacity = 0;
        this.currentFuel = fuelCapacity;
        this.port = "Default";
        this.containerList = new ArrayList<>();
        this.dryStorageContainer = 0;
        this.liquidContainer = 0;
        this.openSideContainer = 0;
        this.openTopContainer = 0;
        this.refrigeratedContainer = 0;
    }

    public Vehicle(String vehicleID, String name, String type, double carryingCapacity, double fuelCapacity, String port) {
        this.vehicleID = vehicleID;
        this.name = name;
        this.type = type;
        this.carryingCapacity = carryingCapacity;
        this.fuelCapacity = fuelCapacity;
        this.currentCarryingCapacity = 0;
        this.currentFuel = fuelCapacity;
        this.port = port;
        this.containerList = new ArrayList<>();
        this.dryStorageContainer = 0;
        this.liquidContainer = 0;
        this.openSideContainer = 0;
        this.openTopContainer = 0;
        this.refrigeratedContainer = 0;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        this.currentFuel = currentFuel;
    }

    public double getCurrentCarryingCapacity() {
        return currentCarryingCapacity;
    }

    public void setCurrentCarryingCapacity(double currentCarryingCapacity) {
        this.currentCarryingCapacity = currentCarryingCapacity;
    }

    public double getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(double carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<Container> getContainerList() {
        return containerList;
    }

    public void setContainerList(List<Container> containerList) {
        this.containerList = containerList;
    }

    public int getDryStorageContainer() {
        return dryStorageContainer;
    }

    public void setDryStorageContainer(int dryStorageContainer) {
        this.dryStorageContainer = dryStorageContainer;
    }

    public int getLiquidContainer() {
        return liquidContainer;
    }

    public void setLiquidContainer(int liquidContainer) {
        this.liquidContainer = liquidContainer;
    }

    public int getOpenSideContainer() {
        return openSideContainer;
    }

    public void setOpenSideContainer(int openSideContainer) {
        this.openSideContainer = openSideContainer;
    }

    public int getOpenTopContainer() {
        return openTopContainer;
    }

    public void setOpenTopContainer(int openTopContainer) {
        this.openTopContainer = openTopContainer;
    }

    public int getRefrigeratedContainer() {
        return refrigeratedContainer;
    }

    public void setRefrigeratedContainer(int refrigeratedContainer) {
        this.refrigeratedContainer = refrigeratedContainer;
    }

    public Vehicle loadContainer(Container container) {
        this.containerList.add(container);
        this.currentCarryingCapacity += container.getWeight();
        switch (container.getContainerType()) {
            case "Dry Storage Container" -> this.dryStorageContainer += 1;
            case "Open Top Container" -> this.openTopContainer += 1;
            case "Open Side Container" -> this.openSideContainer += 1;
            case "Refrigerated Container" -> this.refrigeratedContainer += 1;
            default -> this.liquidContainer += 1;
        }
        return this;
    }

    public Vehicle unloadContainer(Container container) {
        this.containerList.remove(container);
        this.currentCarryingCapacity -= container.getWeight();
        switch (container.getContainerType()) {
            case "Dry Storage Container" -> this.dryStorageContainer -= 1;
            case "Open Top Container" -> this.openTopContainer -= 1;
            case "Open Side Container" -> this.openSideContainer -= 1;
            case "Refrigerated Container" -> this.refrigeratedContainer -= 1;
            default -> this.liquidContainer -= 1;
        }
        return this;
    }

    public boolean canMoveToPort(Port port) throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Port> portList = manageDatabase.getPortDatabase();

        double distance = 0;
        double fuelConsumption = 0;
        Port currentPort;
        for (Port port1 : portList) {
            if (port1.getName().equals(this.port)) {
                currentPort = port1;
                distance = currentPort.calculateDistance(port);
            }
        }

        if (this.type.equals("Ship")) {
            List<Container> portContainerList = port.getContainerList();
            double currentStoringCapacity = 0;
            for (Container container : portContainerList) {
                currentStoringCapacity += container.getWeight();
                double containerWeightInTons = container.getWeight() / 1000;
                fuelConsumption +=  containerWeightInTons * container.getShipFuelConsumption() * distance;
            }
            boolean canStore = currentStoringCapacity + this.carryingCapacity < port.getStoringCapacity();
            boolean enoughFuel = currentFuel > fuelConsumption;
            return canStore && enoughFuel;
        } else {
            if (port.isLanding()) {
                List<Container> portContainerList = port.getContainerList();
                double currentStoringCapacity = 0;
                for (Container container : portContainerList) {
                    currentStoringCapacity += container.getWeight();
                    double containerWeightInTons = container.getWeight() / 1000;
                    fuelConsumption +=  containerWeightInTons * container.getTruckFuelConsumption() * distance;
                }
                boolean canStore = currentStoringCapacity + this.carryingCapacity < port.getStoringCapacity();
                boolean enoughFuel = currentFuel > fuelConsumption;
                return canStore && enoughFuel;
            } else {
                return false;
            }
        }
    }

    public Vehicle moveToPort(Port port) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();
        Random random = new Random();

        // Get the database
        List<Trip> tripList = manageDatabase.getTripDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();

        String oldPort = this.port;
        double distance = 0;
        double fuelConsumption = 0;
        Port currentPort = null;
        do {
            for (Port port1 : portList) {
                if (port1.getName().equals(oldPort)) {
                    currentPort = port1;
                    distance = currentPort.calculateDistance(port);
                }
            }
        } while (currentPort == null);

        this.port = port.getName();

        List<Container> vehicleContainerList = new ArrayList<>();
        if (!this.getContainerList().isEmpty()) {
            for (Container container : this.containerList) {
                container.setCurrentPort(port.getName());
                vehicleContainerList.add(container);
            }
            this.containerList = vehicleContainerList;
        }

        if (this.type.equals("Ship")) {
            for (Container container : this.containerList) {
                double containerWeightInTons = container.getWeight() / 1000;
                fuelConsumption +=  containerWeightInTons * container.getShipFuelConsumption() * distance;
            }
        } else {
            for (Container container : this.containerList) {
                double containerWeightInTons = container.getWeight() / 1000;
                fuelConsumption +=  containerWeightInTons * container.getTruckFuelConsumption() * distance;
            }
        }
        currentFuel -= Math.round(fuelConsumption);

        // Record new trip.
        LocalDate departureDate;
        while (true) {
            System.out.print("Enter the date that you want this vehicle to move to the new port (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                departureDate = LocalDate.parse(date, dateTimeFormatter);
                if (!departureDate.isBefore(LocalDate.now())) {
                    break;
                } else {
                    System.out.println("You can not enter a day which is before today!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }

        LocalDate arrivalDate;
        while (true) {
            System.out.print("Enter the date that you want this vehicle to arrive at the new port (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                arrivalDate = LocalDate.parse(date, dateTimeFormatter);
                boolean isValidArrivalDate = arrivalDate.isAfter(departureDate);
                if (isValidArrivalDate) {
                    break;
                } else {
                    System.out.println("You should enter the arrival date after the departure date!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }
        String tripID = null;
        boolean isExistID = true;
        while (isExistID) {
            tripID = "T" + random.nextInt(1000, 9999);
            for (Trip trip : tripList) {
                isExistID = tripID.equals(trip.getTripID());
            }
        }
        Trip newTrip = new Trip(tripID, this.getName(), fuelConsumption, departureDate, arrivalDate, oldPort, port.getName(),"In progress");
        tripList.add(newTrip);

        currentPort = currentPort.addPortTrip(newTrip);
        port = port.addPortTrip(newTrip);

        // Update trip to database
        File tripDatabase = new File("src/model/trip.dat");
        FileOutputStream fos1 = new FileOutputStream(tripDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(tripList);

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port1 : portList) {
            if (port1.getPortID().equals(port.getPortID())) {
                port1 = port;
            }
            if (port1.getPortID().equals(currentPort.getPortID())) {
                port1 = currentPort;
            }
            newPortList.add(port1);
        }
        File portDatabase = new File("src/model/port.dat");
        FileOutputStream fos2 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newPortList);
        return this;
    }

    public Vehicle refuel() {
        if (this.currentFuel == fuelCapacity) {
            System.out.println(this.getName() + "'s fuel is full! You don't need to refuel it." );
        } else {
            this.currentFuel = fuelCapacity;
            System.out.println("Refuel " + this.getName() + " successfully!");
        }
        return this;
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        return String.valueOf(formatter.format("| %-10s | %-20s | %-15s | %-20s | %18s | %18s | %15s | %15s | %10s |", vehicleID, name, type, port, carryingCapacity, currentCarryingCapacity, fuelCapacity, currentFuel, containerList.size()));
    }
}
