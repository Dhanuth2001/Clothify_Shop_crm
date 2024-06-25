package edu.icet.crm.controller;

import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.dto.User;
import edu.icet.crm.service.*;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.EncryptionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CreateAccountPageController {
    public JFXTextField txtEmployeeID;
    public Button btnVerify;
    public JFXTextField txtEmail;
    public JFXTextField txtPassword;
    public JFXTextField txtConfirmPassword;
    public Button btnCreate;
    public Button btnBack;

    private UserBo userBo;

    private EmployeeBo employeeBo;

    public void initialize() throws SQLException, ClassNotFoundException {
        this.userBo = BoFactory.getInstance().getBo(BoType.USER);
        this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);

    }
    public void btnCreateOnAction(ActionEvent actionEvent) {
        String employeeID = txtEmployeeID.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (employeeID == null || employeeID.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee ID cannot be empty");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email cannot be empty");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password cannot be empty");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match");
            return;
        }

        if (!employeeBo.isEmployeeIDValid(employeeID)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Employee ID");
            return;
        }

        if (userBo.isEmployeeIDInUserTable(employeeID)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee ID is already in use");
            return;
        }

        Integer roleID = employeeBo.getRoleIDByEmployeeID(employeeID);
        if (roleID == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No role found for this Employee ID");
            return;
        }

        try {
            String encryptedPassword = EncryptionUtil.encrypt(password);
            User user = new User(1, email, encryptedPassword, roleID, Integer.parseInt(employeeID));

            boolean success = userBo.createUser(user);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Encryption error occurred");
        }
    }


    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        String fxmlPath = "/view/LoginPage.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void btnVerifyOnAction(ActionEvent actionEvent) {
        String employeeID = txtEmployeeID.getText();
        if (employeeID == null || employeeID.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee ID cannot be empty");
            return;
        }

        if (!employeeBo.isEmployeeIDValid(employeeID)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Employee ID");
            return;
        }

        if (userBo.isEmployeeIDInUserTable(employeeID)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee ID is already in use");
            return;
        }

        String email = employeeBo.getEmailByEmployeeID(employeeID);
        if (email == null || email.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No email found for this Employee ID");
            return;
        }

        txtEmail.setText(email);
        txtEmail.setDisable(true);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Employee ID verified and email filled");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtEmployeeID.clear();

    }
}
