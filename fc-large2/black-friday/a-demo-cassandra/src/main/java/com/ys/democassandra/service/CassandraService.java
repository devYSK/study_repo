package com.ys.democassandra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ys.democassandra.cassandra.entity.Employee;
import com.ys.democassandra.cassandra.entity.EmployeePrimaryKey;
import com.ys.democassandra.cassandra.repository.EmployeeRepository;

@Service
public class CassandraService {

	@Autowired
	EmployeeRepository employeeRepository;

	public void casTest() {
		Employee employee1 = new Employee(
			new EmployeePrimaryKey("seoul", "business", "key"),
			"010-1111-2222"
		);
		employeeRepository.save(employee1);

		Employee employee2 = new Employee(
			new EmployeePrimaryKey("seoul", "business", "joy"),
			"010-3333-4444"
		);
		employeeRepository.save(employee2);

		var result = employeeRepository.findByKeyLocationAndKeyDepartment(
			"seoul", "business"
		);

		result.stream()
			.map(e ->
				String.format("location: %s, department: %s, name: %s, phoneNumber: %s",
					e.key.location, e.key.department, e.key.name, e.phoneNumber)
			)
			.forEach(System.out::println);
	}
}
