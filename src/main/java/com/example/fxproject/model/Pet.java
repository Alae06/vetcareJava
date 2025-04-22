package com.example.fxproject.model;

import java.time.LocalDate;

public class Pet {
    private String id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private Owner owner;

    public Pet(String name, String species, String breed, LocalDate birthDate, Owner owner) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
        this.owner = owner;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    @Override
    public String toString() {
        return name + " (" + species + ")";
    }
} 