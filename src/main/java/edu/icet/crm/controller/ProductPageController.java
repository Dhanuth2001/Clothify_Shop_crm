package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.ProductBo;
import edu.icet.crm.bo.custom.SupplierBo;
import edu.icet.crm.dto.Product;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.CategoryType;
import edu.icet.crm.util.SizeType;
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

public class ProductPageController {
    public TextField txtProductId;
    public TextField txtProductName;


    public TextField txtUnitPrice;

    public Button btnSearch;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;

    public TableColumn colName;
    public TableColumn colDate;
    public TableColumn colCategory;
    public TableColumn colSize;
    public TableColumn colQuantity;
    public TableColumn colUnitPrice;
    public TableColumn colSupplierId;
    public Label txtTime;
    public Label txtDate;

    public TableView productTable;
    public TableColumn colId;
    public Button btnClear;
    public ComboBox comboProductCategory;
    public ComboBox comboProductSize;
    public ComboBox comboSupplierID;
    public TextField txtProductQuantity;

    private ProductBo productBo;
    private SupplierBo supplierBo;
    private ObservableList<Product> productList;


    public ProductPageController() throws SQLException, ClassNotFoundException {
        this.productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
        this.supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
        this.productList = FXCollections.observableArrayList();

    }

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        loadDateAndTime();
        initializeTable();
        loadProductsTable();
        loadCategories();
        loadSupplierIDs();
        loadSizes();
    }

    private void loadSupplierIDs() throws SQLException, ClassNotFoundException {
        List<Supplier> allSuppliers = supplierBo.getAllSuppliers();

        ObservableList ids = FXCollections.observableArrayList();

        allSuppliers.forEach(supplier -> {
            ids.add(supplier.getSupplierID());

        });
        System.out.println(ids);
        comboSupplierID.setItems(ids);

    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierID"));

        productTable.setItems(productList);
    }

    private void loadProductsTable() {
        List<Product> products = productBo.getAllProducts();
        if (products != null && !products.isEmpty()) {
            productList.clear();
            productList.addAll(products);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load products details.");
        }
    }

    private void loadCategories() {
        ObservableList<Object> cat = FXCollections.observableArrayList();
        cat.add("MEN");
        cat.add("WOMEN");
        cat.add("KID");

        comboProductCategory.setItems(cat);
    }

    private void loadSizes() {
        ObservableList<Object> sizes = FXCollections.observableArrayList();
        sizes.add("XS");
        sizes.add("S");
        sizes.add("M");
        sizes.add("L");
        sizes.add("XL");
        sizes.add("XXL");
        sizes.add("XXXL");

        comboProductSize.setItems(sizes);
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        Integer productId = Integer.valueOf(txtProductId.getText());
        if (productId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        Product product = productBo.getProductById(productId);
        if (product != null) {
            populateProductDetails(product);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
        }
    }

    private void populateProductDetails(Product product) {
        txtProductId.setText(String.valueOf(product.getProductID()));
        txtProductName.setText(product.getName());
        comboProductCategory.setValue(product.getCategory());
        txtProductQuantity.setText(String.valueOf(product.getQuantity()));
        comboProductSize.setValue(product.getSize());
        txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
        comboSupplierID.setValue(product.getSupplierID());
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Product product = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    LocalDate.now(),
                    txtProductName.getText(),
                    CategoryType.valueOf(comboProductCategory.getValue().toString()),
                    SizeType.valueOf(comboProductSize.getValue().toString()),
                    Double.valueOf(txtUnitPrice.getText()),
                    Integer.valueOf(txtProductQuantity.getText()),
                    Integer.valueOf(comboSupplierID.getValue().toString())
            );
            boolean success = productBo.addProduct(product);
            System.out.println(success);
            if (success) {
                productList.add(product);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
            }
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Product product = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    LocalDate.now(),
                    txtProductName.getText(),
                    CategoryType.valueOf(comboProductCategory.getValue().toString()),
                    SizeType.valueOf(comboProductSize.getValue().toString()),
                    Double.valueOf(txtUnitPrice.getText()),
                    Integer.valueOf(txtProductQuantity.getText()),
                    Integer.valueOf(comboSupplierID.getValue().toString())
            );

            boolean success = productBo.updateProduct(product);
            if (success) {
                int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    productList.set(selectedIndex, product);
                } else {

                    for (int i = 0; i < productList.size(); i++) {
                        if (productList.get(i).getProductID() == product.getProductID()) {
                            productList.set(i, product);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
                loadProductsTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product.");
            }
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        Integer productId = Integer.parseInt(txtProductId.getText());
        if (productId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        boolean success = productBo.deleteProduct(productId);
        if (success) {
            productList.removeIf(product -> product.getProductID().equals(productId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
        }
    }

    private boolean isValidInput() {
        if (txtProductId.getText().isEmpty() ||
                txtProductName.getText().isEmpty() ||
                comboProductCategory.getValue().toString().isEmpty() ||
                comboSupplierID.getValue().toString().isEmpty() ||
                comboProductSize.getValue().toString().isEmpty()||
                txtProductQuantity.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        comboSupplierID.setValue(null);
        txtProductQuantity.clear();
        comboProductCategory.setValue(null);
        txtUnitPrice.clear();
        txtProductName.clear();
        comboProductSize.setValue(null);
        txtProductId.clear();
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

        Timeline timeline= new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            LocalTime time = LocalTime.now();
            txtTime.setText(
                    time.getHour()+" : "+time.getMinute()+" : "+ time.getSecond()
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


