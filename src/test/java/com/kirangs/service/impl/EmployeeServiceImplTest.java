package com.kirangs.service.impl;

import com.kirangs.document.Employee;
import com.kirangs.exception.EmployeeNotFoundException;
import com.kirangs.repository.EmployeeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeReactiveRepository employeeReactiveRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployee() {

        Employee employee = new Employee(1L, "Kiran", "Karnataka", "Bangalore");

        Mockito.when(employeeReactiveRepository.save(ArgumentMatchers.isA(Employee.class)))
                .thenReturn(Mono.just(employee));

        Mono<Employee> employeeMono = employeeService.createEmployee(employee);

        StepVerifier.create(employeeMono)
                .expectNext(employee)
                .expectComplete()
                .verify();
    }

    @Test
    void retrieveEmployeeIdTest() {

        Employee employee = new Employee(1L, "Kiran", "Karnataka", "Bangalore");
        Long id = 1L;

        Mockito.when(employeeReactiveRepository.findById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.just(employee));

        Mono<Employee> employeeMono = employeeService.retrieveEmployeeById(id);

        StepVerifier.create(employeeMono)
                .expectNext(employee)
                .verifyComplete();

    }

    @Test
    void retrieveEmployeeIdThrowsEmployeeNotFoundExceptionTest() {

        Long id = 1L;

        Mockito.when(employeeReactiveRepository.findById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.empty());

        Mono<Employee> employeeMono = employeeService.retrieveEmployeeById(id);

        StepVerifier.create(employeeMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof EmployeeNotFoundException && throwable.getMessage().equals("Employee not found with ID : 1"))
                .verify();
    }

    @Test
    void retrieveAllEmployeesTest() {

        Employee employee1 = new Employee(1L, "Kiran", "Karnataka", "Bangalore");
        Employee employee2 = new Employee(2L, "Nandish", "Karnataka", "Bangalore");

        Mockito.when(employeeReactiveRepository.findAll()).thenReturn(Flux.just(employee1, employee2));

        Flux<Employee> employeeFlux = employeeService.retrieveAllEmployees();

        StepVerifier.create(employeeFlux)
                .expectNext(employee1)
                .expectNext(employee2)
                .expectComplete()
                .verify();
    }

    @Test
    void updateEmployeeByIdTest() {

        Employee employee1 = new Employee(1L, "Kiran", "Karnataka", "Bangalore");
        Long id = 1L;

        Mockito.when(employeeReactiveRepository.findById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.just(employee1));
        Mockito.when(employeeReactiveRepository.save(ArgumentMatchers.isA(Employee.class)))
                .thenReturn(Mono.just(employee1));

        Mono<Employee> employeeMono = employeeService.updateEmployeeById(id, employee1);

        StepVerifier.create(employeeMono)
                .expectNext(employee1)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteEmployeeById() {
        Long id = 1L;
        Mockito.when(employeeReactiveRepository.deleteById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.empty());
        Mono<Void> voidMono = employeeService.deleteEmployeeById(id);
        StepVerifier.create(voidMono)
                .verifyComplete();
    }
}