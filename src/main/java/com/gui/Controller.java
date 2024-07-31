package com.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
    Button latest, home;

    @FXML
    TextField numberPage, search;

    ObservableList<String> list = FXCollections.observableArrayList();
    static JsonArray arrayOfNews = new OtherActionAndJson().read("src\\main\\resources\\com\\data\\data.json");
    static List<String> suggestions = new OtherActionAndJson().loadSuggestions("src\\main\\resources\\com\\data\\suggestions.json");
    int currentPage = 1;
    static int Max = 0;
    static int start = 0;
    int defaultSize = arrayOfNews.size();

    int loadListView(int start, int temp) {
        int countToRemove = list.size();
        currentPage = temp;
        for(int j = start + (currentPage-1)*15; j < start + (currentPage*15); j++) {
            if(j == arrayOfNews.size()) {
                break;
            }
            JsonObject  jsonObject = arrayOfNews.get(j).getAsJsonObject();
            String showString = jsonObject.get("title").getAsString() + "\n" + "By: " +jsonObject.get("author").getAsString() + "   At: " + jsonObject.get("datetimeCreation").getAsString();
            list.add(showString);
        }
        return countToRemove;
    }

    void removeCur(int countToRemove) {
        for(int i = 0; i < countToRemove; i++) {
            list.remove(0);
        }
    }
   
    void loadPreview(int start, int index)  {
        if(start + index + 15*(currentPage-1) >= arrayOfNews.size()) return;
        Image image = OtherActionAndJson.images[start + index + 15*(currentPage-1)];
        imageView.setImage(image);
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        imageView.setClip(clip);
        summary.setText(arrayOfNews.get(start + index + 15*(currentPage-1)).getAsJsonObject().get("summary").getAsString());
   }

   void showProperty(int start, int index) {
        Stage stage = new Stage();
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load(arrayOfNews.get(start + index + 15*(currentPage-1)).getAsJsonObject().get("link").getAsString());
        engine.setJavaScriptEnabled(false);
        StackPane stackPane = new StackPane(webView);
        Scene scene = new Scene(stackPane, 800, 600);
        stage.setScene(scene);
        stage.show();
   }
    
   @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // gan gia tri so trang lon nhat cho nut trang cuo
        getMaxPage(arrayOfNews);
        latest.setText(String.valueOf(Max));
       
        handleOfSearch(suggestions);
        home.setOnAction(e -> {
                start = 0;
                int i = defaultSize;
                while(arrayOfNews.size() != defaultSize) arrayOfNews.remove(i);
                getMaxPage(arrayOfNews);
                latest.setText(String.valueOf(Max));
                currentPage = 1;
                numberPage.setText("");
                loadPreview(start, 0);
                loadListView(start, 1);
            });
                     
        try {
            OtherActionAndJson.loadImage(arrayOfNews); // load anh truoc khi chay
            //them gia tri vao ListView qua list(ObservableList)
            listView.setItems(list);
            
            loadListView(start, 1);
        } catch (Exception e) {
        }
        handleOfNumberPage(); // xu ly so trang (ham nay bat buoc goi o day de tu dong load ListView khi nhap so trang) 
    }

    static void getMaxPage(JsonArray jsonArray){
        if( jsonArray.size()%15 == 0) {
            Max = jsonArray.size() / 15;
        }
        else {
            Max = jsonArray.size() / 15 + 1;
        }
    }

    void handleOfSearch(List<String> suggestionList) {
        if(search.equals("")) return;
        TextFields.bindAutoCompletion(search, suggestionRequest -> {
            List<String> filteredSuggestions = new ArrayList<>();
            String currentText = suggestionRequest.getUserText().toLowerCase();
            filteredSuggestions.add(currentText);
            for(String suggestion : suggestionList)  {
                if(suggestion.toLowerCase().contains(currentText)) {
                    filteredSuggestions.add(suggestion);
                } 
                if(filteredSuggestions.size() >= 15) break;
            }
            return filteredSuggestions;
        });
        search.setOnAction(event -> {
            int countToRemove = list.size();
            String currentText = search.getText().toLowerCase();
            Optional<String> match = suggestionList.stream()
            .filter(suggestion -> suggestion.toLowerCase().contains(currentText)).findFirst();
            if(match.isEmpty()) {
                Image image = new Image(getClass().getResource("/com/image/shy.jpg").toExternalForm());
                imageView.setImage(image);
                summary.setText("No match found for " + currentText);
                currentPage = 0;
                for(int i = 0; i < countToRemove ; i++) {
                    list.remove(0);
                }
            }
            else{
                // System.out.println(match.get());
                currentPage = 1;
                start = arrayOfNews.size();
                JsonArray jsonArray = new JsonArray();
                int add = start;
                for(int i = 0; i < suggestionList.size() ; i++) {
                    if(suggestionList.get(i).toLowerCase().contains(currentText)) {   
                        jsonArray.add(arrayOfNews.get(i));
                        arrayOfNews.add(arrayOfNews.get(i));
                        OtherActionAndJson.images[add++] = OtherActionAndJson.images[i];
                    }
                }
                getMaxPage(jsonArray);
                latest.setText(String.valueOf(Max));
                loadPreview(start, 0);
                int i = loadListView(start , 1); 
                removeCur(i);
                numberPage.setText("");
                listView.scrollTo(0);
            }
        });
    }

    @FXML
    void handleOfListView(MouseEvent event) {
        if(event.getClickCount() == 1) { // khi click 1 lan vao item 
            listView.getSelectionModel().selectedIndexProperty().addListener((ob, o, n) -> {
                if(currentPage != 0){
                    //theo dõi sự thay đổi khi chọn một bản tin
                    if(n!= null) {
                            loadPreview(start, (int)n);
                        }
                    }
                }
            );
        }
        if(event.getClickCount() == 2) { // khi click 2 lan vao item
            int n = listView.getSelectionModel().getSelectedIndex(); //chọn vị trí trong bản tin
            showProperty(start, n);
        }
    }

    @FXML
    void handleOfPageNumber(ActionEvent event) { // khi bam cac button chon trang  
        Button selectedButton = (Button) event.getSource();
        switch (selectedButton.getText().toLowerCase()) {
            case "<":
                if(currentPage > 1) currentPage--;
                break;
            case ">":
                if(currentPage < Max) currentPage++;
                else return;
                break;
            case "first page":
                currentPage = 1;
                break;
            case "end page":
                if(currentPage == Max) return;
                currentPage = Max;              
                break;
            default:
                if(currentPage == Max && selectedButton.getText().toLowerCase().equals("16")) return;
                if(Integer.parseInt(selectedButton.getText()) > Max)  return;
                currentPage = Integer.parseInt(selectedButton.getText());
                break;
        }        
        loadPreview(start, 0);
        
        int i = loadListView(start, currentPage); 
        removeCur(i);    
        listView.scrollTo(0);
        numberPage.setText(String.valueOf(currentPage));
        return;
    }

    @FXML
    void handleOfNumberPage() { //call this method in initialize method
        numberPage.textProperty().addListener((ob, o, n) -> {
            try {
                if(!n.equals("")) {
                    int value = Integer.parseInt(n);
                    if(value > 0 && value <= Integer.parseInt(latest.getText())) {
                        currentPage = value;
                        loadPreview(start, 0);
                        int i = loadListView(start, currentPage);
                        removeCur(i);
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
            if(!n) {
                warning.setText("");  
                return;
            }
        });  
    }
}
