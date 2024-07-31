package com.gui;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.scene.image.Image;
import javafx.util.Pair;

public class OtherActionAndJson {
    String path;
    static Image[] images = new Image[5000];

    JsonArray read(String path) {
        try (FileReader fileReader = new FileReader(path)){
            JsonArray jsonArray = new Gson().fromJson(fileReader, JsonArray.class);
            fileReader.close();
            return jsonArray;
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }

    <T> void write(List<T> dataList, String path) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(dataList);
            FileWriter fileWriter = new FileWriter(path, Charset.forName("UTF-8"));
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void loadImage(JsonArray arrayOfNews) {
        int count = arrayOfNews.size();
        CountDownLatch latch = new CountDownLatch(count);
        for(int i = 0; i < arrayOfNews.size(); i++) {
            int j = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Image image = new Image(arrayOfNews.get(j).getAsJsonObject().get("linkImage").getAsString(),true);
                    images[j] = image;
                    latch.countDown();
                }
            }); 
            thread.start();
        }
        try {
            latch.await();
        } catch (Exception e) {
        }        
    }

    List<String> loadSuggestions(String path) {
        // List<String> suggestionList = new ArrayList<>();
        List<String> suggestionArray = new ArrayList<>();
        try {
            JsonArray jsonArray = read("src\\main\\resources\\com\\data\\data.json");
            for(int i = 0; i < jsonArray.size(); i++) {
                suggestionArray.add(jsonArray.get(i).getAsJsonObject().get("title").getAsString());
                // System.out.println(suggestionArray);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(suggestionArray);
            FileWriter fileWriter = new FileWriter(path, Charset.forName("UTF-8"));
            fileWriter.write(json);
            fileWriter.close();
            System.out.println(("SC"));
            // System.out.println(suggestionArray);
            return suggestionArray;
        } catch (Exception e) {
            return null;
        }
    }
}
