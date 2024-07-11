package edu.icet.crm.dto;

import edu.icet.crm.util.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sales {
    private Integer productID;
    private String name;
    private CategoryType category;
    private Integer quantity;
    private Double cost;
}
