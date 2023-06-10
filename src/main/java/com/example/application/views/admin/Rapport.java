package com.example.application.views.admin;

import com.example.application.models.Vente;
import com.example.application.service.CustomPrintPreviewReport;
import com.example.application.service.VenteService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jsoup.Jsoup;
import org.vaadin.addons.parttio.lightchart.LightChart;
import org.vaadin.reports.PrintPreviewReport;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.List;

@Route(value = "/Rapport" , layout = MainLayout.class)
@PageTitle("Rapport")
public class Rapport extends VerticalLayout {
    private VenteService venteService;
    private Button downloadButton;
    public Rapport(VenteService venteService) {
        this.venteService = venteService;
        Chart chart = new LightChart(ChartType.COLUMN);
        XAxis xAxis = new XAxis();
        YAxis yAxis = new YAxis();

        DataSeries dataSeries = new DataSeries();
        List<Object[]> salesByMonth = venteService.countVentesByMonth();

        for (Object[] result : salesByMonth) {
            int month = (int) result[0];
            long count = (long) result[1];
            String monthLabel = Month.of(month).name();  // Get the month label from the month number

            DataSeriesItem dataItem = new DataSeriesItem(monthLabel, count);
            dataSeries.add(dataItem);
        }

        xAxis.setCategories(
                salesByMonth.stream()
                        .map(result -> Month.of((int) result[0]).name())
                        .toArray(String[]::new)
        );
        yAxis.setTitle("Nombre De Ventes");
        chart.getConfiguration().addxAxis(xAxis);
        chart.getConfiguration().addyAxis(yAxis);
        chart.getConfiguration().addSeries(dataSeries);
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().setTitle("Ventes De L'anné : 2023");


        Chart charttypes = new Chart(ChartType.PIE);
        DataSeries dataSeriestypes = new DataSeries();
        List<Object[]> venteTypeCounts = venteService.countVentesByType();
        for (Object[] row : venteTypeCounts) {
            String venteType = (String) row[0];
            Long count = (Long) row[1];
            DataSeriesItem item = new DataSeriesItem(venteType, count);
            item.setY(count);
            item.setName(venteType + " (" + count + ")");
            dataSeriestypes.add(item);
        }
        charttypes.getConfiguration().setSeries(dataSeriestypes);
        charttypes.getConfiguration().setTitle("Vente Types Statistics");




        H3 title = new H3("Statistiques");
        setAlignItems(Alignment.CENTER);
        add(title , chart , charttypes , createDownloadButton());

    }

    private Button createDownloadButton() {
        Button button = new Button("Download as PDF", VaadinIcon.DOWNLOAD.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> downloadReportAsPDF());
        return button;
    }

    private void downloadReportAsPDF() {

    }

}
