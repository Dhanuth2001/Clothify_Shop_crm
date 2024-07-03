package edu.icet.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderDetails")
public class OrderDetailsEntity {
 @Id

 @Column(name = "id")
 private Integer orderID;
 private Integer productID;

 private Integer quantity;
 private String sizes;
 private Double unitPrice;
 private Double cost;

}

