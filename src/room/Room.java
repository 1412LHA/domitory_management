package room;

import java.util.Scanner;

public class Room {
    private String rcode;
    private String name;
    private String dom;
    private String floor;
    private String type;
    private int beds;
    private int booked;
    private double price;
    
    // Constructor mặc định - CẦN cho inputData()
    public Room() {
    }
    
    // Constructor với validation - dùng khi load từ file hoặc tạo trực tiếp
    public Room(String rcode, String name, String dom, String floor, String type, int beds, int booked, double price) {
        // Validate type
        if (!type.equalsIgnoreCase("double") && !type.equalsIgnoreCase("triple")) {
            throw new IllegalArgumentException("Type must be 'double' or 'triple'");
        }
        
        // Validate beds theo type
        if ((type.equalsIgnoreCase("double") && beds != 4) || 
            (type.equalsIgnoreCase("triple") && beds != 6)) {
            throw new IllegalArgumentException("Beds must be 4 for double room or 6 for triple room");
        }
        
        // Validate booked
        if (booked < 0 || booked > beds) {
            throw new IllegalArgumentException("Booked must be between 0 and " + beds);
        }
        
        // Validate price
        if (price < 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        
        this.rcode = rcode;
        this.name = name;
        this.dom = dom;
        this.floor = floor;
        this.type = type;
        this.beds = beds;
        this.booked = booked;
        this.price = price;
    }
    
    // ========== GETTERS ONLY ==========
    public String getRcode() {
        return rcode;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDom() {
        return dom;
    }
    
    public String getFloor() {
        return floor;
    }
    
    public String getType() {
        return type;
    }
    
    public int getBeds() {
        return beds;
    }
    
    public int getBooked() {
        return booked;
    }
    
    public double getPrice() {
        return price;
    }
    
    // ========== INPUT FROM KEYBOARD - YÊU CẦU CỦA ĐỀ BÀI ==========
    public void inputData(Scanner sc) {
        System.out.println("===== INPUT ROOM DATA =====");
        
        // Input rcode
        System.out.print("Enter room code: ");
        this.rcode = sc.nextLine().trim();
        
        // Input name
        System.out.print("Enter room name: ");
        this.name = sc.nextLine().trim();
        
        // Input dom
        System.out.print("Enter dormitory: ");
        this.dom = sc.nextLine().trim();
        
        // Input floor
        System.out.print("Enter floor: ");
        this.floor = sc.nextLine().trim();
        
        // Input type với validation
        while (true) {
            System.out.print("Enter room type (double/triple): ");
            String inputType = sc.nextLine().trim();
            if (inputType.equalsIgnoreCase("double")) {
                this.type = "double";
                this.beds = 4;
                break;
            } else if (inputType.equalsIgnoreCase("triple")) {
                this.type = "triple";
                this.beds = 6;
                break;
            } else {
                System.out.println("Invalid! Type must be 'double' or 'triple'. Try again.");
            }
        }
        
        // Input booked với validation
        while (true) {
            try {
                System.out.print("Enter number of booked beds (0-" + this.beds + "): ");
                int inputBooked = Integer.parseInt(sc.nextLine().trim());
                if (inputBooked >= 0 && inputBooked <= this.beds) {
                    this.booked = inputBooked;
                    break;
                } else {
                    System.out.println("Invalid! Booked must be between 0 and " + this.beds + ". Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }
        
        // Input price với validation
        while (true) {
            try {
                System.out.print("Enter price per bed: ");
                double inputPrice = Double.parseDouble(sc.nextLine().trim());
                if (inputPrice >= 0) {
                    this.price = inputPrice;
                    break;
                } else {
                    System.out.println("Invalid! Price must be positive. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a valid number.");
            }
        }
        
        System.out.println("Room data input successfully!");
    }
    
    // ========== PARSE FROM FILE - CẦN CHO loadFromFile() ==========
    // Format: rcode|name|dom|floor|type|beds|booked|price
    public static Room parseFromFile(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length != 8) {
                throw new IllegalArgumentException("Invalid file format");
            }
            
            String rcode = parts[0].trim();
            String name = parts[1].trim();
            String dom = parts[2].trim();
            String floor = parts[3].trim();
            String type = parts[4].trim();
            int beds = Integer.parseInt(parts[5].trim());
            int booked = Integer.parseInt(parts[6].trim());
            double price = Double.parseDouble(parts[7].trim());
            
            return new Room(rcode, name, dom, floor, type, beds, booked, price);
        } catch (Exception e) {
            System.out.println("Error parsing room data: " + e.getMessage());
            return null;
        }
    }
    
    // ========== TO FILE STRING - CẦN CHO saveToFile() ==========
    public String toFileString() {
        return rcode + "|" + name + "|" + dom + "|" + floor + "|" + type + "|" + beds + "|" + booked + "|" + price;
    }
    
    // ========== BOOKING MANAGEMENT ==========
    
    public boolean hasAvailableBeds() {
        return (beds - booked) > 0;
    }
    
    public int getAvailableBeds() {
        return beds - booked;
    }
    
    // Dùng khi booking (3.2)
    public boolean bookBed() {
        if (hasAvailableBeds()) {
            booked++;
            return true;
        }
        return false;
    }
    
    // Dùng khi leave room (3.6)
    public boolean releaseBed() {
        if (booked > 0) {
            booked--;
            return true;
        }
        return false;
    }
    
    // Kiểm tra có thể xóa không (1.6)
    public boolean canDelete() {
        return booked == 0;
    }
    
    // ========== DISPLAY ==========
    
    @Override
    public String toString() {
        return String.format("%-10s | %-20s | %-15s | %-5s | %-8s | %-4d | %-6d | %-9d | %.2f", 
                            rcode, name, dom, floor, type, beds, booked, getAvailableBeds(), price);
    }
    
    public void displayInfo() {
        System.out.println("===== ROOM INFORMATION =====");
        System.out.println("Room Code    : " + rcode);
        System.out.println("Room Name    : " + name);
        System.out.println("Dormitory    : " + dom);
        System.out.println("Floor        : " + floor);
        System.out.println("Type         : " + type);
        System.out.println("Total Beds   : " + beds);
        System.out.println("Booked Beds  : " + booked);
        System.out.println("Available    : " + getAvailableBeds());
        System.out.println("Price/Bed    : " + price);
        System.out.println("============================");
    }
}