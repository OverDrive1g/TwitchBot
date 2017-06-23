package com.overdrive.bangtwitch;

import com.overdrive.bangtwitch.model.OAuth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BangTitch");

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
        engine.load("https://api.twitch.tv/kraken/oauth2/authorize?client_id=16w9wz5ge9mtipznr4rmk1cda0z4we&redirect_uri=https://www.twitch.tv/kraken/oauth2/authorize&response_type=token&scope=channel_subscriptions+chat_login");

        rootLayout.setCenter(webView);


        Runnable r = () -> {
            while (true) {
                int i = engine.getLocation().indexOf("https://www.twitch.tv/kraken/oauth2/authorize#access_token=");
                if (i != -1) {
                    System.out.println(engine.getLocation());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

    }

    private void authorization() {

        OAuth storageOAuth = null;

        try {
            FileInputStream fis = new FileInputStream("/tmp/OAuth.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            storageOAuth = (OAuth) ois.readObject();

            ois.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            System.out.println("OAuth class not found");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            showAuthLayout();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (storageOAuth != null) {
            System.out.println("Key = " + storageOAuth.OAuthKey);

            // send request
            // is response valid goto main layout
        } else {
            showAuthLayout();
        }

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
