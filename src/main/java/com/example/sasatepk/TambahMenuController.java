package com.example.sasatepk;

import com.example.sasatepk.model.produk;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class TambahMenuController implements Initializable {

    @FXML
    private TextField addID;

    @FXML
    private TextField addProduk;

    @FXML
    private TextField addHargaJual;

    @FXML
    private Button batal;

    @FXML
    private Button simpan;

    @FXML
    private Button hapus;

    private final String fileName = "daftarMenu.txt";

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    protected void onClickBatal() {
        addID.clear();
        addProduk.clear();
        addHargaJual.clear();
    }

    @FXML
    protected void onClickSimpan(ActionEvent event) {
        try {
            double id = Double.parseDouble(addID.getText());
            String namaProduk = addProduk.getText();
            double harga = Double.parseDouble(addHargaJual.getText());

            if (!produk.isValidMenu(id, namaProduk, harga)) {
                showError("Data tidak valid. Pastikan semua input benar.");
                return;
            }

            // Cek apakah ID sudah ada
            if (produk.isIdExist(fileName, id)) {
                // Tampilkan konfirmasi untuk mengganti data
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi");
                alert.setHeaderText("Data dengan ID ini sudah ada.");
                alert.setContentText("Apakah Anda ingin mengganti data?");
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    // Hapus data lama dan simpan data baru
                    produk.deleteById(fileName, id);
                    produk.writeToFile(fileName, new produk(id, namaProduk, harga).toString());
                    showInfo("Data berhasil diganti!");
                } else {
                    showInfo("Perubahan dibatalkan.");
                }
            } else {
                // Simpan data baru
                produk.writeToFile(fileName, new produk(id, namaProduk, harga).toString());
                showInfo("Data berhasil disimpan!");
            }

            onClickBatal();

        } catch (NumberFormatException e) {
            showError("Format input tidak valid. ID dan Harga harus berupa angka.");
        } catch (IOException e) {
            showError("Terjadi kesalahan saat memproses file.");
        }
    }

    @FXML
    protected void onClickHapus(ActionEvent event) {
        try {
            double id = Double.parseDouble(addID.getText());

            if (!produk.isIdExist(fileName, id)) {
                showError("Data dengan ID ini tidak ditemukan.");
                return;
            }

            produk.deleteById(fileName, id);
            showInfo("Data berhasil dihapus!");

            onClickBatal();

        } catch (NumberFormatException e) {
            showError("Format input tidak valid. ID harus berupa angka.");
        } catch (IOException e) {
            showError("Terjadi kesalahan saat memproses file.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
