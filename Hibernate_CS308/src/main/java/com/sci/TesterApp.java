package com.sci;

import com.sci.dao.DBConfig;
import com.sci.dao.GenericDAO;
import com.sci.models.*;
import jakarta.persistence.Query;
import org.hibernate.Session;

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

        testQueryCache();

        DBConfig.shutdown();
    }

    private static void testQueryCache() {
        System.out.println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----");

        Session session = DBConfig.getSessionFactory().openSession();

        Query query1 = session.createQuery("from Employee where employeeId = :id", Employee.class).setParameter("id", 101);
        query1.setHint("org.hibernate.cacheable", Boolean.TRUE);
        Employee employee1 = (Employee) query1.getSingleResult();
        System.out.println(employee1.getFirstName() + ' ' + employee1.getLastName());

        System.out.println("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----");

        Query query2 = session.createQuery("from Employee where employeeId = :id", Employee.class).setParameter("id", 101);
        query2.setHint("org.hibernate.cacheable", Boolean.TRUE);
        Employee employee2 = (Employee) query2.getSingleResult();
        System.out.println(employee2.getFirstName() + ' ' + employee2.getLastName());

        session.close();
    }
}
