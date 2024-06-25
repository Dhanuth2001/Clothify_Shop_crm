package edu.icet.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userID;
    private String email;
    private String password;
    private Integer roleID;
    private Integer employeeID;

}
