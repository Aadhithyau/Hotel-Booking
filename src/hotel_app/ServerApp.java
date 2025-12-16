package hotel_app;

import java.sql.*;
import java.util.Scanner;

public class ServerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== SERVER / ADMIN PANEL =====");
            System.out.println("1. Add District");
            System.out.println("2. Add Hotel");
            System.out.println("3. View User Payments");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addDistrict();
                    break;

                case 2:
                    addHotel();
                    break;

                case 3:
                    viewPayments();
                    break;

                case 4:
                    System.exit(0);
            }
        }
    }

    static void addDistrict() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter District Name: ");
            String district = sc.nextLine();

            String sql = "INSERT INTO districts(name) VALUES(?)";
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, district);
            ps.executeUpdate();

            System.out.println("✔ District added!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    static void addHotel() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter District ID: ");
            int did = sc.nextInt(); sc.nextLine();

            System.out.print("Enter Hotel Name: ");
            String hname = sc.nextLine();

            System.out.print("Enter Price Per Day: ");
            int price = sc.nextInt();

            String sql = "INSERT INTO hotels(district_id, name, price) VALUES(?,?,?)";
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, did);
            ps.setString(2, hname);
            ps.setInt(3, price);
            ps.executeUpdate();

            System.out.println("✔ Hotel Added!");
        } catch (Exception e) { e.printStackTrace(); }
    }

    static void viewPayments() {
        try {
            Connection con = DB.getConnection();
            String sql = "SELECT * FROM bookings";
            ResultSet rs = con.createStatement().executeQuery(sql);

            System.out.println("\n=== USER PAYMENTS ===");
            while (rs.next()) {
                String username = rs.getString("username");
                int hotelId = rs.getInt("hotel_id");
                int days = rs.getInt("days");
                int amount = rs.getInt("total_amount");

                // Fetch hotel name
                String hotelName = "";
                PreparedStatement ps = con.prepareStatement("SELECT name FROM hotels WHERE id=?");
                ps.setInt(1, hotelId);
                ResultSet rs2 = ps.executeQuery();
                if (rs2.next()) {
                    hotelName = rs2.getString("name");
                }

                System.out.println("User: " + username);
                System.out.println("Hotel: " + hotelName);
                System.out.println("Days: " + days);
                System.out.println("Amount Paid: " + amount);
                System.out.println("------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
