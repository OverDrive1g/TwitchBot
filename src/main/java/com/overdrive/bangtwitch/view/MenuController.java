package com.overdrive.bangtwitch.view;

import com.jfoenix.controls.JFXListView;
import com.overdrive.bangtwitch.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuController {

    @FXML
    public Label chatLabel;
    @FXML
    public Label settingsLabel;
    @FXML
    private JFXListView<Label> listView;

    private MainApp mainApp;

    public MenuController() {
    }

    @FXML
    private void initialize() {
        listView.expandedProperty().set(true);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(chatLabel)){
                //switch to chat main layout
                System.out.println("chat");
            }else if (newValue.equals(settingsLabel)){
                //switch to setting layout
                System.out.println("sett");
            }
        });
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
