package menu;

import manage.*;
import port.Trip;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemAdminMenu implements SystemAdminMenuInterface{
    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String option;
        do {
            System.out.println("---------- System Admin Menu ----------");
            System.out.println("1. Manage Ports");
            System.out.println("2. Manage Vehicles");
            System.out.println("3. Manage Containers");
            System.out.println("4. Manage Port Managers");
            System.out.println("5. Calculate How Much Fuel Has Been Use In A Given Day");
            System.out.println("6. View All Trips");
            System.out.println("7. View All Trips In A Given Day");
            System.out.println("8. View All Trip From Day A To Day B");
            System.out.println("0. Go Back To Login Menu");
            System.out.print("Choose an action (0-8): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> managePorts();
                case "2" -> manageVehicles();
                case "3" -> manageContainers();
                case "4" -> managePortManagers();
                case "5" -> calculateFuelInGivenDay();
                case "6" -> viewAllTrips();
                case "7" -> viewTripsInGivenDay();
                case "8" -> viewTripsFromDayAToDayB();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 8!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void managePorts() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManagePort managePort = new ManagePort();
        String option;
        do {
            System.out.println("-------------------- Manage Ports --------------------");
            System.out.println("1. View All Ports");
            System.out.println("2. Add New Port");
            System.out.println("3. Update Port's Information");
            System.out.println("4. Remove Port");
            System.out.println("5. Assign A Port Manager To A Port");
            System.out.println("6. View All Containers In A Port");
            System.out.println("7. Add A Container To A Port");
            System.out.println("8. Remove A Container From A Port");
            System.out.println("9. View All Vehicles In A Port");
            System.out.println("10. View All Ships In A Port");
            System.out.println("11. View All Trucks In A Port");
            System.out.println("12. Add A Vehicle To A Port");
            System.out.println("13. Remove A Vehicle From A Port");
            System.out.println("14. Load A Container To A Vehicle In A Port");
            System.out.println("15. Unload A Container From A Vehicle In A Port");
            System.out.println("16. Move A Vehicle From A Port To Another Port");
            System.out.println("17. Calculate The Distance Between 2 Ports");
            System.out.println("18. View A Port's Trip History");
            System.out.println("0. Go Back To System Admin Menu");
            System.out.print("Choose an action (0-18): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> managePort.viewPorts();
                case "2" -> managePort.addPort();
                case "3" -> managePort.updatePort();
                case "4" -> managePort.removePort();
                case "5" -> managePort.assignPortManagerToPort();
                case "6" -> managePort.viewPortContainers();
                case "7" -> managePort.addContainerToPort();
                case "8" -> managePort.removeContainerFromPort();
                case "9" -> managePort.viewPortVehicles();
                case "10" -> managePort.viewPortShips();
                case "11" -> managePort.viewPortTrucks();
                case "12" -> managePort.addVehicleToPort();
                case "13" -> managePort.removeVehicleFromPort();
                case "14" -> managePort.loadPortContainer();
                case "15" -> managePort.unloadPortContainer();
                case "16" -> managePort.movePortVehicle();
                case "17" -> managePort.calculateDistance();
                case "18" -> managePort.viewPortTripHistory();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 18!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void manageVehicles() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManagePort managePort = new ManagePort();
        ManageVehicle manageVehicle = new ManageVehicle();
        String option;
        do {
            System.out.println("-------------------- Manage Vehicles --------------------");
            System.out.println("1. View All Vehicles");
            System.out.println("2. View All Ships");
            System.out.println("3. View All Trucks");
            System.out.println("4. Add New Vehicle");
            System.out.println("5. Update Vehicle's Information");
            System.out.println("6. Remove Vehicle");
            System.out.println("7. Add A Vehicle To A Port");
            System.out.println("8. Remove A Vehicle From A Port");
            System.out.println("9. Refuel A Vehicle");
            System.out.println("10. Load A Container To A Vehicle In A Port");
            System.out.println("11. Unload A Container From A Vehicle In A Port");
            System.out.println("12. Move A Vehicle From A Port To Another Port");
            System.out.println("0. Go Back To System Admin Menu");
            System.out.print("Choose an action (0-12): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> manageVehicle.viewVehicles();
                case "2" -> manageVehicle.viewShips();
                case "3" -> manageVehicle.viewTrucks();
                case "4" -> manageVehicle.addVehicle();
                case "5" -> manageVehicle.updateVehicle();
                case "6" -> manageVehicle.removeVehicle();
                case "7" -> managePort.addVehicleToPort();
                case "8" -> managePort.removeVehicleFromPort();
                case "9" -> manageVehicle.refuelVehicle();
                case "10" -> managePort.loadPortContainer();
                case "11" -> managePort.unloadPortContainer();
                case "12" -> managePort.movePortVehicle();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 12!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void manageContainers() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManagePort managePort = new ManagePort();
        ManageContainer manageContainer = new ManageContainer();
        String option;
        do {
            System.out.println("-------------------- Manage Containers --------------------");
            System.out.println("1. View All Containers");
            System.out.println("2. Add New Container");
            System.out.println("3. Remove Container");
            System.out.println("4. Add A Container To A Port");
            System.out.println("5. Remove A Container From A Port");
            System.out.println("6. Calculate Total Weight Of Each Type Of All Containers");
            System.out.println("7. Load A Container To A Vehicle In A Port");
            System.out.println("8. Unload A Container From A Vehicle In A Port");
            System.out.println("0. Go Back To System Admin Menu");
            System.out.print("Choose an action (0-8): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> manageContainer.viewContainers();
                case "2" -> manageContainer.addContainer();
                case "3" -> manageContainer.removeContainer();
                case "4" -> managePort.addContainerToPort();
                case "5" -> managePort.removeContainerFromPort();
                case "6" -> manageContainer.calculateTotalWeightOfEachType();
                case "7" -> managePort.loadPortContainer();
                case "8" -> managePort.unloadPortContainer();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 8!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void managePortManagers() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManagePort managePort = new ManagePort();
        ManageUser manageUser = new ManageUser();
        String option;
        do {
            System.out.println("-------------------- Manage Port Managers --------------------");
            System.out.println("1. View All Port Managers");
            System.out.println("2. Add New Port Manager");
            System.out.println("3. Update Port Manager's Information");
            System.out.println("4. Remove Port Manager");
            System.out.println("5. Assign A Port Manager To A Port");
            System.out.println("0. Go Back To System Admin Menu");
            System.out.print("Choose an action (0-5): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> manageUser.viewPortManagers();
                case "2" -> manageUser.addPortManager();
                case "3" -> manageUser.updatePortManager();
                case "4" -> manageUser.removePortManager();
                case "5" -> managePort.assignPortManagerToPort();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter an integer number from 0 to 5!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void calculateFuelInGivenDay() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Trip> tripList = manageDatabase.getTripDatabase();

        double totalFuel = 0;
        LocalDate givenDate;
        while (true) {
            System.out.print("Enter a date that you want to calculate fuel (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                givenDate = LocalDate.parse(date, dateTimeFormatter);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }
        for (Trip trip : tripList) {
            if (!((trip.getDepartureDate().isAfter(givenDate) && trip.getArrivalDate().isAfter(givenDate)) || (trip.getDepartureDate().isBefore(givenDate) && trip.getArrivalDate().isBefore(givenDate)))) {
                long days = ChronoUnit.DAYS.between(trip.getDepartureDate(), trip.getArrivalDate());
                totalFuel += trip.getFuelConsuming() / days;
            }
        }
        totalFuel = (double) Math.round(totalFuel * 100) / 100;
        System.out.println("Total fuel has been used in " + givenDate + " is " + totalFuel + " gallons.");
        System.out.println();
    }

    @Override
    public void viewAllTrips() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Trip> tripList = manageDatabase.getTripDatabase();

        if (!tripList.isEmpty()) {
            System.out.println("---------------------------------------------------------------- All Trips -------------------------------------------------------------");
            System.out.printf("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", "Trip ID", "Vehicle", "Departure Port" , "Arrival Port", "Departure Date", "Arrival Date", "Status");
            System.out.println();
            for (Trip trip : tripList) {
                System.out.println(trip);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
        } else {
            System.out.println("There is no trip in the database");
        }
    }

    @Override
    public void viewTripsInGivenDay() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Trip> tripList = manageDatabase.getTripDatabase();

        LocalDate givenDate;
        while (true) {
            System.out.print("Enter a date that you want to view trips in that day (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                givenDate = LocalDate.parse(date, dateTimeFormatter);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }

        List<Trip> givenDayTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if ((trip.getDepartureDate().isBefore(givenDate) && trip.getArrivalDate().isAfter(givenDate)) || trip.getArrivalDate().isEqual(givenDate) || trip.getDepartureDate().isEqual(givenDate)) {
                givenDayTripList.add(trip);
            }
        }

        if (!givenDayTripList.isEmpty()) {
            System.out.println("---------------------------------------------------------------- Trip List -------------------------------------------------------------");
            System.out.printf("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", "Trip ID", "Vehicle", "Departure Port" , "Arrival Port", "Departure Date", "Arrival Date", "Status");
            System.out.println();
            for (Trip trip : givenDayTripList) {
                System.out.println(trip);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = givenDate.format(dateFormatter);
            System.out.println("There is no trip in " + formattedDate + "!");
        }
        System.out.println();
    }

    @Override
    public void viewTripsFromDayAToDayB() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<Trip> tripList = manageDatabase.getTripDatabase();

        LocalDate givenDateA;
        while (true) {
            System.out.print("Enter date A (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                givenDateA = LocalDate.parse(date, dateTimeFormatter);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }

        LocalDate givenDateB;
        while (true) {
            System.out.print("Enter date B (ex: 01/01/2023): ");
            String date = scanner.nextLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                givenDateB = LocalDate.parse(date, dateTimeFormatter);
                boolean isValidArrivalDate = givenDateB.isAfter(givenDateA);
                if (isValidArrivalDate) {
                    break;
                } else {
                    System.out.println("You should enter the arrival date after the departure date!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a date in the correct format (dd/MM/yyyy).");
            }
        }

        List<Trip> tripListFromDayAToDayB = new ArrayList<>();
        for (Trip trip : tripList) {
            if ((trip.getDepartureDate().isAfter(givenDateA) && trip.getArrivalDate().isBefore(givenDateB)) || (trip.getDepartureDate().isAfter(givenDateA) && trip.getArrivalDate().isEqual(givenDateB)) || (trip.getDepartureDate().isEqual(givenDateA) && trip.getArrivalDate().isBefore(givenDateB))) {
                tripListFromDayAToDayB.add(trip);
            }
        }

        if (!tripListFromDayAToDayB.isEmpty()) {
            System.out.println("---------------------------------------------------------------- Trip List -------------------------------------------------------------");
            System.out.printf("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", "Trip ID", "Vehicle", "Departure Port" , "Arrival Port", "Departure Date", "Arrival Date", "Status");
            System.out.println();
            for (Trip trip : tripListFromDayAToDayB) {
                System.out.println(trip);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDateA = givenDateA.format(dateFormatter);
            String formattedDateB = givenDateB.format(dateFormatter);
            System.out.println("There is no trip between " + formattedDateA + " and " + formattedDateB + "!");
        }
        System.out.println();
    }
}
