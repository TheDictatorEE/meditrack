package com.meditrack;

import java.sql.*;
import java.util.Scanner;

public class MediTrackApp {
    static final String DB_URL = "jdbc:mysql://localhost:3306/meditrack";
    static final String USER = "root";
    static final String PASS = "iamgod@gc"; // Replace with your actual password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connected to database successfully.");

            while (true) {
                System.out.println("\n=== MediTrack Menu ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerUser(conn, scanner);
                        break;
                    case 2:
                        loginUser(conn, scanner);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (Patient/Doctor): ");
        String role = scanner.nextLine();

        String sql = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.executeUpdate();
            System.out.println("User registered successfully.");
        }
    }

    private static void loginUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login successful as " + rs.getString("role"));
                    int userId = rs.getInt("user_id");
                    showUserMenu(conn, scanner, userId);
                } else {
                    System.out.println("Invalid credentials.");
                }
            }
        }
    }

    private static void showUserMenu(Connection conn, Scanner scanner, int userId) throws SQLException {
        while (true) {
            System.out.println("\n1. Add Medical Record\n2. Add Prescription\n3. Add Appointment\n4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addMedicalRecord(conn, scanner, userId);
                    break;
                case 2:
                    addPrescription(conn, scanner, userId);
                    break;
                case 3:
                    addAppointment(conn, scanner, userId);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addMedicalRecord(Connection conn, Scanner scanner, int userId) throws SQLException {
        System.out.print("Condition name: ");
        String condition = scanner.nextLine();
        System.out.print("Diagnosis date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        String sql = "INSERT INTO MedicalRecords (user_id, condition_name, diagnosis_date, notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, condition);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setString(4, notes);
            stmt.executeUpdate();
            System.out.println("Medical record added.");
        }
    }

    private static void addPrescription(Connection conn, Scanner scanner, int userId) throws SQLException {
        System.out.print("Medication name: ");
        String name = scanner.nextLine();
        System.out.print("Dosage: ");
        String dosage = scanner.nextLine();
        System.out.print("Frequency: ");
        String frequency = scanner.nextLine();
        System.out.print("Start date (YYYY-MM-DD): ");
        String start = scanner.nextLine();
        System.out.print("End date (YYYY-MM-DD): ");
        String end = scanner.nextLine();

        String sql = "INSERT INTO Prescriptions (user_id, medication_name, dosage, frequency, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, dosage);
            stmt.setString(4, frequency);
            stmt.setDate(5, Date.valueOf(start));
            stmt.setDate(6, Date.valueOf(end));
            stmt.executeUpdate();
            System.out.println("Prescription added.");
        }
    }

    private static void addAppointment(Connection conn, Scanner scanner, int userId) throws SQLException {
        System.out.print("Doctor name: ");
        String doctor = scanner.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Time (HH:MM:SS): ");
        String time = scanner.nextLine();
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine();

        String sql = "INSERT INTO Appointments (user_id, doctor_name, date, time, purpose) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, doctor);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(time));
            stmt.setString(5, purpose);
            stmt.executeUpdate();
            System.out.println("Appointment added.");
        }
    }
}
