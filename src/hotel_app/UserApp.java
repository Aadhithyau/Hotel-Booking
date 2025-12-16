package hotel_app;

import java.sql.*;
import java.util.Scanner;

public class UserApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== USER MENU ======");
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    signup();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    static void signup() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter username: ");
            String u = sc.nextLine();

            System.out.print("Enter password: ");
            String p = sc.nextLine();

            String sql = "INSERT INTO users(username, password) VALUES(?,?)";
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u);
            ps.setString(2, p);
            ps.executeUpdate();

            System.out.println("✔ Signup successful!");

        } catch (Exception e) { e.printStackTrace(); }
    }

    static void login() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Username: ");
            String u = sc.nextLine();

            System.out.print("Password: ");
            String p = sc.nextLine();

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u);
            ps.setString(2, p);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("✔ Login Successful!");
                userBooking(u);
            } else {
                System.out.println("❌ Wrong username or password");
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    static void userBooking(String username) {
        try {
            Scanner sc = new Scanner(System.in);
            Connection con = DB.getConnection();

            // --- List Districts ---
            System.out.println("\n=== DISTRICTS ===");
            ResultSet rsDistricts = con.createStatement().executeQuery("SELECT * FROM districts");
            while (rsDistricts.next()) {
                System.out.println(rsDistricts.getInt("id") + ". " + rsDistricts.getString("name"));
            }

            System.out.print("Select District ID: ");
            int did = sc.nextInt();

            // --- List Hotels ---
            System.out.println("\n=== HOTELS ===");
            PreparedStatement psHotels = con.prepareStatement("SELECT * FROM hotels WHERE district_id=?");
            psHotels.setInt(1, did);
            ResultSet rsHotels = psHotels.executeQuery();
            while (rsHotels.next()) {
                System.out.println(rsHotels.getInt("id") + ". " +
                        rsHotels.getString("name") + "  Rs." + rsHotels.getInt("price"));
            }

            System.out.print("Select Hotel ID: ");
            int hid = sc.nextInt();

            System.out.print("Enter Number of Days: ");
            int days = sc.nextInt();

            // --- Get Hotel Price ---
            PreparedStatement psHotelPrice = con.prepareStatement("SELECT * FROM hotels WHERE id=?");
            psHotelPrice.setInt(1, hid);
            ResultSet rsHotelPrice = psHotelPrice.executeQuery();
            rsHotelPrice.next();
            int price = rsHotelPrice.getInt("price");

            int total = price * days;
            System.out.println("Total Amount: " + total);

            System.out.print("Enter Payment Amount: ");
            int pay = sc.nextInt();

            if (pay == total) {
                // --- Get user_id ---
                PreparedStatement psUser = con.prepareStatement("SELECT id FROM users WHERE username=?");
                psUser.setString(1, username);
                ResultSet rsUser = psUser.executeQuery();
                rsUser.next();
                int userId = rsUser.getInt("id");

                // --- Insert Booking ---
                String sql = "INSERT INTO bookings(user_id, hotel_id, days, total_amount, username) VALUES(?,?,?,?,?)";
                PreparedStatement psBooking = con.prepareStatement(sql);
                psBooking.setInt(1, userId);      
                psBooking.setInt(2, hid);         
                psBooking.setInt(3, days);        
                psBooking.setInt(4, pay);         
                psBooking.setString(5, username); 
                psBooking.executeUpdate();

                System.out.println("✔ Booking Confirmed!");
            } else {
                System.out.println("❌ Payment Failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
