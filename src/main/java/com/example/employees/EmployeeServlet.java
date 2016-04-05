package com.example.employees;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by youngseokkim on 04.04.2016.
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"})
public class EmployeeServlet extends HttpServlet {
    EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("searchAction");
        if (action != null) {
            switch (action) {

                case "searchById":
                    searchEmployeeById(req, resp);
                    break;
                case "searchByName":
                    searchEmployeeByName(req, resp);
                    break;
            }


        }
        else
        {
            List<Employee> result = employeeService.getAllEmployees();
            forwardListEmployees( req, resp, result);
        }
    }

    private void forwardListEmployees(HttpServletRequest req, HttpServletResponse resp, List<Employee> employeeList) throws ServletException, IOException {
        String nextJSP = "/jsp/list-employees.jsp";
        RequestDispatcher dispatcer = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("employeeList",employeeList );
        dispatcer.forward(req, resp);
    }

    private void searchEmployeeByName(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
            String employeeName = req.getParameter("employeeName");
            List<Employee> result = employeeService.searchEmployeesByName(employeeName);
            forwardListEmployees(req, resp, result);
    }

    private void searchEmployeeById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long idEmployee = Integer.valueOf(req.getParameter("idEmployee"));
        Employee employee = null;
        try{
            employee = employeeService.getEmployee(idEmployee);

        }
        catch (Exception e)
        {
            Logger.getLogger( EmployeeServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        req.setAttribute("employee", employee);
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/new-employee.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }
}
