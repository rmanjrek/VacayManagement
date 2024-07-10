package com.management.project.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.project.model.Employee;
import com.management.project.model.Request;
import com.management.project.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService empService;
	
	private final String failure = "FAILURE";
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);

	
	/**
	 * Function to get all requests submitted by the employee himself
	 * @param employee
	 * @param status
	 * @return
	 */
	@GetMapping("/employee/getMyRequests")
	public ResponseEntity<List<Request>> getAllMyRequests(@RequestBody Employee employee, @RequestParam("status") String status) {
		//if sending null in status then we are fetching all requests
		List<Request> reqList = empService.getEmployeeRequestList(employee.getId(), status);
		if (reqList.size() == 0) return new ResponseEntity<List<Request>>(reqList, HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Request>>(reqList, HttpStatus.CREATED);
	}
	
	/**
	 * Function to get vacation days at any given point for employee himself
	 * @param employee
	 * @return
	 */
	@GetMapping("/employee/getMyVacationDays")
	public ResponseEntity<Integer> getRemainingVacationDays(@RequestBody Employee employee) {
			return new ResponseEntity<Integer>(employee.getVacationDays(), HttpStatus.CREATED);
	}
	
	/**
	 * Function for an employee to create a request of leave
	 * @param employee
	 * @return
	 */
	@PostMapping("/employee/createMyVacation/startdate/{startDate}/enddate/{endDate}")
	public ResponseEntity<Request> createVacayRequest(@RequestBody Employee employee, @PathVariable String startDate, @PathVariable String endDate) {
		
		Request r = empService.createRequestForEmployee(employee, convertStringToDate(startDate), convertStringToDate(endDate)); 
		String res = empService.addRequestForEmployee(employee, r);
		if (res.equalsIgnoreCase(failure)) {
			return new ResponseEntity<Request>(r, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Request>(r, HttpStatus.CREATED);
	}
	
	/**
	 * Functions for a Manager employee to get all requests assigned to him
	 * @param employee
	 * @param status
	 * @return
	 */
	@GetMapping("/employee/manager/getAssignedRequests")
	public ResponseEntity<List<Request>> getRequestsAssigned(@RequestBody Employee employee, @RequestParam("status") String status) {
		List<Request> reqList = empService.getAllRequestsAssignedToMe(employee.getId(), status);
		
		return new ResponseEntity<List<Request>>(reqList, HttpStatus.CREATED);	
	}
	
	/**
	 * Function for a Manager employee to get list of leave 
	 * requests submitted by an employee under him
	 * @param employee
	 * @return
	 */
	@GetMapping("/employee/manager/getEmployeeRequest")
	public ResponseEntity<List<Request>> getEmployeeRequest(@RequestBody Employee employee, @RequestParam("status") String status) {
		List<Request> reqList = empService.getEmployeeRequestList(employee.getId(), null);
		return new ResponseEntity<List<Request>>(reqList, HttpStatus.CREATED);
	}
	
	/**
	 * Function for a manager to get a list of requests
	 * from the employees under him which are overlapping
	 * @param employee
	 * @return
	 */
	@GetMapping("/employee/manager/getOverlappingRequest")
	public ResponseEntity<List<Request>> getOverlappingRequest(@RequestBody Employee employee) {
		List<Request> reqList = empService.getAllOverlappingRequests(employee.getId());
		return new ResponseEntity<List<Request>>(reqList, HttpStatus.CREATED);
	}
	
	/**
	 * Function for a manager employee to update status of a leave request
	 * assigned to him
	 * @param employee
	 * @param request
	 * @param status
	 * @return
	 */
	@PostMapping("/employee/manager/updateRequestStatus")
	public ResponseEntity<Request> updateRequestStatus(@RequestBody Employee employee, @RequestBody Request request, @RequestParam("status") String status) {
		Request r = empService.approveRejectRequest(request.getReqId(), status, employee.getId());
		return new ResponseEntity<Request>(r, HttpStatus.CREATED);
	}
	
	/**
	 * Helper method to convert a string to java.util.Date object and format it
	 * @param str
	 * @return
	 */
	private Date convertStringToDate(String str) {
		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		
		return date;
	}
	
}
