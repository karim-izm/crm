package com.example.application.views.admin;

import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.spreadsheet.Spreadsheet;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.io.InputStream;

@Route(value = "/spreadsheet", layout = MainLayout.class)
public class SpreadSheet extends VerticalLayout {
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Select<String> fileSelect = new Select<>();
    private final Spreadsheet spreadsheet = new Spreadsheet();

    public SpreadSheet() {
        upload.addSucceededListener(event -> {
            try {
                String fileName = event.getFileName();
                InputStream inputStream = buffer.getInputStream();
                spreadsheet.read(inputStream);
                fileSelect.setValue(fileName);
                Notification.show("File uploaded successfully!");
            } catch (Exception e) {
                Notification.show("An error occurred while uploading the file.");
            }
        });

        upload.setAutoUpload(true);
        upload.setAcceptedFileTypes(
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        spreadsheet.setHeight("400px");

        fileSelect.setLabel("Select File");
        fileSelect.addValueChangeListener(event -> {
            String selectedFileName = event.getValue();
            if (selectedFileName != null) {
                InputStream selectedInputStream = buffer.getInputStream();
                try {
                    spreadsheet.read(selectedInputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
            }
        });

        add(upload, fileSelect, spreadsheet);
    }
}
