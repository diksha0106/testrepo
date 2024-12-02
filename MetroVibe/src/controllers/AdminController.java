package controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import dao.TrainDao;
import dao.UserLoginDao;
import models.Train;
import models.UserLogin;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminController {
    @FXML private TableView<UserLogin> userTable;
    @FXML private TableColumn<UserLogin, Long> idColumn;
    @FXML private TableColumn<UserLogin, String> firstNameColumn;
    @FXML private TableColumn<UserLogin, String> lastNameColumn;
    @FXML private TableColumn<UserLogin, String> emailColumn;
    @FXML private TableColumn<UserLogin, String> phoneColumn;
    @FXML private TableColumn<UserLogin, String> addressColumn;
    @FXML private TableColumn<UserLogin, String> roleColumn;

    private UserLoginDao userDao;
    private ObservableList<UserLogin> userList;

    @FXML
    public void initialize() {
        userDao = new UserLoginDao();
        userList = FXCollections.observableArrayList();

        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        

        refreshUserList();
    }

    @FXML
    private void refreshUserList() {
        userList.clear();
        userList.addAll(userDao.getAllUsers());
        userTable.setItems(userList);
    }

    @FXML
    private void deleteUser() {
        UserLogin selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if (selectedUser.getRole().equals("admin")) {
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete admin account");
                return;
            }

            if (userDao.deleteUser(selectedUser.getId())) {
                refreshUserList();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user");
            }
        }
    }

    @FXML
    private void logout() {
        try {
            Stage stage = (Stage) userTable.getScene().getWindow();
            URL location = getClass().getResource("/views/LoginView.fxml");
            if (location == null) {
                throw new Exception("Cannot find LoginView.fxml");
            }
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showTrainManagement() {
        try {
            // Load the TrainView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TrainView.fxml"));
            Parent root = loader.load();

            // Create a new stage for Train Management
            Stage stage = new Stage();
            stage.setTitle("Train Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load train management view: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}