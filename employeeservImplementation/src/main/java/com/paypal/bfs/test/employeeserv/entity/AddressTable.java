package com.paypal.bfs.test.employeeserv.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Address")
@Builder
public class AddressTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    @Column(name = "zip_code")
    private String zipCode;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    @ToString.Exclude
    private EmployeeTable employee;
}

