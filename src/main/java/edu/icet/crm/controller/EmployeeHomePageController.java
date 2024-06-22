package edu.icet.crm.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

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

    public void btnDropFromCartOnAction(ActionEvent actionEvent) {
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) {
    }
    
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }

    public void btnClearOtherDetailsOnAction(ActionEvent actionEvent) {
    }

    public void btnSelectOtherDetailsOnAction(ActionEvent actionEvent) {
    }

    public void btnLoadOrderDetailsOnAction(ActionEvent actionEvent) {
    }

    public void btnCancelOrderOnAction(ActionEvent actionEvent) {
    }


}
