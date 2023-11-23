package manage;

import port.Port;
import user.PortManager;
import user.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ManageUser implements ManageUserInterface{
    private final File portDatabase = new File("src/model/port.dat");
    private final File userDatabase = new File("src/model/user.dat");

    @Override
    public void viewPortManagers() throws IOException, ClassNotFoundException {
        ManageDatabase manageDatabase = new ManageDatabase();
        List<User> userList = manageDatabase.getUserDatabase();
        List<User> portManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getRole().equals("Port Manager")) {
                portManagerList.add(user);
            }
        }
        if (!portManagerList.isEmpty()) {
            System.out.println("------------------------------------ All Port Managers -----------------------------------");
            System.out.printf("| %-8s | %-15s | %-16s | %-15s | %-20s |", "User ID" , "Role", "Username", "Password", "Manage Port");
            System.out.println();
            for (User user : portManagerList) {
                System.out.println(user);
            }
            System.out.println("------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There is no port manager in the database!");
        }
        System.out.println();
    }

    @Override
    public void addPortManager() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<User> userList = manageDatabase.getUserDatabase();
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Port> availablePortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortManager().equals("None")) {
                availablePortList.add(port);
            }
        }

        // Generate random and unique port manager ID
        String portManagerID = null;
        boolean isExistID = true;
        while (isExistID) {
            portManagerID = "PM" + random.nextInt(1000, 9999);
            for (User user : userList) {
                isExistID = portManagerID.equals(user.getUserID());
            }
        }

        // Prompt user
        boolean isExistUsername = true;
        String username = null;
        while (isExistUsername) {
            System.out.print("Enter the port manager's username (ex. portmanager1): ");
            username = scanner.nextLine();
            for (User user : userList) {
                isExistUsername = username.equals(user.getUsername());
                if (isExistUsername) {
                    System.out.println("The username is already exist. Please enter another username");
                    break;
                }
            }
        }
        System.out.print("Enter the port manager's password (ex. portmanager1): ");
        String password = scanner.nextLine();
        String isAssign;
        String portID = null;
        String portName = null;
        while (true) {
            System.out.print("Do you want to assign this port manager to a port? (yes/no): ");
            isAssign = scanner.nextLine();
            if (isAssign.equals("yes")) {
                if (!availablePortList.isEmpty()) {
                    System.out.println("------------------------------------------------------------------- Available Ports ------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
                    System.out.println();
                    for (Port port : availablePortList) {
                        System.out.println(port);
                    }
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                    while (true) {
                        System.out.print("Please enter a Port's ID from the above list (ex. P0001): ");
                        portID = scanner.nextLine();
                        for (Port port : availablePortList) {
                            if (port.getPortID().equals(portID)) {
                                portName = port.getName();
                            }
                        }
                        if (portName != null) {
                            break;
                        } else {
                            System.out.println("This port's id does not exist! Please enter another port's id.");
                        }
                    }

                } else {
                    System.out.println("There is no available port! You can add a new port and assign this port manager later.");
                    portName = "None";
                }
                break;
            } else if (isAssign.equals("no")) {
                portName = "None";
                break;
            } else {
                System.out.println("Invalid input! You should enter 'yes' or 'no'.");
            }
        }

        // Update user database
        User user = new PortManager(portManagerID, username, password, portName);
        userList.add(user);
        FileOutputStream fos1 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(userList);
        oos1.close();

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortID().equals(portID)) {
                port.setPortManager(portManagerID);
            }
            newPortList.add(port);
        }
        FileOutputStream fos2 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newPortList);
        oos2.close();
        System.out.println("Add new port manager successfully!");
        System.out.println();
    }

    @Override
    public void updatePortManager() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<User> userList = manageDatabase.getUserDatabase();
        List<User> portManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getRole().equals("Port Manager")) {
                portManagerList.add(user);
            }
        }
        List<Port> portList = manageDatabase.getPortDatabase();
        List<Port> availablePortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortManager().equals("None")) {
                availablePortList.add(port);
            }
        }

        // Prompt User
        User oldPortManager = null;
        User updatingPortManager = null;
        String updatingPortManagerID;
        while (true) {
            viewPortManagers();
            System.out.print("Enter the Port Manager's ID that you want to update (ex. PM0001): ");
            updatingPortManagerID = scanner.nextLine();
            for (User user : portManagerList) {
                if (user.getUserID().equals(updatingPortManagerID)) {
                    oldPortManager = user;
                    updatingPortManager = user;
                }
            }
            if (updatingPortManager != null) {
                break;
            } else {
                System.out.println("This port manager's id does not exist! Please enter another port's id.");
            }
        }
        String updatingUsername;
        while (true) {
            System.out.print("Do you want to update the port manager's username? (yes, no): ");
            String isChangingUsername = scanner.nextLine();
            if (isChangingUsername.equals("yes")) {
                System.out.print("Enter the new username (ex. portmanager1): ");
                updatingUsername = scanner.nextLine();
                updatingPortManager.setUsername(updatingUsername);
                break;
            } else if (isChangingUsername.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }
        String updatingPassword;
        while (true) {
            System.out.print("Do you want to update the port manager's password? (yes, no): ");
            String isChangingPassword = scanner.nextLine();
            if (isChangingPassword.equals("yes")) {
                System.out.print("Enter the new password (ex. portmanager1): ");
                updatingPassword = scanner.nextLine();
                updatingPortManager.setPassword(updatingPassword);
                break;
            } else if (isChangingPassword.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }
        String newPortName = null;
        while (true) {
            System.out.print("Do you want to update this port manager's assigned port? (yes, no): ");
            String isChangingPort = scanner.nextLine();
            if (isChangingPort.equals("yes")) {
                if (!updatingPortManager.getPort().equals("None")){
                    while (true) {
                        System.out.println("This port manager has already manage a port.");
                        System.out.print("Do you want to remove or change this port manager's assigned port? (remove/change): ");
                        String updateAction = scanner.nextLine();
                        if (updateAction.equals("remove")) {
                            newPortName = "None";
                            updatingPortManager.setPort("None");
                            break;
                        } else if (updateAction.equals("change")) {
                            System.out.println("------------------------------------------------------------------- Available Ports ------------------------------------------------------------------");
                            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
                            System.out.println();
                            for (Port port : availablePortList) {
                                System.out.println(port);
                            }
                            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                            while (true) {
                                System.out.print("Please enter a Port 's ID from the above list (ex. P0001): ");
                                String newPortID = scanner.nextLine();
                                for (Port port : availablePortList) {
                                    if (port.getPortID().equals(newPortID)) {
                                        newPortName = port.getName();
                                    }
                                }
                                if (newPortName != null) {
                                    updatingPortManager.setPort(newPortName);
                                    break;
                                } else {
                                    System.out.println("This port's id does not exist! Please enter another port's id.");
                                }
                            }
                            break;
                        } else {
                            System.out.println("Invalid input! Please enter 'remove' or 'change'!");
                        }
                    }
                } else {
                    while (true) {
                        System.out.println("This port manager hasn't manage any port.");
                        System.out.print("Do you want to assign this port manager to a port? (yes, no): ");
                        String isChangingName = scanner.nextLine();
                        if (isChangingName.equals("yes")) {
                            System.out.println("------------------------------------------------------------------- Available Ports ------------------------------------------------------------------");
                            System.out.printf("| %-10s | %-20s | %-20s | %10s | %10s | %15s | %-10s | %10s | %8s | %6s |", "Port ID", "Name", "Port Manager" , "Latitude", "Longitude", "Capacity" , "isLanding", "Containers", "Vehicles", "Trips");
                            System.out.println();
                            for (Port port : availablePortList) {
                                System.out.println(port);
                            }
                            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                            while (true) {
                                System.out.print("Please enter a Port 's ID from the above list (ex. P0001): ");
                                String newPortID = scanner.nextLine();
                                for (Port port : availablePortList) {
                                    if (port.getPortID().equals(newPortID)) {
                                        newPortName = port.getName();
                                    }
                                }
                                if (newPortName != null) {
                                    updatingPortManager.setPort(newPortName);
                                    break;
                                } else {
                                    System.out.println("This port's id does not exist! Please enter another port's id.");
                                }
                            }
                            break;
                        } else if (isChangingName.equals("no")) {
                            break;
                        } else {
                            System.out.println("Invalid input! Please enter yes or no!");
                        }
                    }
                }
            } else if (isChangingPort.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input! Please enter yes or no!");
            }
        }

        // Update user database
        List<User> newUserList = new ArrayList<>();
        for (User user : portManagerList) {
            if (user.getUserID().equals(updatingPortManagerID)) {
                user = updatingPortManager;
            }
            newUserList.add(user);
        }
        FileOutputStream fos1 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(newUserList);
        oos1.close();

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        if (!oldPortManager.getPort().equals("None")) {
            for (Port port : portList) {
                if (port.getName().equals(oldPortManager.getPort())) {
                    port.setPortManager("None");
                }
                if (port.getName().equals(updatingPortManager.getPort())) {
                    port.setPortManager(updatingPortManager.getUserID());
                }
                newPortList.add(port);
            }
        } else {
            for (Port port : portList) {
                if (port.getName().equals(updatingPortManager.getPort())) {
                    port.setPortID(updatingPortManager.getUserID());
                }
                newPortList.add(port);
            }
        }
        FileOutputStream fos2 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newPortList);
        oos2.close();
        System.out.println("Update port manager " + updatingPortManagerID + "'s information successfully!");
        System.out.println();
    }

    @Override
    public void removePortManager() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<User> userList = manageDatabase.getUserDatabase();
        List<User> portManagerList = new ArrayList<>();
        for (User user : userList) {
            if (user.getRole().equals("Port Manager")) {
                portManagerList.add(user);
            }
        }
        List<Port> portList = manageDatabase.getPortDatabase();

        User removingPortManager = null;
        String removingPortManagerID;
        while (true) {
            viewPortManagers();
            System.out.print("Enter the port manager's id that you want to remove(ex. PM0001): ");
            removingPortManagerID = scanner.nextLine();
            for (User user : portManagerList) {
                if (user.getUserID().equals(removingPortManagerID)) {
                    removingPortManager = user;
                }
            }
            if (removingPortManager != null) {
                break;
            } else {
                System.out.println("This port manager's id does not exist! Please enter another port manager's id.");
            }
        }

        // Update user database
        userList.remove(removingPortManager);
        FileOutputStream fos1 = new FileOutputStream(userDatabase);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(userList);
        oos1.close();

        // Update port database
        List<Port> newPortList = new ArrayList<>();
        for (Port port : portList) {
            if (port.getPortManager().equals(removingPortManagerID)) {
                port.setPortManager("None");
            }
            newPortList.add(port);
        }
        FileOutputStream fos2 = new FileOutputStream(portDatabase);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(newPortList);
        oos2.close();
        System.out.println("Remove port " + removingPortManagerID + " successfully!");
        System.out.println();
    }
}
