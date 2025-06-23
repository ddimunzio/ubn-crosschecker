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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

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
    private void handleRun(ActionEvent event) throws Exception {
        ADIFReader reader = new ADIFReader(ADIFFile.getAbsolutePath());
        UBNReader ubnReader = new UBNReader();
        UbnResult UbnResult = ubnReader.readUbnFile(new File(UBNFile.getAbsolutePath()), reader.read());
        populateErrorPercentageByOP(UbnResult);
        populateNilBarChart(UbnResult);
        populateBadExchangeBarChart(UbnResult);
        populateInvalidCalls(UbnResult);
        populateTotalByOpBarChart(UbnResult);
        //RateStats rateStats = new RateStats();
        //rateStats.bestHourlyRateByOperator(reader.read());

    }


    private void createSeries(Map<String, Long> counts, BarChart<String, Number> chart) {
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            chart.getData().add(series);
        }
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

}