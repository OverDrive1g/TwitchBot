package com.overdrive.bangtwitch.viewcontroller;

import com.jfoenix.controls.JFXProgressBar;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

@FXMLController("/com/overdrive/bangtwitch/fxml/home.fxml")
public class HomeViewController {
    @ActionHandler
    private FlowActionHandler actionHandler;
    @FXML
    private StackPane root;
    @FXML
    private JFXProgressBar progressBar;

    @PostConstruct
    public void init() {}
}
