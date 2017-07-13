package viewcontroller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToolbar;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.util.VetoException;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.Helper;
import model.Storage;
import model.api.ApiModule;
import model.api.TwitchService;
import model.dto.TokenStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.PostConstruct;

@FXMLController("/fxml/main.fxml")
public class MainViewController {

    @FXML
    private StackPane root;
    @FXML
    private JFXDrawer drawer;

    //toolbar
    @FXML
    private JFXToolbar toolbar;
    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXHamburger titleBurger;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    private StackPane optionsBurger;


    private FlowHandler flowHandler;

    @PostConstruct
    public void init() throws FlowException, VetoException {
        Flow innerFlow = new Flow(SpinnerViewController.class)
                .withLink(SpinnerViewController.class, "authNotPass", LoginViewController.class)
                .withLink(SpinnerViewController.class, "authAccept", HomeViewController.class)
                .withLink(LoginViewController.class, "loginAccept", HomeViewController.class);
        flowHandler = innerFlow.createHandler();

        drawer.setContent(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));

        toolbar.setVisible(false);
        initToolBar();
        authorization();
    }

    private void initToolBar() {
        drawer.setOnDrawerOpening(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(1);
            animation.play();
        });
        drawer.setOnDrawerClosing(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(-1);
            animation.play();
        });
        titleBurgerContainer.setOnMouseClicked(e -> {
            if (drawer.isHidden() || drawer.isHidding()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });
    }

    private void authorization() throws VetoException, FlowException {
        String token = (String) Storage.getInstance().getPreference().get("OAuth");
        if (token != null) {
            validateToken(token);
        } else {
            flowHandler.handle("authNotPass");
        }
    }

    private void validateToken(String token) {
        TwitchService client = ApiModule.getTwitchApiInterface();

        Call<TokenStatus> call = client.getTokenStatus("OAuth "+token,ApiModule.BASE_URL);
        call.enqueue(new Callback<TokenStatus>() {
            @Override
            public void onResponse(Call<TokenStatus> call, Response<TokenStatus> response) {
                if(response.body().getToken().getValid()){
                    Helper.preferance.put("userName", response.body().getToken().getUserName());
                    Platform.runLater(() -> {
                        try {
                            flowHandler.handle("authAccept");
                        } catch (VetoException | FlowException e) {
                            e.printStackTrace();
                        }
                    });
                } else{
                    Platform.runLater(() -> {
                        try {
                            flowHandler.handle("authNotPass");
                            toolbar.setVisible(true);
                        } catch (VetoException | FlowException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TokenStatus> call, Throwable throwable) {
                try {
                    flowHandler.handle("authNotPass");
                    toolbar.setVisible(true);
                } catch (VetoException | FlowException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}