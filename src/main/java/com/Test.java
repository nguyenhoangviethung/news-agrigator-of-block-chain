package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        Image image = new Image(getClass().getResource("/com/image/shy.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root,300,200);


        primaryStage.setScene(scene);
        primaryStage.setTitle("AutoComplete Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        
    }
}

