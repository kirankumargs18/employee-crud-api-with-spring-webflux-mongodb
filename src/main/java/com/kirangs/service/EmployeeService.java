package com.kirangs.service;

import com.kirangs.document.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<Employee> createEmployee(Employee employee);

    Mono<Employee> retrieveEmployeeById(Long id);

    Flux<Employee> retrieveAllEmployees();

    Mono<Employee> updateEmployeeById(Long id, Employee employee);

    Mono<Void> deleteEmployeeById(Long id);

}
