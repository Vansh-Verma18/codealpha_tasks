import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

enum RoomType {
    STANDARD(100.0, 2),
    DELUXE(200.0, 3),
    SUITE(350.0, 4),
    EXECUTIVE(500.0, 2);

    private final double basePrice;
    private final int maxOccupancy;

    RoomType(double basePrice, int maxOccupancy) {
        this.basePrice = basePrice;
        this.maxOccupancy = maxOccupancy;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }
}

class Room {
    private String roomNumber;
    private RoomType roomType;
    private boolean isAvailable;

    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = true;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class Reservation {
    private String reservationId;
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private double totalCost;
    private PaymentStatus paymentStatus;

    enum PaymentStatus {
        PENDING, PAID, CANCELLED
    }

    public Reservation(String guestName, Room room, LocalDate checkInDate, 
                       LocalDate checkOutDate, int numberOfGuests) {
        this.reservationId = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.paymentStatus = PaymentStatus.PENDING;
        calculateTotalCost();
    }

    private void calculateTotalCost() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double nightlyRate = room.getRoomType().getBasePrice();
        
        double additionalGuestFee = numberOfGuests > room.getRoomType().getMaxOccupancy() 
            ? (numberOfGuests - room.getRoomType().getMaxOccupancy()) * 50.0 
            : 0.0;
        
        this.totalCost = (nights * nightlyRate) + (nights * additionalGuestFee);
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void processPayment() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public String getDetailsString() {
        return String.format(
            "Reservation ID: %s\n" +
            "Guest: %s\n" +
            "Room: %s (%s)\n" +
            "Check-in: %s\n" +
            "Check-out: %s\n" +
            "Guests: %d\n" +
            "Total Cost: $%.2f\n" +
            "Payment Status: %s",
            reservationId, guestName, room.getRoomNumber(), 
            room.getRoomType(), checkInDate, checkOutDate, 
            numberOfGuests, totalCost, paymentStatus
        );
    }
}

class HotelReservationSystem {
    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;
    private Scanner scanner;

    public HotelReservationSystem() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeRooms();
    }

    private void initializeRooms() {
        String[] roomNumbers = {"101", "102", "103", "104", "201", "202", "203", "204", 
                                "301", "302", "303", "304", "401", "402", "403", "404"};
        RoomType[] types = {RoomType.STANDARD, RoomType.DELUXE, 
                            RoomType.SUITE, RoomType.EXECUTIVE};

        for (String roomNumber : roomNumbers) {
            RoomType type = types[(int)(Math.random() * types.length)];
            rooms.add(new Room(roomNumber, type));
        }
    }

    public void searchAvailableRooms() {
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Number of guests: ");
        int guests = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nAvailable Rooms:");
        boolean foundRooms = false;

        for (Room room : rooms) {
            if (isRoomAvailable(room, checkIn, checkOut) && 
                room.getRoomType().getMaxOccupancy() >= guests) {
                System.out.printf("Room %s - Type: %s, Price: $%.2f/night, Max Occupancy: %d\n", 
                    room.getRoomNumber(), 
                    room.getRoomType(), 
                    room.getRoomType().getBasePrice(),
                    room.getRoomType().getMaxOccupancy()
                );
                foundRooms = true;
            }
        }

        if (!foundRooms) {
            System.out.println("No rooms available matching your criteria.");
        }
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation reservation : reservations) {
            if (reservation.getDetailsString().contains(room.getRoomNumber())) {
                LocalDate existingCheckIn = LocalDate.parse(
                    reservation.getDetailsString().split("\n")[3].split(": ")[1]);
                LocalDate existingCheckOut = LocalDate.parse(
                    reservation.getDetailsString().split("\n")[4].split(": ")[1]);

                if (!(checkOut.isBefore(existingCheckIn) || checkIn.isAfter(existingCheckOut))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeReservation() {
        System.out.print("Enter your name: ");
        String guestName = scanner.nextLine();
        
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Number of guests: ");
        int guests = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = findRoom(roomNumber);
        
        if (selectedRoom == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!isRoomAvailable(selectedRoom, checkIn, checkOut)) {
            System.out.println("Room is not available for selected dates.");
            return;
        }

        Reservation reservation = new Reservation(guestName, selectedRoom, checkIn, checkOut, guests);
        
        System.out.println("\nReservation Details:");
        System.out.println(reservation.getDetailsString());
        
        System.out.print("\nConfirm reservation? (Y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            processPayment(reservation);
            reservations.add(reservation);
            System.out.println("Reservation confirmed!");
        } else {
            System.out.println("Reservation cancelled.");
        }
    }

    private void processPayment(Reservation reservation) {
        System.out.println("\n--- Payment Processing ---");
        System.out.printf("Total Amount Due: $%.2f\n", reservation.getTotalCost());
        System.out.println("Payment Methods:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. Cash");
        
        System.out.print("Select payment method: ");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine();

        reservation.processPayment();
        System.out.println("Payment Successful!");
    }

    private Room findRoom(String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("\n=== CURRENT RESERVATIONS ===");
        for (Reservation reservation : reservations) {
            System.out.println(reservation.getDetailsString());
            System.out.println("--------------------");
        }
    }

    public void run() {
        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    searchAvailableRooms();
                    break;
                case "2":
                    makeReservation();
                    break;
                case "3":
                    viewReservations();
                    break;
                case "4":
                    System.out.println("Exiting Hotel Reservation System...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        HotelReservationSystem reservationSystem = new HotelReservationSystem();
        reservationSystem.run();
    }
}
