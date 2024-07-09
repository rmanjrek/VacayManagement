package com.management.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.project.model.Employee;
import com.management.project.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {

}
