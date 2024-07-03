package edu.icet.crm.controller;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.OrderBo;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.dto.Order;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    private EmployeeBo employeeBo;
    private OrderBo orderBo;


    public AdminDashboardController() throws SQLException, ClassNotFoundException {
        this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        this.orderBo = BoFactory.getInstance().getBo(BoType.ORDER);


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

    public void profileOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/EmployeeProfilePage.fxml");
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
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

    public void lblOrdersOnClick(MouseEvent mouseEvent) throws IOException {
        loadFXML("/view/OrderPage.fxml");
    }

    private void loadUserName(){
        String loggedUserEmail = LoginPageController.getLoggedInEmployeeEmail();
        String username = employeeBo.getUsernameByEmail(loggedUserEmail);
        if(loggedUserEmail.isEmpty()){
            lblLoggedUserName.setText("No user");
            return;
        }
        lblLoggedUserName.setText(username);
    }

    public void btnGenerateDailyReportOnAction(ActionEvent actionEvent) {
        generateReport("Daily");
    }

    public void btnGenerateMonthlyReportOnAction(ActionEvent actionEvent) {
        generateReport("Monthly");
    }

    public void btnGenerateAnnualReportOnAction(ActionEvent actionEvent) {
        generateReport("Annual");
    }

    private void generateReport(String reportType) {
        try {
            List<Order> reportData = null;
            String reportPath = null;

            switch (reportType) {
                case "Daily":
                    reportData = orderBo.getDailyReportData();
                    reportPath = "/path/to/daily_report_template.jrxml";
                    break;
                case "Monthly":
                    reportData = orderBo.getMonthlyReportData();
                    reportPath = "/path/to/monthly_report_template.jrxml";
                    break;
                case "Annual":
                    reportData = orderBo.getAnnualReportData();
                    reportPath = "/path/to/annual_report_template.jrxml";
                    break;
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(reportPath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Report Generation Error", "Failed to generate " + reportType + " report.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
