package com.kirangs.controller;

import com.kirangs.document.Employee;
import com.kirangs.exception.EmployeeNotFoundException;
import com.kirangs.exception.ErrorResponse;
import com.kirangs.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = EmployeeController.class)
@AutoConfigureWebTestClient
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    private static final String URL = "/api/v1/employee-service";

    @Test
    void saveEmployeeTest() {

        Employee employee = new Employee(1L, "Kiran", "Karnataka", "Bangalore");

        Mockito.when(employeeService.createEmployee(ArgumentMatchers.isA(Employee.class)))
                .thenReturn(Mono.just(employee));

        webTestClient
                .post()
                .uri(URL)
                .bodyValue(employee)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Employee.class)
                .consumeWith(employeeEntityExchangeResult -> {
                    Employee responseBody = employeeEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                });
    }

    @Test
    void retrieveEmployeeByIdTest() {

        Long id = 1L;
        Mockito.when(employeeService.retrieveEmployeeById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.just(new Employee(1L, "Kiran", "Karnataka", "Bangalore")));

        webTestClient
                .get()
                .uri(URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Employee.class)
                .consumeWith(employeeEntityExchangeResult -> {
                    Employee responseBody = employeeEntityExchangeResult.getResponseBody();
                    assertEquals("Kiran", responseBody.getName());
                });
    }

    @Test
    void retrieveEmployeeByIdTestThrowsErrorResponseTest() {

        Long id = 1L;
        Mockito.when(employeeService.retrieveEmployeeById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.error(new EmployeeNotFoundException("Employee not found with ID : " + id)));

        webTestClient
                .get()
                .uri(URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(errorResponseEntityExchangeResult -> {
                    ErrorResponse responseBody = errorResponseEntityExchangeResult.getResponseBody();
                    assertEquals("Employee not found with ID : 1", responseBody.getMessages().get(0));
                    assertEquals("/api/v1/employee-service/1", responseBody.getDetails());
                });
    }

    @Test
    void retrieveAllEmployeesTest() {

        Employee employee1 = new Employee(1L, "Kiran", "Karnataka", "Bangalore");
        Employee employee2 = new Employee(2L, "Nandish", "Karnataka", "Bangalore");

        Mockito.when(employeeService.retrieveAllEmployees())
                .thenReturn(Flux.just(employee1, employee2));

        webTestClient
                .get()
                .uri(URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Employee.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<Employee> employeeList = listEntityExchangeResult.getResponseBody();
                    assertEquals(2, employeeList.size());
                });
    }

    @Test
    void updateEmployeeByIdTest() {

        Employee employee1 = new Employee(1L, "Kiran", "Karnataka Updated", "Bangalore");
        Long id = 1L;

        Mockito.when(employeeService.updateEmployeeById(ArgumentMatchers.isA(Long.class), ArgumentMatchers.isA(Employee.class)))
                .thenReturn(Mono.just(employee1));

        webTestClient
                .put()
                .uri(URL + "/{id}", id)
                .bodyValue(employee1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Employee.class)
                .consumeWith(employeeEntityExchangeResult -> {
                    Employee employee = employeeEntityExchangeResult.getResponseBody();
                    assertEquals("Karnataka Updated", employee.getAddress());
                });
    }

    @Test
    void deleteEmployeeById() {

        Long id = 1L;

        Mockito.when(employeeService.deleteEmployeeById(ArgumentMatchers.isA(Long.class)))
                .thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(URL + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("Employee with ID : 1 deleted successfully", responseBody);
                });

    }

    @Test
    void testEmployeeRequestBodyValidation() {

        Employee employee = new Employee(1L, "", "", "");

        webTestClient
                .post()
                .uri(URL)
                .bodyValue(employee)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(errorResponseEntityExchangeResult -> {
                    ErrorResponse errorResponse = errorResponseEntityExchangeResult.getResponseBody();
                    assertEquals(3, errorResponse.getMessages().size());
                });
    }
}

