package edu.icet.crm.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class LoginPageController {
    public JFXTextField userEmail;
    public Button btnLogin;
    public Hyperlink forgotPassword;
    public Hyperlink createAccount;
    public JFXPasswordField userPassword;


    public void btnLoginOnAction(ActionEvent actionEvent) {
        System.out.println("hi");
    }

    public void forgetPasswordOnAction(ActionEvent actionEvent) {
        System.out.println("hi");
    }

    public void createAccountOnAction(ActionEvent actionEvent) {
        System.out.println("hi");
    }
}
