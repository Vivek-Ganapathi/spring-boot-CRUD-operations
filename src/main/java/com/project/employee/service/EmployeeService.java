package com.project.employee.service;

import com.project.employee.entity.Employee;

public interface EmployeeService  {

    public Iterable<Employee> getEmployeeList();

    public Employee getEmployeeById(long id);

    public Employee addEmployee(Employee employee);

    public Employee updateEmployee(long id, Employee updatedEmployee);

    public Employee deleteEmployee(long id);
}
