package com.example.application.views.admin;

import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Route(value = "excel" , layout = MainLayout.class)
@PageTitle("compagnes")
public class VueDeCompagne extends VerticalLayout {

    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Grid<List<String>> grid = new Grid<>();
    private final Select<String> fileSelect = new Select<>();

    private final Map<String, List<List<String>>> fileDataMap = new HashMap<>();

    public VueDeCompagne() {
        fileSelect.setLabel("Select File");
        fileSelect.addValueChangeListener(event -> {
            String fileName = event.getValue();
            if (fileName != null) {
                grid.removeAllColumns();
                List<List<String>> fileData = new ArrayList<>(fileDataMap.get(fileName)); // create new instance
                if (fileData != null) {
                    List<String> headers = fileData.get(0);
                    headers.forEach(header -> grid.addColumn(row -> row.get(headers.indexOf(header)))
                            .setHeader(header)
                            .setWidth("150px"));
                    fileData.remove(0); // remove headers from rows
                    grid.setHeight(null);
                    grid.setItems(fileData);
                }
            }
        });
        upload.addSucceededListener(event -> {
            try {
                String fileName = event.getFileName();
                InputStream inputStream = buffer.getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                List<String> headers = new ArrayList<>();
                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();
                    Iterator<Cell> headerCellIterator = headerRow.cellIterator();
                    while (headerCellIterator.hasNext()) {
                        Cell headerCell = headerCellIterator.next();
                        headers.add(headerCell.getStringCellValue());
                    }
                }
                List<List<String>> rows = new ArrayList<>();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.setCellType(CellType.STRING);
                        rowData.add(cell.getStringCellValue());
                    }
                    rows.add(rowData);
                }
                fileDataMap.put(fileName, new ArrayList<>(rows));
                fileSelect.setItems(fileDataMap.keySet());
                fileSelect.setValue(fileName);
                Notification.show("File uploaded successfully!");
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                Notification.show("An error occurred while uploading the file.");
            }
        });
        upload.setAutoUpload(true);
        upload.setAcceptedFileTypes("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        grid.setSizeFull();
        grid.getStyle().set("minWidth", "500px");
        add(upload, fileSelect, grid);
    }
}

