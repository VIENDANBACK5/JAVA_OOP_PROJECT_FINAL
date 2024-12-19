package HotelManagement.controller;

import HotelManagement.model.DatabaseConnection;
import HotelManagement.model.Room;
import HotelManagement.view.RoomView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RoomController {
    private RoomView roomView;

    public RoomController(RoomView roomView) {
        this.roomView = roomView;
        loadRoomData();

        roomView.getRefreshButton().addActionListener(e -> loadRoomData());
    }

    private void loadRoomData() {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM rooms";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getString("id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("status")
                ));
            }
            roomView.setRoomData(rooms);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(roomView, "Error loading room data!");
        }
    }
}
