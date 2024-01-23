package com.project.employee.service;

import com.project.employee.dao.EmployeeRepository;
import com.project.employee.entity.Employee;
import com.project.employee.exception.DataAlreadyExistException;
import com.project.employee.exception.DataNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeesRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getEmployeeListNotEmpty() {
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(new Employee(1L, "Vivek","Bengaluru", 14312L));

        when(employeesRepository.findAll()).thenReturn(mockEmployeeList);

        List<Employee> result = employeeService.getEmployeeList();
        Assertions.assertNotNull(result);
        assertEquals(1L, result.size());
        assertEquals("Vivek", result.get(0).getName());
        assertEquals("Bengaluru", result.get(0).getAddress());
        assertEquals(14312L, result.get(0).getContact());

        verify(employeesRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeListEmpty() {
        // Arrange
        when(employeesRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(NullPointerException.class, () -> employeeService.getEmployeeList());

        verify(employeesRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById() {
        Employee employee = new Employee(1L, "Vivek","Bengaluru", 14312L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee employee1 = employeeService.getEmployeeById(1L);

        assertEquals(employee, employee1);
    }

    @Test
    void getEmployeeById_withException(){
        when(employeesRepository.findById(any())).thenReturn(any());
        assertThrows(NullPointerException.class, () -> employeeService.getEmployeeById(1L));

    }

    @Test
    void addEmployee_Success() {
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(employeesRepository.save(employee)).thenReturn(employee);
        Employee result = employeeService.addEmployee(employee);

        assertEquals(employee, result);
    }
    @Test
    void addEmploye_DataAlreadyExistException(){
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(employee));
        DataAlreadyExistException exception = assertThrows(DataAlreadyExistException.class, () ->
                employeeService.addEmployee(employee));
        assertEquals("Employee data already Exist for the ID: 1", exception.getMessage());

        verify(employeesRepository, times(1)).findById(1L);
        verify(employeesRepository, never()).save(employee);
    }

    @Test
    void updateEmployee() {
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeesRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(1L, employee);

        assertEquals(1L, result.getId());
        assertEquals(employee, result);
    }

    @Test
    void updateEmployee_DataNotFoundException(){
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.empty());
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,() -> employeeService.updateEmployee(1L,employee));

        assertEquals("Employee data not found for the given ID: 1", exception.getMessage());
    }

    @Test
    void deleteEmployee() {
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(employee));
        Employee result = employeeService.deleteEmployee(1L);

        assertEquals(employee, result);
    }

    @Test
    void deleteEmployee_DataNotFoundException(){
        Employee employee = new Employee(1L,"Vivek","Bengaluru",10000L);
        when(employeesRepository.findById(1L)).thenReturn(Optional.empty());
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> employeeService.deleteEmployee(1L));

        assertEquals("Employee Data not found", exception.getMessage());
    }
}