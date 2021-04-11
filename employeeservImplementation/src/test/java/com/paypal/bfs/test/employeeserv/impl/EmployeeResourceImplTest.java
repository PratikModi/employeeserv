package com.paypal.bfs.test.employeeserv.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.IEmployeeService;
import com.paypal.bfs.test.employeeserv.validate.RequestValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeResourceImplTest {
    public MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    IEmployeeService employeeService;
    @Autowired
    RequestValidator requestValidator = new RequestValidator();
    Employee employee;
    Address address;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("ABC");
        employee.setLastName("XYZ");
        employee.setDateOfBirth("1990-01-01");
        address = new Address();
        address.setLine1("Line1");
        address.setLine2("Line2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void getEmployeeByIdTest() throws Exception{
        String uri = "/v1/bfs/employees/{id}";
        Mockito.when(employeeService.getEmployeeById(1)).thenReturn(Optional.of(employee));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri,1)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Employee employee = mapFromJson(content, Employee.class);
        Assert.assertEquals("ABC",employee.getFirstName());
        Assert.assertTrue(Objects.nonNull(employee.getAddress()));
    }
    @Test
    public void getEmployeeByIdNotFoundTest() throws Exception {
        String uri = "/v1/bfs/employees/{id}";
        Mockito.when(employeeService.getEmployeeById(2)).thenReturn(Optional.of(employee));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri, 1)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    public void createEmployeeTest() throws Exception{
        String uri = "/v1/bfs/employees";
        Mockito.when(employeeService.getEmployeeById(1)).thenReturn(Optional.empty());
        Mockito.when(employeeService.saveEmployee(Mockito.any())).thenReturn(Optional.of(employee));
        String content = mapToJson(employee);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(201, status);
        Employee employee = mapFromJson(content, Employee.class);
        Assert.assertEquals("ABC",employee.getFirstName());
        Assert.assertTrue(Objects.nonNull(employee.getAddress()));
    }

    @Test
    public void createEmployeeIdempotentTest() throws Exception{
        String uri = "/v1/bfs/employees";
        Mockito.when(employeeService.getEmployeeById(1)).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.saveEmployee(Mockito.any())).thenReturn(Optional.of(employee));
        String content = mapToJson(employee);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    public void createEmployeeValidationErrorTest() throws Exception{
        String uri = "/v1/bfs/employees";
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("ABC");
        newEmployee.setLastName("XYZ");
        newEmployee.setDateOfBirth("19901-01-01");
        Address newAddress = new Address();
        newAddress.setLine1("Line1");
        newAddress.setLine2("Line2");
        newAddress.setCity("City");
        newAddress.setState("State");
        newAddress.setCountry("Country");
        newAddress.setZipCode("123456");
        newEmployee.setAddress(newAddress);
        Mockito.when(employeeService.getEmployeeById(1)).thenReturn(Optional.empty());
        Mockito.when(employeeService.saveEmployee(Mockito.any())).thenReturn(Optional.of(newEmployee));
        String content = mapToJson(newAddress);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }
}