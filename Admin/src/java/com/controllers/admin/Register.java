/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controllers.admin;

import com.beans.Stateful.AdminSessionBeanLocal;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author dsihl
 */
@WebServlet(name = "Register", urlPatterns = {"/register"})
public class Register extends HttpServlet {

    
    private AdminSessionBeanLocal adminSessionBean;
    
    public void init() throws ServletException {
        try {
            Context ctx = new InitialContext();
            adminSessionBean = (AdminSessionBeanLocal) ctx.lookup("java:global/AdminEAR/AdminEJB/AdminSessionBean!com.beans.Stateful.AdminSessionBeanLocal");
        } catch (NamingException e) {
            throw new ServletException("Failed to lookup Admin Service EJB", e);
        }
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            adminSessionBean.insertUser(username, email, password);
            out.println("<html><body>");
            out.println("<h2>Registration Successful</h2>");
            out.println("<p>User registered successfully!</p>");
            out.println("</body></html>");

        } catch (SQLException e) {
            out.println("<html><body>");
            out.println("<h2>Registration Failed</h2>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            out.println("</body></html>");
            e.printStackTrace(); // Log the exception for debugging
        } finally {
            out.close();
        }
    }


}
