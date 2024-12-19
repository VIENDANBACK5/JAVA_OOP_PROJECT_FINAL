package HotelManagement.controller;

import HotelManagement.view.BookingView;
import HotelManagement.model.Booking;
import HotelManagement.model.Customer;
import HotelManagement.model.Room;
import HotelManagement.model.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingController {
    private BookingView bookingView;
    private Connection conn;

    public BookingController(BookingView bookingView) {
        this.bookingView = bookingView;
        this.conn = DatabaseConnection.getConnection();

        loadBookingData();

        bookingView.getBtnAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBooking();
            }
        });

        bookingView.getBtnEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBooking();
            }
        });

        bookingView.getBtnDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBooking();
            }
        });
    }

    public void loadBookingData() {
        String query = "SELECT * FROM bookings";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ArrayList<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                String bookingId = rs.getString("bookingId");
                String customerId = rs.getString("customerId");
                int roomId = rs.getInt("roomId");
                LocalDate checkInDate = rs.getDate("check_in_date").toLocalDate();
                LocalDate checkOutDate = rs.getDate("check_out_date").toLocalDate();

                Customer customer = getCustomerById(customerId);
                Room room = getRoomById(roomId);

                bookings.add(new Booking(bookingId, customer, room, checkInDate, checkOutDate));
            }
            updateBookingTable(bookings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBookingTable(ArrayList<Booking> bookings) {
        DefaultTableModel model = (DefaultTableModel) bookingView.getBookingTable().getModel();
        model.setRowCount(0); 
        for (Booking booking : bookings) {
            model.addRow(new Object[]{
                booking.getBookingId(),
                booking.getCustomer().getName(),
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.calculateTotalPrice()
            });
        }
    }

    private void addBooking() {
        try {
            String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
            int roomId = Integer.parseInt(JOptionPane.showInputDialog("Enter Room ID:"));
            LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-in Date (YYYY-MM-DD):"));
            LocalDate checkOutDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-out Date (YYYY-MM-DD):"));

            String query = "INSERT INTO bookings (customerId, roomId, check_in_date, check_out_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, customerId);
                stmt.setInt(2, roomId);
                stmt.setDate(3, Date.valueOf(checkInDate));
                stmt.setDate(4, Date.valueOf(checkOutDate));
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Booking added successfully!");
                loadBookingData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void editBooking() {
        try {
            String bookingId = JOptionPane.showInputDialog("Enter Booking ID to edit:");
            String customerId = JOptionPane.showInputDialog("Enter new Customer ID:");
            int roomId = Integer.parseInt(JOptionPane.showInputDialog("Enter new Room ID:"));
            LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter new Check-in Date (YYYY-MM-DD):"));
            LocalDate checkOutDate = LocalDate.parse(JOptionPane.showInputDialog("Enter new Check-out Date (YYYY-MM-DD):"));

            String query = "UPDATE bookings SET customerId = ?, roomId = ?, check_in_date = ?, check_out_date = ? WHERE bookingId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, customerId);
                stmt.setInt(2, roomId);
                stmt.setDate(3, Date.valueOf(checkInDate));
                stmt.setDate(4, Date.valueOf(checkOutDate));
                stmt.setString(5, bookingId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Booking updated successfully!");
                loadBookingData(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }


    private void deleteBooking() {
        try {
            String bookingId = JOptionPane.showInputDialog("Enter Booking ID to delete:");

            String query = "DELETE FROM bookings WHERE bookingId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, bookingId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Booking deleted successfully!");
                loadBookingData(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }


    private Customer getCustomerById(String customerId) {
        return new Customer(customerId, "Example Name", "123456789", "Example Address");
    }


    public Room getRoomById(int roomId) {
        String roomType = "Deluxe";     
        double roomPrice = 150.0;       
        String roomStatus = "Available"; 
    
        return new Room(String.valueOf(roomId), roomType, roomPrice, roomStatus);
    }
    
}
