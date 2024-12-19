package HotelManagement.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerView extends JPanel {
    private JTable customerTable;
    private JButton btnAdd, btnEdit, btnDelete;

    public CustomerView() {
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Customer ID", "Name", "Phone", "Address"});
        customerTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        btnAdd = new JButton("Add Customer");
        btnEdit = new JButton("Edit Customer");
        btnDelete = new JButton("Delete Customer");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getCustomerTable() {
        return customerTable;
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
