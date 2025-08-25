/*
 * UBN Cross Checker - Amateur Radio Contest Log Analysis Tool
 *
 * Copyright (c) 2025 LW5HR
 *
 * This software is provided free of charge for the amateur radio community.
 * Feel free to use, modify, and distribute.
 *
 * @author LW5HR
 * @version 1.0
 * @since 2025
 *
 * 73!
 */
package org.lw5hr.controllers;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import org.lw5hr.model.OperatorErrorStats;
import org.lw5hr.model.Qso;
import org.lw5hr.model.UbnResult;
import org.lw5hr.tool.utils.ADIFReader;
import org.lw5hr.tool.utils.UBNReader;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.scene.chart.CategoryAxis;

public class MainController {
    @FXML
    private PieChart ErrorPercentageByOP;
    @FXML
    private BarChart<String, Number> badExchange;
    @FXML
    private BarChart<String, Number> totalByOp;
    @FXML
    private BarChart<String, Number> nil;
    @FXML
    private BarChart<String, Number> invalidCalls;

    @FXML
    private Button adiButton;

    @FXML
    private TextField adiField;

    @FXML
    private Button ubnButton;

    @FXML
    private TextField ubnPathField;

    @FXML
    private Button run;

    private File ADIFFile;
    private File UBNFile;

    @FXML
    private void handleBrowseADI(ActionEvent event) {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.ADI")
                , new FileChooser.ExtensionFilter("HTML Files", "*.ADIF")
        );
        ADIFFile = fileChooser.showOpenDialog(adiButton.getScene().getWindow());
        if (ADIFFile != null) {
            try {
                adiField.setText(ADIFFile.getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    private void handleBrowseUBN(ActionEvent event) {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        UBNFile = fileChooser.showOpenDialog(ubnButton.getScene().getWindow());
        if (UBNFile != null) {
            try {
                ubnPathField.setText(UBNFile.getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    private void handleRun(ActionEvent event) {
        try {
            // Validate that both files have been selected
            if (ADIFFile == null) {
                showErrorAlert("Missing ADIF File", "Please select an ADIF file before running.");
                return;
            }
            
            if (UBNFile == null) {
                showErrorAlert("Missing UBN File", "Please select a UBN file before running.");
                return;
            }
            
            // Validate that the files exist and are readable
            if (!ADIFFile.exists() || !ADIFFile.canRead()) {
                showErrorAlert("Invalid ADIF File", "The selected ADIF file does not exist or cannot be read.");
                return;
            }
            
            if (!UBNFile.exists() || !UBNFile.canRead()) {
                showErrorAlert("Invalid UBN File", "The selected UBN file does not exist or cannot be read.");
                return;
            }
            
            // Process the files
            ADIFReader reader = new ADIFReader(ADIFFile.getAbsolutePath());
            UBNReader ubnReader = new UBNReader();
            UbnResult ubnResult = ubnReader.readUbnFile(UBNFile, reader.read());
            
            // Update the UI with the results
            populateErrorPercentageByOP(ubnResult);
            populateNilBarChart(ubnResult);
            populateBadExchangeBarChart(ubnResult);
            populateInvalidCalls(ubnResult);
            populateTotalByOpBarChart(ubnResult);
            
        } catch (Exception e) {
            showErrorAlert("Error Processing Files", 
                    "An error occurred while processing the files: " + e.getMessage());
        }
    }
    
    /**
     * Shows an error alert dialog with the specified title and message.
     * 
     * @param title The title of the alert
     * @param message The message to display
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void createSeries(Map<String, Long> counts, BarChart<String, Number> chart) {
        // Clear existing data first
        chart.getData().clear();

        // Create separate series for each operator to maintain color differentiation
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            chart.getData().add(series);
        }

        // Configure category axis to properly display all labels and bars
        if (chart.getXAxis() instanceof CategoryAxis) {
            CategoryAxis categoryAxis = (CategoryAxis) chart.getXAxis();
            categoryAxis.setAutoRanging(true);
            categoryAxis.setGapStartAndEnd(true); // Changed to true to ensure proper spacing
            categoryAxis.setTickLabelRotation(45); // Rotate labels to prevent overlap
        }

        // Force chart layout refresh after configuration
        chart.setAnimated(false);
        chart.applyCss();
        chart.layout();
        chart.requestLayout();
    }

    // Java
    public void populateErrorPercentageByOP(UbnResult ubnResult) {
        ErrorPercentageByOP.setAnimated(true);
        ErrorPercentageByOP.getData().clear();

        // Create the PieChart.Data items
        for (OperatorErrorStats stats : ubnResult.getOperatorErrorStats()) {
            ErrorPercentageByOP.getData().add(
                    new PieChart.Data(stats.getOperator(), stats.getTotalErrors())
            );
        }

        ErrorPercentageByOP.applyCss();
        ErrorPercentageByOP.layout();

        Platform.runLater(() -> {
            // Assuming the list order in ErrorPercentageByOP.getData() matches that in operatorErrorStats
            for (int i = 0; i < ubnResult.getOperatorErrorStats().size(); i++) {
                OperatorErrorStats stats = ubnResult.getOperatorErrorStats().get(i);
                PieChart.Data data = ErrorPercentageByOP.getData().get(i);
                String tooltipText = String.format("%s: %.1f%%", data.getName(), stats.getPercentage());
                Tooltip tooltip = new Tooltip(tooltipText);

                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        addTooltipHandlers(data, tooltip);
                    }
                });

                if (data.getNode() != null) {
                    addTooltipHandlers(data, tooltip);
                }
            }
        });
    }

    private void addTooltipHandlers(PieChart.Data data, Tooltip tooltip) {
        Node node = data.getNode();
        node.setOnMouseEntered(e -> tooltip.show(node, e.getScreenX(), e.getScreenY() + 10));
        node.setOnMouseExited(e -> tooltip.hide());
    }


    // NIL by Operator
    public void populateNilBarChart(UbnResult ubnResult) {
        nil.getData().clear();
        Map<String, Long> nilCounts = ubnResult.getNotInLog().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        nil.setCategoryGap(1);
        nil.setBarGap(0.5);
        createSeries(nilCounts, nil);
    }

    // Bad Exchange by Operator
    public void populateBadExchangeBarChart(UbnResult ubnResult) {
        badExchange.getData().clear();
        Map<String, Long> badExchangeCounts = ubnResult.getIncorrectExchangeInfo().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        badExchange.setCategoryGap(1);
        badExchange.setBarGap(0.5);
        createSeries(badExchangeCounts, badExchange);
    }

    // Invalid Calls by Operator
    public void populateInvalidCalls(UbnResult ubnResult) {
        invalidCalls.getData().clear();
        Map<String, Long> invalidCallCounts = ubnResult.getIncorrectCall().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        invalidCalls.setCategoryGap(1);
        invalidCalls.setBarGap(0.5);
        createSeries(invalidCallCounts, invalidCalls);
    }

    public void populateTotalByOpBarChart(UbnResult ubnResult) {
        totalByOp.getData().clear();
        totalByOp.setCategoryGap(0.5);
        totalByOp.setBarGap(0.5);

        createSeries(ubnResult.getTotalByOperator(), totalByOp);
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        Alert aboutAlert = new Alert(AlertType.INFORMATION);
        aboutAlert.setTitle("About UBN Cross Checker");
        aboutAlert.setHeaderText("UBN Cross Checker v1.0");

        String aboutText = "This application was developed by LW5HR for the free use and distribution of ham radio operators.\n\n" +
                          "UBN Cross Checker helps analyze UBN (Unique, Busted, NIL) reports from amateur radio contests " +
                          "by cross-referencing them with ADIF log files to provide detailed statistics and visualizations.\n\n" +
                          "Features:\n" +
                          "• Cross-reference UBN reports with ADIF logs\n" +
                          "• Generate statistics by operator\n" +
                          "• Visualize errors with interactive charts\n" +
                          "• Export results and analysis\n\n" +
                          "This software is provided free of charge for the amateur radio community.\n" +
                          "Feel free to use, modify, and distribute.\n\n" +
                          "73!\n" +
                          "LW5HR";

        aboutAlert.setContentText(aboutText);
        aboutAlert.setResizable(true);
        aboutAlert.getDialogPane().setPrefWidth(500);
        aboutAlert.getDialogPane().setPrefHeight(400);

        aboutAlert.showAndWait();
    }

}