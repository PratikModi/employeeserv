package com.paypal.bfs.test.employeeserv.validate;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class RequestValidatorTest {

    Employee employee;

    RequestValidator requestValidator;

    @Before
    public void before(){
        employee = new Employee();
        employee.setFirstName("ABC");
        employee.setLastName("XYZ");

        requestValidator = new RequestValidator();
    }

    @Test
    public void employeeValidatorTest(){
        employee.setDateOfBirth("1990-01-01");
        Address address = new Address();
        address.setLine1("Line1");
        address.setLine2("Line2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
        Optional<List<ValidationError>> validationErrors = requestValidator.getValidationErrors(employee);
        Assert.assertTrue(!validationErrors.isPresent());
    }

    @Test
    public void employeeWithoutAddressTest(){
        employee.setDateOfBirth("1990-01-01");
        Optional<List<ValidationError>> validationErrors = requestValidator.getValidationErrors(employee);
        Assert.assertEquals(RequestValidator.ADDRESS,validationErrors.get().get(0).getField());
        Assert.assertEquals(1,validationErrors.get().size());
    }

    @Test
    public void employeeMissingMandatoryFieldTest(){
        Optional<List<ValidationError>> validationErrors = requestValidator.getValidationErrors(employee);
        Assert.assertEquals(3,validationErrors.get().size());
        Assert.assertEquals(RequestValidator.DATE_OF_BIRTH,validationErrors.get().get(0).getField());
    }

    @Test
    public void employeeAddressMissingMandatoryFieldTest(){
        employee.setDateOfBirth("1990-01-01");
        Address address = new Address();
        address.setLine2("Line2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
        Optional<List<ValidationError>> validationErrors = requestValidator.getValidationErrors(employee);
        Assert.assertEquals(1,validationErrors.get().size());
        Assert.assertEquals(RequestValidator.ADDRESS_LINE_1,validationErrors.get().get(0).getField());
    }

    @Test
    public void employeeWithInvalidBirthDate(){
        employee.setDateOfBirth("19901-01-01");
        Address address = new Address();
        address.setLine1("Line1");
        address.setLine2("Line2");
        address.setCity("City");
        address.setState("State");
        address.setCountry("Country");
        address.setZipCode("123456");
        employee.setAddress(address);
        Optional<List<ValidationError>> validationErrors = requestValidator.getValidationErrors(employee);
        Assert.assertEquals(1,validationErrors.get().size());
        Assert.assertEquals(RequestValidator.DATE_OF_BIRTH,validationErrors.get().get(0).getField());

    }

}