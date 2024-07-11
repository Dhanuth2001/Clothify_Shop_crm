package edu.icet.crm.controller;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.*;
import edu.icet.crm.dto.*;
import edu.icet.crm.util.BoType;
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

public class OrderPageController {
    private ObservableList<OrderDetails> tempOrderDetailsList = FXCollections.observableArrayList();
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
    public ComboBox comboCustomerIDs;
    public ComboBox comboPaymentType;
    public TableView tblOrder;
    public ComboBox comboSize;
    public Spinner spinQuantity;
    public Button btnChangeOrderDetails;
    public Button btnDropOrderDetail;
    private CustomerBo customerBo;
    private UserBo userBo;
    private OrderBo orderBo;
    private ProductBo productBo;
    private OrderDetailBo orderDetailsBo;
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        try {
            orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
            customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
            productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
            orderDetailsBo = BoFactory.getInstance().getBo(BoType.ORDERDETAILS);
            userBo = BoFactory.getInstance().getBo(BoType.USER);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }
        loadDateAndTime();
        initializeTable();
        loadOrdersTable();


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

        List<Integer> productIDs = customerBo.getCustomerIDs();
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboCustomerIDs.setItems(observableProductIDs);
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

        List<Integer> productIDs = orderDetailsBo.getProductIDsByOrderID(orderId);
        ObservableList<Integer> observableProductIDs = FXCollections.observableArrayList(productIDs);
        comboProductID.setItems(observableProductIDs);
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        Integer orderId = Integer.valueOf(txtOrderId.getText());
        loadOrderDetails(orderId);
        loadProductIDs(orderId);
        if (orderId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        Order order = orderBo.getOrderById(orderId);
        if (order != null) {
            populateOrder(order);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
        }

    }

    private void loadOrdersTable() {
        List<Order> orders = orderBo.getAllOrders();
        if (orders != null && !orders.isEmpty()) {
            orderList.clear();
            orderList.addAll(orders);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load order details table.");
        }
    }

    private void populateOrder(Order order) {
        comboCustomerIDs.setValue(order.getCustomerID());

        comboPaymentType.setValue(order.getPaymentType());
        txtDiscount.setText(order.getDiscount().toString());
    }

    private void clearFields() {
        txtOrderId.clear();
        comboCustomerIDs.setValue(null);

        comboPaymentType.setValue(null);
        txtDiscount.clear();
        spinQuantity.getValueFactory().setValue(null);
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

    public void btnOrderDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        Integer orderId = Integer.parseInt(txtOrderId.getText());
        if (orderId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an order ID.");
            return;
        }

        boolean success = orderBo.deleteOrder(orderId);
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

        loadSizesByProductId(productId);
        limitQuantitySpinner(productId);

        OrderDetails orderDetailsByOrderIDandProductID = orderDetailsBo.
                getOrderDetailsByOrderIDandProductID(orderId, productId);

        comboSize.setValue(orderDetailsByOrderIDandProductID.getSizes());
        spinQuantity.getValueFactory().setValue(orderDetailsByOrderIDandProductID.getQuantity());
        txtUnitPrice.setText(orderDetailsByOrderIDandProductID.getUnitPrice().toString());


    }

    public void btnUpdateOrderOnAction(ActionEvent actionEvent) {
        if (isValidOrder()) {
            try {
                // Create a new Order object with updated details
                Integer orderId = Integer.parseInt(txtOrderId.getText());
                String employeeEmail = LoginPageController.getLoggedInEmployeeEmail();

                Integer employeeID = userBo.getEmployeeIdByEmail(employeeEmail);
                Integer customerID = Integer.parseInt(comboCustomerIDs.getValue().toString());
                String paymentType = comboPaymentType.getValue().toString();
                Double discount = Double.parseDouble(txtDiscount.getText());
                LocalDate datePlaced = LocalDate.now();

                // Calculate total cost
                double totalCost = tempOrderDetailsList.stream()
                        .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getUnitPrice())
                        .sum();

                Order updatedOrder = new Order();
                updatedOrder.setOrderID(orderId);
                updatedOrder.setEmployeeID(employeeID);
                updatedOrder.setCustomerID(customerID);
                updatedOrder.setDiscount(discount);
                updatedOrder.setTotalCost(totalCost);
                updatedOrder.setPaymentType(paymentType);
                updatedOrder.setDatePlaced(datePlaced);
                updatedOrder.setOrderDetailList(tempOrderDetailsList);

                // Call service to update the order
                boolean success = orderBo.updateOrder(updatedOrder);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Order Updated", "Order updated successfully.");
                    loadOrdersTable();
                    clearFields();
                    tempOrderDetailsList.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update order.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order.");
            }
        }
    }



    private void loadSizesByProductId(int productId) {
        List<SizeType> sizes = productBo.getSizesByProductId(productId);
        ObservableList<SizeType> observableSizes = FXCollections.observableArrayList(sizes);
        comboSize.setItems(observableSizes);
    }

    private void limitQuantitySpinner(int productId) {
        int availableQuantity = productBo.getAvailableQuantityByProductId(productId);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, availableQuantity);
        spinQuantity.setValueFactory(valueFactory);
    }

    private boolean isValidOrder() {
        if (txtOrderId.getText().isEmpty() ||

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

                comboPaymentType.getValue().toString().isEmpty()||

                txtQuantity.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        return true;
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
    public void loadOrderDetails(Integer orderId) {
        List<OrderDetails> orderDetailsList = orderDetailsBo.getOrderDetailsByOrderId(orderId);
        if (orderDetailsList != null && !orderDetailsList.isEmpty()) {
            tempOrderDetailsList.setAll(orderDetailsList);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load order details.");
        }
    }
    public void btnChangeOrderDetailOnAction(ActionEvent actionEvent) {
        try {
            Integer orderId = Integer.parseInt(txtOrderId.getText());
            Integer productId = Integer.parseInt(comboProductID.getValue().toString());
            Integer quantity = Integer.parseInt(spinQuantity.getValue().toString());
            Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            String size = comboSize.getValue().toString();

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderID(orderId);
            orderDetails.setProductID(productId);
            orderDetails.setQuantity(quantity);
            orderDetails.setUnitPrice(unitPrice);
            orderDetails.setSizes(size);

            // Remove existing detail for the product and add updated detail
            tempOrderDetailsList.removeIf(od -> od.getProductID().equals(productId));
            tempOrderDetailsList.add(orderDetails);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Order detail recorded. You can now update the order.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please check the order details.");
        }
    }

    public void btnDropOrderDetailOnAction(ActionEvent actionEvent) {
        try {
            Integer productId = Integer.parseInt(comboProductID.getValue().toString());
            tempOrderDetailsList.removeIf(od -> od.getProductID().equals(productId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order detail removed.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please check the order details.");
        }
    }
}
