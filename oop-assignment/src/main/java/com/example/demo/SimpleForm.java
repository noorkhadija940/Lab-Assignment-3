package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleForm extends Application {
    private List<String[]> records = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Create labels and text fields
        Label lblFullName = new Label("Full Name:");
        TextField txtFullName = new TextField();

        Label lblID = new Label("ID:");
        TextField txtID = new TextField();

        Label lblGender = new Label("Gender:");
        TextField txtGender = new TextField();

        Label lblHomeProvince = new Label("Home Province:");
        TextField txtHomeProvince = new TextField();

        Label lblDOB = new Label("DOB:");
        TextField txtDOB = new TextField();

        txtFullName.setPrefWidth(200);
        txtID.setPrefWidth(200);
        txtGender.setPrefWidth(200);
        txtHomeProvince.setPrefWidth(200);
        txtDOB.setPrefWidth(200);

        // Buttons
        Button btnNew = new Button("New");
        Button btnDelete = new Button("Delete");
        Button btnFind = new Button("Find");
        Button btnClose = new Button("Close");

        // Style buttons to black
        btnNew.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        btnDelete.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        btnFind.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        btnClose.setStyle("-fx-background-color: black; -fx-text-fill: white;");

        // Load records from file into the list
        loadRecords();

        // New button functionality
        btnNew.setOnAction(e -> {
            String fullName = txtFullName.getText().trim();
            String id = txtID.getText().trim();
            String gender = txtGender.getText().trim();
            String homeProvince = txtHomeProvince.getText().trim();
            String dob = txtDOB.getText().trim();

            if (id.isEmpty() || fullName.isEmpty() || gender.isEmpty() || homeProvince.isEmpty() || dob.isEmpty()) {
                showAlert("Validation Error", "All fields are required to save the record.");
                return;
            }

            try (FileWriter writer = new FileWriter("records.txt", true)) {
                writer.write(id + "," + fullName + "," + gender + "," + homeProvince + "," + dob + "\n");
                records.add(new String[]{id, fullName, gender, homeProvince, dob});
                showAlert("Success", "Record saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("File Error", "Error writing to the file.");
            }

            clearFields(txtFullName, txtID, txtGender, txtHomeProvince, txtDOB);
        });

        // Find button functionality
        btnFind.setOnAction(e -> {
            String idToFind = txtID.getText().trim();

            if (idToFind.isEmpty()) {
                showAlert("Validation Error", "Please enter an ID to search.");
                return;
            }

            boolean recordFound = false;
            for (String[] record : records) {
                if (record[0].equals(idToFind)) {
                    populateFields(record, txtFullName, txtID, txtGender, txtHomeProvince, txtDOB);
                    recordFound = true;
                    break;
                }
            }

            if (!recordFound) {
                showAlert("Not Found", "No record found with the given ID.");
            }
        });

        // Delete button functionality
        btnDelete.setOnAction(e -> {
            String idToDelete = txtID.getText().trim();

            if (idToDelete.isEmpty()) {
                showAlert("Validation Error", "Please enter an ID to delete.");
                return;
            }

            boolean recordDeleted = false;
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i)[0].equals(idToDelete)) {
                    records.remove(i);
                    recordDeleted = true;
                    break;
                }
            }

            if (recordDeleted) {
                saveRecordsToFile();
                showAlert("Success", "Record deleted successfully!");
                clearFields(txtFullName, txtID, txtGender, txtHomeProvince, txtDOB);
            } else {
                showAlert("Not Found", "No record found with the given ID.");
            }
        });

        // Close button functionality
        btnClose.setOnAction(e -> primaryStage.close());

        // Layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);

        formGrid.add(lblFullName, 0, 0);
        formGrid.add(txtFullName, 1, 0);
        formGrid.add(lblID, 0, 1);
        formGrid.add(txtID, 1, 1);
        formGrid.add(lblGender, 0, 2);
        formGrid.add(txtGender, 1, 2);
        formGrid.add(lblHomeProvince, 0, 3);
        formGrid.add(txtHomeProvince, 1, 3);
        formGrid.add(lblDOB, 0, 4);
        formGrid.add(txtDOB, 1, 4);

        VBox buttonBox = new VBox(10, btnNew, btnDelete, btnFind, btnClose);
        HBox root = new HBox(20, formGrid, buttonBox);

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setTitle("Simple Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRecords() {
        records.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("No file found or error reading file.");
        }
    }

    private void saveRecordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("records.txt"))) {
            for (String[] record : records) {
                writer.write(String.join(",", record) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateFields(String[] record, TextField txtFullName, TextField txtID, TextField txtGender,
                                TextField txtHomeProvince, TextField txtDOB) {
        txtID.setText(record[0]);
        txtFullName.setText(record[1]);
        txtGender.setText(record[2]);
        txtHomeProvince.setText(record[3]);
        txtDOB.setText(record[4]);
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
