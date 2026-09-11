import java.util.Scanner;

class Room {

    // Private data members - Encapsulation
    private int roomNumber;
    private String roomType;
    private String customerName;
    private int numberOfDays;
    private double pricePerDay;
    private boolean bookingStatus;

    // Static variable to count total bookings
    private static int totalBookings = 0;

    // Parameterized constructor using this keyword
    public Room(int roomNumber, String roomType, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.customerName = "";
        this.numberOfDays = 0;
        this.bookingStatus = false;
    }

    // Getter and Setter methods

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(boolean bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    // Method to book a room
    public void bookRoom(String customerName, int numberOfDays) {

        if (bookingStatus) {
            System.out.println("Room " + roomNumber + " is already booked.");
            return;
        }

        if (numberOfDays <= 0) {
            System.out.println("Number of days must be greater than 0.");
            return;
        }

        this.customerName = customerName;
        this.numberOfDays = numberOfDays;
        this.bookingStatus = true;

        totalBookings++;

        System.out.println();
        System.out.println("===== Booking Confirmation =====");
        System.out.println("Room Number  : " + roomNumber);
        System.out.println("Room Type    : " + roomType);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Number of Days: " + numberOfDays);
        System.out.println("Price Per Day: ₹" + String.format("%.0f", pricePerDay));
        System.out.println("Total Bill   : ₹" + String.format("%.0f", calculateBill()));
        System.out.println("Room booked successfully.");
    }

    // Method to display room details
    public void displayRoomDetails() {

        System.out.println();
        System.out.println("===== Room Details =====");
        System.out.println("Room Number   : " + roomNumber);
        System.out.println("Room Type     : " + roomType);
        System.out.println("Price Per Day : ₹" + String.format("%.0f", pricePerDay));

        if (bookingStatus) {
            System.out.println("Customer Name : " + customerName);
            System.out.println("Number of Days: " + numberOfDays);
            System.out.println("Booking Status: Booked");
        } else {
            System.out.println("Customer Name : Not Available");
            System.out.println("Number of Days: 0");
            System.out.println("Booking Status: Available");
        }
    }

    // Method to calculate bill
    public double calculateBill() {

        return numberOfDays * pricePerDay;
    }

    // Method to cancel booking
    public void cancelBooking() {

        if (!bookingStatus) {
            System.out.println("Room " + roomNumber + " is not currently booked.");
            return;
        }

        customerName = "";
        numberOfDays = 0;
        bookingStatus = false;

        System.out.println("Booking for Room " + roomNumber + " has been cancelled.");
    }

    // Method to check room status
    public void checkRoomStatus() {

        System.out.println();

        if (bookingStatus) {
            System.out.println("Room " + roomNumber + " Status: BOOKED");
            System.out.println("Booked by: " + customerName);
        } else {
            System.out.println("Room " + roomNumber + " Status: AVAILABLE");
        }
    }

    // Static method to get total bookings
    public static int getTotalBookings() {
        return totalBookings;
    }
}


public class HotelRoomBookingSystem {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Creating Room objects
        Room room201 = new Room(201, "Standard", 2000);
        Room room202 = new Room(202, "Standard", 2000);
        Room room203 = new Room(203, "Deluxe", 3500);
        Room room204 = new Room(204, "Deluxe", 3500);
        Room room205 = new Room(205, "Premium", 5000);

        Room[] rooms = {
            room201,
            room202,
            room203,
            room204,
            room205
        };

        while (true) {

            System.out.println();
            System.out.println("===== Hotel Room Booking System =====");
            System.out.println("1. Book Room");
            System.out.println("2. Display Room Details");
            System.out.println("3. Calculate Bill");
            System.out.println("4. Check Room Status");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Display Total Bookings");
            System.out.println("7. Exit");
            System.out.println();

            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number from 1 to 7.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println("===== Available Rooms =====");

                    for (Room room : rooms) {
                        if (!room.isBookingStatus()) {
                            System.out.println(
                                room.getRoomNumber()
                                + " - "
                                + room.getRoomType()
                                + " - ₹"
                                + String.format("%.0f", room.getPricePerDay())
                                + " per day"
                            );
                        }
                    }

                    System.out.print("Enter room number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid room number.");
                        scanner.next();
                        continue;
                    }

                    int roomNumber = scanner.nextInt();
                    scanner.nextLine();

                    Room selectedRoom = null;

                    for (Room room : rooms) {
                        if (room.getRoomNumber() == roomNumber) {
                            selectedRoom = room;
                            break;
                        }
                    }

                    if (selectedRoom == null) {
                        System.out.println("Room not found.");
                        continue;
                    }

                    if (selectedRoom.isBookingStatus()) {
                        System.out.println("Room " + roomNumber + " is already booked.");
                        continue;
                    }

                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();

                    System.out.print("Enter number of days: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid number of days.");
                        scanner.next();
                        continue;
                    }

                    int numberOfDays = scanner.nextInt();
                    scanner.nextLine();

                    selectedRoom.bookRoom(customerName, numberOfDays);

                    break;

                case 2:

                    System.out.print("Enter room number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid room number.");
                        scanner.next();
                        continue;
                    }

                    int displayRoomNumber = scanner.nextInt();
                    scanner.nextLine();

                    Room displayRoom = null;

                    for (Room room : rooms) {
                        if (room.getRoomNumber() == displayRoomNumber) {
                            displayRoom = room;
                            break;
                        }
                    }

                    if (displayRoom != null) {
                        displayRoom.displayRoomDetails();
                    } else {
                        System.out.println("Room not found.");
                    }

                    break;

                case 3:

                    System.out.print("Enter room number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid room number.");
                        scanner.next();
                        continue;
                    }

                    int billRoomNumber = scanner.nextInt();
                    scanner.nextLine();

                    Room billRoom = null;

                    for (Room room : rooms) {
                        if (room.getRoomNumber() == billRoomNumber) {
                            billRoom = room;
                            break;
                        }
                    }

                    if (billRoom == null) {
                        System.out.println("Room not found.");
                    } else if (!billRoom.isBookingStatus()) {
                        System.out.println("Room " + billRoomNumber + " is not booked.");
                    } else {
                        System.out.println();
                        System.out.println("===== Bill Details =====");
                        System.out.println("Room Number  : " + billRoom.getRoomNumber());
                        System.out.println("Customer Name: " + billRoom.getCustomerName());
                        System.out.println("Number of Days: " + billRoom.getNumberOfDays());
                        System.out.println("Price Per Day: ₹"
                                + String.format("%.0f", billRoom.getPricePerDay()));
                        System.out.println("Total Bill   : ₹"
                                + String.format("%.0f", billRoom.calculateBill()));
                    }

                    break;

                case 4:

                    System.out.print("Enter room number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid room number.");
                        scanner.next();
                        continue;
                    }

                    int statusRoomNumber = scanner.nextInt();
                    scanner.nextLine();

                    Room statusRoom = null;

                    for (Room room : rooms) {
                        if (room.getRoomNumber() == statusRoomNumber) {
                            statusRoom = room;
                            break;
                        }
                    }

                    if (statusRoom != null) {
                        statusRoom.checkRoomStatus();
                    } else {
                        System.out.println("Room not found.");
                    }

                    break;

                case 5:

                    System.out.print("Enter room number: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid room number.");
                        scanner.next();
                        continue;
                    }

                    int cancelRoomNumber = scanner.nextInt();
                    scanner.nextLine();

                    Room cancelRoom = null;

                    for (Room room : rooms) {
                        if (room.getRoomNumber() == cancelRoomNumber) {
                            cancelRoom = room;
                            break;
                        }
                    }

                    if (cancelRoom != null) {
                        cancelRoom.cancelBooking();
                    } else {
                        System.out.println("Room not found.");
                    }

                    break;

                case 6:

                    System.out.println();
                    System.out.println("===== Booking Statistics =====");
                    System.out.println("Total Bookings Made: "
                            + Room.getTotalBookings());

                    break;

                case 7:

                    System.out.println();
                    System.out.println("Thank you for using the Hotel Room Booking System.");
                    System.out.println("Program exited successfully.");

                    scanner.close();
                    break;

                default:

                    System.out.println("Invalid menu choice! Please select 1 to 7.");
                    continue;
            }

            if (choice == 7) {
                break;
            }
        }
    }
}