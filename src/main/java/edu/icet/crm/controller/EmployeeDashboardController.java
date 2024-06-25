package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.util.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class EmployeeDashboardController {
    public Label lblHome;
    public Label lblOrders;
    public Label lblProducts;
    public Label lblSupplier;
    public AnchorPane contentPane;
    public Button btnLogOut;

    public Label lblLoggedUserName;
    public Label lblCustomer;

    private EmployeeBo employeeBo;

    public EmployeeDashboardController() throws SQLException, ClassNotFoundException {
        this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);

    }
    public void initialize() throws IOException {
        loadUserName();
        loadFXML("/view/EmployeeHomePage.fxml");
    }
    public void lblHomeOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/EmployeeHomePage.fxml");
    }

    public void lblOrdersOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/OrderPage.fxml");
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

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        // Show a confirmation dialog before logging out
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed with logout
            try {
                // Load the login page
                String fxmlPath = "/view/LoginPage.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login page.");
            }
        }
    }

    public void profileOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/EmployeeProfilePage.fxml");
    }

    private void loadUserName(){
        String loggedUserEmail = LoginPageController.getLoggedInEmployeeEmail();
        String username = employeeBo.getUsernameByEmail(loggedUserEmail);
        if(loggedUserEmail.isEmpty()){
            lblLoggedUserName.setText("No user");
        }
        lblLoggedUserName.setText(username);
    }

    public void lblCustomerOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/CustomerPage.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
