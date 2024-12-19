package HotelManagement.view;

import HotelManagement.model.Room;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class RoomView extends JPanel {
    private JTable roomTable;
    private JButton refreshButton, addButton, editButton, deleteButton;
    private JTextField searchField;
    private JComboBox<String> statusFilterCombo;

    public RoomView() {
        setLayout(new BorderLayout());

        roomTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusFilterCombo = new JComboBox<>(new String[]{"All", "Available", "Booked"});
        filterPanel.add(new JLabel("Filter by Status:"));
        filterPanel.add(statusFilterCombo);
        add(filterPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Room");
        editButton = new JButton("Edit Room");
        deleteButton = new JButton("Delete Room");
        refreshButton = new JButton("Refresh Rooms");  
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton); 
        add(buttonPanel, BorderLayout.SOUTH);

        startAutoRefresh();

        searchField.getDocument().addDocumentListener((javax.swing.event.DocumentListener) new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterRooms(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterRooms(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterRooms(); }
        });

        statusFilterCombo.addActionListener(e -> filterRooms());
    }

    public void setRoomData(List<Room> rooms) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Type", "Price", "Status"}, 0);
        for (Room room : rooms) {
            model.addRow(new Object[]{
                room.getId(),
                room.getType(),
                room.getPrice(),
                room.getStatus()
            });
        }
        roomTable.setModel(model);
        enableSorting();
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    private void startAutoRefresh() {
        Timer timer = new Timer(5000, e -> refreshRoomData());
        timer.start();
    }

    private void refreshRoomData() {
        List<Room> updatedRooms = getUpdatedRoomList();
        setRoomData(updatedRooms);
    }

    private List<Room> getUpdatedRoomList() {
        return List.of(
            new Room("101", "Single", 100, "Available"),
            new Room("102", "Double", 150, "Booked"),
            new Room("103", "Suite", 250, "Available")
        );
    }

    private void filterRooms() {
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        roomTable.setRowSorter(sorter);

        String text = searchField.getText();
        String statusFilter = (String) statusFilterCombo.getSelectedItem();

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1, 2, 3));

        if (statusFilter != null && !statusFilter.equals("All")) {
            sorter.setRowFilter(RowFilter.regexFilter(statusFilter, 3));
        }
    }

    private void enableSorting() {
        roomTable.getTableHeader().setReorderingAllowed(false);
    }
}
