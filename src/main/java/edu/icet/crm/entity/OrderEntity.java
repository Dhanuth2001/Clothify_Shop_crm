package edu.icet.crm.entity;

import edu.icet.crm.dto.OrderDetails;
import jakarta.persistence.*;
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
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(name = "orderID")
    private Integer orderID;


    private Integer employeeID;


    private Integer customerID;

    private Double discount;
    private Double totalCost;
    private String paymentType;
    private LocalDate datePlaced;


    @Transient
    private List<OrderDetails> orderDetailList;
}
