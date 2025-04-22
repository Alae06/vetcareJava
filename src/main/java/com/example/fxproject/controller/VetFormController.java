package com.example.fxproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import com.example.fxproject.model.Veterinarian;
import com.example.fxproject.model.DataManager;
import javafx.beans.property.SimpleStringProperty;

public class VetFormController {
    @FXML private TextField nameField;
    @FXML private TextField specializationField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TableView<Veterinarian> veterinarianTable;
    @FXML private TableColumn<Veterinarian, String> nameColumn;
    @FXML private TableColumn<Veterinarian, String> specializationColumn;
    @FXML private TableColumn<Veterinarian, String> phoneColumn;
    @FXML private TableColumn<Veterinarian, String> emailColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private DataManager dataManager;
    private Veterinarian selectedVeterinarian;
    private boolean isEditing = false;

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
    private void handleTableClick() {
        Veterinarian vet = veterinarianTable.getSelectionModel().getSelectedItem();
        if (vet != null) {
            selectedVeterinarian = vet;
            editButton.setDisable(false);
            deleteButton.setDisable(false);
            
            // Fill the form with the selected veterinarian's data
            nameField.setText(vet.getName());
            specializationField.setText(vet.getSpecialization());
            phoneField.setText(vet.getPhone());
            emailField.setText(vet.getEmail());
        }
    }

    @FXML
    private void handleEdit() {
        if (!isEditing) {
            // Start editing mode
            isEditing = true;
            editButton.setText("Update");
        } else {
            // Update the veterinarian
            if (isInputValid()) {
                updateVeterinarian();
                isEditing = false;
                editButton.setText("Edit");
                clearFields();
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedVeterinarian != null) {
            dataManager.removeVeterinarian(selectedVeterinarian);
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            showAlert("Success", "Veterinarian deleted successfully!", AlertType.INFORMATION);
        }
    }

    private void updateVeterinarian() {
        selectedVeterinarian.setName(nameField.getText().trim());
        selectedVeterinarian.setSpecialization(specializationField.getText().trim());
        selectedVeterinarian.setPhone(phoneField.getText().trim());
        selectedVeterinarian.setEmail(emailField.getText().trim());
        
        // Refresh the table
        veterinarianTable.refresh();
        showAlert("Success", "Veterinarian updated successfully!", AlertType.INFORMATION);
    }

    @FXML
    private void handleSave() {
        if (isEditing) {
            showAlert("Error", "Please finish editing the current veterinarian first.", AlertType.ERROR);
            return;
        }
        
        if (isInputValid()) {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            Veterinarian veterinarian = new Veterinarian(name, specialization, phone, email);
            dataManager.addVeterinarian(veterinarian);
            showAlert("Success", "Veterinarian saved successfully!", AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Name is required!\n";
        }
        if (specializationField.getText() == null || specializationField.getText().trim().isEmpty()) {
            errorMessage += "Specialization is required!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Phone is required!\n";
        }
        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            errorMessage += "Email is required!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage, AlertType.ERROR);
            return false;
        }
    }

    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void clearFields() {
        if (isEditing) {
            isEditing = false;
            editButton.setText("Edit");
        }
        
        selectedVeterinarian = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        
        nameField.clear();
        specializationField.clear();
        phoneField.clear();
        emailField.clear();
    }
} 