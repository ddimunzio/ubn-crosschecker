package org.lw5hr.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.lw5hr.model.Qso;
import org.lw5hr.model.UbnResult;
import org.lw5hr.tool.utils.ADIFReader;
import org.lw5hr.tool.utils.RateStats;
import org.lw5hr.tool.utils.UBNReader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.lw5hr.model.OperatorErrorStats;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {

    public PieChart ErrorPercentageByOP;
    public BarChart<String, Number> badExchange;
    public BarChart<String, Number> totalByOp;
    public BarChart<String, Number> nil;
    public BarChart<String, Number> invalidCalls;
    public Tab totalQSO;
    public StackPane pieChartStack;
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
        RateStats rateStats = new RateStats();
        rateStats.bestHourlyRateByOperator(reader.read());

    }

    public void populateErrorPercentageByOP(UbnResult ubnResult) {
        ErrorPercentageByOP.getData().clear();

        for (OperatorErrorStats stats : ubnResult.getOperatorErrorStats()) {
            ErrorPercentageByOP.getData().add(
                    new PieChart.Data(stats.getOperator(), stats.getTotalErrors())
            );
        }

        ErrorPercentageByOP.applyCss();
        ErrorPercentageByOP.layout();

        double total = ErrorPercentageByOP.getData().stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();

        Platform.runLater(() -> {
            for (PieChart.Data data : ErrorPercentageByOP.getData()) {
                double percent = total > 0 ? (data.getPieValue() * 100.0) / total : 0.0;
                String tooltipText = String.format("%s: %.1f%%", data.getName(), percent);
                Tooltip tooltip = new Tooltip(tooltipText);

                // If node is already available
                if (data.getNode() != null) {
                    addTooltipHandlers(data, tooltip);
                }

                // If node is not yet available, add a listener
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        addTooltipHandlers(data, tooltip);
                    }
                });
            }
        });
    }

    private void addTooltipHandlers(PieChart.Data data, Tooltip tooltip) {
        Node node = data.getNode();
        node.setOnMouseEntered(e -> {
            tooltip.show(node, e.getScreenX(), e.getScreenY() + 10);
        });
        node.setOnMouseExited(e -> {
            tooltip.hide();
        });
    }

    // NIL by Operator
    public void populateNilBarChart(UbnResult ubnResult) {
        nil.getData().clear();
        Map<String, Long> nilCounts = ubnResult.getNotInLog().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        createSeries(nilCounts, nil);
    }

    // Bad Exchange by Operator
    public void populateBadExchangeBarChart(UbnResult ubnResult) {
        badExchange.getData().clear();
        Map<String, Long> badExchangeCounts = ubnResult.getIncorrectExchangeInfo().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        createSeries(badExchangeCounts, badExchange);
    }

    private void createSeries(Map<String, Long> counts, BarChart<String, Number> chart) {
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            chart.getData().add(series);
        }
    }

    // Invalid Calls by Operator
    public void populateInvalidCalls(UbnResult ubnResult) {
        invalidCalls.getData().clear();
        Map<String, Long> invalidCallCounts = ubnResult.getIncorrectCall().stream()
                .collect(Collectors.groupingBy(Qso::getOperator, Collectors.counting()));
        createSeries(invalidCallCounts, invalidCalls);
    }

    // Total by Operator
    public void populateTotalByOpBarChart(UbnResult ubnResult) {
        totalByOp.getData().clear();
        Map<String, Long> totalByOperator = ubnResult.getTotalByOperator();
        if (totalByOperator != null) {
            createSeries(totalByOperator, totalByOp);
        }
    }


}