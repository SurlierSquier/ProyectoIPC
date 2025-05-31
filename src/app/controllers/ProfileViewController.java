/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import model.User;
import utils.SceneManager;

import java.io.File;
import java.time.LocalDate;

public class ProfileViewController {

    @FXML private Label nicknameLabel;
    @FXML private TextField emailField;
    @FXML private DatePicker birthdatePicker;
    @FXML private ImageView avatarPreview;

    private User currentUser;

    public void setUser(User user) {
        
        this.currentUser = user;
        nicknameLabel.setText("Nickname: " + user.getNickName());
        emailField.setText(user.getEmail());
        birthdatePicker.setValue(user.getBirthdate());
        avatarPreview.setImage(user.getAvatar());
    }

    @FXML
    private void handleSave() {
        String newEmail = emailField.getText();
        LocalDate newBirthdate = birthdatePicker.getValue();

        if (!User.checkEmail(newEmail)) {
            System.out.println("❌ Email inválido");
            return;
        }

        currentUser.setEmail(newEmail);
        currentUser.setBirthdate(newBirthdate);
        System.out.println("✅ Perfil actualizado correctamente");
    }

    @FXML
    private void handleChangeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar nuevo avatar");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image avatar = new Image(file.toURI().toString());
            currentUser.setAvatar(avatar);
            avatarPreview.setImage(avatar);
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstance().showMainView(currentUser);
    }
}
