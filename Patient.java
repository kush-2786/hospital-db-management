package HospitalManagementProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;

public class Patient {

    private int id;
    private String name;
    private int age;
    private String gender;
    private String city;

    public Patient(String name, String ageStr, String gender, String city, Connection con) {

        try {
            int age = Integer.parseInt(ageStr);

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO patients(name, age, gender, city) VALUES (?,?,?,?)");
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, city);
            ps.executeUpdate();

            JavaApp.showInfo("Patient added successfully!");

        } catch (Exception e) {
            JavaApp.showError("Error: " + e.getMessage());
        }
    }

    public Patient(int id, String name, int age, String gender, String city) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.city = city;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getCity() { return city; }

    public static ObservableList<Patient> fetchPatients(Connection con) {

        ObservableList<Patient> list = FXCollections.observableArrayList();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM patients");

            while (rs.next()) {
                list.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("city")
                ));
            }

        } catch (SQLException e) { }

        return list;
    }

    public static ArrayList<Integer> generatePatientId(Connection con) {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT id FROM patients");
            while (rs.next()) ids.add(rs.getInt(1));
        } catch (Exception ignored) {}
        return ids;
    }
}
