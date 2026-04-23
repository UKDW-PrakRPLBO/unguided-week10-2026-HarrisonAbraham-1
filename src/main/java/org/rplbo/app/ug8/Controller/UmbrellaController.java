package org.rplbo.app.ug8.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.rplbo.app.ug8.InventoryItem;
import org.rplbo.app.ug8.UmbrellaApp;
import org.rplbo.app.ug8.UmbrellaDBManager;

import java.net.URL;
import java.util.ResourceBundle;

public class UmbrellaController implements Initializable {
    // Variabel FXML diubah untuk mencerminkan skema Grup B
    @FXML private TextField txtItem, txtInitial, txtSupply;
    @FXML private TableView<InventoryItem> tableInventory;
    @FXML private TableColumn<InventoryItem, String> colName;
    @FXML private TableColumn<InventoryItem, Integer> colInitial, colSupply, colFinal;

    private UmbrellaDBManager db;
    private ObservableList<InventoryItem> masterData = FXCollections.observableArrayList();
    private InventoryItem selectedItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new UmbrellaDBManager();
        System.out.println("LOG: OPERATIVE " + UmbrellaApp.loggedInUser + " ACCESS GRANTED.");
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colInitial.setCellValueFactory(new PropertyValueFactory<>("InitialStock"));
        colSupply.setCellValueFactory(new PropertyValueFactory<>("newSupply"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalStock"));

        // ==============================================================================
        // TODO 1: MENGHUBUNGKAN KOLOM TABEL (TABLE COLUMN MAPPING)
        // ==============================================================================
        // Hubungkan setiap TableColumn (colName, colInitial, colSupply, colFinal)
        // dengan nama atribut (property) yang sesuai di dalam class InventoryItem.
        // Gunakan setCellValueFactory() dan new PropertyValueFactory<>().
        // ==============================================================================

        // --- TULIS KODE ANDA DI BAWAH INI ---





        // ==============================================================================
        // TODO 2: LISTENER KLIK BARIS TABEL (SELECTION MODEL)
        // ==============================================================================
        // Lengkapi logika di dalam listener di bawah ini:
        // 1. Masukkan objek 'newVal' ke dalam variabel global 'selectedItem'.
        // 2. Tampilkan nilai itemName dari newVal ke dalam TextField 'txtItem'.
        // 3. Tampilkan nilai initialStock dari newVal ke dalam TextField 'txtInitial'.
        // 4. Tampilkan nilai newSupply dari newVal ke dalam TextField 'txtSupply'.
        //    (Ingat: Ubah tipe data angka menjadi String menggunakan String.valueOf).
        // 5. Matikan (disable) TextField 'txtItem' agar pengguna tidak bisa mengubah
        //    nama item (Primary Key) saat sedang mengedit data.
        // ==============================================================================

        tableInventory.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedItem = newVal;
                txtItem.setText(newVal.getItemName());
                txtInitial.setText(String.valueOf(newVal.getInitialStock()));
                txtSupply.setText(String.valueOf(newVal.getNewSupply()));
                txtItem.setDisable(true); // itemName tidak boleh diubah



            }
        });

        refreshTable();
    }

    @FXML
    private void handleSave() {
        if (selectedItem != null) {
            int Initial = Integer.parseInt(txtInitial.getText());
            int Supply = Integer.parseInt(txtSupply.getText());
            int stock = Initial - Supply;

            InventoryItem updated = new InventoryItem(
                    selectedItem.getItemName(),
                    Initial,
                    Supply,
                    stock
            );

            if (db.updateItem(updated)) {
                refreshTable();
                clearFields();
            }
        }


    }

    @FXML
    private void handleAdd() {
        String name = txtItem.getText();
        int Initial = Integer.parseInt(txtInitial.getText());
        int Supply = Integer.parseInt(txtSupply.getText());
        int stock = Initial - Supply;

        InventoryItem newItem = new InventoryItem(name, Initial, Supply, stock);
        db.addItem(newItem);
        refreshTable();


    }

    @FXML
    private void handleDelete() {
        InventoryItem item = tableInventory.getSelectionModel().getSelectedItem();
        if (item != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus item " + item.getItemName() + "?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                if (db.deleteItem(item.getItemName())) {
                    masterData.remove(item);
                    clearFields();
                }
            }
        } else {
            Alert warn = new Alert(Alert.AlertType.WARNING, "Pilih item terlebih dahulu!");
            warn.showAndWait();
        }


    }

    // Logout
    @FXML
    private void handleLogout() {
        try {
            UmbrellaApp.switchScene("login-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        txtItem.clear();
        txtInitial.clear();
        txtSupply.clear();
        txtItem.setDisable(false);
        txtSupply.setDisable(false);
        selectedItem = null;
    }
    @FXML
    private void refreshTable() {
        masterData.setAll(db.getAllItems());
        tableInventory.setItems(masterData);
    }
}
