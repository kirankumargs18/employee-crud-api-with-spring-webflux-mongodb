package com.kirangs.repository;

import com.kirangs.document.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeReactiveRepository extends ReactiveMongoRepository<Employee,Long> {
}
