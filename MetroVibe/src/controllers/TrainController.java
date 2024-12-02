//TrainController.java
package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Train;
import dao.TrainDao;
import java.util.Arrays;
import java.util.List;

public class TrainController {

	private TrainDao trainDao;
    private ObservableList<Train> trainList;
    
@FXML private TableView<Train> trainTable;
@FXML private TableColumn<Train, String> trainNumberColumn;
@FXML private TableColumn<Train, String> trainNameColumn;
@FXML private TextField trainNumberField;
@FXML private TextField trainNameField;
@FXML private TextArea stationsArea;

	@FXML
	public void initialize() {
	    // Initialize train table columns
	    trainNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trainNumber"));
	    trainNameColumn.setCellValueFactory(new PropertyValueFactory<>("trainName"));
	
	    // Load train data
	    trainDao = new TrainDao();
	    trainList = FXCollections.observableArrayList();
	    trainList.addAll(trainDao.getAllTrains());
	    trainTable.setItems(trainList);
	}
	
	
	@FXML
	private void handleAddTrain() {
	    String trainNumber = trainNumberField.getText();
	    String trainName = trainNameField.getText();
	    String stationsText = stationsArea.getText();
	    
	    if (trainNumber.isEmpty() || trainName.isEmpty() || stationsText.isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Error", "All fields are required");
	        return;
	    }
	    
	    List<String> stations = Arrays.asList(stationsText.split("\n"));
	    if (stations.isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Error", "At least one station is required");
	        return;
	    }
	    
	    Train train = new Train();
	    train.setTrainNumber(trainNumber);
	    train.setTrainName(trainName);
	    
	    if (trainDao.saveTrain(train, stations)) {
	        showAlert(Alert.AlertType.INFORMATION, "Success", "Train added successfully");
	        clearFields();
	        refreshTrainList();
	    } else {
	        showAlert(Alert.AlertType.ERROR, "Error", "Failed to add train");
	    }
	}
	
	private void refreshTrainList() {
	    trainList.clear();
	    trainList.addAll(trainDao.getAllTrains());
	    trainTable.setItems(trainList);
	}
	
	
	
	
	
	@FXML
	private void handleUpdateTrain() {
	    Train selectedTrain = trainTable.getSelectionModel().getSelectedItem();
	    if (selectedTrain == null) {
	        showAlert(Alert.AlertType.ERROR, "Error", "No train selected for update.");
	        return;
	    }
	    String trainNumber = trainNumberField.getText();
	    String trainName = trainNameField.getText();
	    String stationsText = stationsArea.getText();
	
	    if (trainNumber.isEmpty() || trainName.isEmpty() || stationsText.isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
	        return;
	    }
	
	    List<String> stations = Arrays.asList(stationsText.split("\n"));
	    selectedTrain.setTrainNumber(trainNumber);
	    selectedTrain.setTrainName(trainName);
	
	    if (trainDao.updateTrain(selectedTrain, stations)) {
	        showAlert(Alert.AlertType.INFORMATION, "Success", "Train updated successfully.");
	        refreshTrainList();
	        clearFields();
	    } else {
	        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update train.");
	    }
	}
	
	@FXML
	private void handleDeleteTrain() {
	    Train selectedTrain = trainTable.getSelectionModel().getSelectedItem();
	    if (selectedTrain == null) {
	        showAlert(Alert.AlertType.ERROR, "Error", "No train selected for deletion.");
	        return;
	    }
	
	    if (trainDao.deleteTrain(selectedTrain.getId())) {
	        showAlert(Alert.AlertType.INFORMATION, "Success", "Train deleted successfully.");
	        refreshTrainList();
	    } else {
	        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete train.");
	    }
	}

    private void clearFields() {
        trainNumberField.clear();
        trainNameField.clear();
        stationsArea.clear();
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}