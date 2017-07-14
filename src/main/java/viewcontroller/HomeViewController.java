package viewcontroller;

import com.jfoenix.controls.JFXSnackbar;
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
import model.api.ApiModule;
import model.api.TwitchService;
import model.dto.UserDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.PostConstruct;

@FXMLController("/fxml/home.fxml")
public class HomeViewController {

    public static final int MAX_COUNT_MESSAGES = 500;
    @FXML
    private StackPane home;
    @FXML
    private ListView<Label> chatListView;
    @FXML
    private Button btnAdd;
    @FXML
    private StackPane chatContainer;

    JFXSnackbar bar;

    private IRCBot bot;

    @PostConstruct
    public void init() {
        bar = new JFXSnackbar(home);
        initBot();
    }

    private void initBot(){
        bot = new IRCBot((sender, message) -> addMessage(sender + ": " + message));
        String auth ="oauth:"+Storage.getInstance().getPreference().get("OAuth");
        if(Helper.preferance.get("userName") == null){
            getUserName();
        } else {
            bot.join(auth, "#overdrive1g");
        }

        bot.setOnJoin((channel, sender, login, hostname)->
                bar.enqueue(new JFXSnackbar.SnackbarEvent(String.format("Бот подключился к %s", sender))));

        Helper.preferance.put("bot", bot);
    }

    public void addMessage(String msg){
        Platform.runLater(() -> {
            Label newLabel = new Label(msg);
            newLabel.setPrefWidth(480);
            newLabel.setWrapText(true);

            chatListView.getItems().add(newLabel);
            if(chatListView.getItems().size() > MAX_COUNT_MESSAGES){
                chatListView.getItems().remove(0);
            }
        });
    }

    public void getUserName(){
        TwitchService client = ApiModule.getTwitchApiInterface();
        Call<UserDTO> call = client.getUserInfo("OAuth " + Storage.getInstance().getPreference().get("OAuth"));
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                Helper.preferance.put("userName", response.body().getName());
                bot.join("oauth:"+Storage.getInstance().getPreference().get("OAuth"),
                        "#"+response.body().getName());
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable throwable) {
                System.err.println("Бля, пизде, яхз пока чо сюда написать");
            }
        });
    }
}
