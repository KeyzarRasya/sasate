package com.example.sasatepk.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class produk {
    private double id;
    private String nama;
    private double harga;

    // Konstruktor default
    public produk() {}

    // Konstruktor dengan parameter
    public produk(double id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    // Getter dan Setter
    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    // Representasi string untuk format penyimpanan
    @Override
    public String toString() {
        // Format ID
        String formattedId = (id % 1 == 0) ? String.valueOf((int) id) : String.valueOf(id);
        // Format Harga
        String formattedHarga = (harga % 1 == 0) ? String.valueOf((int) harga) : String.valueOf(harga);

        return formattedId + "," + nama + "," + formattedHarga + ";";
    }

    // Validasi data menu
    public static boolean isValidMenu(double id, String nama, double harga) {
        return id > 0 && nama != null && !nama.isEmpty() && harga > 0;
    }

    // Membaca data dari file teks
    public static List<String> readFromFile(String fileName) {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.err.println("Kesalahan membaca file: " + e.getMessage());
        }
        return data;
    }

    // Menulis data ke file teks
    public static void writeToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            File file = new File(fileName);
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Kesalahan menulis ke file: " + e.getMessage());
        }
    }

    // Mencetak isi file teks ke konsol
    public static void printFileContent(String fileName) {
        List<String> data = readFromFile(fileName);
        for (String line : data) {
            System.out.println(line);
        }
    }
    
        // Cek apakah ID sudah ada
    public static boolean isIdExist(String fileName, double id) throws IOException {
        List<String> data = readFromFile(fileName);
        for (String line : data) {
            if (line.startsWith(String.valueOf((int) id) + ",")) {
                return true;
            }
        }
        return false;
    }

    // Menghapus data berdasarkan ID
    public static void deleteById(String fileName, double id) throws IOException {
        List<String> data = readFromFile(fileName);
        List<String> updatedData = new ArrayList<>();

        // Filter data untuk menghapus ID tertentu
        for (String line : data) {
            if (!line.startsWith(String.valueOf((int) id) + ",")) {
                updatedData.add(line);
            }
        }

        // Tulis ulang file dengan data yang diperbarui
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : updatedData) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    
}
