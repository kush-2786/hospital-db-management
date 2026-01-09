package HospitalManagementProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Appointment {

    private int patientId;
    private int doctorId;
    private String date;

    public Appointment(int piD, int diD, String date, Connection con) {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?,?,?)");
            ps.setInt(1, piD);
            ps.setInt(2, diD);
            ps.setString(3, date);
            ps.executeUpdate();

            JavaApp.showInfo("Appointment added successfully!");

        } catch (Exception e)
        {
            JavaApp.showError("Error: " + e.getMessage());
        }
    }

    public Appointment(int pid, int did, String date) {
        this.patientId = pid;
        this.doctorId = did;
        this.date = date;
    }

    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getDate() { return date; }

    public static ObservableList<Appointment> fetchAppointments(Connection con) {

        ObservableList<Appointment> list = FXCollections.observableArrayList();

        try
        {
            PreparedStatement st = con.prepareStatement("SELECT * FROM appointments");
            ResultSet rs = st.executeQuery();

            while (rs.next())
            {
                list.add(new Appointment(
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date")
                ));
            }

        } catch (SQLException e) {
            JavaApp.showError("Error: " + e.getMessage());
        }

        return list;
    }
}
