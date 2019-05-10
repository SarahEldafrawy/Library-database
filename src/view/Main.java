package view;

import Model.JasperReports;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("AIS BookShop");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1252, 807.75));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
