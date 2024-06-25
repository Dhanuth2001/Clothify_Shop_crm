package edu.icet.crm.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.util.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController {
    public JFXTextField userEmail;
    public Button btnLogin;
    public Hyperlink forgotPassword;
    public Hyperlink createAccount;
    public JFXPasswordField userPassword;

    private static String loggedInEmployeeEmail;

    private UserBo userBo;

    public LoginPageController() throws SQLException, ClassNotFoundException {
        this.userBo = BoFactory.getInstance().getBo(BoType.USER);
    }

    public void setLoggedInEmployeeId() {
        loggedInEmployeeEmail = this.userEmail.getText();
    }

    public static String getLoggedInEmployeeEmail() {
        return loggedInEmployeeEmail;
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String loggedEmail = userEmail.getText();
        String loggedPassword = userPassword.getText();

        if (!isValidEmail(loggedEmail)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        try {
            if (!userBo.isEmployeeEmailInUserTable(loggedEmail)) {
                showAlert(Alert.AlertType.ERROR, "Email Not Found", "The entered email is not registered.");
                return;
            }

            if (userBo.authenticateUser(loggedEmail, loggedPassword)) {
                String userRole = userBo.getUserRole(loggedEmail);
                if (userRole == null) {
                    showAlert(Alert.AlertType.ERROR, "Role Not Found", "User role could not be determined.");
                    return;
                }

                setLoggedInEmployeeId();

                FXMLLoader loader;
                if ("Admin".equalsIgnoreCase(userRole)) {
                    loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
                } else if ("Default".equalsIgnoreCase(userRole)) {
                    loader = new FXMLLoader(getClass().getResource("/view/EmployeeDashboard.fxml"));
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Role", "User role is not recognized.");
                    return;
                }

                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "The email and password do not match.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Authentication error occurred.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();// Placeholder, replace with actual validation logic
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        String fxmlPath = "/view/CreateAccountPage.fxml";
        loadFXML(actionEvent, fxmlPath);
    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) throws IOException {
        String fxmlPath = "/view/ForgotPasswordPage.fxml";
        loadFXML(actionEvent, fxmlPath);
    }

    private void loadFXML(ActionEvent actionEvent, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
