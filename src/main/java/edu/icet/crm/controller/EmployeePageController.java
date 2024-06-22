package edu.icet.crm.controller;

import edu.icet.crm.model.Employee;
import edu.icet.crm.model.Role;
import edu.icet.crm.service.EmployeeService;
import edu.icet.crm.service.RoleService;
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

    private EmployeeService employeeService;
    private RoleService roleService;
    private ObservableList<Employee> employeeList;
    private Map<String, Integer> roleMap;

    public EmployeePageController() throws SQLException, ClassNotFoundException {
        this.employeeService = new EmployeeService();
        this.roleService = new RoleService();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        employeeTable.setItems(employeeList);
    }

    private void loadEmployeeTable() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            employeeList.clear();
            employeeList.addAll(employees);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employee details.");
        }
    }

    private void loadRoleData() {
        List<Role> roles = roleService.getAllRoles();
        for (Role role : roles) {
            roleMap.put(role.getRoleName(), role.getRoleId());
            employeeRole.getItems().add(role.getRoleName());
        }
    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        Integer employeeId = Integer.valueOf(txtEmployeeId.getText());
        if (employeeId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            populateEmployeeDetails(employee);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
        }
    }

    private void populateEmployeeDetails(Employee employee) {
        txtEmployeeId.setText(String.valueOf(employee.getId()));
        txtEmployeeName.setText(employee.getName());
        txtEmployeeDob.setValue(employee.getDob());
        employeeRole.setValue(employee.getRole());
        txtAddress.setText(employee.getAddress());
        txtEmail.setText(employee.getEmail());
        txtContactNo.setText(employee.getContactNo());
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Employee employee = new Employee(
                    Integer.parseInt(txtEmployeeId.getText()),
                    (String) employeeRole.getValue(),
                    txtEmployeeName.getText(),
                    txtEmployeeDob.getValue(),
                    LocalDate.now(),

                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            boolean success = employeeService.addEmployee(employee);
            System.out.println(success);
            if (success) {
                int selectedIndex = employeeTable.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    employeeList.set(selectedIndex, employee);
                } else {

                    for (int i = 0; i < employeeList.size(); i++) {
                        if (employeeList.get(i).getId() == employee.getId()) {
                            employeeList.set(i, employee);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
                loadEmployeeTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product.");
            }
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (isValidInput()) {
            Employee employee = new Employee(
                    Integer.parseInt(txtEmployeeId.getText()),
                    (String) employeeRole.getValue(),
                    txtEmployeeName.getText(),
                    txtEmployeeDob.getValue(),
                    LocalDate.now(),

                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtContactNo.getText()
            );
            boolean success = employeeService.updateEmployee(employee);
            if (success) {
                int selectedIndex = employeeTable.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {
                    employeeList.set(selectedIndex, employee);
                } else {

                    for (int i = 0; i < employeeList.size(); i++) {
                        if (employeeList.get(i).getId() == employee.getId()) {
                            employeeList.set(i, employee);
                            break;
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
                loadEmployeeTable();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product.");
            }
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Integer employeeId = Integer.parseInt(txtEmployeeId.getText());
        if (employeeId==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an employee ID.");
            return;
        }

        boolean success = employeeService.deleteEmployee(employeeId);
        if (success) {
            employeeList.removeIf(employee -> employee.getId().equals(employeeId));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
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
        return true;
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
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        txtDate.setText(f.format(date));

        Timeline timeline= new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            LocalTime time = LocalTime.now();
            txtTime.setText(
                    time.getHour()+" : "+time.getMinute()+" : "+ time.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }
}

