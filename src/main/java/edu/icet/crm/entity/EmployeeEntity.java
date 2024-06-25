package edu.icet.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeEntity {

    private Integer employeeId;

    private Integer roleId;
    private String name;
    private LocalDate dob;
    private LocalDate doJoin;
    private String address;
    private String email;
    private String contactNo;



}
