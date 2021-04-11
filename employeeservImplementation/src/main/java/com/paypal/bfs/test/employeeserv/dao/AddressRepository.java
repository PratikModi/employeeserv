package com.paypal.bfs.test.employeeserv.dao;

import com.paypal.bfs.test.employeeserv.entity.AddressTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressTable, Integer> {
}
