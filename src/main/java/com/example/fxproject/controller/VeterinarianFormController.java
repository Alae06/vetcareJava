package com.example.fxproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import com.example.fxproject.model.Veterinarian;
import com.example.fxproject.model.DataManager;
import javafx.beans.property.SimpleStringProperty;

public class VeterinarianFormController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TableView<Veterinarian> veterinarianTable;
    @FXML
    private TableColumn<Veterinarian, String> nameColumn;
    @FXML
    private TableColumn<Veterinarian, String> specializationColumn;
    @FXML
    private TableColumn<Veterinarian, String> phoneColumn;
    @FXML
    private TableColumn<Veterinarian, String> emailColumn;

    private DataManager dataManager;

    @FXML
    private void initialize() {
        dataManager = DataManager.getInstance();
        
        // Initialize the table columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        specializationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecialization()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        
        // Set the items to the table
        veterinarianTable.setItems(dataManager.getVeterinarians());
    }

    @FXML
    public void handleAddVeterinarian() {
        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        Veterinarian veterinarian = new Veterinarian(name, specialization, phone, email);
        dataManager.addVeterinarian(veterinarian);
        clearFields();
    }

    @FXML
    public void clearFields() {
        nameField.clear();
        specializationField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}