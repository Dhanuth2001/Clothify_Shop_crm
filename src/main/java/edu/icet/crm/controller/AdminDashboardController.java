package edu.icet.crm.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AdminPageController {


    public Label lblHome;
    public Label lblEmployee;
    public Label lblProducts;
    public Label lblSupplier;

    public AnchorPane contentPane;
    public Label lblName;
    public Button btnLogOut;


    public void initialize() throws IOException {

         loadFXML("/view/AdminHomePage.fxml");
    }


    public void lblHomeOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/AdminHomePage.fxml");
    }


    public void lblEmployeeOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/EmployeePage.fxml");
    }


    public void lblProductsOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/ProductPage.fxml");
    }


    public void lblSupplierOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/SupplierPage.fxml");
    }

    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();


        contentPane.getChildren().clear();


        contentPane.getChildren().add(root);
    }

    public void profileOnClick(MouseEvent mouseEvent) {
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
    }
}
