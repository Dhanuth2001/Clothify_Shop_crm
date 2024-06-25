package edu.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private Integer supplierID;
    private String company;
    private String address;
    private String contactNumber;
    private String email;
    private LocalDate date;
}
