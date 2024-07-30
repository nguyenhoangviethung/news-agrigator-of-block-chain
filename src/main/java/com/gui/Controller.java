package com.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Controller implements Initializable{
    @FXML
    ListView<String> listView;

    @FXML
    ImageView imageView;

    @FXML
    Label summary;
    ObservableList<String> list = FXCollections.observableArrayList();
    JsonArray arrayOfNews = new ActionOnJson().read("src\\main\\resources\\com\\gui\\data.json");
    int currentPage = 1;

    @FXML
    Button latest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if( arrayOfNews.size()%15 == 0) {
            latest.setText(String.valueOf(arrayOfNews.size() / 15));
        }
        else {
            latest.setText(String.valueOf(arrayOfNews.size() / 15 + 1));
        }

        try {
            ActionOnJson.loadImage(arrayOfNews);
            Thread.sleep(5000);
            listView.setItems(list);
            for(int i = 0; i < 15; i++) {
                JsonObject jsonObject = arrayOfNews.get(i).getAsJsonObject();
                String showString = jsonObject.get("title").getAsString() + "\n" + "By: " + jsonObject.get("author").getAsString() + "  At: " + jsonObject.get("datetimeCreation").getAsString();
                list.add(showString);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    void handleOfListView(MouseEvent event) {
        if(event.getClickCount() == 1) {
            listView.getSelectionModel().selectedIndexProperty().addListener((ob, o, n) -> {
                if(n!= null) {
                            Image image = ActionOnJson.images[(int)n + 15*(currentPage-1)];
                            imageView.setImage(image);
                            Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                            clip.setArcWidth(40);
                            clip.setArcHeight(40);
                            imageView.setClip(clip);
                            summary.setText(arrayOfNews.get((int)n).getAsJsonObject().get("summary").getAsString());
                    }
                }
            );
        }
    }

    @FXML
    void handleOfPageNumber(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        switch (selectedButton.getText().toLowerCase()) {
            case "first page":
                currentPage = 1;
                break;
            case "end page":
                if(arrayOfNews.size()%15 == 0) {
                    currentPage = arrayOfNews.size() / 15;
                }
                else {
                    currentPage = arrayOfNews.size() / 15 + 1;
                }
                
                break;
            default:
                currentPage = Integer.parseInt(selectedButton.getText());
                break;
        }
        
        imageView.setImage(ActionOnJson.images[(currentPage-1)*15]);
        summary.setText(arrayOfNews.get((currentPage-1)*15).getAsJsonObject().get("summary").getAsString());
        int k = 15;
        for(int j = (currentPage-1)*15; j < (currentPage*15); j++) {
            if(j == arrayOfNews.size()) {
                while(k-- > 0) list.remove(0);
                break;
            }
            JsonObject  jsonObject = arrayOfNews.get(j).getAsJsonObject();
            String showString = jsonObject.get("title").getAsString() + "\n" + "By: " +jsonObject.get("author").getAsString() + "   At: " + jsonObject.get("datetimeCreation").getAsString();
            list.add(showString);
            list.remove(0);
            k--;
        }
        // for(int k = 0; k < 15)
        listView.scrollTo(0);
    }
}
