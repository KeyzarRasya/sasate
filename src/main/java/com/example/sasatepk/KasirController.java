package com.example.sasatepk;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KasirController {

    private Map<String, String> idMenu = Map.ofEntries(
            Map.entry("1", "Sate Bakar"),
            Map.entry("2", "Ayam Bakar")
    );
    private Map<String, Double> idPrice = Map.ofEntries(
            Map.entry("1", 25000d),
            Map.entry("2", 30000d)
    );

    private List<Pane> orderList = new ArrayList<>();
    private List<String> orderListStr = new ArrayList<>();

    private String folderPath;
    private String filename;

    @FXML
    private TextField menuId;

    @FXML
    private TextField qty;

    @FXML
    private Pane order;

    @FXML
    private Text total;

    @FXML
    private Button addButton;

    @FXML
    private Button finishButton;

    @FXML
    private Text errorMsg;

    @FXML
    private Pane historyOrder;

    private Double totalPrice = 0d;

    private float y = 20f;

    private float reportY = 14f;

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
        String orderStr = menu + "," + priceMenu + "," + quantity + "," + (priceMenu * quantity) + "," + LocalDateTime.now().format(formatter) + ";";
        System.out.println(orderStr);
        orderListStr.add(orderStr);
        // Create order pane and add to display
        Pane orderPane = createPaneOrder(menu, quantity, priceMenu * quantity);
        order.getChildren().add(orderPane);
        orderList.add(orderPane);

        // Update total price
        total.setText("Rp. " + totalPrice.toString());

        // Adjust y-coordinate for next order pane
        y += 60;

        // Clear input fields
        menuId.setText("");
        qty.setText("");
        errorMsg.setText("");
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
        pane.setLayoutX(11.0);
        pane.setLayoutY(y);
        pane.setPrefSize(342.0, 40.0);
        pane.setStyle("-fx-background-color: #FFF;");

        // Menu text
        Text menuText = new Text();
        menuText.setLayoutX(14.0);
        menuText.setLayoutY(25.0);
        menuText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        menuText.setStrokeWidth(0.0);
        menuText.setText(menu);

        // Quantity text
        Text qtyText = new Text();
        qtyText.setLayoutX(188.0);
        qtyText.setLayoutY(25.0);
        qtyText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        qtyText.setStrokeWidth(0.0);
        qtyText.setText(qty.toString());

        // Price text
        Text priceText = new Text();
        priceText.setLayoutX(270.0);
        priceText.setLayoutY(25.0);
        priceText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        priceText.setStrokeWidth(0.0);
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
        onResetButtonClicked();

    }

    @FXML
    private void readFile(){
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

        Pane newPane = createPane(parts[0],parts[1], parts[2], parts[3], parts[4], parts[5]);
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
