package com.project.employee.controller;

import com.project.employee.dao.EmployeeRepository;
import com.project.employee.entity.Employee;
import com.project.employee.exception.DataAlreadyExistException;
import com.project.employee.exception.DataNotFoundException;
import com.project.employee.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    EmployeeServiceImpl employeeService;

    @InjectMocks
    Controller controller;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void getAllEmployeeData() throws Exception {
        List<Employee> employeeList = Arrays.asList(new Employee(1L,"acasc","asca",1287L));
        when(employeeService.getEmployeeList()).thenReturn(employeeList);
        ResponseEntity  responseBody = controller.getEmployeeList();

        Assertions.assertEquals(HttpStatus.OK, responseBody.getStatusCode());
        Assertions.assertEquals(employeeList, responseBody.getBody());

    }

    @Test
    public void testGetAllEmployees_Exception() throws Exception {
        when(employeeService.getEmployeeList())
                .thenThrow(NullPointerException.class);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getEmployeeList());
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
        assertEquals("No Employee content", exception.getReason());
    }
//
//    @Test
//    public void testGetEmployeeById_Exception() throws Exception {
//        Long id = 1L;
//        when(employeeService.getEmployeeById(id))
//                .thenThrow(NullPointerException.class);
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.getEmployeeListById(id));
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        assertEquals("Employee Data not found for the ID: 1", exception.getReason());
//
//    }

    @Test
    public void testGetEmployeeListById_EmployeeFound() throws Exception {
        Employee employee = new Employee(1L,"acasc","asca",1287L);
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        ResponseEntity  responseBody = controller.getEmployeeListById(1L);

        Assertions.assertEquals(HttpStatus.OK, responseBody.getStatusCode());
        Assertions.assertEquals(employee, responseBody.getBody());
    }

    @Test
    public void testGetEmployeeListById_EmployeeNotFound() {
        // Mock the service to return null, indicating employee not found
        when(employeeService.getEmployeeById(Mockito.anyLong())).thenThrow(NullPointerException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getEmployeeListById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Employee Data not found for the ID: 1", exception.getReason());
    }
//
//    @Test
//    public void testGetEmployeeListById_NullPointerException() {
//        // Mock the service to throw a NullPointerException
//        when(employeeService.getEmployeeById(Mockito.anyLong())).thenThrow(NullPointerException.class);
//
//        // Call the controller method and expect a ResponseStatusException
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//                () -> controller.getEmployeeListById(1L));
//
//        // Assert the exception status is NOT_FOUND
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        // Assert the exception message
//        assertEquals("Employee Data not found for the ID: 1", exception.getReason());
//    }

    @Test
    void addEmployee_Success() {
        Employee mockEmployee = new Employee(1L,"Vivek", "Bengaluru", 100000L);
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);
        when(employeeService.addEmployee(mockEmployee)).thenReturn(mockEmployee);

        ResponseEntity response = controller.addEmployee(mockEmployee);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockEmployee, response.getBody());

    }

    @Test
    void addEmployee_DataAlreadyExistException() {
        Employee mockEmployee = new Employee(1L,"Vivek", "Bengaluru", 100000L);
        when(employeeService.addEmployee(mockEmployee)).thenThrow(DataAlreadyExistException.class);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.addEmployee(mockEmployee));

        Assertions.assertEquals("Data already Exist for the ID: 1", exception.getReason());
        Assertions.assertEquals(ResponseStatusException.class, exception.getClass());
    }

    @Test
    void updateEmployee_Success() {
        long id = 1L;
        Employee mockEmployee = new Employee(1L,"Vivek", "Bengaluru", 100000L);
        when(employeeService.updateEmployee(id, mockEmployee)).thenReturn(mockEmployee);
        ResponseEntity<Employee> responseEntity = controller.updateEmployee(mockEmployee, id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockEmployee, responseEntity.getBody());
    }

    @Test
    void updateEmployee_DataNotFoundException() {
        // Arrange
        long id = 1L;
        Employee mockEmployee = new Employee(1L,"Vivek", "Bengaluru", 100000L);
        doThrow(DataNotFoundException.class).when(employeeService).updateEmployee(eq(id), any(Employee.class));

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateEmployee(mockEmployee, id);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Data not found for the given ID: " + id, exception.getReason());
    }

    @Test
    void deleteEmployee_Success() throws Exception{
        long id = 1L;
        Employee employee = new Employee();

        when(employeeService.deleteEmployee(id)).thenReturn(employee);
        ResponseEntity<?> responseEntity = controller.deteleEmployee(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(id);
    }

    @Test
    void deleteEmployee_DataNotFoundException() {

        long id = 1L;
        doThrow(DataNotFoundException.class).when(employeeService).deleteEmployee(eq(id));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.deteleEmployee(id);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Employee data not found to delete", exception.getReason());
    }

}