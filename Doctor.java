package HospitalManagementProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;

public class Doctor {

    private int id;
    private String name;
    private String speciality;

    public Doctor(String name, String speciality, Connection con) {

        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO doctors(name, speciality) VALUES (?,?)");
            ps.setString(1, name);
            ps.setString(2, speciality);
            ps.executeUpdate();

            JavaApp.showInfo("Doctor added successfully!");

        } catch (Exception e) {
            JavaApp.showError("Error: " + e.getMessage());
        }
    }

    public Doctor(int id, String name, String speciality) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpeciality() { return speciality; }

    public static ObservableList<Doctor> fetchDoctors(Connection con) {

        ObservableList<Doctor> list = FXCollections.observableArrayList();

        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM doctors");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("speciality")
                ));
            }

        } catch (SQLException ex) {}

        return list;
    }

    public static ArrayList<Integer> generateDoctorId(Connection con) {

        ArrayList<Integer> list = new ArrayList<>();

        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT id FROM doctors");
            while (rs.next())
            {
                list.add(rs.getInt(1));
            }
        } catch (Exception e) {}

        return list;
    }
}
