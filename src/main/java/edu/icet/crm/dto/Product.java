package edu.icet.crm.dto;


import edu.icet.crm.util.CategoryType;
import edu.icet.crm.util.SizeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer productID;
    private LocalDate date;
    private String name;
    private CategoryType category;
    private SizeType size;
    private Double unitPrice;
    private Integer quantity;
    private Integer supplierID;
}
