package com.paypal.bfs.test.employeeserv.service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

import java.util.Optional;

public interface IEmployeeService {

    Optional<Employee> getEmployeeById(Integer id);
    Optional<Employee> saveEmployee(Employee employee) throws Exception;
}
