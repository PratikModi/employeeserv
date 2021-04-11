package com.paypal.bfs.test.employeeserv.mapper;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AddressTable;
import com.paypal.bfs.test.employeeserv.entity.EmployeeTable;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee employeeTableToEmployee(EmployeeTable et){
        Employee employee = new Employee();
        employee.setId(et.getId());
        employee.setFirstName(et.getFirstName());
        employee.setLastName(et.getLastName());
        employee.setDateOfBirth(et.getDateOfBirth());
        Address address = new Address();
        address.setLine1(et.getAddress().getLine1());
        address.setLine2(et.getAddress().getLine2());
        address.setCity(et.getAddress().getCity());
        address.setCountry(et.getAddress().getCountry());
        address.setState(et.getAddress().getState());
        address.setZipCode(et.getAddress().getZipCode());
        employee.setAddress(address);
        return employee;
    }

    public EmployeeTable employeeToEmployeeTable(Employee e){
        EmployeeTable ET =  EmployeeTable.builder()
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .dateOfBirth(e.getDateOfBirth())
                .build();
        AddressTable AT = AddressTable.builder()
                    .line1(e.getAddress().getLine1())
                        .line2(e.getAddress().getLine2())
                        .city(e.getAddress().getCity())
                        .state(e.getAddress().getState())
                        .country(e.getAddress().getCountry())
                        .zipCode(e.getAddress().getZipCode())
                        .employee(ET)
                        .build();
        ET.setAddress(AT);
        return ET;
    }


}
