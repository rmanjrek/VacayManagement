/**
 * 
 */
package com.management.project.service;

import java.util.List;
import java.util.Optional;

import com.management.project.model.Employee;
import com.management.project.model.Request;

/**
 * 
 */
public interface EmployeeService {

	public Employee addEmployee(Employee emp);
	
	public String removeEmployee(int empId);
	
	public Optional<Employee> findEmployeeById(int EmpId);
	
	public String updateEmployee(Employee emp);
	
	public List<Employee> getAllEmployeesByManagerId(int managerId);
	
	public List<Request> getEmployeeRequestList(int empId, String status);
	
	public String addRequestForEmployee(Employee emp, Request req);
	
	public List<Request> getAllRequestsAssignedToMe(int empId);
	
	public String approveRequest(int reqId, String status);
}
