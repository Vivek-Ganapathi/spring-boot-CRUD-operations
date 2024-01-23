package com.project.employee.service;

import com.project.employee.entity.Employee;
import com.project.employee.exception.DataAlreadyExistException;
import com.project.employee.exception.DataNotFoundException;
import com.project.employee.dao.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

     @Autowired
    private EmployeeRepository employeesRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeesRepository = employeeRepository;
    }

    public List<Employee> getEmployeeList(){
        logger.info("Getting Employee List");
        List<Employee> employeeList = employeesRepository.findAll();
        if(employeeList.isEmpty()){
            throw new NullPointerException("No Data available");
        }
        return employeeList;
    }
    public Employee getEmployeeById(long id){
        logger.info("Getting employeeList by ID: "+id);
        Optional<Employee> employeeOptional = employeesRepository.findById(id);
        return employeeOptional.orElseThrow(() -> new NullPointerException("Employee not found with ID: " + id));
    }
    public Employee addEmployee(@Valid Employee employee){
        logger.info("Adding Employee data");
        Optional<Employee> employees = employeesRepository.findById(employee.getId());
        if(employees.isPresent()){
            throw new DataAlreadyExistException("Employee data already Exist for the ID: " + employee.getId());
        }
        else {
            employeesRepository.save(employee);
            return employee;
        }

    }

//    private List<String> validateEmployee(Employee employee) {
//        logger.info("Getting Employee List");
//        List<String> validationErrors = new ArrayList<>();
//
//        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
//            return List.of("Employee name cannot be empty");
//        }
//        return List.of();
//        }

    public Employee updateEmployee(long id, @Valid Employee updatedEmployee) {
        logger.info("Updating Employee data by ID: "+id);
        Optional<Employee> employee = employeesRepository.findById(id);
        if (!employee.isPresent()) {
            throw new DataNotFoundException("Employee data not found for the given ID: "+id);
        }
        employeesRepository.delete(employee.get());
        employeesRepository.save(updatedEmployee);
        return updatedEmployee;
    }
    public Employee deleteEmployee(long id) {
        logger.info("Deleting Employee data by ID: "+id);
        Optional<Employee> employee = employeesRepository.findById(id);
        if(!employee.isPresent()){
            throw new DataNotFoundException("Employee Data not found");
        }
        employeesRepository.deleteById(id);
        return employee.get();
    }
}
