package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.IEmployeeService;
import com.paypal.bfs.test.employeeserv.validate.RequestValidator;
import com.paypal.bfs.test.employeeserv.validate.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class for employee resource.
 */
@RestController
@Slf4j
public class EmployeeResourceImpl implements EmployeeResource {

    private IEmployeeService employeeService;

    private RequestValidator requestValidator;

    @Autowired
    public EmployeeResourceImpl(IEmployeeService employeeService, RequestValidator requestValidator) {
        this.employeeService = employeeService;
        this.requestValidator = requestValidator;
    }

    @Override
    public ResponseEntity createEmployee(@RequestBody Employee newEmployee, UriComponentsBuilder ucBuilder) {
        Optional<Employee> employee = null;
        try {
            if(Objects.nonNull(newEmployee.getId())){
                employee = employeeService.getEmployeeById(newEmployee.getId());
                if(employee.isPresent()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee with id "+newEmployee.getId()+" already Exists.");
                }
            }
            Optional<List<ValidationError>> errors = requestValidator.getValidationErrors(newEmployee);
            if(errors.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            employee = employeeService.saveEmployee(newEmployee);
            UriComponents uriComponents =
                    ucBuilder.path("/v1/bfs/employees/{id}").buildAndExpand(employee.get().getId());
            URI location = uriComponents.toUri();
            return ResponseEntity.created(location).body(employee);
        }catch (Exception e){
            log.error("Error while creating employee",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Override
    public ResponseEntity employeeGetById(Integer id) {
        Employee employee;
        if(Objects.nonNull(id)) {
            employee = employeeService.getEmployeeById(id).orElse(null);
            if (employee != null)
                return ResponseEntity.ok(employee);
        }
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found With Id "+id);
    }
}
