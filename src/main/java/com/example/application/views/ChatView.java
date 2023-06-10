package com.example.application.views;

import com.example.application.models.User;
import com.example.application.service.UserService;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;

@Route("/chat")
public class ChatView extends VerticalLayout {
    private TextArea messageDisplay;
    private     UserService userService ;
    private TextField messageInput;
    private Button sendButton;

    public ChatView(UserService userService) {
        this.userService = userService;
        MessageList list = new MessageList();
        MessageListItem message1 = new MessageListItem("hi");
        list.setItems(Arrays.asList(message1));
        add(list);
    }

    private void sendMessage() {
        String messageText = messageInput.getValue();
        // Send the message to the server-side WebSocket endpoint or messaging controller
        // Code for sending the message to the server goes here
        messageInput.clear();
    }

    public void addMessage(String message) {
        // Add the new message to the message display
        messageDisplay.setValue(messageDisplay.getValue() + "\n" + message);
    }
}
