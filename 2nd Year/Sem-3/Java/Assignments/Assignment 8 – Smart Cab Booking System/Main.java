import java.util.Scanner;
import cab.booking.CabType;
import cab.booking.SmartCabBooking;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Smart Cab Booking System =====");

        System.out.print("Passenger Name: ");
        String passengerName = scanner.nextLine();

        System.out.print("Cab Type (MINI/SEDAN/SUV): ");
        String cabInput = scanner.nextLine();

        CabType cabType = CabType.valueOf(cabInput.toUpperCase());

        System.out.print("Base Fare: ₹");
        double fareInput = scanner.nextDouble();

        scanner.nextLine();

        System.out.print("Pickup Location: ");
        String pickupLocation = scanner.nextLine();

        // Autoboxing: int -> Integer
        int id = 101;
        Integer passengerId = id;

        // Autoboxing: double -> Double
        Double baseFare = fareInput;

        SmartCabBooking booking = new SmartCabBooking(
                passengerId,
                passengerName,
                cabType,
                baseFare
        );

        System.out.println();

        booking.displayBookingSummary(pickupLocation);

        scanner.close();
    }
}