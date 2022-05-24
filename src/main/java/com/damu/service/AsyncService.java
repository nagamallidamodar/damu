package com.damu.service;

import com.damu.pojo.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {

    @Async
    public CompletableFuture<Employee> getEmp(String name,long id) throws InterruptedException {
        log.error("Recieved the request :: " + name);
        Thread.sleep(10000L);
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPlace("Async");
        employee.setId(id);
        log.error("Completed the request :: " + name);
        return CompletableFuture.completedFuture(employee);
    }
}
