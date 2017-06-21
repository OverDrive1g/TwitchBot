package com.overdrive.bangtwitch;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class AuthController {
    @FXML
    private JFXTextField oauthTextField;

    private MainApp mainApp;

    public AuthController() {
    }

    @FXML
    private void initialize() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
