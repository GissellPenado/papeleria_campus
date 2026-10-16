package com.tuempresa.papeleria_campus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductosApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ProductosApplication.class.getResource("producto-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1000, 650);

        stage.setTitle("Papelería Campus - Productos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}