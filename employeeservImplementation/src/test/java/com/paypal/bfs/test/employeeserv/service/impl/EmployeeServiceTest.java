package com.paypal.bfs.test.employeeserv.service.impl;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.dao.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.entity.AddressTable;
import com.paypal.bfs.test.employeeserv.entity.EmployeeTable;
import com.paypal.bfs.test.employeeserv.mapper.EmployeeMapper;
import com.paypal.bfs.test.employeeserv.service.IEmployeeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    IEmployeeService employeeService;

    EmployeeMapper employeeMapper;

    EmployeeTable ET;

    AddressTable AT;

    @Before
    public void before(){
        employeeMapper = new EmployeeMapper();
        employeeService = new EmployeeService(employeeRepository,employeeMapper);
        ET = EmployeeTable.builder()
                .id(1)
                .firstName("FirstName")
                .lastName("LastName")
                .dateOfBirth("1990-01-01")
                .build();
        AT = AddressTable.builder()
                .id(1)
                .line1("Address Line 1")
                .line2("Address Line 2")
                .city("City")
                .state("State")
                .country("Country")
                .zipCode("123456")
                .employee(ET)
                .build();
        ET.setAddress(AT);
    }

    @Test
    public void getEmployeeByIdTest(){
        Mockito.when(employeeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(ET));
        Optional<Employee> employee = employeeService.getEmployeeById(1);
        Assert.assertEquals("FirstName",employee.get().getFirstName());
    }

    @Test
    public void getEmployeeByIdNullTest(){
      //  Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(ET));
        Optional<Employee> employee = employeeService.getEmployeeById(2);
        Assert.assertTrue(!employee.isPresent());
    }

    @Test
    public void createEmployeeTest() throws Exception{
        Mockito.when(employeeRepository.save(Mockito.any())).thenReturn(ET);
        Employee employee = new Employee();
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setDateOfBirth("1990-01-01");
        Address address = new Address();
        address.setLine1("Address Line 1");
        address.setLine2("Address Line 2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
        Optional<Employee> newEmployee = employeeService.saveEmployee(employee);
        Assert.assertEquals("FirstName",newEmployee.get().getFirstName());
    }

    @Test(expected = Exception.class)
    public void createEmployeeWithExceptionTest() throws Exception {
        Mockito.when(employeeRepository.save(Mockito.any())).thenThrow(new Exception("Error while creating new employee..."));
        Employee employee = new Employee();
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setDateOfBirth("1990-01-01");
        Address address = new Address();
        address.setLine1("Address Line 1");
        address.setLine2("Address Line 2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
        employeeService.saveEmployee(employee);
    }



}