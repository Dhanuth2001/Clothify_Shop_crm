package edu.icet.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/*@Entity
@Table(name = "orderDetails")*/
public class OrderDetailsEntity {
   /* @Id
    @Column(name = "orderID")*/
    private Integer orderID;
    private Integer productID;
    private Integer quantity;
    private String sizes;
    private Double unitPrice;
    private Double cost;

}

