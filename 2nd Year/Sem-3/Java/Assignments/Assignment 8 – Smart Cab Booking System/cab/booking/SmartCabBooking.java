package cab.booking;

public class SmartCabBooking {

    // Wrapper Classes
    private Integer passengerId;
    private Double baseFare;

    // Final booking fee
    private final Double bookingFee = 50.0;

    private String passengerName;
    private CabType cabType;

    // Inner Class
    public class PickupLocation {

        private String location;

        public PickupLocation(String location) {
            this.location = location;
        }

        public void displayLocation() {
            System.out.println("Pickup Location: " + location);
        }
    }

    public SmartCabBooking(Integer passengerId, String passengerName,
                           CabType cabType, Double baseFare) {

        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.cabType = cabType;
        this.baseFare = baseFare;
    }

    public void displayBookingSummary(String pickupLocation) {

        // Unboxing: Double object converted to primitive double
        double fare = baseFare;

        double finalFare = fare + bookingFee;

        // StringBuilder
        StringBuilder summary = new StringBuilder();

        summary.append("===== Smart Cab Booking System =====\n\n");
        summary.append("Passenger Name: ").append(passengerName).append("\n");
        summary.append("Cab Type: ").append(cabType).append("\n");
        summary.append("Base Fare: ₹").append(fare).append("\n");
        summary.append("Booking Fee: ₹").append(bookingFee).append("\n");
        summary.append("Final Fare: ₹").append(finalFare).append("\n");

        // Inner Class object
        PickupLocation pickup = new PickupLocation(pickupLocation);

        System.out.println(summary);

        pickup.displayLocation();

        // Anonymous Class
        Confirmation confirmation = new Confirmation() {

            @Override
            public void confirm() {
                System.out.println("Booking confirmed successfully.");
            }
        };

        confirmation.confirm();
    }

    // Interface for Anonymous Class
    interface Confirmation {
        void confirm();
    }
}