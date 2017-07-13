import com.jfoenix.controls.JFXDecorator;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Helper;
import model.IRCBot;
import model.Storage;
import viewcontroller.MainViewController;

import java.io.*;
import java.nio.file.Paths;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Flow flow = new Flow(MainViewController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        FlowHandler flowHandler = flow.createHandler();
        flowHandler.start(container);

        JFXDecorator decorator = new JFXDecorator(primaryStage, container.getView());
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 1280, 720);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainApp.class.getResource("css/style.css").toExternalForm());

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {

        IRCBot bot = (IRCBot) Helper.preferance.get("bot");
        bot.disconnect();
        bot.dispose();

        Storage instance = Storage.getInstance();
        try {
            String filePath = Paths.get(System.getProperty("user.home"), ".bang-twitch").toString() +
                    System.getProperty("file.separator");

            File f = new File(filePath);
            if(!f.exists()){
                f.mkdir();
            }

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(
                    filePath = Paths.get(System.getProperty("user.home"), ".bang-twitch").toString() +
                            System.getProperty("file.separator")+"pref.ser"
            )));
            out.writeObject(instance);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
