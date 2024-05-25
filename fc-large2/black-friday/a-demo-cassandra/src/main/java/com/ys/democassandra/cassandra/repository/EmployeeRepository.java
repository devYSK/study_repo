package com.ys.democassandra.cassandra.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.ys.democassandra.cassandra.entity.Employee;
import com.ys.democassandra.cassandra.entity.EmployeePrimaryKey;

@Repository
public interface EmployeeRepository extends CassandraRepository<Employee, EmployeePrimaryKey> {

    List<Employee> findByKeyLocationAndKeyDepartment(final String location, final String department);
}
