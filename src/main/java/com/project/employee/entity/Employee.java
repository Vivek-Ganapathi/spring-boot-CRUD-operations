package com.project.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "EMPLOYEE_DETAILS")
public class Employee {


    @Id
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private long id;

    @Column(name = "EMPLOYEE_NAME")
    @NotBlank(message = "Name should be mandatory")
    private String name;

    @Column(name = "EMPLOYEE_ADDRESS")
    @NotBlank(message = "Address should be mandatory")
    private String address;

    @Column(name = "EMPLOYEE_CONTACT")
    private long contact;

}
