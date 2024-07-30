package com.kirangs.service.impl;

import com.kirangs.document.Employee;
import com.kirangs.exception.EmployeeNotFoundException;
import com.kirangs.repository.EmployeeReactiveRepository;
import com.kirangs.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeReactiveRepository employeeReactiveRepository;

    @Override
    public Mono<Employee> createEmployee(Employee employee) {
        return employeeReactiveRepository.save(employee).log();
    }

    @Override
    public Mono<Employee> retrieveEmployeeById(Long id) {
        return employeeReactiveRepository.findById(id).log()
                .switchIfEmpty(Mono.error(() -> new EmployeeNotFoundException("Employee not found with ID : " + id)));
    }

    @Override
    public Flux<Employee> retrieveAllEmployees() {
        return employeeReactiveRepository.findAll().log();
    }

    @Override
    public Mono<Employee> updateEmployeeById(Long id, Employee employee) {

        return employeeReactiveRepository.findById(id).log().flatMap(employee1 -> {
            employee1.setName(employee.getName());
            employee1.setAddress(employee.getAddress());
            employee1.setLocation(employee.getLocation());
            return employeeReactiveRepository.save(employee1).log();
        });
    }

    @Override
    public Mono<Void> deleteEmployeeById(Long id) {
        return employeeReactiveRepository.deleteById(id).log();
    }
}
