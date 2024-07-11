/**
 * 
 */
package com.management.project.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.project.model.Employee;
import com.management.project.model.Request;
import com.management.project.repository.EmployeeRepo;
import com.management.project.repository.RequestRepo;
import com.management.project.service.EmployeeService;

/**
 * 
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private RequestRepo requestRepo;
	
	private final String success = "SUCCESS";
	private final String failure = "FAILURE";
	private final String pending = "PENDING";
	
	@Override
	public String addRequestForEmployee(Employee emp, Request req) {
		if (validateRequest(req) && employeeHasVacayLeft(emp, req)) {
			try {
				requestRepo.save(req);
				Employee updatedEmp = new Employee();
				updatedEmp = emp;
				int leaveDays =  getDifferenceInStartEndDates(req.getStartDate(), req.getEndDate());
				updatedEmp.setVacationDays(emp.getVacationDays()- leaveDays);
				employeeRepo.save(updatedEmp);
				return success;
			} catch(Exception e) {
				System.out.println( " Exception in inserting request for employer " + e);
			}
		}
		return failure;
	}
	
	@Override
	public List<Request> getAllRequestsAssignedToMe(int empId, String status) {
		List<Request> reqList = requestRepo.findAll().stream()
				.distinct().filter(r -> r.getResolvedBy()==empId).collect(Collectors.toList());
		if(status!= null) {
			reqList = reqList.stream().filter(r -> r.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
		}
		return reqList;
	}

	@Override
	public Request approveRejectRequest(int reqId, String status, int managerId) {
	
		Request r = requestRepo.getReferenceById(reqId);
		
		//manager can only approve/reject a request when it is assigned to them
		if(r.getResolvedBy() != managerId) return null;
		r.setStatus(status);
		try {
			requestRepo.save(r);
			return r;
		} catch(Exception e) {
			System.out.println( " Exception in updating request status " + e);
		}
		return null;
		
	}

	@Override
	public List<Request> getEmployeeRequestList(int empId, String status) {
		List<Request> reqList = requestRepo.findAll().stream()
				.distinct().filter(r -> r.getAuthorId()==empId).collect(Collectors.toList());
		if(status!= null) {
			reqList = reqList.stream().filter(r -> r.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
		}
		
		return reqList;
	}

	@Override
	public List<Request> getAllOverlappingRequests(int managerId) {
		List<Request> reqList = requestRepo.findAll().stream()
				.distinct().filter(r -> r.getResolvedBy()==managerId).collect(Collectors.toList());
		List<Request> result = new ArrayList<>();
		
		//sorting the request list based 
		reqList.sort((r1,r2) -> r1.getStartDate().compareTo(r2.getStartDate()));
		Request first = reqList.get(0);
		HashSet<Integer> set = new HashSet<>();
		for(int i =1;i<reqList.size();i++) {
			Request next = reqList.get(i);
			if(next.getStartDate().compareTo(first.getEndDate()) <= 0) {
				if (!set.contains(first.getReqId())) {
					result.add(first);
					set.add(first.getReqId());
				}
				if (!set.contains(next.getReqId())) {
					result.add(next);
					set.add(next.getReqId());
				}
			} 
			first = next;
		}
		return result;
	}

	@Override
	public Request createRequestForEmployee(Employee emp, Date startDate, Date endDate) {
		Request r = new Request();
		r.setAuthorId(emp.getId());
		r.setResolvedBy(emp.getManagerId());
		r.setCreateDate(new Date());
		r.setStartDate(startDate);
		r.setEndDate(endDate);
		r.setStatus(pending); // whenever a new request is created the status is set to pending by default
		return r;
	}

	//Helper Methods
	
	private int getDifferenceInStartEndDates(Date d1, Date d2) {
		Date startDate = d1;
		Date endDate = d2;
		LocalDate sdate = LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDate edate = LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		int days =  Period.between(sdate, edate).getDays();
		return days;
	}
	
	private boolean employeeHasVacayLeft(Employee emp, Request req) {
		int days =  getDifferenceInStartEndDates(req.getStartDate(), req.getEndDate());
		if (emp.getVacationDays() > 0 && emp.getVacationDays() >= days) return true;
		return false;
	}

	private boolean validateRequest(Request req) {
	// start date to be before end date otherwise invalid request
		if(req.getStartDate().compareTo(req.getEndDate()) > 0) {
			return false;
		}		
		return true;
	}

}
