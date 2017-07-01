package com.overdrive.bangtwitch.view;

import com.overdrive.bangtwitch.MainApp;
import com.overdrive.bangtwitch.model.IRCBot;
import com.overdrive.bangtwitch.model.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ChatController {
    @FXML
    public ListView chatList;

    private MainApp mainApp;

    private IRCBot bot;

    public ChatController() {
        bot = new IRCBot((sender, message) -> addMsgToView(sender, message));
    }

    @FXML
    private void initialize() {
        bot.join((String) Storage.getInstance().getPreference().get("OAuth"), "#OverDrive1g");
    }

    private void addMsgToView(String sender, String message){
        System.out.println("sender = [" + sender + "], message = [" + message + "]");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
