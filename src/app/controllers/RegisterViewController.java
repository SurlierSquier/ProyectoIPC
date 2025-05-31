package app.controllers;

import utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import model.Navigation;
import model.User;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.NavDAOException;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class RegisterViewController {

    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker birthdatePicker;
    @FXML private Label statusLabel;
    @FXML private ImageView avatarPreview;

    private Image avatar;
    private User user;

    private static final String DEFAULT_AVATAR_PATH = "../../resources/user.png"; 

    private static final int MIN_AGE = 16;
    private static final int MIN_NICKNAME_LENGTH = 6;
    private static final int MAX_NICKNAME_LENGTH = 15;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final double STATUS_LABEL_DURATION_SECONDS = 2.5; 

    @FXML
    private void initialize() {
        if (avatar == null) {
            avatar = new Image(getClass().getResource(DEFAULT_AVATAR_PATH).toExternalForm());
            avatarPreview.setImage(avatar);
        }
    }

    public void setUser(User user) {
        this.user = user;
        System.out.println("Sesión iniciada con: " + user.getNickName());
    }

    @FXML
    private void handleRegister() throws NavDAOException {
        String nick = nicknameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        LocalDate birth = birthdatePicker.getValue();

        statusLabel.setText("");

        if (!validateNickname(nick)) {
            return;
        }
        if (!validateEmail(email)) {
            return;
        }
        if (!validatePassword(pass)) {
            return;
        }
        if (!validateBirthdate(birth)) {
            return;
        }

        Navigation nav = Navigation.getInstance();
        if (nav.exitsNickName(nick)) {
            showError("El nickname '" + nick + "' ya está en uso. Por favor, elige otro.");
            return;
        }

        User user = nav.registerUser(nick, email, pass, avatar, birth);
        showSuccess("¡Usuario registrado correctamente!");

        SceneManager.getInstance().setUser(user);
        SceneManager.getInstance().showMainView(user);
    }

    private boolean validateNickname(String nickname) {
        String nicknamePattern = "^[a-zA-Z0-9_-]{" + MIN_NICKNAME_LENGTH + "," + MAX_NICKNAME_LENGTH + "}$";

        if (nickname == null || nickname.isEmpty()) {
            showError("El nickname no puede estar vacío.");
            return false;
        }
        if (nickname.length() < MIN_NICKNAME_LENGTH || nickname.length() > MAX_NICKNAME_LENGTH) {
            showError("El nickname debe tener entre " + MIN_NICKNAME_LENGTH + " y " + MAX_NICKNAME_LENGTH + " caracteres.");
            return false;
        }
        if (nickname.contains(" ")) {
            showError("El nickname no puede contener espacios.");
            return false;
        }
        if (!nickname.matches(nicknamePattern)) {
            showError("El nickname solo puede contener letras, números, guiones y subguiones.");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (email == null || email.isEmpty()) {
            showError("El correo electrónico no puede estar vacío.");
            return false;
        }
        if (!email.matches(emailPattern)) {
            showError("El formato del correo electrónico no es válido.");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            showError("La contraseña no puede estar vacía.");
            return false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            showError("La contraseña debe tener entre " + MIN_PASSWORD_LENGTH + " y " + MAX_PASSWORD_LENGTH + " caracteres.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showError("La contraseña debe contener al menos una letra mayúscula.");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showError("La contraseña debe contener al menos una letra minúscula.");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            showError("La contraseña debe contener al menos un dígito.");
            return false;
        }
        if (!password.matches(".*[!@#$%&*()\\-+=].*")) {
            showError("La contraseña debe contener al menos un carácter especial (!@#$%&*()-+=).");
            return false;
        }
        return true;
    }

    private boolean validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            showError("Por favor, selecciona tu fecha de nacimiento.");
            return false;
        }
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < MIN_AGE) {
            showError("Debes tener al menos " + MIN_AGE + " años para registrarte.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(message);
        hideStatusAfterDelay();
    }

    private void showSuccess(String message) {
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText(message);
        hideStatusAfterDelay();
    }

    private void hideStatusAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(STATUS_LABEL_DURATION_SECONDS));
        pause.setOnFinished(event -> statusLabel.setText(""));
        pause.play();
    }

    @FXML
    private void handleGoToLogin() {
        SceneManager.getInstance().showLogin();
    }

    @FXML
    private void handleSelectAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona tu avatar");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                avatar = new Image(file.toURI().toString());
                avatarPreview.setImage(avatar);
            } catch (Exception e) {
                showError("No se pudo cargar la imagen seleccionada.");
            }
        }
    }
}