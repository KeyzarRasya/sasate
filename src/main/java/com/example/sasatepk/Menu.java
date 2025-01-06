package com.example.sasatepk;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


import java.io.IOException;

public class Menu {

    @FXML
    private Button kasir;
    @FXML
    private Button laporan;
    @FXML
    private Button tambah;
    @FXML
    private Button daftar;
    @FXML
    private Button back;

    @FXML
    void onKasir(){
        openHomePage(kasir, "KasirController.fxml");
    }

    @FXML
    void onLaporan(){
        openHomePage(laporan, "LaporanKeuangan.fxml");
    }

    @FXML
    void onDaftarMenu(){
        openHomePage(daftar, "daftarMenu.fxml");
    }

    @FXML
    void onTambahMenu(){
        openHomePage(tambah, "tambahMenu.fxml");
    }

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

}
