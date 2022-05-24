package com.damu.controller;

import com.damu.pojo.Employee;
import com.damu.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/getEmployee")
public class AppController {

    @Autowired
    AsyncService asyncService;

    @GetMapping
    public Employee getEmployee(){

        WebClient webClient = WebClient.create("http://localhost:8080/employee/2");
        Mono<Employee> result = webClient.get().retrieve().bodyToMono(Employee.class);
        return result.block();

    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable(name = "id") long id) throws InterruptedException, ExecutionException {
        log.error("API request recieved for ID :: " + id);
        CompletableFuture<Employee> emp = asyncService.getEmp("Damodar", 10L);
        log.error("Call to EMP DTLS COmpleted :: ");
        CompletableFuture.allOf(emp);
        return emp.get();
    }
    @GetMapping("/list")
    public Employee getEmployeeList() throws InterruptedException, ExecutionException {
        log.error("API request recieved for Employee List ");
        List<Employee> employeeList = new ArrayList<>();
        List<Employee> employeeListOut = new ArrayList<>();
        Employee employee = new Employee();
        employee.setName("Damodar");
        employee.setId(10L);
        Employee employee1 = new Employee();
        employee1.setName("Ravi");
        employee1.setId(15L);

        Employee employee3 = new Employee();
        employee3.setName("Kumar");
        employee3.setId(20L);
        employeeList.add(employee);
        employeeList.add(employee1);
        employeeList.add(employee3);

        CompletableFuture<Employee> serviceEmp = null;
        Map<String,CompletableFuture<Employee>> map = new HashMap<>();
        List<CompletableFuture<Employee>> futureList = new ArrayList<>();
        for(Employee emp: employeeList){
            log.error("Call to Async Service :: "+ emp.getName());
            serviceEmp = asyncService.getEmp(emp.getName(), emp.getId());
//            map.put(emp.getName(),serviceEmp);
            futureList.add(serviceEmp);
            log.error("Completed calling to Async Service :: "+ emp.getName());
        }
log.error("Completed Employee Loop");

        CompletableFuture.allOf(serviceEmp);
        log.error("Map Data :: " + futureList.get(0).get().getId());
        return serviceEmp.get();
    }
}
