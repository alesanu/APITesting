package tests;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Employee;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testng.annotations.Test;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;


public class EmployeeEndpointTest {
    private final static String BASE_URL = "http://dummy.restapiexample.com/api/v1";

    public static final String CREATE = "/create";
    public static final String DELETE = "/delete";
    public static final String EMPLOYEE = "/employee";
    public static final String EMPLOYEES = "/employees";
    public static final String UPDATE = "/update";

    public Employee employee = new Employee("Popescu Ion", "25", "1500", "");
    public Employee employeeUpdated = new Employee("Popescu Ion", "26", "2000", "image");
    public String employeeId;

    public static String createAllEmployessRequestUrl() {
        return BASE_URL + EMPLOYEES;
    }

    public static String createEmployeeRequestUrl(String id) {
        return BASE_URL + EMPLOYEE + "/" + id;
    }

    public static String updateEmployeeRequestUrl(String id) {
        return BASE_URL + UPDATE + "/" + id;
    }

    public static String deleteEmployeeRequestUrl(String id) {
        return BASE_URL + DELETE + "/" + id;
    }

    public static String createNewEmployeeRequestUrl() {
        return BASE_URL + CREATE;

    }

    @Test(priority = 1)
    public void accessEmployessAndGetStatusCode() {

        given()
                .when()
                .get(createAllEmployessRequestUrl())
                .then()
                .statusCode(200);
    }

    @Test(priority = 1)
    public void createNewEmployee() {

        Response response = given()
                .contentType("application/json")
                .body(employee)
                .when()
                .post(createNewEmployeeRequestUrl());
        System.out.println("Response Body is =>  " + response.asString());
        employeeId =  response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .get("id");
        System.out.println("employeeId is =>  " + employeeId);
    }

    @Test(priority = 2)
    public void validateCreatedEmployee() {

        Response response = given()
                .when()
                .get(createEmployeeRequestUrl(employeeId));
        System.out.println("Response Body is =>  " + response.asString());
        JsonPath createdEmployee = response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath();
        assertEquals(createdEmployee.get("employee_name"), employee.getName());
        assertEquals(createdEmployee.get("employee_age"), employee.getAge());
        assertEquals(createdEmployee.get("employee_salary"), employee.getSalary());
        assertEquals(createdEmployee.get("profile_image"), employee.getProfileImage());
    }

    @Test(priority = 3)
    public void updateCreatedEmployee() {

        Response response = given()
                .contentType("application/json")
                .body(employeeUpdated)
                .when()
                .put(updateEmployeeRequestUrl(employeeId));
        System.out.println("Response Body is =>  " + response.asString());
        JsonPath createdEmployee = response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath();
        assertEquals(createdEmployee.get("employee_name"), employeeUpdated.getName());
        assertEquals(createdEmployee.get("employee_age"), employeeUpdated.getAge());
        assertEquals(createdEmployee.get("employee_salary"), employeeUpdated.getSalary());
        assertEquals(createdEmployee.get("profile_image"), employeeUpdated.getProfileImage());
    }

    @Test(priority = 4)
    public void deleteCreatedEmloyee() {

        given()
                .when()
                .delete(deleteEmployeeRequestUrl(employeeId))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        given()
                .when()
                .get(createEmployeeRequestUrl(employeeId))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    // cannot create more tests because this is not REST api that can be used for this
    // Problems:
    //
    // - on some calls the JSON format is employee_KEY on other calls only KEY
    // - we have different endpoints to manage the employee ( all the methods <PUT, POST, DELETE>
    // should be implemented on /employee/{id} and for get all we should use /employee instead of /employees)
    // - the response code is not relevant (example: 200 when employee is not found)
    // - this api returns a mix of JSON, HTML and XML
}
