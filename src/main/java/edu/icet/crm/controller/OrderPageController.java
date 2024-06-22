package edu.icet.crm.controller;

import edu.icet.crm.model.*;
import edu.icet.crm.service.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderPageController {
    public TextField txtOrderId;
    public Button btnSearch;

    public TableColumn colId;


    public TableColumn colDiscount;

    public Label txtTime;
    public Label txtDate;
    public Button btnClear;

    public TableColumn colCustomerId;
    public Button btnDeleteOrderDetail;
    public TableColumn colEmployeeID;
    public TableColumn colDatePlaced;
    public TableColumn colPaymentType;
    public TableColumn colNetTotal;
    public ComboBox comboProductID;
    public TextField txtQuantity;
    public TextField txtUnitPrice;
    public TextField txtDiscount;
    public Button btnUpdateOrderDetails;
    public Button btnDeleteOrder;
    public Button btnUpdateOrder;
    public Button btnLoad;
    public ComboBox comboEmployeeIDs;
    public ComboBox comboCustomerIDs;
    public ComboBox comboPaymentType;
    public TableView tblOrder;
    public ComboBox comboSize;
    private OrderService orderService;
    private CustomerService customerService;
    private ProductService productService;
    private EmployeeService employeeService;
    private OrderDetailsService orderDetailsService;
    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            orderService = new OrderService();
            customerService = new CustomerService();
            employeeService = new EmployeeService();
            productService = new ProductService();
            orderDetailsService = new OrderDetailsService();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }
        loadDateAndTime();
        initializeTable();
        loadOrdersTable();
        loadEmployeeIDs();
        loadSizes();
        loadCustomerIDs();
        loadPaymentType();
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

        List<Integer> productIDs = customerService.getCustomerIDs();
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboCustomerIDs.setItems(observableProductIDs);
    }

    private void loadEmployeeIDs() {

        List<Integer> productIDs = employeeService.getEmployeeIDs();
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboEmployeeIDs.setItems(observableProductIDs);
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        colDatePlaced.setCellValueFactory(new PropertyValueFactory<>("datePlaced"));
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colNetTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tblOrder.setItems(orderList);
    }

    private void loadProductIDs(Integer orderId) {

        List<Integer> productIDs = orderDetailsService.getProductIDsByOrderID(orderId);
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboProductID.setItems(observableProductIDs);
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        Integer orderId = Integer.valueOf(txtOrderId.getText());
        loadProductIDs(orderId);
        if (orderId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            populateOrder(order);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
        }

    }

    private void loadOrdersTable() {
        List<Order> orders = orderService.getAllOrders();
        if (orders != null && !orders.isEmpty()) {
            orderList.clear();
            orderList.addAll(orders);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load supplier details.");
        }
    }

    private void populateOrder(Order order) {
        comboCustomerIDs.setValue(order.getCustomerID());
        comboEmployeeIDs.setValue(order.getEmployeeID());
        comboPaymentType.setValue(order.getPaymentType());
        txtDiscount.setText(order.getDiscount().toString());
    }

    private void clearFields() {
        txtOrderId.clear();
        comboCustomerIDs.setValue(null);
        comboEmployeeIDs.setValue(null);
        comboPaymentType.setValue(null);
        txtDiscount.clear();
        txtQuantity.clear();
        txtUnitPrice.clear();
        comboSize.setValue(null);
        comboProductID.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void btnOrderDeleteOnAction(ActionEvent actionEvent) {
        Integer orderId = Integer.parseInt(txtOrderId.getText());
        if (orderId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an order ID.");
            return;
        }

        boolean success = orderService.deleteOrder(orderId);
        if (success) {
            orderList.removeIf(order -> order.getOrderID().equals(orderId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order.");
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtOrderId.setEditable(true);
        comboProductID.setEditable(true);
        txtUnitPrice.setEditable(true);
        clearFields();
    }

    public void btnLoadOrderDetailsOnAction(ActionEvent actionEvent) {
        txtOrderId.setEditable(false);
        comboProductID.setEditable(false);
        txtUnitPrice.setEditable(false);

        Integer orderId = Integer.parseInt(txtOrderId.getText());
        Integer productId = Integer.parseInt(comboProductID.getValue().toString());

        OrderDetails orderDetailsByOrderIDandProductID = orderDetailsService.
                getOrderDetailsByOrderIDandProductID(orderId, productId);

        comboSize.setValue(orderDetailsByOrderIDandProductID.getSizes());
        txtQuantity.setText(orderDetailsByOrderIDandProductID.getQuantity().toString());
        txtUnitPrice.setText(orderDetailsByOrderIDandProductID.getUnitPrice().toString());


    }

    public void btnUpdateOrderOnAction(ActionEvent actionEvent) {
        Integer orderId = null;

        try {
            orderId = Integer.parseInt(txtOrderId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid order ID.");
            return;
        }

        if (isValidOrder()) {
            // Create a new Order object and set its attributes
            Order newOrder = new Order();
            newOrder.setOrderID(orderId);
            newOrder.setCustomerID(Integer.parseInt(comboCustomerIDs.getValue().toString()));
            newOrder.setEmployeeID(Integer.parseInt(comboEmployeeIDs.getValue().toString()));
            newOrder.setDiscount(Double.parseDouble(txtDiscount.getText()));
            newOrder.setPaymentType(comboPaymentType.getValue().toString());
            newOrder.setDatePlaced(LocalDate.now());

            // Retrieve the previous discount percentage
            Double previousDiscount = orderService.getDiscountByOrderID(orderId);

            if (previousDiscount == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve original discount.");
                return;
            }

            // Check if the discount has changed
            if (!previousDiscount.equals(newOrder.getDiscount())) {
                // Retrieve the original total cost
                Double originalTotalCost = orderService.getTotalCostByOrderID(orderId);
                if (originalTotalCost == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve original total cost.");
                    return;
                }

                // Calculate the new total cost after applying the discount
                Double discountedTotalCost = Math.round((originalTotalCost - (originalTotalCost * newOrder.getDiscount() / 100)) * 100.0) / 100.0;
                newOrder.setTotalCost(discountedTotalCost);
            } else {
                // If the discount hasn't changed, retain the original total cost
                newOrder.setTotalCost(orderService.getTotalCostByOrderID(orderId));
            }

            boolean success = orderService.updateOrder(newOrder);
            if (success) {
                // Update the table view with the new order details
                int selectedIndex = tblOrder.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    tblOrder.getItems().set(selectedIndex, newOrder);
                } else {
                    for (int i = 0; i < orderList.size(); i++) {
                        if (orderList.get(i).getOrderID().equals(newOrder.getOrderID())) {
                            orderList.set(i, newOrder);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully.");
                loadOrdersTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order.");
            }
        }
    }



    public void btnUpdateOrderDetailOnAction(ActionEvent actionEvent) {
        Integer orderId = null;
        Integer productId = null;
        try {
            orderId = Integer.parseInt(txtOrderId.getText());
            productId = Integer.parseInt(comboProductID.getValue().toString());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid order and product IDs.");
            return;
        }

        if (isValidOrderDetails()) {
            int newQuantity = Integer.parseInt(txtQuantity.getText());

            OrderDetails oldOrderDetails = orderDetailsService.getOrderDetailsByOrderIDandProductID(orderId, productId);
            if (oldOrderDetails == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Order details not found.");
                return;
            }

            int oldQuantity = oldOrderDetails.getQuantity();
            int quantityDifference = newQuantity - oldQuantity;

            // Check if there's enough quantity on hand
            int currentProductQuantity = productService.getProductQuantityById(productId);
            if (currentProductQuantity < quantityDifference) {
                showAlert(Alert.AlertType.ERROR, "Error", "Not enough quantity on hand.");
                return;
            }

            OrderDetails orderDetails = new OrderDetails(
                    orderId,
                    productId,
                    newQuantity,
                    String.valueOf(comboSize.getValue()),
                    Double.valueOf(txtUnitPrice.getText())
            );

            boolean success = orderDetailsService.updateOrderDetail(orderDetails);
            if (success) {
                int selectedIndex = tblOrder.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    tblOrder.getItems().set(selectedIndex, orderDetails);
                } else {
                    for (int i = 0; i < orderList.size(); i++) {
                        Order order = orderList.get(i);
                        if (order.getOrderID().equals(orderDetails.getOrderID())) {
                            List<OrderDetails> orderDetailsList = order.getOrderDetailList();
                            for (int j = 0; j < orderDetailsList.size(); j++) {
                                if (orderDetailsList.get(j).getProductID().equals(orderDetails.getProductID())) {
                                    orderDetailsList.set(j, orderDetails);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                // Update product quantity in the products table
                boolean productUpdateSuccess = productService.updateProductQuantity(productId, quantityDifference);
                if (!productUpdateSuccess) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product quantity.");
                    return;
                }

                // Recalculate and update the total cost in the orders table
                updateOrderTotalCost(orderId);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully.");
                loadOrdersTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order.");
            }
        }
    }



    private void updateOrderTotalCost(Integer orderId) {
        // Retrieve updated list of OrderDetails
        List<OrderDetails> orderDetailsList = orderDetailsService.getOrderDetailsByOrderId(orderId);
        if (orderDetailsList == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve order details.");
            return;
        }

        // Calculate new total cost
        double totalCost = 0;
        for (OrderDetails details : orderDetailsList) {
            totalCost += details.getQuantity() * details.getUnitPrice();
        }
        totalCost = Math.round(totalCost * 100.0) / 100.0; // Round to two decimal places

        // Update total cost in orders table
        boolean success = orderService.updateTotalCost(orderId, totalCost);
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order total cost.");
        }
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

        comboSize.setItems(sizes);
    }
    private boolean isValidOrder() {
        if (txtOrderId.getText().isEmpty() ||
                comboEmployeeIDs.getValue().toString().isEmpty() ||
                comboPaymentType.getValue().toString().isEmpty() ||
                txtDiscount.getText().isEmpty())
                {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        return true;
    }

    private boolean isValidOrderDetails() {
        if (txtOrderId.getText().isEmpty() ||
                comboSize.getValue().toString().isEmpty() ||
                comboProductID.getValue().toString().isEmpty() ||
                comboEmployeeIDs.getValue().toString().isEmpty() ||
                comboPaymentType.getValue().toString().isEmpty()||

                txtQuantity.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        return true;
    }

    public void btnDeleteOrderDetailOnAction(ActionEvent actionEvent){
        Integer orderId = Integer.parseInt(txtOrderId.getText());
        Integer productId = Integer.parseInt(comboProductID.getValue().toString());
        if (orderId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an order ID.");
            return;
        }

        boolean success = orderDetailsService.deleteOrderDetail(orderId,productId);
        if (success) {
            orderList.removeIf(order -> order.getOrderID().equals(orderId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order.");
        }
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
}
