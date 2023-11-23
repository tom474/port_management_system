package menu;

import manage.ManageDatabase;
import user.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainMenu implements MainMenuInterface{
    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String option;
        do {
            System.out.println("---------- Main Menu ----------");
            System.out.println("1. Login");
            System.out.println("0. Exit");
            System.out.print("Choose an action (0-1): ");
            option = scanner.nextLine();
            switch (option) {
                case "1" -> login();
                case "0" -> System.out.println();
                default -> System.out.println("Invalid input! You should enter a number from 0 to 1!");
            }
        } while (!option.equals("0"));
    }

    @Override
    public void login() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ManageDatabase manageDatabase = new ManageDatabase();

        // Get the database
        List<User> userList = manageDatabase.getUserDatabase();

        User loginUser = null;
        String username;
        String password;
        boolean isExistingUsername = false;
        boolean isValidPassword = false;
        while (true) {
            System.out.print("Enter username (ex. portmanager1): ");
            username = scanner.nextLine();
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    isExistingUsername = true;
                    break;
                }
            }
            if (isExistingUsername) {
                while (true) {
                    System.out.print("Enter password (ex. portmanager1): ");
                    password = scanner.nextLine();
                    for (User user : userList) {
                        if (user.getPassword().equals(password)) {
                            isValidPassword = true;
                            break;
                        }
                    }
                    if (isValidPassword) {
                        for (User user : userList) {
                            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                loginUser = user;
                            }
                        }
                        assert loginUser != null;
                        if (loginUser.getRole().equals("System Admin")) {
                            System.out.println("You are login as the System Admin! You can access and process all the system's information.");
                            SystemAdminMenu systemAdminMenu = new SystemAdminMenu();
                            systemAdminMenu.execute();
                        } else {
                            if (!loginUser.getPort().equals("None")) {
                                System.out.println("You are login as the " + loginUser.getPort() + " Manager! You can only process the port that you are in charge.");
                                PortManagerMenu portManagerMenu = new PortManagerMenu();
                                portManagerMenu.execute(loginUser);
                            } else {
                                System.out.println("You are login as the Port Manager but you haven't been assign to any port yet!");
                                System.out.println("Please ask the System Admin to assign you to manage a port and try again.");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Invalid password! Please enter another password.");
                    }
                }
                break;
            } else {
                System.out.println("The username you entered does not exist! Please enter another username.");
            }
        }
    }
}
