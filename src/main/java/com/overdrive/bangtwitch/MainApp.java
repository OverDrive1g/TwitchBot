package com.overdrive.bangtwitch;

import com.overdrive.bangtwitch.model.Storage;
import com.overdrive.bangtwitch.view.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Hashtable;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BangTitch");

        this.primaryStage.setOnCloseRequest(event -> {
            Storage instance = Storage.getInstance();
            try {
                ObjectOutput out = new ObjectOutputStream(new FileOutputStream(Paths.get(System.getProperty("user.home"),
                        ".bang-twitch").toString() + System.getProperty("file.separator")+ "pref.ser"));
                out.writeObject(instance);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        initRootLayout();
        authorization();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAuthLayout() {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
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
                showMenuLayout();
            }
        });


        rootLayout.setCenter(webView);
    }

    private void authorization() {

        Hashtable<String, Object> pref = Storage.getInstance().getPreference();
        if (pref.get("OAuth") != null) {
            System.out.println("Key = " + pref.get("OAuth"));
            showMenuLayout();
        } else {
            showAuthLayout();
        }
    }

    private void showMenuLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MenuLayout.fxml"));
            AnchorPane menuLayout = loader.load();
            
            rootLayout.setLeft(menuLayout);
            MenuController controller = new MenuController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
