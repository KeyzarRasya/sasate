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

public class LaporanKeuangan implements Initializable {

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
        String filename = "records.txt";
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
        main.getChildren().add(createPane(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
    }


    Pane createPane(String menu, String harga, String kuantitas, String jumlah, String jam, String tanggal, String id){
        Pane pane = new Pane();
        pane.setLayoutX(34.0);
        pane.setLayoutY(y);
        pane.setPrefSize(1172.0, 78.0);
        pane.setStyle("-fx-background-color: white;");

        // Membuat Text "Menu"
        Text text1 = new Text(182, 45, menu);
        text1.setFont(new Font(18));

        Text text2 = new Text(337, 45, kuantitas);
        text2.setFont(new Font(18));

        Text text3 = new Text(514, 43, harga);
        text3.setFont(new Font(18));

        Text text4 = new Text(898, 42, jam);
        text4.setFont(new Font(18));

        Text text5 = new Text(1033, 43, tanggal);
        text5.setFont(new Font(18));

        Text text6 = new Text(722, 42, jumlah);
        text6.setFont(new Font(18));

        Text text7 = new Text(14, 45, id);
        text7.setFont(new Font(18));

        // Add Text nodes to the Pane
        pane.getChildren().addAll(text1, text2, text3, text4, text5, text6, text7);

        y+=78;
        return pane;
    }
}