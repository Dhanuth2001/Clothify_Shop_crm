package edu.icet.crm.controller;

import javafx.scene.chart.*;

public class AdminHomePageController {
    public LineChart salesOverTimeChart;

    public BarChart topSellingProductsChart;
    public PieChart costBreakdownChart;


    public void initialize() {
        populateSalesOverTimeChart();
        populateSalesByCategoryChart();
        populateTopSellingProductsChart();

    }

    private void populateSalesOverTimeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        series.getData().add(new XYChart.Data<>("Jan", 200));
        series.getData().add(new XYChart.Data<>("Feb", 150));
        series.getData().add(new XYChart.Data<>("Mar", 300));
        series.getData().add(new XYChart.Data<>("Apr", 250));
        series.getData().add(new XYChart.Data<>("May", 200));
        series.getData().add(new XYChart.Data<>("Jun", 150));
        series.getData().add(new XYChart.Data<>("Jul", 300));
        series.getData().add(new XYChart.Data<>("Aug", 250));
        series.getData().add(new XYChart.Data<>("Sep", 200));
        series.getData().add(new XYChart.Data<>("Oct", 150));
        series.getData().add(new XYChart.Data<>("Nov", 300));
        series.getData().add(new XYChart.Data<>("Dec", 250));

        salesOverTimeChart.getData().add(series);

        series.getNode().setStyle("-fx-stroke: #336699; ");





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

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Products");

        XYChart.Data<String, Number> dataA = new XYChart.Data<>("Kid", 50);
        XYChart.Data<String, Number> dataB = new XYChart.Data<>("Gents", 80);
        XYChart.Data<String, Number> dataC = new XYChart.Data<>("Ladies", 120);

        // Style individual bars
        dataA.nodeProperty().addListener((observable, oldValue, newValue) ->
                newValue.setStyle("-fx-bar-fill: black;")); // Blue
        dataB.nodeProperty().addListener((observable, oldValue, newValue) ->
                newValue.setStyle("-fx-bar-fill: gray;")); // Red
        dataC.nodeProperty().addListener((observable, oldValue, newValue) ->
                newValue.setStyle("-fx-bar-fill:  #0096FF;")); // Green

        series.getData().add(dataA);
        series.getData().add(dataB);
        series.getData().add(dataC);

        topSellingProductsChart.getData().add(series);

    }
}
