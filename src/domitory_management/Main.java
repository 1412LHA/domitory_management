/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package domitory_management;

import java.util.Scanner;

import room.*;

/**
 *
 * @author ASUS
 */
public class Main {

    /**
     * @param args the command line arguments
     */
        // TODO code application logic here
        private static RoomList roomList = new RoomList();
    // TODO: Thêm StudentList và BookingList khi các thành viên khác hoàn thành
    // private static StudentList studentList = new StudentList();
    // private static BookingList bookingList = new BookingList();
    
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();
            
            switch (choice) {
                case "1":
                    roomMenu();
                    break;
                case "2":
                    // studentMenu(); // TODO: Uncomment khi StudentList xong
                    System.out.println("Student menu - Coming soon!");
                    break;
                case "3":
                    // bookingMenu(); // TODO: Uncomment khi BookingList xong
                    System.out.println("Booking menu - Coming soon!");
                    break;
                case "0":
                    System.out.println("Thank you for using Dormitory Management System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n=========================================");
        System.out.println("  DORMITORY MANAGEMENT SYSTEM - MAIN MENU  ");
        System.out.println("=========================================");
        System.out.println("1. Room Management");
        System.out.println("2. Student Management");
        System.out.println("3. Booking Management");
        System.out.println("0. Exit");
        System.out.println("=========================================");
    }
    
    private static void roomMenu() {
        while (true) {
            displayRoomMenu();
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();
            
            switch (choice) {
                case "1.1":
                    roomList.loadFromFile("rooms.txt");
                    break;
                case "1.2":
                    roomList.inputAndAddToEnd(sc);
                    break;
                case "1.3":
                    roomList.displayData();
                    break;
                case "1.4":
                    roomList.saveToFile("rooms.txt");
                    break;
                case "1.5":
                    roomList.searchAndDisplayByRcode(sc);
                    break;
                case "1.6":
                    roomList.deleteByRcodeWithInput(sc);
                    break;
                case "1.7":
                    roomList.sortByRcode();
                    roomList.displayData();
                    break;
                case "1.8":
                    roomList.inputAndAddToBeginning(sc);
                    break;
                case "1.9":
                    roomList.addBeforePosition(sc);
                    break;
                case "1.10":
                    roomList.deletePosition(sc);
                    break;
                case "1.11":
                    roomList.searchByName(sc);
                    break;
                case "1.12":
                    roomList.searchBookedByRcode(sc);
                    break;
                case "0":
                    return; // Back to main menu
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
        }
    }
    
    private static void displayRoomMenu() {
        System.out.println("\n=========================================");
        System.out.println("         ROOM MANAGEMENT MENU              ");
        System.out.println("=========================================");
        System.out.println("1.1.  Load data from file");
        System.out.println("1.2.  Input & add to the end");
        System.out.println("1.3.  Display data");
        System.out.println("1.4.  Save room list to file");
        System.out.println("1.5.  Search by rcode");
        System.out.println("1.6.  Delete by rcode");
        System.out.println("1.7.  Sort by rcode");
        System.out.println("1.8.  Input & add to beginning");
        System.out.println("1.9.  Add before position k");
        System.out.println("1.10. Delete position k");
        System.out.println("1.11. Search by name");
        System.out.println("1.12. Search booked by rcode");
        System.out.println("0.    Back to main menu");
        System.out.println("=========================================");
    }
    
}
