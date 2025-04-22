package com.example.fxproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

public class VetCareApplication extends Application {
    private BorderPane mainLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("VetCare 360");

        // Create the main layout
        mainLayout = new BorderPane();

        // Create the menu bar
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Create the welcome content
        VBox welcomeContent = createWelcomeContent();
        mainLayout.setCenter(welcomeContent);

        // Create the scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu ownersMenu = new Menu("Owners");
        Menu petsMenu = new Menu("Pets");
        Menu visitsMenu = new Menu("Visits");
        Menu vetsMenu = new Menu("Veterinarians");

        // Add menu items
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Add menu items for Owners
        MenuItem addOwnerItem = new MenuItem("Add Owner");
        addOwnerItem.setOnAction(e -> showOwnerForm());
        ownersMenu.getItems().add(addOwnerItem);

        // Add menu items for Pets
        MenuItem addPetItem = new MenuItem("Add Pet");
        addPetItem.setOnAction(e -> showPetForm());
        petsMenu.getItems().add(addPetItem);

        // Add menu items for Visits
        MenuItem addVisitItem = new MenuItem("Add Visit");
        addVisitItem.setOnAction(e -> showVisitForm());
        visitsMenu.getItems().add(addVisitItem);

        // Add menu items for Veterinarians
        MenuItem addVetItem = new MenuItem("Add Veterinarian");
        addVetItem.setOnAction(e -> showVetForm());
        vetsMenu.getItems().add(addVetItem);

        // Add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, ownersMenu, petsMenu, visitsMenu, vetsMenu);

        return menuBar;
    }

    private VBox createWelcomeContent() {
        VBox welcomeBox = new VBox(10);
        welcomeBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label welcomeLabel = new Label("Welcome to VetCare 360");
        welcomeLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Label descriptionLabel = new Label("Veterinary Clinic Management System");
        descriptionLabel.setStyle("-fx-font-size: 16;");

        welcomeBox.getChildren().addAll(welcomeLabel, descriptionLabel);

        return welcomeBox;
    }

    private void showOwnerForm() {
        try {
            URL resource = getClass().getResource("/com/example/fxproject/owner-form.fxml");
            System.out.println("Loading owner form from: " + resource);
            VBox ownerForm = FXMLLoader.load(resource);
            mainLayout.setCenter(ownerForm);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load the owner form: " + e.getMessage());
        }
    }

    private void showPetForm() {
        try {
            URL resource = getClass().getResource("/com/example/fxproject/pet-form.fxml");
            System.out.println("Loading pet form from: " + resource);
            VBox petForm = FXMLLoader.load(resource);
            mainLayout.setCenter(petForm);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load the pet form: " + e.getMessage());
        }
    }

    private void showVisitForm() {
        try {
            URL resource = getClass().getResource("/com/example/fxproject/visit-form.fxml");
            System.out.println("Loading visit form from: " + resource);
            VBox visitForm = FXMLLoader.load(resource);
            mainLayout.setCenter(visitForm);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load the visit form: " + e.getMessage());
        }
    }

    private void showVetForm() {
        try {
            URL resource = getClass().getResource("/com/example/fxproject/vet-form.fxml");
            System.out.println("Loading vet form from: " + resource);
            VBox vetForm = FXMLLoader.load(resource);
            mainLayout.setCenter(vetForm);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load the veterinarian form: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 