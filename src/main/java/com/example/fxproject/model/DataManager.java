package com.example.fxproject.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    
    private final ObservableList<Owner> owners = FXCollections.observableArrayList();
    private final ObservableList<Pet> pets = FXCollections.observableArrayList();
    private final ObservableList<Veterinarian> veterinarians = FXCollections.observableArrayList();
    private final ObservableList<Visit> visits = FXCollections.observableArrayList();
    
    private final Map<Pet, Owner> petToOwner = new HashMap<>();
    private final Map<Owner, List<Pet>> ownerToPets = new HashMap<>();
    private final Map<Visit, Pet> visitToPet = new HashMap<>();
    private final Map<Visit, Veterinarian> visitToVet = new HashMap<>();
    
    private DataManager() {}
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    // Owner methods
    public void addOwner(Owner owner) {
        owners.add(owner);
        ownerToPets.put(owner, new ArrayList<>());
    }
    
    public ObservableList<Owner> getOwners() {
        return owners;
    }
    
    // Pet methods
    public void addPet(Pet pet, Owner owner) {
        pets.add(pet);
        petToOwner.put(pet, owner);
        ownerToPets.get(owner).add(pet);
    }
    
    public ObservableList<Pet> getPets() {
        return pets;
    }
    
    public List<Pet> getPetsForOwner(Owner owner) {
        return ownerToPets.getOrDefault(owner, new ArrayList<>());
    }
    
    public Owner getOwnerForPet(Pet pet) {
        return petToOwner.get(pet);
    }
    
    // Veterinarian methods
    public void addVeterinarian(Veterinarian vet) {
        veterinarians.add(vet);
    }
    
    public ObservableList<Veterinarian> getVeterinarians() {
        return veterinarians;
    }
    
    // Visit methods
    public void addVisit(Visit visit, Pet pet, Veterinarian vet) {
        visits.add(visit);
        visitToPet.put(visit, pet);
        visitToVet.put(visit, vet);
    }
    
    public ObservableList<Visit> getVisits() {
        return visits;
    }
    
    public List<Visit> getVisitsForPet(Pet pet) {
        List<Visit> petVisits = new ArrayList<>();
        for (Visit visit : visits) {
            if (visitToPet.get(visit) == pet) {
                petVisits.add(visit);
            }
        }
        return petVisits;
    }
    
    public List<Visit> getVisitsForVeterinarian(Veterinarian vet) {
        List<Visit> vetVisits = new ArrayList<>();
        for (Visit visit : visits) {
            if (visitToVet.get(visit) == vet) {
                vetVisits.add(visit);
            }
        }
        return vetVisits;
    }
    
    public Pet getPetForVisit(Visit visit) {
        return visitToPet.get(visit);
    }
    
    public Veterinarian getVeterinarianForVisit(Visit visit) {
        return visitToVet.get(visit);
    }

    public void removeVisit(Visit visit) {
        visits.remove(visit);
        visitToPet.remove(visit);
        visitToVet.remove(visit);
    }

    public void removeVeterinarian(Veterinarian vet) {
        // First, remove all visits associated with this veterinarian
        List<Visit> vetVisits = getVisitsForVeterinarian(vet);
        for (Visit visit : vetVisits) {
            removeVisit(visit);
        }
        
        // Then remove the veterinarian
        veterinarians.remove(vet);
    }

    public void removeOwner(Owner owner) {
        // First, get all pets owned by this owner
        List<Pet> ownerPets = getPetsForOwner(owner);
        
        // For each pet, remove all its visits
        for (Pet pet : ownerPets) {
            List<Visit> petVisits = getVisitsForPet(pet);
            for (Visit visit : petVisits) {
                removeVisit(visit);
            }
            // Remove the pet
            pets.remove(pet);
            petToOwner.remove(pet);
        }
        
        // Remove the owner from the owners list and the ownerToPets map
        owners.remove(owner);
        ownerToPets.remove(owner);
    }

    public void removePet(Pet pet) {
        // First, remove all visits associated with this pet
        List<Visit> petVisits = getVisitsForPet(pet);
        for (Visit visit : petVisits) {
            removeVisit(visit);
        }
        
        // Remove the pet from its owner's list
        Owner owner = getOwnerForPet(pet);
        if (owner != null) {
            ownerToPets.get(owner).remove(pet);
        }
        
        // Remove the pet from the pets list and the petToOwner map
        pets.remove(pet);
        petToOwner.remove(pet);
    }

    public void updatePetOwner(Pet pet, Owner oldOwner, Owner newOwner) {
        // Remove the pet from the old owner's list
        if (oldOwner != null) {
            ownerToPets.get(oldOwner).remove(pet);
        }
        
        // Add the pet to the new owner's list
        if (newOwner != null) {
            ownerToPets.get(newOwner).add(pet);
        }
        
        // Update the petToOwner map
        petToOwner.put(pet, newOwner);
    }
} 