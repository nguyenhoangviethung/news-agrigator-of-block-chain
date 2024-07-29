package com.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class Controller implements Initializable{
    @FXML
    ListView<String> listView;

    ObservableList<String> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 10; i++) {
            list.add("Item " + i);
        }
        listView.setItems(list);
    }

}
