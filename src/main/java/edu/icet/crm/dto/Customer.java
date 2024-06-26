package edu.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Integer customerID;
    private String name;
    private LocalDate dob;
    private String contactEmail;
    private String contactNumber;


}
