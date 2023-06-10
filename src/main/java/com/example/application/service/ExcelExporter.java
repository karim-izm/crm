package com.example.application.service;

import com.example.application.models.Vente;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelExporter {

    public static void export(Grid<Vente> grid, String fileName) {
        DataProvider<Vente, ?> dataProvider = grid.getDataProvider();

        // Fetch all items from the data provider
        List<Vente> data = dataProvider.fetch(new Query<>()).collect(Collectors.toList());

        // Create a new workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ventes");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Id");
            headerRow.createCell(1).setCellValue("Ref");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Description");
            headerRow.createCell(4).setCellValue("Nom Client");
            headerRow.createCell(5).setCellValue("Num Tel");
            headerRow.createCell(6).setCellValue("Email");
            headerRow.createCell(7).setCellValue("Date Naissance");
            headerRow.createCell(8).setCellValue("Agent");
            headerRow.createCell(9).setCellValue("Qualification");


            // Populate data rows
            int rowIndex = 1;
            for (Vente v : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                dataRow.createCell(0).setCellValue(v.getVenteId());
                dataRow.createCell(1).setCellValue(v.getContract());
                dataRow.createCell(2).setCellValue(v.getDate());
                dataRow.createCell(3).setCellValue(v.getTypeVente());
                dataRow.createCell(4).setCellValue(v.getNomClient());
                dataRow.createCell(5).setCellValue(v.getNumTel());
                dataRow.createCell(6).setCellValue(v.getEmailClient());
                dataRow.createCell(7).setCellValue(v.getDateNaiss());
                dataRow.createCell(8).setCellValue(v.getAgent().getFullName());
                dataRow.createCell(9).setCellValue(v.getQualification());

            }

            // Auto-size columns
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
            }

            // Show success message or perform further actions
            System.out.println("Data exported successfully to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error exporting data as Excel file");
        }
    }

}
