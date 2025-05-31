/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Session;
import model.User;
import utils.SceneManager;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ResultsViewController {

    @FXML private DatePicker filterDatePicker;
    @FXML private TableView<Session> resultsTable;
    @FXML private TableColumn<Session, LocalDateTime> dateColumn;
    @FXML private TableColumn<Session, Integer> hitsColumn;
    @FXML private TableColumn<Session, Integer> faultsColumn;
    @FXML private Button backButton;
    
    @FXML private Label totalSessionsLabel;
    @FXML private Label totalHitsLabel;
    @FXML private Label totalErrorsLabel;
    @FXML private Label hitPercentageLabel;

    private User user;
    private List<Session> allSessions;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DecimalFormat percentageFormatter = new DecimalFormat("0.0%");

    @FXML
    public void initialize() {
        user = SceneManager.getInstance().getUser();

        if (user == null) {
            showAlert("Error", "Usuario no disponible", "No se pudo cargar la información del usuario.");
            return;
        }

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        dateColumn.setCellFactory(column -> new TableCell<Session, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });
        
        hitsColumn.setCellValueFactory(new PropertyValueFactory<>("hits"));
        faultsColumn.setCellValueFactory(new PropertyValueFactory<>("faults"));

        configureFilterDatePicker();
        
        allSessions = SceneManager.getInstance().getUserSessions();
        showSessions(allSessions);
        
        updateStatistics(allSessions);
    }

    private void configureFilterDatePicker() {
        filterDatePicker.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, formatter) : null;
            }
        });
        
        filterDatePicker.setPromptText("dd/mm/aaaa");
    }

    private void showSessions(List<Session> sessions) {
        ObservableList<Session> observableSessions = FXCollections.observableArrayList(sessions);
        resultsTable.setItems(observableSessions);
        
        updateStatistics(sessions);
    }

    private void updateStatistics(List<Session> sessions) {
        if (totalSessionsLabel == null) {
            return;
        }
        
        int totalSessions = sessions.size();
        int totalHits = sessions.stream().mapToInt(Session::getHits).sum();
        int totalFaults = sessions.stream().mapToInt(Session::getFaults).sum();
        double hitPercentage = totalHits + totalFaults > 0 ? 
                (double) totalHits / (totalHits + totalFaults) : 0.0;
        
        totalSessionsLabel.setText(String.valueOf(totalSessions));
        totalHitsLabel.setText(String.valueOf(totalHits));
        totalErrorsLabel.setText(String.valueOf(totalFaults));
        hitPercentageLabel.setText(percentageFormatter.format(hitPercentage));
    }

    @FXML
    private void handleFilter() {
        LocalDate selectedDate = filterDatePicker.getValue();
        
        if (selectedDate != null) {
            List<Session> filtered = allSessions.stream()
                .filter(s -> s.getTimeStamp().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
                
            showSessions(filtered);
            
            if (filtered.isEmpty()) {
                showAlert("Información", "Sin resultados", 
                         "No se encontraron sesiones para la fecha seleccionada.");
            }
        } else {
            showSessions(allSessions);
        }
    }
    
    @FXML
    private void handleShowAll() {
        filterDatePicker.setValue(null);
        showSessions(allSessions);
    }

    @FXML
    private void backToMain() {
        SceneManager.getInstance().showMainView(user);
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}