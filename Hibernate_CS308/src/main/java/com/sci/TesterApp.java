package com.sci;

import com.sci.criteria.FilterQuery;
import com.sci.criteria.Operator;
import com.sci.dao.DBConfig;
import com.sci.dao.GenericDAO;
import com.sci.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TesterApp {
    public static void main(String[] args) {
        // Create a generic DAO for Employee entity
        GenericDAO<Employee, Integer> employeeDAO = new GenericDAO<>(Employee.class);

        //* Use the findByFilter method from GenericDAO
/*
        List<FilterQuery> filters = new ArrayList<>();

        // Q13, Les02 Oracle SQL Slides:
        filters.add(new FilterQuery("jobId", Arrays.asList("SA_REP", "ST_CLERK"),
                Operator.In));
        filters.add(new FilterQuery("salary", Arrays.asList(2500, 3500, 7000),
                Operator.In));

        List<Employee> emps = employeeDAO.findByFilter(filters, false);
*/
        //* Use the findByFilter method from GenericDAO
        List<Employee> emps = employeeDAO.findWithCustomPredicate((cb, root) ->
                cb.and(
                        root.get("jobId").in(Arrays.asList("SA_REP", "ST_CLERK")),
                        cb.or(
                                cb.equal(root.get("salary"), 2500),
                                cb.equal(root.get("salary"), 3500),
                                cb.equal(root.get("salary"), 7000)
                        )
/*
                        cb.in(root.get("salary"))
                                .value(2500)
                                .value(3500)
                                .value(7000)
*/

                )
        );


        System.out.printf("%-15s %-10s %-10s%n", "LastName", "JobId", "Salary");
        for (Employee employee : emps) {
            System.out.printf("%-15s %-10s %-10d%n",
                    employee.getLastName(),
                    employee.getJobId(),
                    employee.getSalary());
        }

        DBConfig.shutdown();
    }
}
