package org.rplbo.app.ug8.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.rplbo.app.ug8.UmbrellaApp;
import org.rplbo.app.ug8.UmbrellaDBManager;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        UmbrellaDBManager dbManager = new UmbrellaDBManager();

        String fullName = dbManager.validateUser(username, password);


        if (fullName != null) {

            UmbrellaApp.loggedInUser = fullName;

            try {
                UmbrellaApp.switchScene("umbrella-view.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblStatus.setText("AUTHENTICATION FAILED");
        }
    }
}
