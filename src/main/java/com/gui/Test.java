package com.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
        TextField textField = new TextField();

        List<String> possibleSuggestions = List.of("apple", "banana", "grape", "orange", "strawberry", "melon", "watermelon", "cherry");

        AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(textField, suggestionRequest -> {
            List<String> filteredSuggestions = new ArrayList<>();
            String userText = suggestionRequest.getUserText().toLowerCase();

            for (String suggestion : possibleSuggestions) {
                if (suggestion.toLowerCase().contains(userText)) {
                    filteredSuggestions.add(suggestion);
                }
                if (filteredSuggestions.size() >= 5) { // Giới hạn số từ được đề xuất
                    break;
                }
            }

            return filteredSuggestions;
        });

        textField.setOnAction(event -> {
            String currentText = textField.getText();
            Optional<String> match = possibleSuggestions.stream()
                    .filter(suggestion -> suggestion.equalsIgnoreCase(currentText))
                    .findFirst();

            if (match.isEmpty()) {
                textField.setText(""); // Xóa văn bản nếu không có từ nào khớp
            }
        });

        VBox vBox = new VBox(textField);
        Scene scene = new Scene(vBox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("AutoComplete Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
