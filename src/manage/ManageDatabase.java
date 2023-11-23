package manage;

import container.*;
import port.Port;
import port.Trip;
import user.PortManager;
import user.SystemAdmin;
import user.User;
import vehicle.*;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ManageDatabase implements ManageDatabaseInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File vehicleDatabase = new File("src/model/vehicle.dat");
    private final File containerDatabase = new File("src/model/container.dat");
    private final File tripDatabase = new File("src/model/trip.dat");
    private final File userDatabase = new File("src/model/user.dat");

    @Override
    public List<Port> getPortDatabase() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(portDatabase);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        List<Port> portList = (List<Port>) ois.readObject();
        ois.close();
        return portList;
    }

    @Override
    public List<Vehicle> getVehicleDatabase() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(vehicleDatabase);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        List<Vehicle> vehicleList = (List<Vehicle>) ois.readObject();
        ois.close();
        return vehicleList;
    }

    @Override
    public List<Container> getContainerDatabase() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(containerDatabase);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        List<Container> containerList = (List<Container>) ois.readObject();
        ois.close();
        return containerList;
    }

    @Override
    public List<Trip> getTripDatabase() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(tripDatabase);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        List<Trip> tripList = (List<Trip>) ois.readObject();
        ois.close();
        return tripList;
    }

    @Override
    public List<User> getUserDatabase() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(userDatabase);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        List<User> userList = (List<User>) ois.readObject();
        ois.close();
        return userList;
    }

    @Override
    public void updateTripDatabase() throws IOException, ClassNotFoundException {
        List<Trip> tripList = getTripDatabase();
        List<Trip> newTripList = new ArrayList<>();
        List<Port> portList = getPortDatabase();
        List<Port> newPortList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.getArrivalDate().isBefore(LocalDate.now())) {
                trip.setStatus("Completed");
                long days = ChronoUnit.DAYS.between(trip.getArrivalDate(), LocalDate.now());
                if (days <= 7) {
                    for (Port port : portList) {
                        List<Trip> portTripList = port.getTripList();
                        List<Trip> newPortTripList = new ArrayList<>();
                        if (trip.getDeparturePort().equals(port.getName())) {
                            for (Trip portTrip : portTripList) {
                                if (trip.getTripID().equals(portTrip.getTripID())) {
                                    portTrip = trip;
                                }
                                newPortTripList.add(portTrip);
                            }
                        }
                        if (trip.getArrivalPort().equals(port.getName())) {
                            for (Trip portTrip : portTripList) {
                                if (trip.getTripID().equals(portTrip.getTripID())) {
                                    portTrip = trip;
                                }
                                newPortTripList.add(portTrip);
                            }
                        }
                        port.setTripList(newPortTripList);
                        newPortList.add(port);
                    }
                    newTripList.add(trip);
                } else {
                    for (Port port : portList) {
                        if (trip.getDeparturePort().equals(port.getName())) {
                            port = port.removePortTrip(trip);
                        }
                        if (trip.getArrivalPort().equals(port.getName())) {
                            port = port.removePortTrip(trip);
                        }
                        newPortList.add(port);
                    }
                }
            } else {
                newTripList = tripList;
                newPortList = portList;
            }
        }
        FileOutputStream fos1 = new FileOutputStream(tripDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newTripList);
        FileOutputStream fos2 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newPortList);
    }

    @Override
    public void initializeDatabase() throws IOException {
        List<Port> portList = new ArrayList<>();
        Port port1 = new Port("P0001", "Cat Lai Port", "None", 10.7661, 106.7955, 2500000, false);
        Port port2 = new Port("P0002", "Hiep Phuoc Port", "None", 10.6468, 106.7419, 3000000, true);
        Port port3 = new Port("P0003", "Ben Nghe Port", "None", 10.7618, 106.7355, 2000000, true);
        Port port4 = new Port("P0004", "Tan Cang Port", "None", 10.7582, 106.7911, 2500000, false);
        Port port5 = new Port("P0005", "Tan Thuan Port", "None", 10.7572, 106.7319, 3000000, true);
        portList.add(port1);
        portList.add(port2);
        portList.add(port3);
        portList.add(port4);
        portList.add(port5);
        FileOutputStream fos1 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(portList);
        oos1.close();

        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicle1 = new Ship("SH0001", "Ship 1", 15000, 1500, "None");
        Vehicle vehicle2 = new Ship("SH0002", "Ship 2", 15000, 1500, "None");
        Vehicle vehicle3 = new Ship("SH0003", "Ship 3", 15000, 1500, "None");
        Vehicle vehicle4 = new Ship("SH0004", "Ship 4", 15000, 1500, "None");
        Vehicle vehicle5 = new Ship("SH0005", "Ship 5", 15000, 1500, "None");
        Vehicle vehicle6 = new BasicTruck("TR0001", "Basic Truck 1", 14000, 1400, "None");
        Vehicle vehicle7 = new BasicTruck("TR0002", "Basic Truck 2", 14000, 1400, "None");
        Vehicle vehicle8 = new BasicTruck("TR0003", "Basic Truck 3", 14000, 1400, "None");
        Vehicle vehicle9 = new BasicTruck("TR0004", "Basic Truck 4", 14000, 1400, "None");
        Vehicle vehicle10 = new BasicTruck("TR0005", "Basic Truck 5", 14000, 1400, "None");
        Vehicle vehicle11 = new ReeferTruck("TR0006", "Reefer Truck 1", 12000, 1200, "None");
        Vehicle vehicle12 = new ReeferTruck("TR0007", "Reefer Truck 2", 12000, 1200, "None");
        Vehicle vehicle13 = new ReeferTruck("TR0008", "Reefer Truck 3", 12000, 1200, "None");
        Vehicle vehicle14 = new ReeferTruck("TR0009", "Reefer Truck 4", 12000, 1200, "None");
        Vehicle vehicle15 = new ReeferTruck("TR0010", "Reefer Truck 5", 12000, 1200, "None");
        Vehicle vehicle16 = new TankerTruck("TR0011", "Tanker Truck 1", 10000, 1000, "None");
        Vehicle vehicle17 = new TankerTruck("TR0012", "Tanker Truck 2", 10000, 1000, "None");
        Vehicle vehicle18 = new TankerTruck("TR0013", "Tanker Truck 3", 10000, 1000, "None");
        Vehicle vehicle19 = new TankerTruck("TR0014", "Tanker Truck 4", 10000, 1000, "None");
        Vehicle vehicle20 = new TankerTruck("TR0015", "Tanker Truck 5", 10000, 1000, "None");
        Vehicle vehicle21 = new Ship("SH0006", "Ship 6", 15000, 1500, "None");
        Vehicle vehicle22 = new Ship("SH0007", "Ship 7", 15000, 1500, "None");
        Vehicle vehicle23 = new Ship("SH0008", "Ship 8", 15000, 1500, "None");
        Vehicle vehicle24 = new Ship("SH0009", "Ship 9", 15000, 1500, "None");
        Vehicle vehicle25 = new Ship("SH0010", "Ship 10", 15000, 1500, "None");
        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);
        vehicleList.add(vehicle3);
        vehicleList.add(vehicle4);
        vehicleList.add(vehicle5);
        vehicleList.add(vehicle6);
        vehicleList.add(vehicle7);
        vehicleList.add(vehicle8);
        vehicleList.add(vehicle9);
        vehicleList.add(vehicle10);
        vehicleList.add(vehicle11);
        vehicleList.add(vehicle12);
        vehicleList.add(vehicle13);
        vehicleList.add(vehicle14);
        vehicleList.add(vehicle15);
        vehicleList.add(vehicle16);
        vehicleList.add(vehicle17);
        vehicleList.add(vehicle18);
        vehicleList.add(vehicle19);
        vehicleList.add(vehicle20);
        vehicleList.add(vehicle21);
        vehicleList.add(vehicle22);
        vehicleList.add(vehicle23);
        vehicleList.add(vehicle24);
        vehicleList.add(vehicle25);
        FileOutputStream fos2 = new FileOutputStream(vehicleDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(vehicleList);
        oos2.close();

        List<Container> containerList = new ArrayList<>();
        Container container1 = new RefrigeratedContainer("C0001", 1500, "None");
        Container container2 = new RefrigeratedContainer("C0002", 1500, "None");
        Container container3 = new RefrigeratedContainer("C0003", 1500, "None");
        Container container4 = new RefrigeratedContainer("C0004", 1500, "None");
        Container container5 = new RefrigeratedContainer("C0005", 1500, "None");
        Container container6 = new RefrigeratedContainer("C0006", 1500, "None");
        Container container7 = new DryStorageContainer("C0007", 1400, "None");
        Container container8 = new DryStorageContainer("C0008", 1400, "None");
        Container container9 = new DryStorageContainer("C0009", 1400, "None");
        Container container10 = new DryStorageContainer("C0010", 1400, "None");
        Container container11 = new DryStorageContainer("C0011", 1400, "None");
        Container container12 = new DryStorageContainer("C0012", 1400, "None");
        Container container13 = new OpenSideContainer("C0013", 1300, "None");
        Container container14 = new OpenSideContainer("C0014", 1300, "None");
        Container container15 = new OpenSideContainer("C0015", 1300, "None");
        Container container16 = new OpenSideContainer("C0016", 1300, "None");
        Container container17 = new OpenSideContainer("C0017", 1300, "None");
        Container container18 = new OpenSideContainer("C0018", 1300, "None");
        Container container19 = new OpenTopContainer("C0019", 1200, "None");
        Container container20 = new OpenTopContainer("C0020", 1200, "None");
        Container container21 = new OpenTopContainer("C0021", 1200, "None");
        Container container22 = new OpenTopContainer("C0022", 1200, "None");
        Container container23 = new OpenTopContainer("C0023", 1200, "None");
        Container container24 = new OpenTopContainer("C0024", 1200, "None");
        Container container25 = new LiquidContainer("C0025", 1100, "None");
        Container container26 = new LiquidContainer("C0026", 1100, "None");
        Container container27 = new LiquidContainer("C0027", 1100, "None");
        Container container28 = new LiquidContainer("C0028", 1100, "None");
        Container container29 = new LiquidContainer("C0029", 1100, "None");
        Container container30 = new LiquidContainer("C0030", 1100, "None");
        containerList.add(container1);
        containerList.add(container2);
        containerList.add(container3);
        containerList.add(container4);
        containerList.add(container5);
        containerList.add(container6);
        containerList.add(container7);
        containerList.add(container8);
        containerList.add(container9);
        containerList.add(container10);
        containerList.add(container11);
        containerList.add(container12);
        containerList.add(container13);
        containerList.add(container14);
        containerList.add(container15);
        containerList.add(container16);
        containerList.add(container17);
        containerList.add(container18);
        containerList.add(container19);
        containerList.add(container20);
        containerList.add(container21);
        containerList.add(container22);
        containerList.add(container23);
        containerList.add(container24);
        containerList.add(container25);
        containerList.add(container26);
        containerList.add(container27);
        containerList.add(container28);
        containerList.add(container29);
        containerList.add(container30);
        FileOutputStream fos3 = new FileOutputStream(containerDatabase);
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(containerList);
        oos3.close();

        List<Trip> tripList = new ArrayList<>();
        Trip trip1 = new Trip("T0001", "Ship 1", 200, LocalDate.of(2023, 9, 20), LocalDate.of(2023, 9, 30), "Hiep Phuoc Port", "Cat Lai Port", "In Progress");
        Trip trip2 = new Trip("T0002", "Basic Truck 1", 150, LocalDate.of(2023, 9, 20), LocalDate.of(2023, 9, 30), "Hiep Phuoc Port", "Ben Nghe Port", "In Progress");
        Trip trip3 = new Trip("T0003", "Tanker Truck 1", 150, LocalDate.of(2023, 9, 20), LocalDate.of(2023, 9, 30), "Ben Nghe Port", "Hiep Phuoc Port", "In Progress");
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        FileOutputStream fos4 = new FileOutputStream(tripDatabase);
        ObjectOutputStream oos4 = new ObjectOutputStream(fos4);
        oos4.writeObject(tripList);
        oos4.close();

        List<User> userList = new ArrayList<>();
        User user1 = new SystemAdmin("SA0001", "admin", "admin");
        User user2 = new PortManager("PM0001", "portmanager1", "portmanager1", "None");
        User user3 = new PortManager("PM0002", "portmanager2", "portmanager2", "None");
        User user4 = new PortManager("PM0003", "portmanager3", "portmanager3", "None");
        User user5 = new PortManager("PM0004", "portmanager4", "portmanager4", "None");
        User user6 = new PortManager("PM0005", "portmanager5", "portmanager5", "None");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
        FileOutputStream fos5 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos5 = new ObjectOutputStream(fos5);
        oos5.writeObject(userList);
        oos5.close();
        System.out.println("Initialize the database successfully!");
    }
}
