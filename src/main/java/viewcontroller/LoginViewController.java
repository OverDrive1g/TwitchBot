package viewcontroller;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Storage;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@FXMLController("/fxml/login.fxml")
public class LoginViewController {

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private WebView webView;

    @PostConstruct
    public void init() {

        Platform.runLater(()->{
            WebEngine engine = webView.getEngine();
            engine.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17");
            engine.load("https://api.twitch.tv/kraken/oauth2/authorize?client_id=16w9wz5ge9mtipznr4rmk1cda0z4we&redirect_uri=https://www.twitch.tv/kraken/oauth2/authorize&response_type=token&scope=chat_login+user_read");

            engine.locationProperty().addListener((observable, oldValue, newValue) -> {
                int i = newValue.indexOf("https://www.twitch.tv/kraken/oauth2/authorize#access_token=");
                if (i != -1) {
                    try {
                        String oauth = new URL(newValue).getRef().split("&")[0].split("=")[1];
                        Storage.getInstance().getPreference().put("OAuth", oauth);

                        Platform.runLater(() -> {
                            try {
                                actionHandler.handle("loginAccept");
                            } catch (VetoException | FlowException e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
