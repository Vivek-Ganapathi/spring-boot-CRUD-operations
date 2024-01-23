package com.project.employee.controller;

import com.project.employee.entity.Employee;
import com.project.employee.exception.DataAlreadyExistException;
import com.project.employee.exception.DataNotFoundException;
import com.project.employee.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/employee")
public class Controller {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @GetMapping("/getEmployeeList")
    public ResponseEntity getEmployeeList() {
        try{
            return new ResponseEntity(employeeService.getEmployeeList(), HttpStatus.OK );
        }catch (NullPointerException nullPointerException){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Employee content");
        }
    }
    @GetMapping("/getEmployeeList/{id}")
    public ResponseEntity getEmployeeListById(@PathVariable long id) {
        try {
            return new ResponseEntity(employeeService.getEmployeeById(id), HttpStatus.OK );
        } catch (NullPointerException nullPointerException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Data not found for the ID: " + id);
        }
    }

    @PostMapping("/addEmployee")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee) {

        try {
            return new ResponseEntity(employeeService.addEmployee(employee), HttpStatus.CREATED);
        } catch (DataAlreadyExistException dataAlreadyExistException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data already Exist for the ID: " + employee.getId());
        }
    }
    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity updateEmployee(@Valid @RequestBody Employee employee, @PathVariable long id) {
        try {
            return new ResponseEntity(employeeService.updateEmployee(id, employee),HttpStatus.OK);
        } catch (DataNotFoundException dataNotFoundException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data not found for the given ID: " + id);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deteleEmployee(@PathVariable long id) {
        try {
            return new ResponseEntity(employeeService.deleteEmployee(id), HttpStatus.OK);
        } catch (DataNotFoundException dataNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee data not found to delete");
        }
    }
}
