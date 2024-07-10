package com.management.project.serviceImpl.test;

import static org.mockito.ArgumentMatchers.any;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.management.project.model.Employee;
import com.management.project.model.Request;
import com.management.project.repository.EmployeeRepo;
import com.management.project.repository.RequestRepo;
import com.management.project.service.EmployeeService;
import com.management.project.serviceImpl.EmployeeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TestEmployeeServiceImpl {

	@InjectMocks
	EmployeeServiceImpl empServiceImpl;

	@Mock
	RequestRepo requestRepo;

	@Mock
	EmployeeRepo empRepo;

	private List<Employee> empList;

	public void setup() throws Exception {
		empList = createEmployees();
	}

	@Test
	public void testCreateRequestForEmployee() {
		empList = createEmployees();
		Employee emp = empList.get(1);
		Request r1 = new Request();
		r1.setReqId(123);
		r1.setAuthorId(emp.getId());
		r1.setResolvedBy(emp.getManagerId());
		r1.setStartDate(new Date());
		r1.setEndDate(new Date());

		Request r2 = empServiceImpl.createRequestForEmployee(emp, new Date(), new Date());
		Assert.assertEquals(r1.getAuthorId(), r2.getAuthorId());

	}

	@Test
	public void testGetEmployeeRequestList() {
		Employee e1 = new Employee();
		e1.setId(1);
		e1.setAge(55);
		e1.setManagerId(0);
		e1.setVacationDays(30);

		Employee e2 = new Employee();
		e2.setId(2);
		e2.setAge(35);
		e2.setManagerId(1);
		e2.setVacationDays(30);

		Employee e3 = new Employee();
		e3.setId(3);
		e3.setAge(25);
		e3.setManagerId(1);
		e3.setVacationDays(30);

		Request r1 = new Request();
		r1.setReqId(123);
		r1.setAuthorId(e2.getId());
		r1.setResolvedBy(e2.getManagerId());
		r1.setStartDate(new Date());
		r1.setEndDate(new Date());
		r1.setStatus("REJECTED");

		Request r2 = new Request();
		r2.setReqId(124);
		r2.setAuthorId(e1.getId());
		r2.setResolvedBy(e2.getManagerId());
		r2.setStartDate(new Date());
		r2.setEndDate(new Date());
		r2.setStatus("APPROVED");

		Request r3 = new Request();
		r3.setReqId(125);
		r3.setAuthorId(e2.getId());
		r3.setResolvedBy(e2.getManagerId());
		r3.setStartDate(new Date());
		r3.setEndDate(new Date());
		r3.setStatus("PENDING");

		List<Request> lReq = new ArrayList<>();
		lReq.add(r3);
		lReq.add(r2);
		lReq.add(r1);
		Mockito.when(requestRepo.findAll()).thenReturn(lReq);
		List<Request> list1 = empServiceImpl.getEmployeeRequestList(2, null);
		Assert.assertEquals(2, list1.size());

		List<Request> list2 = empServiceImpl.getEmployeeRequestList(2, "PENDING");
		Assert.assertEquals(1, list2.size());

	}

	@Test
	public void testApproveRejectRequest() {
		Employee e2 = new Employee();
		e2.setId(2);
		e2.setAge(35);
		e2.setManagerId(1);
		e2.setVacationDays(30);

		Request r2 = empServiceImpl.createRequestForEmployee(e2, new Date(), new Date());

		Employee e1 = new Employee();
		e1.setId(1);
		e1.setAge(55);
		e1.setManagerId(0);
		e1.setVacationDays(30);

		Mockito.when(requestRepo.getReferenceById(any())).thenReturn(r2);
		Request rUpdate = empServiceImpl.approveRejectRequest(r2.getReqId(), "APPROVED", e2.getManagerId());
		Assert.assertEquals(rUpdate.getStatus(), "APPROVED");

		// Test when manager id is not correct
		Request rUpdate1 = empServiceImpl.approveRejectRequest(r2.getReqId(), "APPROVED", e1.getManagerId());
		Assert.assertNull(rUpdate1);

	}

	@Test
	public void testGetAllRequestsAssignedToMe() {
		Employee e2 = new Employee();
		e2.setId(2);
		e2.setAge(35);
		e2.setManagerId(1);
		e2.setVacationDays(30);
		Employee e3 = new Employee();
		e3.setId(3);
		e3.setManagerId(1);

		Employee e1 = new Employee();
		e1.setId(1);

		Request r1 = new Request();
		r1.setReqId(123);
		r1.setAuthorId(e2.getId());
		r1.setResolvedBy(e2.getManagerId());
		r1.setStartDate(new Date());
		r1.setEndDate(new Date());
		r1.setStatus("REJECTED");

		Request r2 = new Request();
		r2.setReqId(124);
		r2.setAuthorId(e2.getId());
		r2.setResolvedBy(e2.getManagerId());
		r2.setStartDate(new Date());
		r2.setEndDate(new Date());
		r2.setStatus("APPROVED");

		Request r3 = new Request();
		r3.setReqId(125);
		r3.setAuthorId(e3.getId());
		r3.setResolvedBy(e3.getManagerId());
		r3.setStartDate(new Date());
		r3.setEndDate(new Date());
		r3.setStatus("PENDING");

		List<Request> lReq = new ArrayList<>();
		lReq.add(r3);
		lReq.add(r2);
		lReq.add(r1);
		Mockito.when(requestRepo.findAll()).thenReturn(lReq);

		List<Request> l1 = empServiceImpl.getAllRequestsAssignedToMe(1, null);
		Assert.assertEquals(3, l1.size());

		List<Request> l2 = empServiceImpl.getAllRequestsAssignedToMe(1, "REJECTED");
		Assert.assertEquals(1, l2.size());

		List<Request> l3 = empServiceImpl.getAllRequestsAssignedToMe(5, null);
		Assert.assertEquals(0, l3.size());

		List<Request> l4 = empServiceImpl.getAllOverlappingRequests(1);
		Assert.assertEquals(3, l4.size());

	}

	@Test
	public void testAddRequestForEmployee() throws ParseException {
		Employee e2 = new Employee();
		e2.setId(2);
		e2.setAge(35);
		e2.setManagerId(1);
		e2.setVacationDays(0);
		Employee e1 = new Employee();
		e1.setId(1);

		Request r2 = empServiceImpl.createRequestForEmployee(e2, convertStringToDate("20-09-2024"), convertStringToDate("25-09-2024"));

		String f = empServiceImpl.addRequestForEmployee(e2, r2);
		Assert.assertEquals("FAILURE", f);

		e2.setVacationDays(10);
		Mockito.when(requestRepo.save(any())).thenReturn(r2);
		Mockito.when(empRepo.save(any())).thenReturn(e2);
		String s = empServiceImpl.addRequestForEmployee(e2, r2);
		Assert.assertEquals("SUCCESS", s);
		
		e2.setVacationDays(2);
		String f1 = empServiceImpl.addRequestForEmployee(e2, r2);
		Assert.assertEquals("FAILURE", f1);
		
		Request r3 = empServiceImpl.createRequestForEmployee(e2, convertStringToDate("20-09-2024"), convertStringToDate("10-09-2024"));
		String f2 = empServiceImpl.addRequestForEmployee(e2, r3);
		Assert.assertEquals("FAILURE", f2);
	}
	
	private Date convertStringToDate(String str) throws ParseException {
		
	       //Instantiating the SimpleDateFormat class
	       SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");      
	       //Parsing the given String to Date object
	       Date date = formatter.parse(str); 

	       return date;
	}

	private List<Employee> createEmployees() {
		Employee e1 = new Employee();
		e1.setId(1);
		e1.setAge(55);
		e1.setManagerId(0);
		e1.setVacationDays(30);

		Employee e2 = new Employee();
		e2.setId(2);
		e2.setAge(35);
		e2.setManagerId(1);
		e2.setVacationDays(30);

		Employee e3 = new Employee();
		e3.setId(3);
		e3.setAge(25);
		e3.setManagerId(1);
		e3.setVacationDays(30);

		Employee e4 = new Employee();
		e4.setId(4);
		e4.setAge(30);
		e4.setManagerId(2);
		e4.setVacationDays(3);

		List<Employee> list = new ArrayList<>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);

		return list;
	}
}
