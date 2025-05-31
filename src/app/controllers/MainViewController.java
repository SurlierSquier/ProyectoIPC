/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.User;
import utils.SceneManager;

public class MainViewController {

    private User user;

    public void setUser(User user) {
    if (user == null) {
        System.err.println("Usuario no establecido al cargar MainView.");
        return;
    }

    this.user = user;
    System.out.println("Usuario logueado: " + user.getNickName());
}


    @FXML
    private void goToProblem() {
        System.out.println("Ir a resolver problema...");
        SceneManager.getInstance().showProblemView();
    }

    @FXML
    private void goToResults() {
        System.out.println("Ver resultados...");
        SceneManager.getInstance().showResultsView();
    }

    @FXML
    private void goToProfile() {
        System.out.println("Modificar perfil...");

    SceneManager.getInstance().showProfile();
}
    

    @FXML
    private void logout() {
        System.out.println("Cerrando sesión...");
        SceneManager.getInstance().showLogin();
    }

}