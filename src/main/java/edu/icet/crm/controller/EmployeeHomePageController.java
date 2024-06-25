package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.*;
import edu.icet.crm.dto.*;
import edu.icet.crm.service.*;
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

public class EmployeeHomePageController {
    public Button btnDropFromCart;
    public TableView tblOrder;
    public TableColumn colProductId;
    public TableColumn colSize;
    public TableColumn colQuantity;
    public TableColumn colUnitPrice;
    public TableColumn colCost;
    public Label txtTime;
    public Label txtDate;
    public ComboBox comboProductID;
    public TextField txtDiscount;
    public Button btnAddToCart;
    public Button btnClearOtherDetails;
    public Button btnSelectOtherDetails;
    public Button btnLoad;
    public ComboBox comboCustomerIDs;
    public ComboBox comboPaymentType;
    public ComboBox comboSize;
    public Label lblOrderId;
    public Label lblUnitPrice;
    public Spinner spinQuantity;
    public Label lblTotalAmount;
    public Label lblDiscountGiven;
    public Label lblNetAmount;
    public Button btnCancelOrder;
    public Button btnPlaceOrder;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public DatePicker txtCustomerDob;
    public TextField txtEmail;
    public TextField txtContactNo;
    public Button btnAddCustomer;
    private OrderBo orderBo;
    private CustomerBo customerBo;
    private ProductBo productBo;
    //private EmployeeBo employeeBo;
    //private OrderDetailBo orderDetailBo;

    private EmailService emailService;

    private UserBo userBo;

    private ObservableList<OrderDetails> orderDetailList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            this.userBo = BoFactory.getInstance().getBo(BoType.USER);
            //this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
            this.productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
            this.orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
            this.customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
            //this.orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDERDETAILS);
            emailService = new EmailService();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }
        loadProductIDs();
        loadCustomerIDs();
        loadPaymentType();
        generateOrderId();
        loadDateAndTime();
        initializeTable();
    }

    private void loadOrderDetailTable() {
    }
    private void loadPaymentType() {
        ObservableList<Object> paymentTypes = FXCollections.observableArrayList();
        paymentTypes.add("CASH");
        paymentTypes.add("DEBIT CARD");
        paymentTypes.add("CREDIT CARD");
        paymentTypes.add("ONLINE TRANSFER");


        comboPaymentType.setItems(paymentTypes);
    }

    private void loadCustomerIDs() {

        List<Integer> productIDs = customerBo.getCustomerIDs();
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboCustomerIDs.setItems(observableProductIDs);
    }

    private void initializeTable() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productID"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("sizes"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));


        tblOrder.setItems(orderDetailList);
    }

    private void loadProductIDs() {

        List<Integer> productIDs = productBo.getAllProductIDs();
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboProductID.setItems(observableProductIDs);
    }

    private void loadSizesByProductId(int productId) {
        List<String> sizes = productBo.getSizesByProductId(productId);
        ObservableList<String> observableSizes = FXCollections.observableArrayList(sizes);
        comboSize.setItems(observableSizes);
    }

    private void limitQuantitySpinner(int productId) {
        int availableQuantity = productBo.getAvailableQuantityByProductId(productId);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, availableQuantity);
        spinQuantity.setValueFactory(valueFactory);
    }

    public void btnDropFromCartOnAction(ActionEvent actionEvent) {
        /*OrderDetails selectedOrderDetail = (OrderDetails) tblOrder.getSelectionModel().getSelectedItem();
        if (selectedOrderDetail != null) {
            orderDetailList.remove(selectedOrderDetail);
            tblOrder.refresh();
            updateTotalAmounts();
        }*/

        // Prompt the user to select a product ID to remove
        String productIdStr = comboProductID.getValue().toString(); // Assuming productID is selected from a ComboBox
        if (productIdStr == null || productIdStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Product Selected", "Please select a product to remove.");
            return;
        }

        int productId = Integer.parseInt(productIdStr);

        // Find the OrderDetails object in the list that matches the selected productID
        OrderDetails selectedOrderDetail = null;
        for (OrderDetails orderDetail : orderDetailList) {
            if (orderDetail.getProductID() == productId) {
                selectedOrderDetail = orderDetail;
                break;
            }
        }

        if (selectedOrderDetail != null) {
            // Remove the matched OrderDetails object from the list
            orderDetailList.remove(selectedOrderDetail);
            tblOrder.refresh();
            updateTotalAmounts();

        } else {
            showAlert(Alert.AlertType.WARNING, "Product Not Found", "The selected product is not in the cart.");
        }
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        int productId = Integer.parseInt(comboProductID.getValue().toString());
        String size = comboSize.getValue().toString();
        int quantity = (int) spinQuantity.getValue();
        double unitPrice = productBo.getUnitPriceByProductId(productId);
        String discountText = txtDiscount.getText();
        OrderDetails orderDetails = new OrderDetails(Integer.parseInt(lblOrderId.getText()), productId, quantity, size, unitPrice, quantity * unitPrice);
        if (discountText == null || discountText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Discount cannot be empty.");
            return;
        }
        orderDetailList.add(orderDetails);
        tblOrder.refresh();
        updateTotalAmounts();
        loadOrderDetailTable();
        clearOrderDetailFields();
    }

    private void updateTotalAmounts() {
        String discountText = txtDiscount.getText();


        double totalAmount = orderDetailList.stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getUnitPrice())
                .sum();

        try {
            double discount = Double.parseDouble(discountText);
            double discountAmount = totalAmount * (discount / 100);
            double netAmount = totalAmount - discountAmount;

            lblTotalAmount.setText(String.format("%.2f", totalAmount));
            lblDiscountGiven.setText(String.format("%.2f", discountAmount));
            lblNetAmount.setText(String.format("%.2f", netAmount));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Discount must be a valid number.");
        }
    }
    public void btnAddCustomerOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Customer customer = new Customer(
                    Integer.parseInt(txtCustomerId.getText()),
                    txtCustomerName.getText(),
                    txtCustomerDob.getValue(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            boolean success = customerBo.addCustomer(customer);
            System.out.println(success);
            if (success) {

                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
                clearCustomerFields();
                loadOrderDetailTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
            }
        }
    }

    private void clearCustomerFields() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerDob.setValue(null);
        txtEmail.clear();
        txtContactNo.clear();

    }

    private void clearOrderFields() {
        comboCustomerIDs.setValue(null);
        comboPaymentType.setValue(null);


        txtDiscount.setText(null);

    }

    private void clearOrderDetailFields() {
        comboProductID.setValue(null);
        comboSize.setValue(null);
        spinQuantity.getValueFactory().setValue(null);

        lblUnitPrice.setText(null);

    }

    private boolean isValidInput() {
        if (txtCustomerId.getText().isEmpty() || txtCustomerName.getText().isEmpty() || txtCustomerDob.getValue() == null ||
                txtEmail.getText().isEmpty() || txtContactNo.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all customer details.");
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

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        try {
            // Get employee ID from logged-in user email
            String employeeEmail = LoginPageController.getLoggedInEmployeeEmail();
            System.out.println(employeeEmail);
            int employeeID = userBo.getEmployeeIdByEmail(employeeEmail);
            System.out.println(employeeID);

            // Get customer ID and discount from UI components
            int customerID = Integer.parseInt(comboCustomerIDs.getValue().toString());
            double discount = Double.parseDouble(txtDiscount.getText());

            // Validate order details
            if (orderDetailList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Order", "Cannot place an empty order.");
                return;
            }

            // Calculate total cost of the order
            double totalCost = calculateTotalCost();

            // Get payment type from UI component
            String paymentType = comboPaymentType.getValue().toString();

            // Create Order object
            Order order = createOrderObject(employeeID, customerID, discount, totalCost, paymentType);

            // Call service to save the order
            boolean success = orderBo.placeOrder(order);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Order placed successfully.");
                clearAllFields();
                generateOrderId(); // Clear order ID after order placement

                // Send email to customer
                sendOrderConfirmationEmail(order);
            } else {
                showAlert(Alert.AlertType.ERROR, "Order Placement Failed", "Failed to place order.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Discount must be a valid number.");
        }
    }

    private double calculateTotalCost() {
        return orderDetailList.stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getUnitPrice())
                .sum();
    }

    private Order createOrderObject(int employeeID, int customerID, double discount, double totalCost, String paymentType) {
        LocalDate datePlaced = LocalDate.now();
        Order order = new Order();
        order.setOrderID(Integer.parseInt(lblOrderId.getText())); // Assuming orderID is displayed in a Label
        order.setEmployeeID(employeeID);
        order.setCustomerID(customerID);
        order.setDiscount(discount);
        order.setTotalCost(totalCost);
        order.setPaymentType(paymentType);
        order.setDatePlaced(datePlaced);
        order.setOrderDetailList(orderDetailList);
        return order;
    }

    private void sendOrderConfirmationEmail(Order order) {
        String customerEmail = customerBo.getCustomerEmailById(order.getCustomerID());
        String subject = "Order Confirmation";
        String body = "Dear Customer,\n\nYour order has been placed successfully. Details:\n" +
                "Order ID: " + order.getOrderID() + "\n" +
                "Total Cost: $" + order.getTotalCost() + "\n" +
                "Payment Type: " + order.getPaymentType() + "\n" +
                "Date Placed: " + order.getDatePlaced() + "\n\n" +
                "Thank you for shopping with us!";

        boolean emailSent = emailService.sendEmail(customerEmail, subject, body);

        if (emailSent) {
            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Order placed successfully. Bill sent to customer.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Email Sending Failed", "Failed to send email to customer.");
        }
    }

    private void clearAllFields() {
        clearOrderDetailFields();
        clearCustomerFields();
        clearOrderFields();
        orderDetailList.clear();
        lblOrderId.setText("");
        lblDiscountGiven.setText("");
        lblUnitPrice.setText("");
        lblNetAmount.setText("");
        lblTotalAmount.setText("");
        loadDateAndTime(); // Assuming this method updates date and time fields
    }


    public void btnLoadOrderDetailsOnAction(ActionEvent actionEvent) {
        int productID = Integer.parseInt(comboProductID.getValue().toString());
        loadSizesByProductId(productID);
        limitQuantitySpinner(productID);
        double unitPrice = productBo.getUnitPriceByProductId(productID);
        lblUnitPrice.setText(String.valueOf(unitPrice));
    }

    public void btnCancelOrderOnAction(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerDob.setValue(null);
        txtEmail.clear();
        txtContactNo.clear();

        // Clear order-related fields
        comboCustomerIDs.setValue(null);
        comboPaymentType.setValue(null);
        txtDiscount.clear();
        lblTotalAmount.setText("");
        lblDiscountGiven.setText("");
        lblNetAmount.setText("");

        // Clear order detail-related fields
        comboProductID.setValue(null);
        comboSize.setValue(null);
        if (spinQuantity.getValueFactory() != null) {
            spinQuantity.getValueFactory().setValue(1);
        }
        spinQuantity.getEditor().clear();
        lblUnitPrice.setText("");

        // Clear the order details table
        orderDetailList.clear();
        tblOrder.refresh();
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

    public void generateOrderId() {
        try {
            String newOrderId = orderBo.generateOrderId();
            lblOrderId.setText(newOrderId);
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate order ID: " + e.getMessage()).show();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
