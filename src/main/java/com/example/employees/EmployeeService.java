package com.example.employees;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by youngseokkim on 31.03.2016.
 */
public class EmployeeService {
    List<Employee> employeeList = com.example.employees.EmployeeList.getInstance();

    //Get all employees.
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    //Get all employees whose Name or Last Name match the searchParam, order by name and last name.
    public List<Employee> searchEmployeesByName(String searchParam) {
        Comparator<Employee> groupByComparator = Comparator.comparing(Employee::getName)
                .thenComparing(Employee::getLastName);
        List<Employee> result = employeeList
                .stream()
                .filter(e -> e.getName().equalsIgnoreCase(searchParam) || e.getLastName().equalsIgnoreCase(searchParam))
                .sorted(groupByComparator)
                .collect(Collectors.toList());
        return result;
    }

    //Get the employees by id
    public Employee getEmployee(long id) throws Exception {
        Optional<Employee> match
                = employeeList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new Exception("The Employee id " + id + " not found");
        }
    }
}
