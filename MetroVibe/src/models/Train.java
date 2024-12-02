package models;

public class Train {
    private Long id;
    private String trainNumber;
    private String trainName;
    
    public Train() {}
    
    public Train(Long id, String trainNumber, String trainName) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
}