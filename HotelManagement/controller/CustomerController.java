package HotelManagement.controller;

import HotelManagement.model.Customer;
import HotelManagement.model.DatabaseConnection;
import HotelManagement.view.CustomerView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerController {
    private CustomerView customerView;
    private Connection conn;

    public CustomerController(CustomerView customerView) {
        this.customerView = customerView;
        this.conn = DatabaseConnection.getConnection();

        customerView.getBtnAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        customerView.getBtnEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });

        customerView.getBtnDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        loadCustomerData();
    }

    private void addCustomer() {
        String name = JOptionPane.showInputDialog("Enter Customer Name:");
        String phone = JOptionPane.showInputDialog("Enter Phone Number:");
        String address = JOptionPane.showInputDialog("Enter Address:");

        if (name != null && phone != null && address != null) {
            String query = "INSERT INTO customers (name, phone, address) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, address);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Customer added successfully!");
                loadCustomerData(); 
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding customer.");
            }
        }
    }

    private void editCustomer() {
        int selectedRow = customerView.getCustomerTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to edit.");
            return;
        }

        String customerId = (String) customerView.getCustomerTable().getValueAt(selectedRow, 0);
        String name = JOptionPane.showInputDialog("Enter new Customer Name:");
        String phone = JOptionPane.showInputDialog("Enter new Phone Number:");
        String address = JOptionPane.showInputDialog("Enter new Address:");

        if (name != null && phone != null && address != null) {
            String query = "UPDATE customers SET name = ?, phone = ?, address = ? WHERE customerId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, address);
                stmt.setString(4, customerId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Customer updated successfully!");
                loadCustomerData(); 
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating customer.");
            }
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerView.getCustomerTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to delete.");
            return;
        }

        String customerId = (String) customerView.getCustomerTable().getValueAt(selectedRow, 0);
        String query = "DELETE FROM customers WHERE customerId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Customer deleted successfully!");
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting customer.");
        }
    }

    private void loadCustomerData() {
        String query = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ArrayList<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                String customerId = rs.getString("customerId");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                customers.add(new Customer(customerId, name, phone, address));
            }

            updateCustomerTable(customers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateCustomerTable(ArrayList<Customer> customers) {
        DefaultTableModel model = (DefaultTableModel) customerView.getCustomerTable().getModel();
        model.setRowCount(0); 
    
        for (Customer customer : customers) {
            model.addRow(new Object[] {
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getAddress()
            });
        }
    }
    
}
