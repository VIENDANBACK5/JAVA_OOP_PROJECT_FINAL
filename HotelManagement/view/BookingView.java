package HotelManagement.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookingView extends JPanel {
    private JTable bookingTable;
    private JButton btnAdd, btnEdit, btnDelete;

    public BookingView() {
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Booking ID", "Customer Name", "Room ID", "Check-in Date", "Check-out Date"});
        bookingTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        btnAdd = new JButton("Add Booking");
        btnEdit = new JButton("Edit Booking");
        btnDelete = new JButton("Delete Booking");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getBookingTable() {
        return bookingTable;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }
}
