/**
 * 
 */
package com.management.project.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
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

	@Override
	public Employee addEmployee(Employee emp) {
		Employee employee = employeeRepo.save(emp);
		return employee;
	}

	@Override
	public String removeEmployee(int empId) {
		employeeRepo.deleteById(empId);
		
		return "success";
	}

	@Override
	public Optional<Employee> findEmployeeById(int empId) {
		Optional<Employee> employee = employeeRepo.findById(empId);
		if(employee.isPresent()) return employee;
		return null;
	}

	@Override
	public String updateEmployee(Employee emp) {
		Optional<Employee> employee = employeeRepo.findById(emp.getId());
		if(employee.isPresent()) {
			Employee e1= new Employee();
			employeeRepo.save(e1);
			return "success";
		} else {
			return "Employee not found";
		}
		
	}

	@Override
	public List<Employee> getAllEmployeesByManagerId(int managerId) {
	//	List<Employee> empList = employeeRepo.findA
		return null;
	}

	@Override
	public String addRequestForEmployee(Employee emp, Request req) {
		boolean f = validateRequest(req);
		boolean hasVacayLeft = employeeHasVacayLeft(emp, req);
		if (f && hasVacayLeft) {
			try {
				requestRepo.save(req);
				return "success";
			} catch(Exception e) {
				System.out.println( " Exception in inserting request for employer " + e);
			}
		}
		return "Failure";
	}
	
	
	
	private boolean employeeHasVacayLeft(Employee emp, Request req) {
		Date startDate = req.getStartDate();
		Date endDate = req.getEndDate();
		LocalDate sdate = LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDate edate = LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		int days =  Period.between(sdate, sdate).getDays();
		if (emp.getVacationDays() > 0 && emp.getVacationDays() >= days) return true;
		return false;
	}

	private boolean validateRequest(Request req) {
		// if no manager id found request invalid
		if (employeeRepo.findById(req.getResolvedBy()) == null) {
			return false;
		}
		// start date to be before end date otherwise invalid request
		if(req.getStartDate().compareTo(req.getEndDate()) > 0) {
			return false;
		}		
		return true;
	}

	@Override
	public List<Request> getAllRequestsAssignedToMe(int empId) {
		List<Request> reqList = requestRepo.findAll().stream()
				.distinct().filter(r -> r.getResolvedBy()==empId).collect(Collectors.toList());
		return reqList;
	}

	@Override
	public String approveRequest(int reqId, String status) {
	
		Request r = requestRepo.getReferenceById(reqId);
		r.setStatus(status);
		try {
			requestRepo.save(r);
			return "success";
		} catch(Exception e) {
			System.out.println( " Exception in updating request status " + e);
		}
		return "Failure";
		
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

	

}
