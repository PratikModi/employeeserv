package com.paypal.bfs.test.employeeserv.service.impl;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.dao.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.entity.EmployeeTable;
import com.paypal.bfs.test.employeeserv.mapper.EmployeeMapper;
import com.paypal.bfs.test.employeeserv.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    private EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Optional<Employee> getEmployeeById(Integer id) {
        EmployeeTable et = employeeRepository.findById(id).orElse(null);
        if(Objects.nonNull(et)){
            System.out.println(et);
            return Optional.of(employeeMapper.employeeTableToEmployee(et));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Employee> saveEmployee(Employee employee) throws Exception {
        Employee createdEmployee = null;
        try {
            if (Objects.nonNull(employee)) {
                createdEmployee=employeeMapper.employeeTableToEmployee(employeeRepository.save(employeeMapper.employeeToEmployeeTable(employee)));
                return Optional.of(createdEmployee);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error while creating new employee...",e);
        }
        return Optional.empty();
    }
}
