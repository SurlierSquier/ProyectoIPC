/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import app.controllers.MainViewController;
import app.controllers.ProfileViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.Problem;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private User currentUser; 
    private final List<Session> sessionCache = new ArrayList<>();


    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void init(Stage stage) {
        this.primaryStage = stage;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void showLogin() {
        loadScene("LoginView.fxml");
    }

    public void showRegister() {
        loadScene("RegisterView.fxml");
    }

    public void showMainView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            
            SceneManager.getInstance().setUser(currentUser);controller.setUser(currentUser);
            
            setUser(currentUser); // ✅ guardar usuario
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProblemView() {
        loadScene("ProblemView.fxml");
    }

    public void showResultsView() {
        loadScene("ResultsView.fxml");
    }

    public void showProfile() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/ProfileView.fxml"));
        Parent root = loader.load();
        ProfileViewController controller = loader.getController();

        if (currentUser == null) {
            System.err.println("Usuario no establecido en SceneManager");
            return;
        }

        controller.setUser(currentUser);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private void loadScene(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/" + fxmlName));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    public void addSession(int hits, int faults) {
    if (currentUser != null) {
        Session session = new Session(LocalDateTime.now(), hits, faults);
        currentUser.addSession(hits, faults);
        sessionCache.add(session);
        System.out.println("Sesión añadida al cache y al usuario.");
    } else {
        System.err.println("No hay usuario en SceneManager.");
    }
}

public List<Session> getUserSessions() {
    if (currentUser != null && currentUser.getSessions() != null && !currentUser.getSessions().isEmpty()) {
        return currentUser.getSessions();
    }
    return sessionCache;
}


private Problem selectedProblem = null;

public void setSelectedProblem(Problem problem) {
    this.selectedProblem = problem;
}

public Problem getSelectedProblem() {
    return selectedProblem;
}

public void showAllProblemsView() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/AllProblems.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
    } catch (IOException e) {
        System.err.println("Error al cargar la vista de todos los problemas: " + e.getMessage());
        e.printStackTrace();
    }
}

    

}
