package edu.icet.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer orderID;
    private Integer employeeID;
    private Integer customerID;
    private Double discount;
    private Double totalCost;
    private String paymentType;
    private LocalDate datePlaced;
    private List<OrderDetails> orderDetailList;
}
