// TrainDao.java
package dao;

import models.Train;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDao {
    public boolean createTables() {
        String trainTable = """
            CREATE TABLE IF NOT EXISTS trains (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                train_number VARCHAR(20) NOT NULL UNIQUE,
                train_name VARCHAR(100) NOT NULL
            )""";
            
        String stationTable = """
            CREATE TABLE IF NOT EXISTS stations (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                train_id BIGINT,
                station_name VARCHAR(100) NOT NULL,
                sequence INT NOT NULL,
                FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE
            )""";
            
        try (Connection conn = DBConnect.getconnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(trainTable);
            stmt.execute(stationTable);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveTrain(Train train, List<String> stations) {
        String trainSql = "INSERT INTO trains (train_number, train_name) VALUES (?, ?)";
        String stationSql = "INSERT INTO stations (train_id, station_name, sequence) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnect.getconnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(trainSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, train.getTrainNumber());
                pstmt.setString(2, train.getTrainName());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    long trainId = rs.getLong(1);
                    
                    try (PreparedStatement stationStmt = conn.prepareStatement(stationSql)) {
                        for (int i = 0; i < stations.size(); i++) {
                            stationStmt.setLong(1, trainId);
                            stationStmt.setString(2, stations.get(i));
                            stationStmt.setInt(3, i + 1);
                            stationStmt.addBatch();
                        }
                        stationStmt.executeBatch();
                    }
                }
                
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains";
        
        try (Connection conn = DBConnect.getconnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Train train = new Train();
                train.setId(rs.getLong("id"));
                train.setTrainNumber(rs.getString("train_number"));
                train.setTrainName(rs.getString("train_name"));
                trains.add(train);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }
    
    public boolean updateTrain(Train train, List<String> stations) {
        String updateTrainSql = "UPDATE trains SET train_number = ?, train_name = ? WHERE id = ?";
        String deleteStationsSql = "DELETE FROM stations WHERE train_id = ?";
        String insertStationsSql = "INSERT INTO stations (train_id, station_name, sequence) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.getconnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement updateTrainStmt = conn.prepareStatement(updateTrainSql)) {
                updateTrainStmt.setString(1, train.getTrainNumber());
                updateTrainStmt.setString(2, train.getTrainName());
                updateTrainStmt.setLong(3, train.getId());
                updateTrainStmt.executeUpdate();

                try (PreparedStatement deleteStationsStmt = conn.prepareStatement(deleteStationsSql)) {
                    deleteStationsStmt.setLong(1, train.getId());
                    deleteStationsStmt.executeUpdate();
                }

                try (PreparedStatement insertStationsStmt = conn.prepareStatement(insertStationsSql)) {
                    for (int i = 0; i < stations.size(); i++) {
                        insertStationsStmt.setLong(1, train.getId());
                        insertStationsStmt.setString(2, stations.get(i));
                        insertStationsStmt.setInt(3, i + 1);
                        insertStationsStmt.addBatch();
                    }
                    insertStationsStmt.executeBatch();
                }

                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTrain(Long trainId) {
        String deleteTrainSql = "DELETE FROM trains WHERE id = ?";
        try (Connection conn = DBConnect.getconnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteTrainSql)) {
            pstmt.setLong(1, trainId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
