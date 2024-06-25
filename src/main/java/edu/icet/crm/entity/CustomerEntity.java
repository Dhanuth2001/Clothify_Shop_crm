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
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "Customer")
public class CustomerEntity {
    /*@Id
    @Column(name = "customerID")*/
    private Integer customerID;
    private String name;
    private LocalDate dob;
    private String contactEmail;
    private String contactNumber;
}
