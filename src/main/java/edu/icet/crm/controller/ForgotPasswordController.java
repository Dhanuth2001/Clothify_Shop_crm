package edu.icet.crm.controller;

import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.util.BoType;
import edu.icet.crm.util.EncryptionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordController {
    public Button btnVerify;

    public JFXTextField txtEmail;
    public JFXTextField txtNewPassword;
    public JFXTextField txtConfirmPassword;
    public Button btnChange;
    public Button btnBack;
    public Hyperlink hyperVerification;
    public TextField txtOTP;
    public Button btnEnter;
    private String generatedOTP;
    private UserBo userBo;
    private EmployeeBo employeeBo;
    private EmailController emailController;

    public void initialize() throws SQLException, ClassNotFoundException {
        this.employeeBo= BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        this.userBo= BoFactory.getInstance().getBo(BoType.USER);

        emailController = new EmailController();
        txtOTP.setDisable(true);
        txtNewPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);
    }

    public void btnVerifyOnAction(ActionEvent actionEvent) {
        String employeeEmail = txtEmail.getText().trim();

        if (employeeEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee Email cannot be empty");
            return;
        }

        if (!isValidEmail(txtEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            return;
        }

        if (!employeeBo.isEmployeeEmailValid(employeeEmail)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Employee Email");
            return;
        }

        if (!userBo.isEmployeeEmailInUserTable(employeeEmail)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee Email is not registered");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Employee email verified");

        // Enable OTP verification link
        hyperVerification.setDisable(false);

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void btnChangeOnAction(ActionEvent actionEvent) throws Exception {
        String newPassword = txtNewPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match");
            return;
        }

        boolean passwordChanged = false;
        String encryptedPassword = EncryptionUtil.encrypt(confirmPassword);
        passwordChanged = userBo.changePassword(txtEmail.getText().trim(), encryptedPassword);
        if (passwordChanged) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully");
            clearFields();// Navigate to login page or any appropriate page
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password");
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

    public void hyperVerificationOnAction(ActionEvent actionEvent) {
        String employeeEmail = txtEmail.getText().trim();

        if (employeeEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee Email cannot be empty");
            return;
        }

        // Generate OTP
        generatedOTP = generateOTP();
        txtOTP.setDisable(false);
        System.out.println("Generated OTP: " + generatedOTP);

        // Send OTP via email
        boolean emailSent = emailController.sendEmail(employeeEmail, "Verification Code",generatedOTP);
        if (emailSent) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "OTP sent to your email");
            txtOTP.setDisable(false); // Enable OTP input field
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send OTP email");
        }
    }

    public void btnEnterOnAction(ActionEvent actionEvent) {
        String enteredOTP = txtOTP.getText().trim();

        if (enteredOTP.equals(generatedOTP)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "OTP verified");
            txtNewPassword.setDisable(false); // Enable password fields
            txtConfirmPassword.setDisable(false);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid OTP");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

    private void clearFields() {
        txtEmail.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
        txtOTP.clear();
        generatedOTP = null;

        txtOTP.setDisable(true);
        txtNewPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);
        hyperVerification.setDisable(true);
    }
}
