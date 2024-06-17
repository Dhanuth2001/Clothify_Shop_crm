package edu.icet.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String id;
    private String name;
    private LocalDate dob;
    private LocalDate doJoin;
    private String role;
    private String address;
    private String email;
    private String contactNo;



}