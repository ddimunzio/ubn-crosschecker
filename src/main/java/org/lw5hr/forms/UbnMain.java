package org.lw5hr.forms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;

public class UbnMain extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void setPrimaryStage(Stage primaryStage) {
        UbnMain.primaryStage = primaryStage;
    }

    public static void start() throws Exception {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main.fxml")));
        setPrimaryStage(stage);
        primaryStage.setTitle("UBN Checker by LW5HR");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}