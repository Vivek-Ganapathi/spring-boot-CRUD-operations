package com.project.employee.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.project.employee.dao.EmployeeRepository;
import com.project.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

    private EmployeeRepository employeeRepository;

    public Query(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Iterable<Employee> getEmployeeData(){
        return employeeRepository.findAll();
    }
}
