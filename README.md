# VacayManagement

**Employee Management System 
**
Overview : System to manage vacation requests for employees.

There are 2 model classes created 
1. Employee
2. Request - This is an entity which is created/updated/viewed by an Employee

The use cases are the following:
1. Employee can create request
2. Employee can see his own request. Filter his request based on status.
3. Employee can also view how many vacation days he has remianing
4. Employee can be a manage so the manager can see requests assigned to him. Filter requests by status
5. Manager can approve/reject a request assigned to him
6. Manager can view what leave requests any specific employee of his has submitted.
7. Manager can also view any overlapping requests amongst his employees

Assumptions:
1. An employee can only see requests created by him or assined to him.
2. When a new request is created by default the status is PENDING.











### Brief

It's November, and everyone is planning their vacation. But management is struggling! They need a solution to approve vacation requests while also ensuring that there are still enough employees in the office to achieve end-of-year goals.  

Your task is to build a REST API that allows employees to make vacation requests, and another that provides managers with an overview of all vacation requests and allows them to decline or approve requests.

### Tasks

- There should be API routes that allow workers to:
    - See their requests
        - Filter by status (approved, pending, rejected)
    - See their number of remaining vacation days
    - Make a new request if they have not exhausted their total limit (30 per year)
- There should be API routes that allow managers to:
    - See an overview of all requests
        - Filter by pending and approved
    - See an overview for each individual employee
    - See an overview of overlapping requests
    - Process an individual request and either approve or reject it
- Write tests that sufficiently validate your application
- Storing data in memory is acceptable

Each employee vacation request should, at minimum, have the following signature:
```
{
  "id": ENTITY_ID,
  "author": WORKER_ID,
  "status": STATUS_OPTION, // may be one of: "approved", "rejected", "pending"
  "resolved_by": MANAGER_ID,
  "request_created_at": "2020-08-09T12:57:13.506Z",
  "vacation_start_date" "2020-08-24T00:00:00.000Z",
  "vacation_end_date" "2020-09-04T00:00:00.000Z",
}
```
You are expected to design any other required models and routes for your API.

### Evaluation Criteria

- Best practices
- Completeness: Did you include all features?
- Correctness: Does the solution perform in a logical way?
- Maintainability: Is the solution written in a clean, maintainable way?
- Testing: Has the solution been adequately tested?
- Documentation: Is the API well-documented?

### CodeSubmit

Please organize, design, test, and document your code as if it were going into production - then push your changes to the master branch. After you have pushed your code, you must submit the assignment via the assignment page.

Please include steps on how to run the submitted exercise and include any additional steps required to build and run tests for the submission.

Additional bonus points will be awarded for providing a URL to the API hosted on a platform.

All the best and happy coding,

The Dexcom Team
