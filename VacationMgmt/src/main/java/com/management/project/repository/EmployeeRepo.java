package com.management.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.project.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

}
