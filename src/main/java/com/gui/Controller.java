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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller implements Initializable{
    @FXML
    ListView<String> listView;

    @FXML
    ImageView imageView;

    @FXML
    Label summary, warning;

    @FXML
    Button latest;

    @FXML
    TextField numberPage;

    ObservableList<String> list = FXCollections.observableArrayList();
    static JsonArray arrayOfNews = new ActionOnJson().read("src\\main\\resources\\com\\data\\data.json");
    int currentPage = 1;
    static int Max = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMaxPage();
        latest.setText(String.valueOf(Max));
        
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
        handleOfNumberPage();
    }

    static void getMaxPage(){
        if( arrayOfNews.size()%15 == 0) {
            Max = arrayOfNews.size() / 15;
        }
        else {
            Max = arrayOfNews.size() / 15 + 1;
        }
    }

    @FXML
    void handleOfListView(MouseEvent event) {
        if(event.getClickCount() == 1) {
            listView.getSelectionModel().selectedIndexProperty().addListener((ob, o, n) -> {
                //theo dõi sự thay đổi khi chọn một bản tin
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
        if(event.getClickCount() == 2) {
            int n = listView.getSelectionModel().getSelectedIndex(); //chọn vị trí trong bản tin
                Stage stage = new Stage();
                WebView webView = new WebView();
                WebEngine engine = webView.getEngine();
                engine.load(arrayOfNews.get((int)n).getAsJsonObject().get("link").getAsString());
                engine.setJavaScriptEnabled(false);
                StackPane stackPane = new StackPane(webView);
                Scene scene = new Scene(stackPane, 800, 600);
                stage.setScene(scene);
                stage.show();
                event.consume();                       
        }
    }

    @FXML
    void handleOfPageNumber(ActionEvent event) {
        Button selectedButton = (Button) event.getSource();
        switch (selectedButton.getText().toLowerCase()) {
            case "<":
                if(currentPage > 1) currentPage--;
                break;
            case ">":
                if(currentPage < Max) currentPage++;
                break;
            case "first page":
                currentPage = 1;
                break;
            case "end page":
                currentPage = Max;                
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

    @FXML
    void handleOfNumberPage() { //call this method in initialize method
        numberPage.textProperty().addListener((ob, o, n) -> {
            try {
                if(!n.equals("")) {
                    int value = Integer.parseInt(n);
                    if(value > 0 && value <= Integer.parseInt(latest.getText())) {
                        currentPage = value;
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
                        warning.setText("");
                    }
                    else warning.setText("Please enter a valid number");
                }
                else warning.setText("Please enter a valid number");
            } catch (Exception e) {
                warning.setText("Please enter a valid number");
            }
            
        });
        numberPage.focusedProperty().addListener((ob, o, n) -> {
            if(!n) warning.setText("");
        });
    }
}
