package HospitalManagementProject;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class JavaApp extends Application {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/Hospital";
    private static final String user = "root";
    private static final String passwd = "HelloKushal";

    private Connection con;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        connectDB();

        TabPane tabs = new TabPane();

        tabs.getTabs().add(patientTab());
        tabs.getTabs().add(doctorTab());
        tabs.getTabs().add(appointmentTab());

        Image img = new Image(getClass().getResourceAsStream("icon.png"));

        Scene scene = new Scene(tabs, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hospital Management System");
        primaryStage.getIcons().add(img);
        primaryStage.show();
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            showError("DB Connection Failed: " + e.getMessage());
        }
    }

    /* --------------------- PATIENT TAB ---------------------- */

    private Tab patientTab() {

        Tab tab = new Tab("Patients");
        tab.setClosable(false);

        TextField nameField = new TextField();
        TextField ageField = new TextField();
        nameField.setFocusTraversable(false);
        ageField.setFocusTraversable(false);
        nameField.setPromptText("Name");
        ageField.setPromptText("Age");


        ComboBox<String> genderField = new ComboBox<>();
        genderField.getItems().addAll("Male", "Female", "Other");
        genderField.setPromptText("Select gender");

        ComboBox<String> cityField = new ComboBox<>();
        cityField.getItems().addAll(
                "Delhi/NCR","Mumbai","Bengaluru (Bangalore)","Hyderabad","Ahmedabad",
                "Chennai","Kolkata","Surat","Pune","Jaipur","Lucknow","Nagpur","Patna","Indore"
        );
        cityField.setEditable(true);
        cityField.setPromptText("Enter city");
        cityField.setVisibleRowCount(11);

        Button addBtn = new Button("Add Patient");
        Button viewBtn = new Button("Refresh List");

        // -------- TABLEVIEW ----------
        TableView<Patient> table = new TableView<>();

        TableColumn<Patient, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Patient, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, Integer> c3 = new TableColumn<>("Age");
        c3.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Patient, String> c4 = new TableColumn<>("Gender");
        c4.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Patient, String> c5 = new TableColumn<>("City");
        c5.setCellValueFactory(new PropertyValueFactory<>("city"));

        table.getColumns().addAll(c1, c2, c3, c4, c5);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addBtn.setOnAction(e -> new Patient(
                nameField.getText(),
                ageField.getText(),
                genderField.getValue(),
                cityField.getValue(),
                con
        ));

        viewBtn.setOnAction(e -> table.setItems(Patient.fetchPatients(con)));

        VBox box = new VBox(10,
                new Label("Add Patient:"),
                new Label("Name:"), nameField,
                new Label("Age:"), ageField,
                new Label("Gender:"), genderField,
                new Label("City:"), cityField,
                addBtn,
                new Separator(),
                viewBtn,
                table
        );

        box.setPadding(new Insets(10));
        tab.setContent(box);
        return tab;
    }

    /* --------------------- DOCTOR TAB ---------------------- */

    private Tab doctorTab() {

        Tab tab = new Tab("Doctors");
        tab.setClosable(false);

        TextField nameField = new TextField();
        TextField specialityField = new TextField();
        nameField.setPromptText("Name");
        specialityField.setPromptText("Speciality");


        Button addBtn = new Button("Add Doctor");
        Button viewBtn = new Button("Refresh List");

        TableView<Doctor> table = new TableView<>();

        TableColumn<Doctor, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Doctor, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Doctor, String> c3 = new TableColumn<>("Speciality");
        c3.setCellValueFactory(new PropertyValueFactory<>("speciality"));

        table.getColumns().addAll(c1, c2, c3);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addBtn.setOnAction(e -> new Doctor(
                nameField.getText(),
                specialityField.getText(),
                con
        ));

        viewBtn.setOnAction(e -> table.setItems(Doctor.fetchDoctors(con)));

        VBox box = new VBox(10,
                new Label("Add Doctor:"),
                new Label("Name:"), nameField,
                new Label("Speciality:"), specialityField,
                addBtn,
                new Separator(),
                viewBtn,
                table
        );

        box.setPadding(new Insets(10));
        tab.setContent(box);
        return tab;
    }

    /* --------------------- APPOINTMENT TAB ---------------------- */
    // (unchanged – already correct)

    private Tab appointmentTab() {
        Tab tab = new Tab("Appointments");
        tab.setClosable(false);

        ComboBox<Integer> patientIdField = new ComboBox<>();
        ObservableList<Integer> obsList = patientIdField.getItems();
        obsList.addAll(Patient.generatePatientId(con));
        patientIdField.setPromptText("Select Patient ID");

        ComboBox<Integer> doctorIdField = new ComboBox<>();
        ObservableList<Integer> idList = doctorIdField.getItems();
        idList.addAll(Doctor.generateDoctorId(con));
        doctorIdField.setPromptText("Select Doctor ID");

        DatePicker dateField = new DatePicker();
        dateField.setPromptText("DD/MM/YYYY");
        dateField.setEditable(false);

        Button scheduleBtn = new Button("Schedule Appointment");
        Button viewBtn = new Button("Refresh Appointments");

        TableView<Appointment> table = new TableView<>();

        TableColumn<Appointment, Integer> colPatient = new TableColumn<>("Patient ID");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<Appointment, Integer> colDoctor = new TableColumn<>("Doctor ID");
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));

        TableColumn<Appointment, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(colPatient, colDoctor, colDate);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        scheduleBtn.setOnAction(e -> new Appointment(
                patientIdField.getValue(),
                doctorIdField.getValue(),
                dateField.getValue().toString(),
                con
        ));

        viewBtn.setOnAction(e -> table.setItems(Appointment.fetchAppointments(con)));

        VBox box = new VBox(10,
                new Label("Schedule Appointment:"),
                new Label("Patient ID:"), patientIdField,
                new Label("Doctor ID:"), doctorIdField,
                new Label("Appointment Date:"), dateField,
                scheduleBtn,
                new Separator(),
                viewBtn,
                table
        );

        box.setPadding(new Insets(10));
        tab.setContent(box);
        return tab;
    }

    static void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    static void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
