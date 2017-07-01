package com.overdrive.bangtwitch;

import com.overdrive.bangtwitch.model.Storage;
import com.overdrive.bangtwitch.model.api.ApiModule;
import com.overdrive.bangtwitch.model.api.TwitchService;
import com.overdrive.bangtwitch.model.dto.AuthStatusDTO;
import com.overdrive.bangtwitch.view.MenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Objects;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BangTitch");

        initOnStopApp();
        initRootLayout();
        authorization();
    }

    private void initOnStopApp() {
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

        TwitchService client = ApiModule.getTwitchApiInterface();

        Hashtable<String, Object> pref = Storage.getInstance().getPreference();

        String token = (String) pref.get("OAuth");

        if (token != null) {
            Call<AuthStatusDTO> call = client.authorize("16w9wz5ge9mtipznr4rmk1cda0z4we", token);

            call.enqueue(new Callback<AuthStatusDTO>() {
                @Override
                public void onResponse(Call<AuthStatusDTO> call, Response<AuthStatusDTO> response) {
                    if(!Objects.equals(response.body().getStatus(), "ok")){
                        Platform.runLater(()->showAuthLayout());
                    }else {
                        Platform.runLater(()->showMenuLayout());
                    }
                }

                @Override
                public void onFailure(Call<AuthStatusDTO> call, Throwable throwable) {
                    System.err.println("err: "+throwable.getMessage());
                }
            });

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
