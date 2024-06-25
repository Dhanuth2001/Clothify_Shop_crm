package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.CustomerBo;
import edu.icet.crm.dto.Customer;
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
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerPageController {
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public DatePicker txtCustomerDob;
    public TextField txtEmail;
    public TextField txtContactNo;
    public Button btnAddCustomer;
    public Label txtDate;
    public Label txtTime;
    public TableColumn colContactNumber;
    public TableColumn colEmail;
    public TableColumn colDob;
    public TableColumn colName;
    public TableColumn colId;
    public TableView customerTable;
    public Button btnClear;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnSearch;

    private CustomerBo customerBo;
    private ObservableList<Customer> customerList;

    public CustomerPageController() throws SQLException, ClassNotFoundException {
        this.customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
        this.customerList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        loadDateAndTime();
        initializeTable();
        loadCustomersTable();
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        customerTable.setItems(customerList);
    }

    private void loadCustomersTable() throws SQLException, ClassNotFoundException {
        List<Customer> customers = customerBo.getAllCustomers();
        if (customers != null && !customers.isEmpty()) {
            customerList.clear();
            customerList.addAll(customers);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load customer details.");
        }
    }

    @FXML
    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (isValidInput()) {
            Customer customer = new Customer(
                    Integer.parseInt(txtCustomerId.getText()),
                    txtCustomerName.getText(),
                    txtCustomerDob.getValue(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            boolean success = customerBo.addCustomer(customer);
            if (success) {
                customerList.add(customer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer.");
            }
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Integer customerId = Integer.valueOf(txtCustomerId.getText());
        if (customerId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a customer ID.");
            return;
        }

        Customer customer = customerBo.getCustomerById(customerId);
        if (customer != null) {
            populateCustomerDetails(customer);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Customer not found.");
        }
    }

    private void populateCustomerDetails(Customer customer) {
        txtCustomerId.setText(String.valueOf(customer.getCustomerID()));
        txtCustomerName.setText(customer.getName());
        txtCustomerDob.setValue(customer.getDob());
        txtEmail.setText(customer.getContactEmail());
        txtContactNo.setText(customer.getContactNumber());
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (isValidInput()) {
            Customer customer = new Customer(
                    Integer.parseInt(txtCustomerId.getText()),
                    txtCustomerName.getText(),
                    txtCustomerDob.getValue(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );

            boolean success = customerBo.updateCustomer(customer);
            if (success) {
                int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    customerList.set(selectedIndex, customer);
                } else {
                    for (int i = 0; i < customerList.size(); i++) {
                        if (customerList.get(i).getCustomerID() == customer.getCustomerID()) {
                            customerList.set(i, customer);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
                loadCustomersTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.");
            }
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent)  {
        Integer customerId = Integer.parseInt(txtCustomerId.getText());
        if (customerId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a customer ID.");
            return;
        }


        try {
            boolean success = customerBo.deleteCustomer(customerId);
            if (success) {
                customerList.removeIf(customer -> customer.getCustomerID().equals(customerId));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private boolean isValidInput() {
        if (txtCustomerId.getText().isEmpty() ||
                txtCustomerName.getText().isEmpty() ||
                txtCustomerDob.getValue() == null ||
                txtEmail.getText().isEmpty() ||
                txtContactNo.getText().isEmpty()) {
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
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerDob.setValue(null);
        txtEmail.clear();
        txtContactNo.clear();
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

    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }


}
