package com.example.fxproject.controller;

import com.example.fxproject.model.Visit;
import com.example.fxproject.model.Pet;
import com.example.fxproject.model.Veterinarian;
import com.example.fxproject.model.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VisitFormController {
    @FXML private ComboBox<Pet> petComboBox;
    @FXML private ComboBox<Veterinarian> vetComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextArea reasonArea;
    @FXML private TextArea diagnosisArea;
    @FXML private TextArea treatmentArea;
    @FXML private TextArea notesArea;
    @FXML private TableView<Visit> visitTable;
    @FXML private TableColumn<Visit, String> petColumn;
    @FXML private TableColumn<Visit, String> vetColumn;
    @FXML private TableColumn<Visit, String> dateTimeColumn;
    @FXML private TableColumn<Visit, String> reasonColumn;
    @FXML private TableColumn<Visit, String> diagnosisColumn;
    @FXML private TableColumn<Visit, String> treatmentColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private DataManager dataManager;
    private Visit selectedVisit;
    private boolean isEditing = false;

    @FXML
    private void initialize() {
        dataManager = DataManager.getInstance();
        
        // Initialize the combo boxes
        petComboBox.setItems(dataManager.getPets());
        vetComboBox.setItems(dataManager.getVeterinarians());
        
        // Set the current date as the default date
        datePicker.setValue(LocalDate.now());
        
        // Set the current time as the default time
        LocalTime now = LocalTime.now();
        timeField.setText(String.format("%02d:%02d", now.getHour(), now.getMinute()));
        
        // Initialize the table columns
        petColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty(pet != null ? pet.getName() : "");
        });
        vetColumn.setCellValueFactory(cellData -> {
            Veterinarian vet = cellData.getValue().getVeterinarian();
            return new SimpleStringProperty(vet != null ? vet.getName() : "");
        });
        dateTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getDateTime();
            return new SimpleStringProperty(dateTime != null ? 
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
        });
        reasonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReason()));
        diagnosisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiagnosis()));
        treatmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTreatment()));
        
        // Set the items to the table
        visitTable.setItems(dataManager.getVisits());
    }

    @FXML
    private void handleTableClick() {
        Visit visit = visitTable.getSelectionModel().getSelectedItem();
        if (visit != null) {
            selectedVisit = visit;
            editButton.setDisable(false);
            deleteButton.setDisable(false);
            
            // Fill the form with the selected visit's data
            petComboBox.setValue(visit.getPet());
            vetComboBox.setValue(visit.getVeterinarian());
            datePicker.setValue(visit.getDateTime().toLocalDate());
            timeField.setText(visit.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            reasonArea.setText(visit.getReason());
            diagnosisArea.setText(visit.getDiagnosis());
            treatmentArea.setText(visit.getTreatment());
            notesArea.setText(visit.getNotes());
        }
    }

    @FXML
    private void handleEdit() {
        if (!isEditing) {
            // Start editing mode
            isEditing = true;
            editButton.setText("Update");
        } else {
            // Update the visit
            if (isInputValid()) {
                updateVisit();
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
        if (selectedVisit != null) {
            dataManager.removeVisit(selectedVisit);
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            showAlert("Success", "Visit deleted successfully!", AlertType.INFORMATION);
        }
    }

    private void updateVisit() {
        String[] timeParts = timeField.getText().split(":");
        LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), time);
        
        selectedVisit.setPet(petComboBox.getValue());
        selectedVisit.setVeterinarian(vetComboBox.getValue());
        selectedVisit.setDateTime(dateTime);
        selectedVisit.setReason(reasonArea.getText());
        selectedVisit.setDiagnosis(diagnosisArea.getText());
        selectedVisit.setTreatment(treatmentArea.getText());
        selectedVisit.setNotes(notesArea.getText());
        
        // Refresh the table
        visitTable.refresh();
        showAlert("Success", "Visit updated successfully!", AlertType.INFORMATION);
    }

    @FXML
    private void handleSave() {
        if (isEditing) {
            showAlert("Error", "Please finish editing the current visit first.", AlertType.ERROR);
            return;
        }
        
        if (isInputValid()) {
            // Parse the time from the time field
            String[] timeParts = timeField.getText().split(":");
            LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
            
            // Combine date and time
            LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), time);
            
            Visit visit = new Visit(
                petComboBox.getValue(),
                vetComboBox.getValue(),
                dateTime,
                reasonArea.getText()
            );
            
            // Set additional fields
            visit.setDiagnosis(diagnosisArea.getText());
            visit.setTreatment(treatmentArea.getText());
            visit.setNotes(notesArea.getText());
            
            dataManager.addVisit(visit, petComboBox.getValue(), vetComboBox.getValue());
            showAlert("Success", "Visit saved successfully!", AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (petComboBox.getValue() == null) {
            errorMessage += "Pet is required!\n";
        }
        if (vetComboBox.getValue() == null) {
            errorMessage += "Veterinarian is required!\n";
        }
        if (datePicker.getValue() == null) {
            errorMessage += "Date is required!\n";
        }
        if (timeField.getText() == null || timeField.getText().trim().isEmpty()) {
            errorMessage += "Time is required!\n";
        } else {
            // Validate time format
            try {
                String[] timeParts = timeField.getText().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                
                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                    errorMessage += "Invalid time format! Use HH:MM (24-hour format).\n";
                }
            } catch (Exception e) {
                errorMessage += "Invalid time format! Use HH:MM (24-hour format).\n";
            }
        }
        if (reasonArea.getText() == null || reasonArea.getText().trim().isEmpty()) {
            errorMessage += "Reason for visit is required!\n";
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
        
        selectedVisit = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        
        petComboBox.setValue(null);
        vetComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        
        LocalTime now = LocalTime.now();
        timeField.setText(String.format("%02d:%02d", now.getHour(), now.getMinute()));
        
        reasonArea.clear();
        diagnosisArea.clear();
        treatmentArea.clear();
        notesArea.clear();
    }

    // Methods to add pets and veterinarians to the combo boxes
    public void addPet(Pet pet) {
        // This method is no longer needed as we're using DataManager directly
    }
    
    public void addVeterinarian(Veterinarian vet) {
        dataManager.addVeterinarian(vet);
    }
} 