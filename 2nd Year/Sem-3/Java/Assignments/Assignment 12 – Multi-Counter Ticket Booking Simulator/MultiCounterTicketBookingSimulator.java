import java.util.Scanner;

// Shared ticket booking system
class TicketBookingSystem {

    private int availableTickets;

    public TicketBookingSystem(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    // Synchronized method prevents overselling
    public synchronized boolean sellTicket(String counterName) {

        if (availableTickets > 0) {
            availableTickets--;

            System.out.println(
                counterName + " sold ticket. Remaining: "
                + availableTickets
            );

            return true;
        }

        return false;
    }

    public synchronized int getAvailableTickets() {
        return availableTickets;
    }

    public synchronized void resetTickets(int tickets) {
        availableTickets = tickets;
    }
}


// Thread created by extending Thread class
class CounterThread extends Thread {

    private TicketBookingSystem bookingSystem;

    public CounterThread(String name, TicketBookingSystem bookingSystem) {
        super(name);
        this.bookingSystem = bookingSystem;
    }

    @Override
    public void run() {

        while (true) {

            boolean sold = bookingSystem.sellTicket(getName());

            if (!sold) {
                break;
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println(
                    getName() + " was interrupted."
                );
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(getName() + " finished selling.");
    }
}


// Thread created using Runnable interface
class CounterRunnable implements Runnable {

    private TicketBookingSystem bookingSystem;

    public CounterRunnable(TicketBookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
    }

    @Override
    public void run() {

        while (true) {

            boolean sold = bookingSystem.sellTicket(
                Thread.currentThread().getName()
            );

            if (!sold) {
                break;
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println(
                    Thread.currentThread().getName()
                    + " was interrupted."
                );
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(
            Thread.currentThread().getName()
            + " finished selling."
        );
    }
}


// Main class
public class MultiCounterTicketBookingSimulator {

    private static final int INITIAL_TICKETS = 10;

    private static TicketBookingSystem bookingSystem =
        new TicketBookingSystem(INITIAL_TICKETS);

    private static CounterThread counterA;
    private static Thread counterB;

    private static Scanner scanner = new Scanner(System.in);


    // Display menu
    private static void displayMenu() {

        System.out.println();
        System.out.println("===== Multi-Counter Ticket Booking Simulator =====");
        System.out.println();
        System.out.println("Available tickets: "
                + bookingSystem.getAvailableTickets());
        System.out.println();

        System.out.println("1. Start Counter using Thread Class");
        System.out.println("2. Start Counter using Runnable Interface");
        System.out.println("3. Set Thread Priority");
        System.out.println("4. Display Thread Status");
        System.out.println("5. Display Available Tickets");
        System.out.println("6. Wait for All Counters to Finish");
        System.out.println("7. Reset Ticket Pool");
        System.out.println("8. Exit");
        System.out.println();
    }


    // Option 1
    private static void startThreadCounter() {

        if (counterA != null && counterA.isAlive()) {
            System.out.println("Counter-A is already running.");
            return;
        }

        if (bookingSystem.getAvailableTickets() == 0) {
            System.out.println("No tickets available.");
            return;
        }

        counterA = new CounterThread(
            "Counter-A",
            bookingSystem
        );

        System.out.println("Starting Counter-A (Thread class)...");

        counterA.start();
    }


    // Option 2
    private static void startRunnableCounter() {

        if (counterB != null && counterB.isAlive()) {
            System.out.println("Counter-B is already running.");
            return;
        }

        if (bookingSystem.getAvailableTickets() == 0) {
            System.out.println("No tickets available.");
            return;
        }

        CounterRunnable runnable =
            new CounterRunnable(bookingSystem);

        counterB = new Thread(
            runnable,
            "Counter-B"
        );

        System.out.println(
            "Starting Counter-B (Runnable interface)..."
        );

        counterB.start();
    }


    // Option 3
    private static void setThreadPriority() {

        System.out.println();
        System.out.println("===== Set Thread Priority =====");

        System.out.println("1. Counter-A");
        System.out.println("2. Counter-B");

        System.out.print("Select counter: ");

        try {

            int choice = Integer.parseInt(scanner.nextLine());

            System.out.print(
                "Enter priority (1-10): "
            );

            int priority =
                Integer.parseInt(scanner.nextLine());

            if (priority < Thread.MIN_PRIORITY ||
                priority > Thread.MAX_PRIORITY) {

                System.out.println(
                    "Priority must be between 1 and 10."
                );
                return;
            }

            if (choice == 1) {

                if (counterA == null) {
                    System.out.println(
                        "Counter-A has not been created yet."
                    );
                    return;
                }

                if (counterA.isAlive()) {
                    System.out.println(
                        "Cannot change priority while "
                        + "Counter-A is running."
                    );
                    return;
                }

                counterA.setPriority(priority);

                System.out.println(
                    "Counter-A priority set to "
                    + priority
                );

            } else if (choice == 2) {

                if (counterB == null) {
                    System.out.println(
                        "Counter-B has not been created yet."
                    );
                    return;
                }

                if (counterB.isAlive()) {
                    System.out.println(
                        "Cannot change priority while "
                        + "Counter-B is running."
                    );
                    return;
                }

                counterB.setPriority(priority);

                System.out.println(
                    "Counter-B priority set to "
                    + priority
                );

            } else {

                System.out.println(
                    "Invalid counter selection."
                );
            }

        } catch (NumberFormatException e) {

            System.out.println(
                "Error: Please enter a valid number."
            );
        }
    }


    // Option 4
    private static void displayThreadStatus() {

        System.out.println();
        System.out.println("===== Thread Status =====");

        if (counterA != null) {

            System.out.println(
                "Name: " + counterA.getName()
                + " | Priority: " + counterA.getPriority()
                + " | Alive: " + counterA.isAlive()
            );

        } else {

            System.out.println(
                "Counter-A: Not created"
            );
        }


        if (counterB != null) {

            System.out.println(
                "Name: " + counterB.getName()
                + " | Priority: " + counterB.getPriority()
                + " | Alive: " + counterB.isAlive()
            );

        } else {

            System.out.println(
                "Counter-B: Not created"
            );
        }
    }


    // Option 5
    private static void displayAvailableTickets() {

        System.out.println();
        System.out.println("===== Ticket Status =====");

        System.out.println(
            "Available Tickets: "
            + bookingSystem.getAvailableTickets()
        );
    }


    // Option 6
    private static void waitForCounters() {

        System.out.println();
        System.out.println(
            "Waiting for all counters to finish (join)..."
        );

        try {

            if (counterA != null) {
                counterA.join();
            }

            if (counterB != null) {
                counterB.join();
            }

            System.out.println(
                "All counters finished selling."
            );

            System.out.println(
                "Final available tickets: "
                + bookingSystem.getAvailableTickets()
            );

        } catch (InterruptedException e) {

            System.out.println(
                "Error: Main thread was interrupted."
            );

            Thread.currentThread().interrupt();
        }
    }


    // Option 7
    private static void resetTicketPool() {

        if ((counterA != null && counterA.isAlive()) ||
            (counterB != null && counterB.isAlive())) {

            System.out.println(
                "Cannot reset while counters are running."
            );

            return;
        }

        bookingSystem.resetTickets(INITIAL_TICKETS);

        System.out.println(
            "Ticket pool reset successfully."
        );

        System.out.println(
            "Available tickets: "
            + bookingSystem.getAvailableTickets()
        );
    }


    // Main method
    public static void main(String[] args) {

        boolean running = true;

        System.out.println();
        System.out.println(
            "===== Multi-Counter Ticket Booking Simulator ====="
        );

        while (running) {

            displayMenu();

            System.out.print("Enter your choice: ");

            try {

                int choice =
                    Integer.parseInt(scanner.nextLine());

                switch (choice) {

                    case 1:
                        startThreadCounter();
                        break;

                    case 2:
                        startRunnableCounter();
                        break;

                    case 3:
                        setThreadPriority();
                        break;

                    case 4:
                        displayThreadStatus();
                        break;

                    case 5:
                        displayAvailableTickets();
                        break;

                    case 6:
                        waitForCounters();
                        break;

                    case 7:
                        resetTicketPool();
                        break;

                    case 8:
                        System.out.println(
                            "Exiting Multi-Counter Ticket Booking Simulator."
                        );
                        running = false;
                        break;

                    default:
                        System.out.println(
                            "Invalid choice. Please select 1-8."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                    "Error: Please enter a valid menu number."
                );
            }
        }

        scanner.close();
    }
}