package edu.montana.csci.csci440.helpers;

import edu.montana.csci.csci440.model.Employee;
import edu.montana.csci.csci440.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmployeeHelper {

    public static String makeEmployeeTree() {
        // TODO, change this to use a single query operation to get all employees
        Employee employee = Employee.find(1); // root employee
        // and use this data structure to maintain reference information needed to build the tree structure
        Map<Long, List<Employee>> employeeMap = new HashMap<>();

        List<Employee> allEmployees = Employee.all();
        for (Employee currentEmployee : allEmployees) {
            Long reportsTo = currentEmployee.getReportsTo();
            if (employeeMap.containsKey(reportsTo)) {
                List<Employee> employees = employeeMap.get(reportsTo);
                employees.add(currentEmployee);
                employeeMap.replace(reportsTo, employees);
            }
            else {
                ArrayList<Employee> employees = new ArrayList<>();
                employees.add(currentEmployee);
                employeeMap.put(reportsTo, employees);
            }
        }
        return "<ul>" + makeTree(employee, employeeMap) + "</ul>";
    }

    // TODO - currently this method just uses the employee.getReports() function, which
    //  issues a query.  Change that to use the employeeMap variable instead
    public static String makeTree(Employee employee, Map<Long, List<Employee>> employeeMap) {
        String list = "<li><a href='/employees" + employee.getEmployeeId() + "'>"
                + employee.getEmail() + "</a><ul>";
        List<Employee> reports = employeeMap.get(employee.getEmployeeId());
        if (reports != null) {
            for (Employee report : reports) {
                list += makeTree(report, employeeMap);
            }
        }
        return list + "</ul></li>";
    }
}
