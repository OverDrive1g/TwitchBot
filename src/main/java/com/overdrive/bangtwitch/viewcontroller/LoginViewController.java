package com.overdrive.bangtwitch.viewcontroller;

import com.jfoenix.controls.JFXSpinner;
import com.overdrive.bangtwitch.model.Helper;
import com.overdrive.bangtwitch.model.Storage;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
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

    private Flow centerFlow;
    private FlowHandler centerFlowHandler;

    @PostConstruct
    public void init() {

        Platform.runLater(()->{
            WebEngine engine = webView.getEngine();
            engine.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17");
            engine.load("https://api.twitch.tv/kraken/oauth2/authorize?client_id=16w9wz5ge9mtipznr4rmk1cda0z4we&redirect_uri=https://www.twitch.tv/kraken/oauth2/authorize&response_type=token&scope=channel_subscriptions+chat_login+channel_read");

            engine.locationProperty().addListener((observable, oldValue, newValue) -> {
                int i = newValue.indexOf("https://www.twitch.tv/kraken/oauth2/authorize#access_token=");
                if (i != -1) {
                    try {
                        String oauth = new URL(newValue).getRef().split("&")[0].split("=")[1];
                        Storage.getInstance().getPreference().put("OAuth", oauth);

                        Flow flow = new Flow(HomeViewController.class);
                        DefaultFlowContainer container = new DefaultFlowContainer();
                        flow.createHandler(Helper.getViewFlowContext()).start(container);
//                        drawer.setContent(centerFlowHandler.start());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FlowException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
