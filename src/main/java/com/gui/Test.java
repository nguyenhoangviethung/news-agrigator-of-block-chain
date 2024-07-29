package com.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "https://www.coindesk.com/resizer/3XS7K8QRpHBaUXN8ICyFDO05NVk=/1056x594/filters:quality(80):format(webp)/cloudfront-us-east-1.images.arcpublishing.com/coindesk/ZCUDHLPSB5EDBHDOIO2BDNJVI4";
        Image image = new Image(url);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        AnchorPane  layout = new AnchorPane();
        layout.getChildren().add(imageView);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
