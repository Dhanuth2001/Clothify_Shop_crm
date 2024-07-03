package edu.icet.crm.entity;


import edu.icet.crm.util.CategoryType;
import edu.icet.crm.util.SizeType;
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
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @Column(name = "productID")
    private Integer productID;
    private LocalDate date;
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Enumerated(EnumType.STRING)
    private SizeType sizeType;

    private Double unitPrice;
    private Integer quantity;



    private Integer supplierID;
}
