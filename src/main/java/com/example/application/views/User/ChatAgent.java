package com.example.application.views.User;

import com.example.application.models.User;
import com.example.application.views.layout.AgentLayout;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Route(value = "/chatAgent", layout = AgentLayout.class)
public class ChatAgent extends VerticalLayout {
    private ScheduledExecutorService executorService;
    private final UserInfo userInfo;
    private User currentUser;

    public ChatAgent() {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        userInfo = new UserInfo(currentUser.getFullName(), currentUser.getFullName());
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        add(getChatLayout());

        // Set styles
        setHeightFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getChatLayout().addClassName("bg-contrast-5");
    }

    private Component getChatLayout() {
        VerticalLayout chatLayout = new VerticalLayout();
        CollaborationMessageList messageList = new CollaborationMessageList(userInfo, "chat");
        CollaborationMessageInput messageInput = new CollaborationMessageInput(messageList);
        H2 header = new H2("Chat");
        header.getStyle().set("font-family", "Arial, sans-serif");
        header.getStyle().set("font-size", "24px");
        header.getStyle().set("font-weight", "bold");
        header.getStyle().set("color", "#333333");
        header.getStyle().set("margin-bottom", "20px");
        chatLayout.add(header);
        chatLayout.add(messageList);
        chatLayout.add(messageInput);


        chatLayout.expand(messageList);
        chatLayout.setSizeFull();
        chatLayout.setPadding(true);
        chatLayout.setSpacing(true);
        chatLayout.setMargin(true);
        chatLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        chatLayout.setAlignItems(Alignment.START);
        messageInput.setWidth("50%");
        messageList.setWidth("90%");
        messageList.getStyle().set("border", "2px solid #e6e6e6");
        messageList.getStyle().set("border-radius", "5px");
        messageList.getStyle().set("padding", "10px");
        messageList.getStyle().set("overflow-y", "auto");
        messageList.getStyle().set("max-height", "700px");


        return chatLayout;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        executorService.shutdown();
    }
}
