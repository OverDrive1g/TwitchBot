package model;

import io.datafx.controller.flow.context.ViewFlowContext;

public class Helper {

    public static final String CLIENT_ID = "16w9wz5ge9mtipznr4rmk1cda0z4we";

    private static ViewFlowContext viewFlowContext;

    public static ViewFlowContext getViewFlowContext() {
        return viewFlowContext;
    }

    public static void setViewFlowContext(ViewFlowContext viewFlowContext) {
        Helper.viewFlowContext = viewFlowContext;
    }
}
