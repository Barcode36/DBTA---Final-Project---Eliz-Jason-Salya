package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    public static Dialog primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginpage.fxml"));
        primaryStage.setTitle("Database");
        primaryStage.setScene(new Scene(root, 557, 426));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
