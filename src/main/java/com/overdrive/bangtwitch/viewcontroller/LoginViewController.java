package com.overdrive.bangtwitch.viewcontroller;

import com.jfoenix.controls.JFXSpinner;
import com.overdrive.bangtwitch.model.Storage;
import io.datafx.controller.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@FXMLController("/com/overdrive/bangtwitch/fxml/login.fxml")
public class LoginViewController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private WebView webView;

    @PostConstruct
    public void init() {

        JFXSpinner spinner = new JFXSpinner();

        //show spinner

        Platform.runLater(()->{
            WebEngine engine = webView.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.load("https://api.twitch.tv/kraken/oauth2/authorize?client_id=16w9wz5ge9mtipznr4rmk1cda0z4we&redirect_uri=https://www.twitch.tv/kraken/oauth2/authorize&response_type=token&scope=channel_subscriptions+chat_login+channel_read");

            engine.locationProperty().addListener((observable, oldValue, newValue) -> {
                int i = newValue.indexOf("https://www.twitch.tv/kraken/oauth2/authorize#access_token=");
                if (i != -1) {
                    try {
                        String oauth = new URL(newValue).getRef().split("&")[0].split("=")[1];
                        Storage.getInstance().getPreference().put("OAuth", oauth);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
