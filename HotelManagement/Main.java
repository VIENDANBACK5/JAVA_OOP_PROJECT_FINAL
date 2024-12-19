package HotelManagement;

import HotelManagement.controller.BookingController;
import HotelManagement.controller.CustomerController;
import HotelManagement.controller.RoomController;
import HotelManagement.view.BookingView;
import HotelManagement.view.CustomerView;
import HotelManagement.view.RoomView;
import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {

            JFrame mainFrame = new JFrame("Hotel Management");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(800, 600);
            mainFrame.setLayout(new BorderLayout());

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            RoomView roomView = new RoomView();
            BookingView bookingView = new BookingView();
            CustomerView customerView = new CustomerView();

            mainPanel.add(roomView, "Room");
            mainPanel.add(bookingView, "Booking");
            mainPanel.add(customerView, "Customer");

            new RoomController(roomView);
            new BookingController(bookingView);
            new CustomerController(customerView);

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new GridLayout(3, 1));

            JButton btnRoom = new JButton("Manage Rooms");
            JButton btnBooking = new JButton("Manage Bookings");
            JButton btnCustomer = new JButton("Manage Customers");

            btnRoom.addActionListener(e -> cardLayout.show(mainPanel, "Room"));
            btnBooking.addActionListener(e -> cardLayout.show(mainPanel, "Booking"));
            btnCustomer.addActionListener(e -> cardLayout.show(mainPanel, "Customer"));

            sidebar.add(btnRoom);
            sidebar.add(btnBooking);
            sidebar.add(btnCustomer);

            mainFrame.add(sidebar, BorderLayout.WEST);
            mainFrame.add(mainPanel, BorderLayout.CENTER);

            mainFrame.setVisible(true);
        });
    }
}
