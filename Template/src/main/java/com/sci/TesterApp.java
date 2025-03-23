package com.sci;

import com.sci.dao.*;
import com.sci.models.*;
import java.util.List;

public class TesterApp {
    public static void main(String[] args) {
        DBEmployee dbEmployee = new DBEmployee();
        List<Employee> employees = dbEmployee.get();
/*
        for (Employee employee : employees) {
            System.out.println(employee);
        }
*/
        employees.forEach(System.out::println);

        DBConfig.shutdown();
    }
}
