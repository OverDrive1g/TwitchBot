package com.overdrive.bangtwitch;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXSnackbar;
import com.overdrive.bangtwitch.model.Helper;
import com.overdrive.bangtwitch.model.Storage;
import com.overdrive.bangtwitch.viewcontroller.MainViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

public class MainApp extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        flowContext = new ViewFlowContext();
        Helper.setViewFlowContext(flowContext);
        flowContext.register("stage", primaryStage);

        Flow flow = new Flow(MainViewController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flow.createHandler(flowContext).start(container);

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
        super.stop();
        Storage instance = Storage.getInstance();
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(Paths.get(System.getProperty("user.home"),
                    ".bang-twitch").toString() + System.getProperty("file.separator")+ "pref.ser"));
            out.writeObject(instance);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
