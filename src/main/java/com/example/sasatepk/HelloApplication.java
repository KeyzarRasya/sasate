package com.example.sasatepk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sasate");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static List<String> splitData(String input) {
        // Hapus titik koma di akhir, jika ada
        if (input.endsWith(";")) {
            input = input.substring(0, input.length() - 1);
        }

        // Pisahkan string berdasarkan koma
        String[] parts = input.split(",");

        // Simpan hasil ke dalam List
        System.out.println(parts[0]);
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            System.out.println(part);
            result.add(part.trim()); // Menghapus spasi ekstra
        }

        return result;
    }

    public static void main(String[] args) {
        launch();
    }
}