package com.example.fxproject.controller;

import com.example.fxproject.model.Pet;
import com.example.fxproject.model.Owner;
import com.example.fxproject.model.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PetFormController {
    @FXML private TextField nameField;
    @FXML private TextField speciesField;
    @FXML private TextField breedField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Owner> ownerComboBox;
    @FXML private TableView<Pet> petTable;
    @FXML private TableColumn<Pet, String> nameColumn;
    @FXML private TableColumn<Pet, String> speciesColumn;
    @FXML private TableColumn<Pet, String> breedColumn;
    @FXML private TableColumn<Pet, String> birthDateColumn;
    @FXML private TableColumn<Pet, String> ownerColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private DataManager dataManager;
    private ObservableList<Owner> owners = FXCollections.observableArrayList();
    private Pet selectedPet;
    private boolean isEditing = false;

    @FXML
    private void initialize() {
        dataManager = DataManager.getInstance();
        
        // Initialize the owner combo box
        ownerComboBox.setItems(dataManager.getOwners());
        
        // Set the current date as the default birth date
        birthDatePicker.setValue(LocalDate.now());
        
        // Initialize the table columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        speciesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecies()));
        breedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBreed()));
        birthDateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getBirthDate();
            return new SimpleStringProperty(date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
        });
        ownerColumn.setCellValueFactory(cellData -> {
            Owner owner = dataManager.getOwnerForPet(cellData.getValue());
            return new SimpleStringProperty(owner != null ? owner.getFirstName() + " " + owner.getLastName() : "");
        });
        
        // Set the items to the table
        petTable.setItems(dataManager.getPets());
    }

    @FXML
    private void handleTableClick() {
        Pet pet = petTable.getSelectionModel().getSelectedItem();
        if (pet != null) {
            selectedPet = pet;
            editButton.setDisable(false);
            deleteButton.setDisable(false);
            
            // Fill the form with the selected pet's data
            nameField.setText(pet.getName());
            speciesField.setText(pet.getSpecies());
            breedField.setText(pet.getBreed());
            birthDatePicker.setValue(pet.getBirthDate());
            ownerComboBox.setValue(dataManager.getOwnerForPet(pet));
        }
    }

    @FXML
    private void handleEdit() {
        if (!isEditing) {
            // Start editing mode
            isEditing = true;
            editButton.setText("Update");
        } else {
            // Update the pet
            if (isInputValid()) {
                updatePet();
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
        if (selectedPet != null) {
            dataManager.removePet(selectedPet);
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            showAlert("Success", "Pet deleted successfully!", AlertType.INFORMATION);
        }
    }

    private void updatePet() {
        Owner oldOwner = dataManager.getOwnerForPet(selectedPet);
        Owner newOwner = ownerComboBox.getValue();
        
        selectedPet.setName(nameField.getText().trim());
        selectedPet.setSpecies(speciesField.getText().trim());
        selectedPet.setBreed(breedField.getText().trim());
        selectedPet.setBirthDate(birthDatePicker.getValue());
        selectedPet.setOwner(newOwner);
        
        // If the owner has changed, update the relationships
        if (oldOwner != newOwner) {
            dataManager.updatePetOwner(selectedPet, oldOwner, newOwner);
        }
        
        // Refresh the table
        petTable.refresh();
        showAlert("Success", "Pet updated successfully!", AlertType.INFORMATION);
    }

    @FXML
    private void handleSave() {
        if (isEditing) {
            showAlert("Error", "Please finish editing the current pet first.", AlertType.ERROR);
            return;
        }
        
        if (isInputValid()) {
            Pet pet = new Pet(
                nameField.getText().trim(),
                speciesField.getText().trim(),
                breedField.getText().trim(),
                birthDatePicker.getValue(),
                ownerComboBox.getValue()
            );
            dataManager.addPet(pet, ownerComboBox.getValue());
            showAlert("Success", "Pet saved successfully!", AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Pet name is required!\n";
        }
        if (speciesField.getText() == null || speciesField.getText().trim().isEmpty()) {
            errorMessage += "Species is required!\n";
        }
        if (birthDatePicker.getValue() == null) {
            errorMessage += "Birth date is required!\n";
        }
        if (ownerComboBox.getValue() == null) {
            errorMessage += "Owner is required!\n";
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
        
        selectedPet = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        
        nameField.clear();
        speciesField.clear();
        breedField.clear();
        birthDatePicker.setValue(LocalDate.now());
        ownerComboBox.setValue(null);
    }

    // Method to add an owner to the combo box
    public void addOwner(Owner owner) {
        owners.add(owner);
    }
} 