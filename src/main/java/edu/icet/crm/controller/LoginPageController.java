package edu.icet.crm.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.crm.db.DbConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private Connection connection = DbConnection.getInstance().getConnection();

    public LoginPageController() throws SQLException, ClassNotFoundException {
    }


    public void setLoggedInEmployeeId() {
        loggedInEmployeeEmail = this.userEmail.getText();
    }

    public static String getLoggedInEmployeeEmail() {
        return loggedInEmployeeEmail;
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String loggedEmail = userEmail.getText();
        String loggedPassword = userPassword.getText();
        System.out.println(loggedEmail);
        System.out.println(loggedPassword);

        if (!isValidEmail(loggedEmail)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!isEmailInDatabase(loggedEmail)) {
            showAlert(Alert.AlertType.ERROR, "Email Not Found", "The entered email is not registered.");
            return;
        }

        if (!isPasswordCorrect(loggedEmail, loggedPassword)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "The email and password do not match.");
            return;
        }

        String userRole = getUserRole(loggedEmail);
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
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isEmailInDatabase(String email) {

        try (
             ResultSet resultSet = connection.createStatement().executeQuery(
                     "SELECT COUNT(*) FROM User WHERE email = '" + email + "'")) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println("Email count for " + email + ": " + count);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean isPasswordCorrect(String email, String password) throws ClassNotFoundException {

        try (
             ResultSet resultSet = connection.createStatement().executeQuery(
                     "SELECT password FROM User WHERE email = '" + email + "'")){
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                System.out.println(storedPassword);
                return storedPassword.equals(password);  // Consider using hash comparison for security
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getUserRole(String email) throws ClassNotFoundException {
        String query = "SELECT r.rolename FROM User u JOIN Role r ON u.roleid = r.roleid WHERE u.email = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("rolename");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void forgetPasswordOnAction(ActionEvent actionEvent) {

    }

    public void createAccountOnAction(ActionEvent actionEvent) {

    }
}
