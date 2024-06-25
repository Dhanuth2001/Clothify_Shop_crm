package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.SupplierBo;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.util.BoType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupplierPageController {
    public TextField txtSupplierId;
    public TextField txtSupplierCompany;
    public TextField txtEmail;
    public TextField txtContactNo;
    public TextArea txtSupplierAddress;
    public Button btnSearch;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public TableColumn colCompany;
    public TableColumn colDateAdded;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public TableColumn colContactNo;
    public Label txtTime;
    public Label txtDate;
    public Button btnClear;
    public TableView supplierTable;
    private SupplierBo supplierBo;
    public TableColumn colId;


    private ObservableList<Supplier> supplierList;

    public SupplierPageController() throws SQLException, ClassNotFoundException {
        this.supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
        this.supplierList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        loadDateAndTime();
        initializeTable();
        loadSuppliersTable();
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDateAdded.setCellValueFactory(new PropertyValueFactory<>("date"));
        supplierTable.setItems(supplierList);
    }

    private void loadSuppliersTable() {
        List<Supplier> suppliers = supplierBo.getAllSuppliers();
        if (suppliers != null && !suppliers.isEmpty()) {
            supplierList.clear();
            supplierList.addAll(suppliers);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load supplier details.");
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        Integer supplierId = Integer.valueOf(txtSupplierId.getText());
        if (supplierId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a supplier ID.");
            return;
        }

        Supplier supplier = supplierBo.getSupplierById(supplierId);
        if (supplier != null) {
            populateSupplierDetails(supplier);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Supplier not found.");
        }
    }

    private void populateSupplierDetails(Supplier supplier) {
        txtSupplierId.setText(String.valueOf(supplier.getSupplierID()));
        txtSupplierCompany.setText(supplier.getCompany());
        txtSupplierAddress.setText(supplier.getAddress());
        txtEmail.setText(supplier.getEmail());
        txtContactNo.setText(supplier.getContactNumber());
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Supplier supplier = new Supplier(
                    Integer.parseInt(txtSupplierId.getText()),
                    txtSupplierCompany.getText(),
                    txtSupplierAddress.getText(),
                    txtContactNo.getText(),
                    txtEmail.getText(),
                    LocalDate.now()
            );
            boolean success = supplierBo.addSupplier(supplier);
            if (success) {
                supplierList.add(supplier);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier added successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add supplier.");
            }
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Supplier supplier = new Supplier(
                    Integer.parseInt(txtSupplierId.getText()),
                    txtSupplierCompany.getText(),
                    txtSupplierAddress.getText(),
                    txtContactNo.getText(),
                    txtEmail.getText(),
                    LocalDate.now()
            );
            boolean success = supplierBo.updateSupplier(supplier);
            if (success) {
                int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    supplierList.set(selectedIndex, supplier);
                } else {

                    for (int i = 0; i < supplierList.size(); i++) {
                        if (supplierList.get(i).getSupplierID() == supplier.getSupplierID()) {
                            supplierList.set(i, supplier);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
                loadSuppliersTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product.");
            }
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        Integer supplierId = Integer.parseInt(txtSupplierId.getText());
        if (supplierId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a supplier ID.");
            return;
        }

        boolean success = supplierBo.deleteSupplier(supplierId);
        if (success) {
            supplierList.removeIf(supplier -> supplier.getSupplierID().equals(supplierId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier.");
        }
    }

    private boolean isValidInput() {
        if (txtSupplierId.getText().isEmpty() ||
                txtSupplierCompany.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtContactNo.getText().isEmpty() ||
                txtSupplierAddress.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        if (!isValidEmail(txtEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            return false;
        }

        if (!isValidPhoneNumber(txtContactNo.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid phone number that starts with 0 and has 10 digits.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void clearFields() {
        txtSupplierId.clear();
        txtSupplierCompany.clear();
        txtEmail.clear();
        txtContactNo.clear();
        txtSupplierAddress.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        txtDate.setText(f.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            LocalTime time = LocalTime.now();
            txtTime.setText(
                    time.getHour() + " : " + time.getMinute() + " : " + time.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

}
