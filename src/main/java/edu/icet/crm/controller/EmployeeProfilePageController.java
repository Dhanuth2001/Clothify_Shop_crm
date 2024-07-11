package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.OrderBo;
import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.bo.custom.UserBo;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.util.BoType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class EmployeeProfilePageController {
    public TextArea txtBio;
    public Label lblActualDaySales;
    public TextField txtDaySalesTarget;
    public PieChart myPerformanceChart;
    public Label lblSalesTargetProgressMonth;
    public Label lblSalesTargetProgressDay;
    public Label lblActualSalesMonth;
    public TextField txtSalesTargetMonth;

    public Label txtDate;

    public Label txtTime;
    public Label txtName;
    public Label txtDob;
    public Label txtRole;
    public Label txtAddress;

    public Label txtEmail;
    public Label txtContanctNo;
    public Hyperlink hyperOk;
    public Hyperlink hyperEdit;
    private EmployeeBo employeeBo;
    private RoleBo roleBo;
    private UserBo userBo;
    private OrderBo orderBo;
    private boolean isEditMode = false;

    @FXML
    public void initialize() throws SQLException {
        try {
            this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
            this.roleBo = BoFactory.getInstance().getBo(BoType.ROLE);
            this.userBo = BoFactory.getInstance().getBo(BoType.USER);
            this.orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }
        loadDateAndTime();
        String employeeEmail = LoginPageController.getLoggedInEmployeeEmail();
        System.out.println(employeeEmail);
        int employeeID = userBo.getEmployeeIdByEmail(employeeEmail);
        System.out.println(employeeID);
        Employee employee = employeeBo.getEmployeeById(employeeID);

        if (employee != null) {
            txtName.setText(employee.getName());
            txtDob.setText(employee.getDob().toString());
            txtRole.setText(roleBo.getRoleNameById(employee.getRoleId()));
            txtAddress.setText(employee.getAddress());
            txtEmail.setText(employee.getEmail());
            txtContanctNo.setText(employee.getContactNo());
            //fetchAndDisplaySalesData(employeeID);
            txtBio.setEditable(false);
        }

        txtName.setText("");
        txtDob.setText("");
        txtRole.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtContanctNo.setText("");
        txtBio.setEditable(false);
    }

    private void fetchAndDisplaySalesData(int employeeID) throws SQLException {

        double salesMonth = orderBo.getOrderTotalForMonth(employeeID, LocalDate.now().getMonthValue());
        double salesDay = orderBo.getOrderTotalForDay(employeeID, LocalDate.now());

        lblActualSalesMonth.setText(String.format("%.2f", salesMonth));
        lblActualDaySales.setText(String.format("%.2f", salesDay));
    }
    private void loadDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtDate.setText(dateFormat.format(new Date()));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            LocalTime currentTime = LocalTime.now();
            txtTime.setText(String.format("%02d : %02d : %02d", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond()));
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void hyperOkOnAction(ActionEvent actionEvent) {
        if (isEditMode) {
            String newBio = txtBio.getText();
            txtBio.setEditable(false);
            hyperOk.setText("Ok");
            hyperEdit.setDisable(false);
            isEditMode = false;
        }
    }

    public void hyperEditOnAction(ActionEvent actionEvent) {
        if (!isEditMode) {
            // Enable edit mode
            txtBio.setEditable(true);
            hyperOk.setText("Save");
            hyperEdit.setDisable(true);
            isEditMode = true;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}