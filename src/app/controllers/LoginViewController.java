package app.controllers;

import utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.NavDAOException;
import model.Navigation;
import model.User;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;

public class LoginViewController {

    @FXML private TextField nicknameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private CheckBox showPasswordCheckBox;
    @FXML private Label statusLabel;
    @FXML private Label statusNameLabel;
    @FXML private Label statusPassLabel;

    @FXML
    private void initialize() {
        if (passwordVisibleField != null) {
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
        }
        if (passwordField != null && passwordVisibleField != null) {
            passwordField.textProperty().addListener((obs, oldText, newText) -> {
                if (!showPasswordCheckBox.isSelected()) {
                    passwordVisibleField.setText(newText);
                }
            });
            passwordVisibleField.textProperty().addListener((obs, oldText, newText) -> {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setText(newText);
                }
            });
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (showPasswordCheckBox.isSelected()) {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.requestFocus();
            passwordVisibleField.positionCaret(passwordVisibleField.getText().length());
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    @FXML
    private void handleLogin() throws NavDAOException {
        String nick = nicknameField.getText().trim();
        String pass;
        if (showPasswordCheckBox != null && showPasswordCheckBox.isSelected() && passwordVisibleField != null) {
            pass = passwordVisibleField.getText();
        } else {
            pass = passwordField.getText();
        }

        statusLabel.setText("");
        statusNameLabel.setText("");
        statusPassLabel.setText("");

        if (nick.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Por favor, complete todos los campos.");
            hideStatusAfterDelay();
            return;
        }

        Navigation nav = Navigation.getInstance();

        if (nav.exitsNickName(nick)) {
            User user = nav.authenticate(nick, pass);
            if (user != null) {
                SceneManager.getInstance().setUser(user);
                SceneManager.getInstance().showMainView(user);
            } else {
                statusPassLabel.setText("Contraseña incorrecta. Por favor, verifique sus credenciales.");
                passwordField.clear();
                if (passwordVisibleField != null) passwordVisibleField.clear();
                hideStatusAfterDelay();
            }
        } else {
            statusNameLabel.setText("El usuario no existe");
            hideStatusAfterDelay();
        }
    }

    @FXML
    private void handleGoToRegister() {
        statusLabel.setText("Redirigiendo a la página de registro...");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            SceneManager.getInstance().showRegister();
            statusLabel.setText("");
            statusNameLabel.setText("");
            statusPassLabel.setText("");
        });
        pause.play();
    }

    private void hideStatusAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            statusLabel.setText("");
            statusNameLabel.setText("");
            statusPassLabel.setText("");
        });
        pause.play();
    }
}