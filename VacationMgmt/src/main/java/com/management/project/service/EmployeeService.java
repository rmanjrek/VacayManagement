/**
 * 
 */
package com.management.project.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.management.project.model.Employee;
import com.management.project.model.Request;

/**
 * 
 */
public interface EmployeeService {

	/* Operations for all employees */	
	public List<Request> getEmployeeRequestList(int empId, String status);
	
	public String addRequestForEmployee(Employee emp, Request req);
	
	public Request createRequestForEmployee(Employee emp, Date startDate, Date endDate);
	
	
	/* Operations for manager */
	public List<Request> getAllRequestsAssignedToMe(int empId, String status);
	
	public Request approveRejectRequest(int reqId, String status, int managerId);
	
	public List<Request> getAllOverlappingRequests(int managerId);
}
