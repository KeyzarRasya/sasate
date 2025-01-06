package com.example.sasatepk;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class DaftarMenu implements Initializable {

    @FXML
    private AnchorPane main;

    private int y = 116;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readFile();
    }

    @FXML
    private Button back;

    @FXML
    void onBack(){
        openHomePage(back, "main.fxml");
    }

    private void openHomePage(Region region, String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Menu.class.getResource(fxml));
            Parent root = fxmlLoader.load();
            Scene newScene = new Scene(root);

            Stage currentStage = (Stage) region.getScene().getWindow();
            currentStage.setFullScreen(true);
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile(){
        String folderPath = new File("").getAbsolutePath();
        String filename = "daftarMenu.txt";
        String filepath = Paths.get(folderPath, filename).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line until EOF (null)
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                splitDataAndAddPane(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    void splitDataAndAddPane(String input){
        if (input.endsWith(";")) {
            input = input.substring(0, input.length() - 1);
        }

        // Pisahkan string berdasarkan koma
        String[] parts = input.split(",");
        main.getChildren().add(createPane(parts[0], parts[1], parts[2]));
    }

    Pane createPane(String id, String menu, String price){
        Pane pane = new Pane();
        pane.setLayoutX(104.0);
        pane.setLayoutY(y);
        pane.setPrefSize(1044.0, 78.0);
        pane.setStyle("-fx-background-color: white;");

        // Membuat Text 1
        Text text1 = new Text(id);
        text1.setLayoutX(47.0);
        text1.setLayoutY(44.0);
        text1.setFont(new Font(23.0));

        // Membuat Text 2
        Text text2 = new Text(menu);
        text2.setLayoutX(458.0);
        text2.setLayoutY(48.0);
        text2.setFont(new Font(23.0));

        // Membuat Text 3
        Text text3 = new Text(price);
        text3.setLayoutX(903.0);
        text3.setLayoutY(44.0);
        text3.setFont(new Font(23.0));

        // Menambahkan teks ke dalam Pane
        pane.getChildren().addAll(text1, text2, text3);
        y+=78;
        return pane;
    }
}
