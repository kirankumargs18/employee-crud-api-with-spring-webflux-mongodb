package com.kirangs.controller;


import com.kirangs.document.Employee;
import com.kirangs.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/employee-service")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.retrieveEmployeeById(id);
    }

    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.retrieveAllEmployees();
    }

    @PutMapping("/{id}")
    public Mono<Employee> updateEmployeeById(@PathVariable Long id,@Valid @RequestBody Employee employee) {
        return employeeService.updateEmployeeById(id, employee);
    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteEmployeeById(@PathVariable Long id) {
        return employeeService.deleteEmployeeById(id).log()
                .thenReturn("Employee with ID : " + id + " deleted successfully");
    }

}
