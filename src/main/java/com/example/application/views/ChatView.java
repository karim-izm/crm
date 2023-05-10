package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("chat")
public class ChatView extends VerticalLayout {

    private final TextArea chatArea;
    private final TextField inputField;
    private final Button sendButton;
    private final List<String> chatHistory;

    public ChatView() {
        chatArea = new TextArea();
        chatArea.setWidth("100%");
        chatArea.setHeight("400px");
        chatArea.setReadOnly(true);

        inputField = new TextField();
        inputField.setWidth("100%");

        sendButton = new Button("Send");
        sendButton.addClickListener(e -> sendMessage());

        chatHistory = new ArrayList<>();

        add(chatArea, inputField, sendButton);
    }

    private void sendMessage() {
        String message = inputField.getValue();
        if (!message.trim().isEmpty()) {
            chatHistory.add(message);
            chatArea.setValue(String.join("\n", chatHistory));
            inputField.clear();
        }
    }
}
