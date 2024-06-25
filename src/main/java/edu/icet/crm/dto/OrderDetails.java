package edu.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderDetails {
        private Integer orderID;
        private Integer productID;
        private Integer quantity;
        private String sizes;
        private Double unitPrice;
        private Double cost;

    }

