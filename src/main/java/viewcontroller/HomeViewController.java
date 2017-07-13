package viewcontroller;

import io.datafx.controller.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import model.Helper;
import model.IRCBot;
import model.Storage;

import javax.annotation.PostConstruct;

@FXMLController("/fxml/home.fxml")
public class HomeViewController {

    public static final int MAX_COUNT_MESSAGES = 500;

    @FXML
    private ListView<Label> chatListView;
    @FXML
    private Button btnAdd;
    @FXML
    private StackPane chatContainer;

    private IRCBot bot;

    @PostConstruct
    public void init() {
        bot = new IRCBot((sender, message) -> addMessage(sender + ": " + message));
        String auth ="oauth:"+Storage.getInstance().getPreference().get("OAuth");
        System.out.println(auth);
        bot.join(auth,"#overdrive1g");
        Helper.preferance.put("bot", bot);
    }

    public void addMessage(String msg){
        Platform.runLater(() -> {
            Label newLabel = new Label(msg);
            newLabel.setPrefWidth(500);
            newLabel.setWrapText(true);

            chatListView.getItems().add(newLabel);
            if(chatListView.getItems().size() > MAX_COUNT_MESSAGES){
                chatListView.getItems().remove(0);
            }
        });
    }
}
