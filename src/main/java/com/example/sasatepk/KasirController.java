package com.example.sasatepk;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KasirController implements Initializable {

    private Map<String, String> idMenu = new HashMap<>();
    private Map<String, Double> idPrice = new HashMap<>();

    private List<Pane> orderList = new ArrayList<>();
    private List<String> orderListStr = new ArrayList<>();

    private String folderPath;
    private String filename;

    @FXML
    private TextField menuId;

    @FXML
    private TextField qty;

    @FXML
    private AnchorPane order;

    @FXML
    private Text total;

    @FXML
    private Button addButton;

    @FXML
    private Button finishButton;

    @FXML
    private Button plusBtn;

    @FXML
    private Text errorMsg;

    @FXML
    private Pane historyOrder;

    @FXML
    private ScrollPane scp;

    private int transactionId;

    private Double totalPrice = 0d;

    private float y = 39f;

    private float reportY = 14f;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scp.setFitToHeight(true);
        readDaftarMenu();
        findLastTransactionId();
    }

    @FXML
    private Button back;

    void findLastTransactionId(){
        String folderPath = new File("").getAbsolutePath();
        String filename = "records.txt";
        String filepath = Paths.get(folderPath, filename).toString();
        String tempTransactionId = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line until EOF (null)
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }

                // Pisahkan string berdasarkan koma
                String[] parts = line.split(",");
                tempTransactionId=parts[6];
            }
            this.transactionId = Objects.equals(tempTransactionId, "") ?   0 :  Integer.parseInt(tempTransactionId);
            this.transactionId++;
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
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

    private void readDaftarMenu(){
        String folderPath = new File("").getAbsolutePath();
        String filename = "daftarMenu.txt";
        String filepath = Paths.get(folderPath, filename).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line until EOF (null)
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                splitDataandMap(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private void splitDataandMap(String input){
        if (input.endsWith(";")) {
            input = input.substring(0, input.length() - 1);
        }

        // Pisahkan string berdasarkan koma
        String[] parts = input.split(",");
        idMenu.put(parts[0], parts[1]);
        idPrice.put(parts[0], Double.valueOf(parts[2]));
    }


    @FXML
    protected void onAddButtonClicked() {
        if (!inputValidation(menuId, qty)) {
            errorMsg.setText("ERROR: Invalid input!");
            return;
        }

        String menu = idMenu.get(menuId.getText());
        Double priceMenu = idPrice.get(menuId.getText());
        Integer quantity = Integer.valueOf(qty.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,dd-MM-yyyy");
        String orderStr = menu + "," + priceMenu + "," + quantity + "," + (priceMenu * quantity) + "," + LocalDateTime.now().format(formatter) + ","+transactionId + ";";
        System.out.println(orderStr);
        orderListStr.add(orderStr);


        // Create order pane and add to display
        Pane orderPane = createPaneOrder(menu, quantity, priceMenu * quantity);
        order.getChildren().add(orderPane);
        orderList.add(orderPane);

        // Update total price
        total.setText("Rp. " + totalPrice.toString());

        // Adjust y-coordinate for next order pane
        y += 89;

        // Clear input fields
        menuId.setText("");
        qty.setText("");
        errorMsg.setText("");
    }

    @FXML
    protected void onPlusButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tambahMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage(); // Membuat jendela baru
        stage.setTitle("Tambah Pesanan");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show(); // Menampilkan jendela baru tanpa menutup jendela utama
    }

    @FXML
    protected void onResetButtonClicked() {
        // Clear all children from the 'order' pane (remove all added panes)
        order.getChildren().clear();

        // Reset the orderList to an empty list
        orderList.clear();

        // Reset total price and update UI
        totalPrice = 0d;
        total.setText("Rp. 0");

        // Reset y-coordinate for future orders
        y = 20f;
    }

    private boolean inputValidation(TextField menuId, TextField qty) {
        // Check if fields are empty
        if (menuId.getText().isEmpty() || qty.getText().isEmpty()) {
            return false;
        }

        // Check if menu ID exists in idMenu
        if (!idMenu.containsKey(menuId.getText())) {
            return false;
        }

        // Check if quantity is a positive number
        try {
            int quantity = Integer.parseInt(qty.getText());
            if (quantity <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false; // Invalid quantity input
        }

        return true;
    }

    private Pane createPaneOrder(String menu, Integer qty, Double price) {
        Pane pane = new Pane();
        pane.setLayoutX(25.0);
        pane.setLayoutY(y);
        pane.setPrefHeight(68.0);
        pane.setPrefWidth(1215.0);
        pane.setStyle("-fx-background-color: #fff;");

        // Menu text layoutX="25.0" layoutY="39.0" prefHeight="68.0" prefWidth="1215.0" style="-fx-background-color: #fff;"
        Text menuText = new Text();
        menuText.setLayoutX(30.0);
        menuText.setLayoutY(38.0);
        menuText.setText(menu);

        // Quantity text
        Text qtyText = new Text();
        qtyText.setLayoutX(433.0);
        qtyText.setLayoutY(39.0);
        qtyText.setText(qty.toString());

        // Price text
        Text priceText = new Text();
        priceText.setLayoutX(903.0);
        priceText.setLayoutY(39.0);
        priceText.setText(price.toString());

        // Add to total price
        totalPrice += price;

        // Add all components to the pane
        pane.getChildren().addAll(menuText, qtyText, priceText);
        return pane;
    }

    @FXML
    private void payOnClick() throws IOException {
        // Setup path
        String folderPath = new File("").getAbsolutePath();
        String filename = "records.txt";
        Path filePath = Paths.get(folderPath, filename);

        // Debugging path
        System.out.println("Writing to file: " + filePath.toAbsolutePath());

        // Check if the order list is valid
        List<String> oList = this.orderListStr;
        if (oList == null || oList.isEmpty()) {
            System.out.println("No records to write.");
            return;
        }

        // Write to file
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String val : oList) {
                writer.write(val);
                writer.newLine();
                System.out.println("Writed");
            }
            writer.flush(); // Force write
            System.out.println("Records saved successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            throw e;
        }
        findLastTransactionId();
        onResetButtonClicked();

    }

    @FXML
    private void readFile() {
        historyOrder.getChildren().clear();
        String folderPath = new File("").getAbsolutePath();
        String filename = "records.txt";
        String filepath = Paths.get(folderPath, filename).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            // Read each line until EOF (null)
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print the line
                Pane oPanel = splitDataAndCreatePane(line);
                historyOrder.getChildren().add(oPanel);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public Pane splitDataAndCreatePane(String input) {
        // Hapus titik koma di akhir, jika ada
            if (input.endsWith(";")) {
                input = input.substring(0, input.length() - 1);
            }

            // Pisahkan string berdasarkan koma
            String[] parts = input.split(",");

        Pane newPane = createPane(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        return newPane;

    }

    public Pane createPane(String menu, String price, String qty, String total, String time, String date) {
        // Membuat Pane
        Pane paneList = new Pane();
        paneList.setLayoutX(29.0);
        paneList.setLayoutY(reportY);
        paneList.setPrefHeight(74.0);
        paneList.setPrefWidth(519.0);
        paneList.setStyle("-fx-background-color: white;");

        // Menambahkan Text "Nama Menu"
        Text textNamaMenu = new Text(menu);
        textNamaMenu.setLayoutX(14.0);
        textNamaMenu.setLayoutY(42.0);

        // Menambahkan Text "Qty"
        Text textQty = new Text(qty);
        textQty.setLayoutX(197.0);
        textQty.setLayoutY(42.0);

        // Menambahkan Text "Price"
        Text textPrice = new Text(price);
        textPrice.setLayoutX(122.0);
        textPrice.setLayoutY(42.0);

        // Menambahkan Text "Total"
        Text textTotal = new Text(total);
        textTotal.setLayoutX(290.0);
        textTotal.setLayoutY(42.0);

        Text textTanggal = new Text(date);
        textTanggal.setLayoutX(435.0);
        textTanggal.setLayoutY(28.0);

        Text textJam = new Text(time);
        textJam.setLayoutX(448.0);
        textJam.setLayoutY(65.0);

        reportY += 80;
        paneList.getChildren().addAll(textNamaMenu, textQty, textPrice, textTotal, textTanggal, textJam);

        return paneList;
    }
}
