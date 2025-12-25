import java.sql.*;
import java.util.*;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static MovieDAO movieDAO = new MovieDAO();
    private static BookingDAO bookingDAO = new BookingDAO();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("\n🎬 Welcome to Movie Ticket Booking System 🎟️");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Signup");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> signup();
                default -> System.out.println("⚠️ Invalid choice! Try again.");
            }
        }
    }

    // ---------------- LOGIN ----------------
    private static void login() {
        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            loggedInUser = userDAO.login(username, password);
            if (loggedInUser != null) {
                System.out.println("✅ Login successful! Welcome " + loggedInUser.getUsername() + "!");
                showMenu();
            } else {
                System.out.println("❌ Invalid username or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- SIGNUP ----------------
    private static void signup() {
        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            System.out.print("Enter your city: ");
            String city = sc.nextLine();

            User user = new User(null, username, password, city);
            if (userDAO.register(user)) {
                System.out.println("🎉 Signup successful! You can now log in.");
            } else {
                System.out.println("⚠️ Signup failed! Try another username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- MAIN MENU ----------------
    private static void showMenu() {
        while (true) {
            System.out.println("\n🎞️ Movie Menu 🎞️");
            System.out.println("1. View Movies");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Add New Movie");
            System.out.println("5. View My Booked Tickets");
            System.out.println("6. Top Booked Movies Report 📊");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1 -> movieDAO.viewMovies();

                    case 2 -> {
                        System.out.print("Enter movie ID to book: ");
                        int movieId = sc.nextInt();
                        sc.nextLine();
                        if (bookingDAO.bookTicket(loggedInUser.getId(), movieId)) {
                            System.out.println("🎟️ Ticket booked successfully!");
                        } else {
                            System.out.println("❌ Booking failed (maybe sold out or invalid movie ID).");
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter booking ID to cancel: ");
                        int bookingId = sc.nextInt();
                        sc.nextLine();
                        if (bookingDAO.cancelBooking(bookingId, loggedInUser.getId())) {
                            System.out.println("❌ Ticket canceled successfully!");
                        } else {
                            System.out.println("⚠️ Invalid booking ID or not your ticket!");
                        }
                    }

                    case 4 -> {
                        System.out.print("Enter movie title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter genre: ");
                        String genre = sc.nextLine();
                        System.out.print("Enter total tickets: ");
                        int tickets = sc.nextInt();
                        System.out.print("Enter ticket price: ");
                        double price = sc.nextDouble();
                        sc.nextLine();

                        Movie movie = new Movie(0, title, genre, tickets, price);
                        movieDAO.addMovie(movie);
                        System.out.println("✅ Movie added successfully!");
                    }

                    case 5 -> bookingDAO.viewUserBookings(loggedInUser.getId());

                    case 6 -> bookingDAO.showTopMoviesReport(); // 🆕 New Feature

                    case 7 -> {
                        System.out.println("👋 Logged out successfully!");
                        loggedInUser = null;
                        return;
                    }

                    default -> System.out.println("⚠️ Invalid choice!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
