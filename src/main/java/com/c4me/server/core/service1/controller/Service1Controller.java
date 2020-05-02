package com.c4me.server.core.service1.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-23-2020
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class Service1Controller {

    @GetMapping
    @LogAndWrap(log="test GET method")
    public String listTasks(){
        return "GET Method";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @LogAndWrap(log="test POST method")
    public String newTasks(){
        return "POST Method";
    }

    @PutMapping("/{taskId}")
    @LogAndWrap(log="test PUT method")
    public String updateTasks(@PathVariable("taskId")Integer id){
        return "PUT Method";
    }

    @DeleteMapping("/{taskId}")
    @LogAndWrap(log="test DELETE method")
    public String deleteTasks(@PathVariable("taskId")Integer id){
        return "DELETE Method";
    }
}
