package com.overdrive.bangtwitch.viewcontroller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXSnackbar;
import com.overdrive.bangtwitch.model.Helper;
import com.overdrive.bangtwitch.model.Storage;
import com.overdrive.bangtwitch.model.api.ApiModule;
import com.overdrive.bangtwitch.model.api.TwitchService;
import com.overdrive.bangtwitch.model.dto.AuthStatusDTO;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.util.Objects;

@FXMLController("/com/overdrive/bangtwitch/fxml/main.fxml")
public class MainViewController {

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    private StackPane root;

    @FXML
    private StackPane titleBurgerContainer;

    @FXML
    private JFXHamburger titleBurger;

    @FXML
    private JFXRippler optionsRippler;

    @FXML
    private StackPane optionsBurger;

    @FXML
    private JFXDrawer drawer;

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    private Flow centerFlow;
    private FlowHandler centerFlowHandler;

    @PostConstruct
    public void init() throws FlowException {
        centerFlow = new Flow(SpinnerViewController.class);
        centerFlowHandler = centerFlow.createHandler(Helper.getViewFlowContext());
        drawer.setContent(centerFlowHandler.start());

        authorization();
        initToolBar();
    }

    private void initToolBar(){
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

    private void authorization(){
        final String token = (String) Storage.getInstance().getPreference().get("OAuth");

        if(token != null){
            TwitchService client = ApiModule.getTwitchApiInterface();
            Call<AuthStatusDTO> call = client.authorize(Helper.CLIENT_ID, token);

            call.enqueue(new Callback<AuthStatusDTO>() {
                @Override
                public void onResponse(Call<AuthStatusDTO> call, Response<AuthStatusDTO> response) {
                    if(Objects.equals(response.body().getStatus(), "ok")){
                        Platform.runLater(()->{
                            centerFlow = new Flow(HomeViewController.class);
                            centerFlowHandler = centerFlow.createHandler(Helper.getViewFlowContext());
                            try {
                                drawer.setContent(centerFlowHandler.start());
                            } catch (FlowException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<AuthStatusDTO> call, Throwable throwable) {
                    centerFlow = new Flow(LoginViewController.class);
                    centerFlowHandler = centerFlow.createHandler(Helper.getViewFlowContext());
                    try {
                        drawer.setContent(centerFlowHandler.start());
                    } catch (FlowException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else{
            centerFlow = new Flow(LoginViewController.class);
            centerFlowHandler = centerFlow.createHandler(Helper.getViewFlowContext());
            try {
                drawer.setContent(centerFlowHandler.start());
            } catch (FlowException e) {
                e.printStackTrace();
            }
        }
    }
}
