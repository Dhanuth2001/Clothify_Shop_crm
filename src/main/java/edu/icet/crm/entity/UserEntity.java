package edu.icet.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/*@Entity
@Table(name = "user")*/
public class UserEntity {
    /*@Id
    @Column(name = "userID")*/
    private Integer userID;
    private String email;
    private String password;


    private Integer roleID;
    private Integer employeeID;

}
