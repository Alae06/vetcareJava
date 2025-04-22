package com.example.fxproject.controller;

import com.example.fxproject.model.Owner;
import com.example.fxproject.model.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class OwnerFormController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField searchField;
    @FXML private TableView<Owner> ownerTable;
    @FXML private TableColumn<Owner, String> firstNameColumn;
    @FXML private TableColumn<Owner, String> lastNameColumn;
    @FXML private TableColumn<Owner, String> addressColumn;
    @FXML private TableColumn<Owner, String> phoneColumn;
    @FXML private TableColumn<Owner, String> emailColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private DataManager dataManager;
    private FilteredList<Owner> filteredData;
    private Owner selectedOwner;
    private boolean isEditing = false;

    @FXML
    private void initialize() {
        dataManager = DataManager.getInstance();
        
        // Initialize the table columns
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        
        // Set up the filtered list
        filteredData = new FilteredList<>(dataManager.getOwners(), p -> true);
        
        // Create a sorted list from the filtered list
        SortedList<Owner> sortedData = new SortedList<>(filteredData);
        
        // Bind the sorted list to the table
        ownerTable.setItems(sortedData);
        sortedData.comparatorProperty().bind(ownerTable.comparatorProperty());
        
        // Add listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });
    }

    @FXML
    private void handleTableClick() {
        Owner owner = ownerTable.getSelectionModel().getSelectedItem();
        if (owner != null) {
            selectedOwner = owner;
            editButton.setDisable(false);
            deleteButton.setDisable(false);
            
            // Fill the form with the selected owner's data
            firstNameField.setText(owner.getFirstName());
            lastNameField.setText(owner.getLastName());
            addressField.setText(owner.getAddress());
            phoneField.setText(owner.getPhone());
            emailField.setText(owner.getEmail());
        }
    }

    @FXML
    private void handleEdit() {
        if (!isEditing) {
            // Start editing mode
            isEditing = true;
            editButton.setText("Update");
        } else {
            // Update the owner
            if (isInputValid()) {
                updateOwner();
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
        if (selectedOwner != null) {
            dataManager.removeOwner(selectedOwner);
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            showAlert("Success", "Owner deleted successfully!", AlertType.INFORMATION);
        }
    }

    private void updateOwner() {
        selectedOwner.setFirstName(firstNameField.getText().trim());
        selectedOwner.setLastName(lastNameField.getText().trim());
        selectedOwner.setAddress(addressField.getText().trim());
        selectedOwner.setPhone(phoneField.getText().trim());
        selectedOwner.setEmail(emailField.getText().trim());
        
        // Refresh the table
        ownerTable.refresh();
        showAlert("Success", "Owner updated successfully!", AlertType.INFORMATION);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText == null || searchText.isEmpty()) {
            filteredData.setPredicate(owner -> true);
        } else {
            filteredData.setPredicate(owner -> {
                String lastName = owner.getLastName().toLowerCase();
                return lastName.contains(searchText);
            });
        }
    }
    
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        filteredData.setPredicate(owner -> true);
    }

    @FXML
    private void handleSave() {
        if (isEditing) {
            showAlert("Error", "Please finish editing the current owner first.", AlertType.ERROR);
            return;
        }
        
        if (isInputValid()) {
            Owner owner = new Owner(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                addressField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
            );
            dataManager.addOwner(owner);
            showAlert("Success", "Owner saved successfully!", AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errorMessage += "First name is required!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            errorMessage += "Last name is required!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Phone number is required!\n";
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
        
        selectedOwner = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
    }
} 