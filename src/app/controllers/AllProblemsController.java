package app.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Problem;
import model.Answer;
import model.Navigation;
import model.User;
import model.NavDAOException;
import utils.SceneManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AllProblemsController {

    @FXML private TextField searchField;
    @FXML private TableView<Problem> problemsTable;
    @FXML private TableColumn<Problem, String> idColumn;
    @FXML private TableColumn<Problem, String> titleColumn;
    @FXML private TableColumn<Problem, String> difficultyColumn;
    @FXML private TableColumn<Problem, String> categoryColumn;
    @FXML private TableColumn<Problem, String> statusColumn;
    @FXML private Label problemTitleLabel;
    @FXML private Label problemDescriptionLabel;
    @FXML private Button startButton;
    @FXML private ComboBox<String> difficultyFilter;

    private User user;
    private ObservableList<Problem> allProblems;
    private FilteredList<Problem> filteredProblems;

    @FXML
    public void initialize() throws NavDAOException {
        user = SceneManager.getInstance().getUser();
        if (user == null) {
            showAlert("Error", "Usuario no disponible", "No se pudo cargar la información del usuario.");
            return;
        }

        configureFilters();
        
        configureTableColumns();
        
        loadProblems();
        
        problemsTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> handleProblemSelection(newSelection));
    }
    
    private void configureFilters() {
        if (difficultyFilter != null) {
            difficultyFilter.setItems(FXCollections.observableArrayList(
                "Todos", "Básicos", "Intermedios", "Avanzados"
            ));
            difficultyFilter.getSelectionModel().selectFirst();
            difficultyFilter.setOnAction(event -> applyFilters());
        }
    }

    private void configureTableColumns() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(String.valueOf(cellData.getValue().hashCode())));
        }
        
        titleColumn.setCellValueFactory(cellData -> {
            String text = cellData.getValue().getText();
            return new SimpleStringProperty(extractTitle(text));
        });
        
        if (difficultyColumn != null) {
            difficultyColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Estándar"));
        }
        
        if (categoryColumn != null) {
            categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Navegación"));
        }
        
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty("Pendiente");
            });
        }
    }
    
    private String extractTitle(String text) {
        if (text == null || text.isEmpty()) {
            return "Sin título";
        }
        
        int endIndex = Math.min(text.length(), 50);
        if (endIndex < text.length()) {
            int lastSpace = text.lastIndexOf(' ', endIndex);
            if (lastSpace > 0) {
                endIndex = lastSpace;
            }
            return text.substring(0, endIndex) + "...";
        }
        return text;
    }

    private void loadProblems() throws NavDAOException {
        try {
            List<Problem> problems = Navigation.getInstance().getProblems();
            allProblems = FXCollections.observableArrayList(problems);
            
            filteredProblems = new FilteredList<>(allProblems, p -> true);
            problemsTable.setItems(filteredProblems);
            
            System.out.println("Problemas cargados: " + problems.size());
        } catch (Exception e) {
            System.err.println("Error al cargar los problemas: " + e.getMessage());
            showAlert("Error", "Error al cargar problemas", 
                      "No se pudieron cargar los problemas: " + e.getMessage());
        }
    }

    private void handleProblemSelection(Problem selectedProblem) {
        if (selectedProblem != null) {
            problemTitleLabel.setText(extractTitle(selectedProblem.getText()));
            problemDescriptionLabel.setText(selectedProblem.getText());
            startButton.setDisable(false);
        } else {
            problemTitleLabel.setText("Seleccione un problema para ver detalles");
            problemDescriptionLabel.setText("");
            startButton.setDisable(true);
        }
    }

    @FXML
    private void handleSearch() {
        applyFilters();
    }
    
    private void applyFilters() {
        String searchTerm = searchField.getText().toLowerCase();
        
        filteredProblems.setPredicate(problem -> {
            if (searchTerm.isEmpty()) {
                return true; 
            }
            
            return problem.getText().toLowerCase().contains(searchTerm);
        });
        
        if (filteredProblems.isEmpty()) {
            showAlert("Información", "Sin resultados", 
                     "No se encontraron problemas que coincidan con la búsqueda.");
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstance().showMainView(user);
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
private void handleStartProblem() {
    Problem selectedProblem = problemsTable.getSelectionModel().getSelectedItem();
    if (selectedProblem != null) {
        try {
            SceneManager.getInstance().setSelectedProblem(selectedProblem);
            
            SceneManager.getInstance().showProblemView();
        } catch (Exception e) {
            System.err.println("Error al mostrar el problema: " + e.getMessage());
            showAlert("Error", "Error al abrir problema", 
                     "No se pudo abrir el problema seleccionado: " + e.getMessage());
        }
    } else {
        showAlert("Aviso", "Problema no seleccionado", 
                 "Por favor, seleccione un problema de la lista.");
    }
}


}