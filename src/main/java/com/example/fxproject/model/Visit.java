package com.example.fxproject.model;

import java.time.LocalDateTime;

public class Visit {
    private String id;
    private Pet pet;
    private Veterinarian veterinarian;
    private LocalDateTime dateTime;
    private String reason;
    private String diagnosis;
    private String treatment;
    private String notes;

    public Visit(Pet pet, Veterinarian veterinarian, LocalDateTime dateTime, String reason) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.dateTime = dateTime;
        this.reason = reason;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    
    public Veterinarian getVeterinarian() { return veterinarian; }
    public void setVeterinarian(Veterinarian veterinarian) { this.veterinarian = veterinarian; }
    
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Visit for " + pet.getName() + " with " + veterinarian.toString() + " on " + dateTime.toString();
    }
} 