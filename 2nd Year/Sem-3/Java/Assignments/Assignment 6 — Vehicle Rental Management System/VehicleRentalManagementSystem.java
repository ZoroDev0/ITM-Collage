import java.util.Scanner;

// Parent Class
class Vehicle {
    private String vehicleNumber;
    private String vehicleModel;
    private String customerName;
    private int rentalDays;

    // Constructor
    Vehicle(String vehicleNumber, String vehicleModel,
            String customerName, int rentalDays) {

        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.customerName = customerName;
        this.rentalDays = rentalDays;
    }

    // Getters
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    // Common method
    public void displayDetails() {
        System.out.println("Vehicle Number : " + vehicleNumber);
        System.out.println("Model          : " + vehicleModel);
        System.out.println("Customer Name  : " + customerName);
        System.out.println("Rental Days    : " + rentalDays);
    }

    // Method to be overridden
    public double calculateRentalCharges() {
        return 0;
    }
}


// Car Class
class Car extends Vehicle {
    private int numberOfSeats;

    Car(String vehicleNumber, String vehicleModel,
        String customerName, int rentalDays, int numberOfSeats) {

        super(vehicleNumber, vehicleModel, customerName, rentalDays);
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public void displayDetails() {
        System.out.println("Vehicle Type   : Car");
        super.displayDetails();
        System.out.println("Number of Seats: " + numberOfSeats);
        System.out.println("Rate Per Day   : ₹1500");
    }

    @Override
    public double calculateRentalCharges() {
        return getRentalDays() * 1500;
    }
}


// Bike Class
class Bike extends Vehicle {
    private int engineCapacity;

    Bike(String vehicleNumber, String vehicleModel,
         String customerName, int rentalDays, int engineCapacity) {

        super(vehicleNumber, vehicleModel, customerName, rentalDays);
        this.engineCapacity = engineCapacity;
    }

    @Override
    public void displayDetails() {
        System.out.println("Vehicle Type   : Bike");
        super.displayDetails();
        System.out.println("Engine Capacity: " + engineCapacity + " cc");
        System.out.println("Rate Per Day   : ₹700");
    }

    @Override
    public double calculateRentalCharges() {
        return getRentalDays() * 700;
    }
}


// Scooter Class
class Scooter extends Vehicle {
    private int storageCapacity;

    Scooter(String vehicleNumber, String vehicleModel,
            String customerName, int rentalDays, int storageCapacity) {

        super(vehicleNumber, vehicleModel, customerName, rentalDays);
        this.storageCapacity = storageCapacity;
    }

    @Override
    public void displayDetails() {
        System.out.println("Vehicle Type   : Scooter");
        super.displayDetails();
        System.out.println("Storage Capacity: " + storageCapacity + " litres");
        System.out.println("Rate Per Day    : ₹500");
    }

    @Override
    public double calculateRentalCharges() {
        return getRentalDays() * 500;
    }
}


// Additional Vehicle Type
class ElectricCar extends Vehicle {
    private double batteryCapacity;

    ElectricCar(String vehicleNumber, String vehicleModel,
                String customerName, int rentalDays,
                double batteryCapacity) {

        super(vehicleNumber, vehicleModel, customerName, rentalDays);
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public void displayDetails() {
        System.out.println("Vehicle Type    : Electric Car");
        super.displayDetails();
        System.out.println("Battery Capacity: " + batteryCapacity + " kWh");
        System.out.println("Rate Per Day    : ₹2000");
    }

    @Override
    public double calculateRentalCharges() {
        return getRentalDays() * 2000;
    }
}


// Main Class
public class VehicleRentalManagementSystem {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Vehicle rentedVehicle = null;

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("     VEHICLE RENTAL SYSTEM");
            System.out.println("================================");
            System.out.println("1. Rent a Car");
            System.out.println("2. Rent a Bike");
            System.out.println("3. Rent a Scooter");
            System.out.println("4. Rent an Electric Car");
            System.out.println("5. Display Rental Details");
            System.out.println("6. Calculate Rental Charges");
            System.out.println("7. Exit");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n--- Rent a Car ---");

                    System.out.print("Enter Vehicle Number: ");
                    String carNumber = scanner.nextLine();

                    System.out.print("Enter Vehicle Model: ");
                    String carModel = scanner.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String carCustomer = scanner.nextLine();

                    System.out.print("Enter Rental Days: ");
                    int carDays = scanner.nextInt();

                    System.out.print("Enter Number of Seats: ");
                    int seats = scanner.nextInt();

                    rentedVehicle = new Car(
                            carNumber,
                            carModel,
                            carCustomer,
                            carDays,
                            seats
                    );

                    System.out.println("\nVehicle rented successfully!");
                    break;


                case 2:
                    System.out.println("\n--- Rent a Bike ---");

                    System.out.print("Enter Vehicle Number: ");
                    String bikeNumber = scanner.nextLine();

                    System.out.print("Enter Vehicle Model: ");
                    String bikeModel = scanner.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String bikeCustomer = scanner.nextLine();

                    System.out.print("Enter Rental Days: ");
                    int bikeDays = scanner.nextInt();

                    System.out.print("Enter Engine Capacity (cc): ");
                    int engineCapacity = scanner.nextInt();

                    rentedVehicle = new Bike(
                            bikeNumber,
                            bikeModel,
                            bikeCustomer,
                            bikeDays,
                            engineCapacity
                    );

                    System.out.println("\nVehicle rented successfully!");
                    break;


                case 3:
                    System.out.println("\n--- Rent a Scooter ---");

                    System.out.print("Enter Vehicle Number: ");
                    String scooterNumber = scanner.nextLine();

                    System.out.print("Enter Vehicle Model: ");
                    String scooterModel = scanner.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String scooterCustomer = scanner.nextLine();

                    System.out.print("Enter Rental Days: ");
                    int scooterDays = scanner.nextInt();

                    System.out.print("Enter Storage Capacity (litres): ");
                    int storageCapacity = scanner.nextInt();

                    rentedVehicle = new Scooter(
                            scooterNumber,
                            scooterModel,
                            scooterCustomer,
                            scooterDays,
                            storageCapacity
                    );

                    System.out.println("\nVehicle rented successfully!");
                    break;


                case 4:
                    System.out.println("\n--- Rent an Electric Car ---");

                    System.out.print("Enter Vehicle Number: ");
                    String electricNumber = scanner.nextLine();

                    System.out.print("Enter Vehicle Model: ");
                    String electricModel = scanner.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String electricCustomer = scanner.nextLine();

                    System.out.print("Enter Rental Days: ");
                    int electricDays = scanner.nextInt();

                    System.out.print("Enter Battery Capacity (kWh): ");
                    double batteryCapacity = scanner.nextDouble();

                    rentedVehicle = new ElectricCar(
                            electricNumber,
                            electricModel,
                            electricCustomer,
                            electricDays,
                            batteryCapacity
                    );

                    System.out.println("\nElectric car rented successfully!");
                    break;


                case 5:
                    System.out.println("\n===== VEHICLE RENTAL DETAILS =====");

                    if (rentedVehicle != null) {
                        rentedVehicle.displayDetails();
                    } else {
                        System.out.println("No vehicle has been rented yet.");
                    }

                    break;


                case 6:
                    System.out.println("\n===== VEHICLE RENTAL RECEIPT =====");

                    if (rentedVehicle != null) {

                        rentedVehicle.displayDetails();

                        double totalAmount =
                                rentedVehicle.calculateRentalCharges();

                        System.out.println("--------------------------------");
                        System.out.println("Total Amount : ₹" + totalAmount);
                        System.out.println("--------------------------------");
                    } else {
                        System.out.println("No vehicle has been rented yet.");
                    }

                    break;


                case 7:
                    System.out.println("\nThank you for using");
                    System.out.println("Vehicle Rental Management System!");
                    break;


                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 7);

        scanner.close();
    }
}