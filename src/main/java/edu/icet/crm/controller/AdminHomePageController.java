package edu.icet.crm.controller;

import com.jfoenix.controls.JFXButton;
import edu.icet.crm.bo.BoFactory;
import edu.icet.crm.bo.custom.OrderBo;
import edu.icet.crm.bo.custom.OrderDetailBo;
import edu.icet.crm.service.*;
import edu.icet.crm.util.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.Map;

public class AdminHomePageController {
    public LineChart salesOverTimeChart;

    public BarChart topSellingProductsChart;
    public PieChart costBreakdownChart;
    public TableView tblSales;
    public TableColumn colProductId;
    public TableColumn colName;
    public TableColumn colCategory;
   
    public TableColumn colTotalSales;
    public TableColumn colTotalQuantity;
    public Label lblVisitCustomers;
    
    public Label lblCustomerProgress;
    
    public Label lblActualSales;
    public Label lblSalesTargetProgress;

    private OrderBo orderBo;
    private OrderDetailBo orderDetailBo;
   
    public JFXButton btnEdit;
    public TextField txtCustomerTarget;
    public TextField txtSalesTarget;
    public JFXButton btnOk;


    public void initialize() {
        try {
            orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
            orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDERDETAILS);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }
        txtCustomerTarget.setEditable(false);
        txtSalesTarget.setEditable(false);
        //populateSalesOverTimeChart();
        populateSalesByCategoryChart();
        populateTopSellingProductsChart();
        calculateAndUpdateCustomerProgress();
        calculateAndUpdateSalesProgress();

    }

    private void populateSalesOverTimeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        salesOverTimeChart.setTitle("Sales Over Time");

        xAxis.setLabel("Month");
        yAxis.setLabel("Sales Amount");

        salesOverTimeChart.getXAxis().setAutoRanging(true);
        salesOverTimeChart.getYAxis().setAutoRanging(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        Map<String, Double> salesData = orderBo.getMonthlySalesData();
        String currentMonth = "";
        double totalSales = 0;
        int count = 0;

        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            String month = entry.getKey();
            double salesAmount = entry.getValue();

            // Aggregate sales for the same month
            if (currentMonth.isEmpty() || month.equals(currentMonth)) {
                totalSales += salesAmount;
                currentMonth = month;
                count++;
            } else {
                // Add average sales for the month
                series.getData().add(new XYChart.Data<>(currentMonth, totalSales / count));

                // Reset variables for next month
                currentMonth = month;
                totalSales = salesAmount;
                count = 1;
            }
        }

        // Add the last month's data
        if (!currentMonth.isEmpty()) {
            series.getData().add(new XYChart.Data<>(currentMonth, totalSales / count));
        }

        salesOverTimeChart.getData().add(series);
        series.getNode().setStyle("-fx-stroke: #336699;");
    }

    private void populateSalesByCategoryChart() {

        PieChart.Data slice1 = new PieChart.Data("Profit", 30);
        PieChart.Data slice2 = new PieChart.Data("Marketing", 15);
        PieChart.Data slice3 = new PieChart.Data("Salaries", 45);
        PieChart.Data slice4 = new PieChart.Data("Other", 10);

        costBreakdownChart.getData().addAll(slice1, slice2, slice3,slice4);
    }

    private void populateTopSellingProductsChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        topSellingProductsChart.setTitle("Top Selling Products");
        xAxis.setLabel("Product Category");
        yAxis.setLabel("Quantity Sold");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantity Sold");

        Map<String, Integer> topProducts = orderDetailBo.getTopSellingProductsByCategory();

        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            String category = entry.getKey();
            int quantitySold = entry.getValue();

            series.getData().add(new XYChart.Data<>(category, quantitySold));
        }

        topSellingProductsChart.getData().add(series);
    }

    public void btnEditOnAction(ActionEvent actionEvent){
        txtSalesTarget.setEditable(true);
        txtCustomerTarget.setEditable(true);

    }



    private void calculateAndUpdateCustomerProgress() {
        try {
            int targetCustomers = Integer.parseInt(txtCustomerTarget.getText());
            int visitingCustomers = Integer.parseInt(lblVisitCustomers.getText());

            if (targetCustomers > 0) {
                double progressPercentage = ((double) visitingCustomers / targetCustomers) * 100;
                lblCustomerProgress.setText(String.format("%.2f%%", progressPercentage));
            } else {
                lblCustomerProgress.setText("0%");
            }
        } catch (NumberFormatException e) {
            lblCustomerProgress.setText("N/A");
        }
    }

    private void calculateAndUpdateSalesProgress() {
        try {
            int targetSales = Integer.parseInt(txtSalesTarget.getText());
            int actualSales = Integer.parseInt(lblActualSales.getText());

            if (targetSales > 0) {
                double progressPercentage = ((double) actualSales / targetSales) * 100;
                lblSalesTargetProgress.setText(String.format("%.2f%%", progressPercentage));
            } else {
                lblSalesTargetProgress.setText("0%");
            }
        } catch (NumberFormatException e) {
            lblSalesTargetProgress.setText("N/A");
        }
    }

    public void btnOkOnAction(ActionEvent actionEvent) {
        txtSalesTarget.setEditable(false);
        txtCustomerTarget.setEditable(false);
        calculateAndUpdateSalesProgress();
        calculateAndUpdateCustomerProgress();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
