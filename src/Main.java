import menu.MainMenu;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("CONTAINER PORT MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Dr. Phong Ngo");
        System.out.println("Group: WAO");
        System.out.println("s3974735, Tran Manh Cuong");
        System.out.println("s3965528, Truong Quang Bao Loc");
        System.out.println("s3979259, Truong Tuong Hao");

        // Start the program
        MainMenu mainMenu = new MainMenu();
        mainMenu.execute();
    }
}