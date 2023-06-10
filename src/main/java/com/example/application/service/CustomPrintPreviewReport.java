package com.example.application.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.reports.PrintPreviewReport;

import java.util.List;

public class CustomPrintPreviewReport<T> extends PrintPreviewReport<T> {

    public CustomPrintPreviewReport() {
        super();
    }

    public CustomPrintPreviewReport(Class<T> type) {
        super(type);
    }

    public CustomPrintPreviewReport(Class<T> type, String... columnIds) {
        super(type, columnIds);
    }

    @Override
    public void setItems(List<? extends T> items) {
        super.setItems(items);

        // Get the main layout of the report
        VerticalLayout mainLayout = getContent();

        // Find the HTML container
        Div htmlContainer = mainLayout.getChildren()
                .filter(component -> component instanceof Div)
                .map(component -> (Div) component)
                .findFirst()
                .orElse(null);

        // Check if the HTML container exists
        if (htmlContainer != null) {
            // Get the first child component (HTML content)
            Component htmlContent = htmlContainer.getChildren().findFirst().orElse(null);

            // Check if the child component is an Html instance
            if (htmlContent instanceof Html) {
                Html html = (Html) htmlContent;

                // Get the HTML content
                String htmlString = html.getInnerHtml();

                // Remove any existing top-level elements (span, html, etc.)
                htmlString = htmlString.replaceAll("<span>", "").replaceAll("</span>", "");
                htmlString = htmlString.replaceAll("<html>", "").replaceAll("</html>", "");

                // Update the HTML content
                html.setHtmlContent("<div>" + htmlString + "</div>");
            }
        }
    }
}
