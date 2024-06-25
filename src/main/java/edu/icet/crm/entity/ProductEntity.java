package edu.icet.crm.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/*@Entity
@Table(name = "product")*/
public class ProductEntity {
    /*@Id
    @Column(name = "productID")*/
    private Integer productID;
    private LocalDate date;
    private String name;
    private String category;
    private String size;
    private Double unitPrice;
    private Integer quantity;


    private Integer supplierID;
}
