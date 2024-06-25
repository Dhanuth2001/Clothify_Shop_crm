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
@Table(name = "role")*/
public class RoleEntity {
    /*@Id
    @Column(name = "roleId")*/
    private Integer roleId;
    private String roleName;



}
