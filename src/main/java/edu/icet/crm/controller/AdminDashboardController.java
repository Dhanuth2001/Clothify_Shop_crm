package edu.icet.crm.controller;
import edu.icet.crm.service.DashboardService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class AdminDashboardController {


    public Label lblHome;
    public Label lblEmployee;
    public Label lblProducts;
    public Label lblSupplier;

    public AnchorPane contentPane;

    public Button btnLogOut;
    public Label lblOrders;
    public Label lblWelcome;
    public Label lblLoggedUserName;

    private DashboardService adminDashboardService;

    public AdminDashboardController() throws SQLException, ClassNotFoundException {
        this.adminDashboardService = new DashboardService();

    }

    public void initialize() throws IOException {
        loadUserName();
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

    public void lblOrdersOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/OrderPage.fxml");
    }

    private void loadUserName(){
        /*String loggedUserEmail = LoginPageController.getLoggedInEmployeeEmail();
        String username = adminDashboardService.getUsernameByEmail(loggedUserEmail);
        if(loggedUserEmail.isEmpty()){
            lblLoggedUserName.setText("No user");
            return;
        }
        lblLoggedUserName.setText(username);*/
    }
}
