package edu.icet.crm.controller;

import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.EmployeeBo;
import edu.icet.crm.bo.custom.RoleBo;
import edu.icet.crm.dto.Employee;
import edu.icet.crm.dto.Role;
import edu.icet.crm.util.BoType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeePageController {
    public TextField txtEmployeeId;
    public TextField txtEmployeeName;
    public DatePicker txtEmployeeDob;
    public ChoiceBox employeeRole;
    public TextArea txtAddress;
    public Button btnSearch;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colDob;
    public TableColumn colRole;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public TableColumn colContactNo;
    public Label txtTime;
    public Label txtDate;
    public TextField txtEmail;
    public TextField txtContactNo;
    public TableView employeeTable;
    public Button btnClear;





    private EmployeeBo employeeBo;
    private RoleBo roleBo;
    private ObservableList<Employee> employeeList;
    private Map<String, Integer> roleMap;

    public EmployeePageController() throws SQLException, ClassNotFoundException {
        this.employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        this.roleBo = BoFactory.getInstance().getBo(BoType.ROLE);
        this.employeeList = FXCollections.observableArrayList();
        this.roleMap = new HashMap<>();
    }

    @FXML
    public void initialize() {
        loadDateAndTime();
        initializeTable();
        loadEmployeeTable();
        loadRoleData();
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        employeeTable.setItems(employeeList);
    }

    private void loadEmployeeTable() {
        try {
            List<Employee> employees = employeeBo.getAllEmployees();
            employeeList.setAll(employees);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employee details.");
        }
    }

    private void loadRoleData() {
        try {
            List<Role> roles = roleBo.getAllRoles();
            System.out.println(roles);
            for (Role role : roles) {
                roleMap.put(role.getRoleName(), role.getRoleId());
                employeeRole.getItems().add(role.getRoleName());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load role data.");
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            int employeeId = Integer.parseInt(txtEmployeeId.getText());
            Employee employee = employeeBo.getEmployeeById(employeeId);
            if (employee != null) {
                populateEmployeeDetails(employee);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid employee ID.");
        }
    }

    private void populateEmployeeDetails(Employee employee) {
        txtEmployeeId.setText(String.valueOf(employee.getEmployeeId()));
        txtEmployeeName.setText(employee.getName());
        txtEmployeeDob.setValue(employee.getDob());
        employeeRole.setValue(roleBo.getRoleNameById(employee.getRoleId()));
        txtAddress.setText(employee.getAddress());
        txtEmail.setText(employee.getEmail());
        txtContactNo.setText(employee.getContactNo());
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Employee employee = new Employee(
                    Integer.parseInt(txtEmployeeId.getText()),
                    roleBo.getRoleIdByName(employeeRole.getValue().toString()),
                    txtEmployeeName.getText(),
                    txtEmployeeDob.getValue(),
                    LocalDate.now(),
                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            try {
                System.out.println(employee);
                boolean success = employeeBo.addEmployee(employee);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");
                    loadEmployeeTable();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
            }
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Employee employee = new Employee(
                    Integer.parseInt(txtEmployeeId.getText()),
                    roleBo.getRoleIdByName(employeeRole.getValue().toString()),
                    txtEmployeeName.getText(),
                    txtEmployeeDob.getValue(),
                    LocalDate.now(),
                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            try {
                boolean success = employeeBo.updateEmployee(employee);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully.");
                    loadEmployeeTable();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee.");
            }
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            int employeeId = Integer.parseInt(txtEmployeeId.getText());
            boolean success = employeeBo.deleteEmployee(employeeId);
            if (success) {
                employeeList.removeIf(employee -> employee.getEmployeeId() == employeeId);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid employee ID.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidInput() {
        if (txtEmployeeId.getText().isEmpty() ||
                txtEmployeeName.getText().isEmpty() ||
                txtEmployeeDob.getValue() == null ||
                employeeRole.getValue() == null ||
                txtAddress.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtContactNo.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields.");
            return false;
        }
        if (!isValidEmployeeId(txtEmployeeId.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee ID must start with 1.");
            return false;
        }

        if (!isValidEmail(txtEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            return false;
        }

        if (!isValidPhoneNumber(txtContactNo.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid phone number that starts with 0 and has 10 digits.");
            return false;
        }

        return true;
    }

    private boolean isValidEmployeeId(String employeeId) {
        return employeeId.startsWith("1");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void clearFields() {
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeDob.setValue(null);
        employeeRole.setValue(null);
        txtAddress.clear();
        txtEmail.clear();
        txtContactNo.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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

    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }
}

