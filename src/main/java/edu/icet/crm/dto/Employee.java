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
public class Employee {
    private Integer employeeId;
    private Integer roleId;
    private String name;
    private LocalDate dob;
    private LocalDate doJoin;
    private String address;
    private String email;
    private String contactNo;



}
