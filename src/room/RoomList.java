package room;

import java.io.*;
import java.util.Scanner;

public class RoomList {
    Node head, tail;

    public RoomList() {
        this.head = this.tail = null;
    }

    // ========== BASIC OPERATIONS ==========
    
    public boolean isEmpty() {
        return (head == null);
    }

    public void clear() {
        head = tail = null;
    }
    
    // ========== 1.1. LOAD DATA FROM FILE ==========
    public void loadFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File " + filename + " does not exist. Creating new file...");
                file.createNewFile();
                return;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Room room = Room.parseFromFile(line);
                    if (room != null) {
                        addLast(room);
                        count++;
                    }
                }
            }
            br.close();
            System.out.println("Loaded " + count + " rooms from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    
    // ========== 1.2. INPUT & ADD TO THE END ==========
    public void inputAndAddToEnd(Scanner sc) {
        Room room = new Room();
        room.inputData(sc);
        
        // Check if rcode already exists
        if (searchByRcode(room.getRcode()) != null) {
            System.out.println("Error: Room code " + room.getRcode() + " already exists!");
            return;
        }
        
        addLast(room);
        System.out.println("Room added to the end successfully!");
    }
    
    // ========== 1.3. DISPLAY DATA ==========
    public void displayData() {
        if (isEmpty()) {
            System.out.println("Room list is empty!");
            return;
        }
        
        System.out.println("\n========== ROOM LIST ==========");
        System.out.println(String.format("%-10s | %-20s | %-15s | %-5s | %-8s | %-10s | %-10s | %-10s | %-10s", 
                          "RCode", "Name", "Dormitory", "Floor", "Type", "Beds", "Booked", "Available", "Price"));
        System.out.println("=".repeat(130));
        
        Node current = head;
        while (current != null) {
            System.out.println(current.info.toString());
            current = current.next;
        }
        System.out.println("=".repeat(130));
        System.out.println("Total rooms: " + count());
    }
    
    // ========== 1.4. SAVE ROOM LIST TO FILE ==========
    public void saveToFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            Node current = head;
            int count = 0;
            
            while (current != null) {
                bw.write(current.info.toFileString());
                bw.newLine();
                current = current.next;
                count++;
            }
            
            bw.close();
            System.out.println("Saved " + count + " rooms to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    
    // ========== 1.5. SEARCH BY RCODE ==========
    public Room searchByRcode(String rcode) {
        Node current = head;
        while (current != null) {
            if (current.info.getRcode().equalsIgnoreCase(rcode)) {
                return current.info;
            }
            current = current.next;
        }
        return null;
    }
    
    public void searchAndDisplayByRcode(Scanner sc) {
        System.out.print("Enter room code to search: ");
        String rcode = sc.nextLine().trim();
        
        Room room = searchByRcode(rcode);
        if (room != null) {
            System.out.println("\nRoom found:");
            room.displayInfo();
        } else {
            System.out.println("Room with code " + rcode + " not found!");
        }
    }
    
    // ========== 1.6. DELETE BY RCODE ==========
    // Note: Must check booked = 0 and delete related bookings first
    public boolean deleteByRcode(String rcode) {
        Node current = head;
        
        while (current != null) {
            if (current.info.getRcode().equalsIgnoreCase(rcode)) {
                // Check if room can be deleted (booked must be 0)
                if (!current.info.canDelete()) {
                    System.out.println("Cannot delete! Room still has " + current.info.getBooked() + " booked beds.");
                    System.out.println("Please make sure all students have left the room first.");
                    return false;
                }
                
                // Delete the node
                if (current == head) {
                    removeFirst();
                } else if (current == tail) {
                    removeLast();
                } else {
                    Node prev = getPrevious(current);
                    prev.next = current.next;
                    current.next = null;
                }
                
                System.out.println("Room " + rcode + " deleted successfully!");
                return true;
            }
            current = current.next;
        }
        
        System.out.println("Room with code " + rcode + " not found!");
        return false;
    }
    
    public void deleteByRcodeWithInput(Scanner sc) {
        System.out.print("Enter room code to delete: ");
        String rcode = sc.nextLine().trim();
        deleteByRcode(rcode);
    }
    
    // ========== 1.7. SORT BY RCODE ==========
    public void sortByRcode() {
        if (isEmpty() || head == tail) {
            return;
        }
        
        // Bubble sort
        Node i = head;
        while (i != null) {
            Node j = i.next;
            while (j != null) {
                if (i.info.getRcode().compareToIgnoreCase(j.info.getRcode()) > 0) {
                    swapRooms(i, j);
                }
                j = j.next;
            }
            i = i.next;
        }
        
        System.out.println("Room list sorted by rcode (ascending)!");
    }
    
    // ========== 1.8. INPUT & ADD TO BEGINNING ==========
    public void inputAndAddToBeginning(Scanner sc) {
        Room room = new Room();
        room.inputData(sc);
        
        // Check if rcode already exists
        if (searchByRcode(room.getRcode()) != null) {
            System.out.println("Error: Room code " + room.getRcode() + " already exists!");
            return;
        }
        
        addFirst(room);
        System.out.println("Room added to the beginning successfully!");
    }
    
    // ========== 1.9. ADD BEFORE POSITION K ==========
    public void addBeforePosition(Scanner sc) {
        Room room = new Room();
        room.inputData(sc);
        
        // Check if rcode already exists
        if (searchByRcode(room.getRcode()) != null) {
            System.out.println("Error: Room code " + room.getRcode() + " already exists!");
            return;
        }
        
        System.out.print("Enter position k to insert before (0-based index): ");
        try {
            int k = Integer.parseInt(sc.nextLine().trim());
            
            if (k < 0 || k > count()) {
                System.out.println("Invalid position! Position must be between 0 and " + count());
                return;
            }
            
            if (k == 0) {
                addFirst(room);
            } else {
                Node nodeAtK = getIndex(k);
                if (nodeAtK != null) {
                    insertBefore(room, nodeAtK);
                } else {
                    System.out.println("Invalid position!");
                    return;
                }
            }
            
            System.out.println("Room inserted at position " + k + " successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
    
    // ========== 1.10. DELETE POSITION K ==========
    public void deletePosition(Scanner sc) {
        if (isEmpty()) {
            System.out.println("Room list is empty!");
            return;
        }
        
        System.out.print("Enter position k to delete (0-based index): ");
        try {
            int k = Integer.parseInt(sc.nextLine().trim());
            
            if (k < 0 || k >= count()) {
                System.out.println("Invalid position! Position must be between 0 and " + (count() - 1));
                return;
            }
            
            Node nodeToDelete = getIndex(k);
            if (nodeToDelete != null) {
                // Check if room can be deleted
                if (!nodeToDelete.info.canDelete()) {
                    System.out.println("Cannot delete! Room still has " + nodeToDelete.info.getBooked() + " booked beds.");
                    return;
                }
                
                remove(nodeToDelete);
                System.out.println("Room at position " + k + " deleted successfully!");
            } else {
                System.out.println("Invalid position!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
    
    // ========== 1.11. SEARCH BY NAME ==========
    public void searchByName(Scanner sc) {
        System.out.print("Enter room name to search: ");
        String name = sc.nextLine().trim();
        
        Node current = head;
        boolean found = false;
        int count = 0;
        
        System.out.println("\nSearching for rooms with name containing: " + name);
        System.out.println("=".repeat(130));
        
        while (current != null) {
            if (current.info.getName().toLowerCase().contains(name.toLowerCase())) {
                if (!found) {
                    System.out.println(String.format("%-10s | %-20s | %-15s | %-5s | %-8s | %-10s | %-10s | %-10s | %-10s", 
                                      "RCode", "Name", "Dormitory", "Floor", "Type", "Beds", "Booked", "Available", "Price"));
                    System.out.println("=".repeat(130));
                    found = true;
                }
                System.out.println(current.info.toString());
                count++;
            }
            current = current.next;
        }
        
        if (!found) {
            System.out.println("No rooms found with name containing: " + name);
        } else {
            System.out.println("=".repeat(130));
            System.out.println("Total found: " + count + " room(s)");
        }
    }
    
    // ========== 1.12. SEARCH BOOKED BY RCODE ==========
    // This will be implemented when we have BookingList
    // For now, just display the room info
    public void searchBookedByRcode(Scanner sc) {
        System.out.print("Enter room code to search: ");
        String rcode = sc.nextLine().trim();
        
        Room room = searchByRcode(rcode);
        if (room != null) {
            System.out.println("\nRoom found:");
            room.displayInfo();
            
            if (room.getBooked() > 0) {
                System.out.println("\nThis room has " + room.getBooked() + " booked bed(s).");
                System.out.println("Students living in this room will be listed here (requires BookingList).");
                // TODO: List all students still living in this room from BookingList
            } else {
                System.out.println("\nNo students are currently living in this room.");
            }
        } else {
            System.out.println("Room with code " + rcode + " not found!");
        }
    }
    
    // ========== HELPER METHODS ==========
    
    private void addFirst(Room x) {
        Node newNode = new Node(x, null);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }
    
    private void addLast(Room x) {
        Node newNode = new Node(x, null);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }
    
    private void removeFirst() {
        if (isEmpty()) {
            System.out.println("List is empty");
        } else if (head == tail) {
            clear();
        } else {
            Node temp = head;
            head = head.next;
            temp.next = null;
        }
    }
    
    private void removeLast() {
        if (isEmpty()) {
            System.out.println("List is empty");
        } else if (head == tail) {
            clear();
        } else {
            Node current = head;
            while (current.next != tail) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
    }
    
    private void remove(Node p) {
        if (isEmpty()) {
            System.out.println("List is empty");
        } else if (p == head) {
            removeFirst();
        } else if (p == tail) {
            removeLast();
        } else {
            Node prev = getPrevious(p);
            if (prev != null) {
                prev.next = p.next;
                p.next = null;
            }
        }
    }
    
    private Node getPrevious(Node p) {
        Node current = head;
        Node prev = null;
        while (current != null && current != p) {
            prev = current;
            current = current.next;
        }
        return prev;
    }
    
    private Node getIndex(int index) {
        Node current = head;
        int cnt = 0;
        while (current != null && cnt < index) {
            cnt++;
            current = current.next;
        }
        return current;
    }
    
    private void insertBefore(Room x, Node y) {
        if (y == head) {
            addFirst(x);
        } else {
            Node prev = getPrevious(y);
            if (prev != null) {
                Node newNode = new Node(x, y);
                prev.next = newNode;
            }
        }
    }
    
    private void swapRooms(Node p, Node q) {
        Room temp = p.info;
        p.info = q.info;
        q.info = temp;
    }
    
    public int count() {
        int cnt = 0;
        Node current = head;
        while (current != null) {
            cnt++;
            current = current.next;
        }
        return cnt;
    }
}