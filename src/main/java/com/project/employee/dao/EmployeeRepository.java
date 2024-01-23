package com.project.employee.dao;

import com.project.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee employee = new Employee(1L, "Vivek", "Bengalure", 1231L);
}
