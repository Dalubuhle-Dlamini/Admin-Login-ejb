/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controllers.admin;

import com.beans.Stateful.AdminSessionBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

/**
 *
 * @author dsihl
 */
@WebServlet(name = "UpdatePassword", urlPatterns = {"/change-password"})
public class UpdatePassword extends HttpServlet {

    private AdminSessionBeanLocal adminSessionBean;

    public void init() throws ServletException {
        try {
            Context ctx = new InitialContext();
            adminSessionBean = (AdminSessionBeanLocal) ctx.lookup("java:global/AdminEAR/AdminEJB/AdminSessionBean!com.beans.Stateful.AdminSessionBeanLocal");
        } catch (NamingException e) {
            throw new ServletException("Failed to lookup Admin Service EJB", e);
        }
    }
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    /**
     * Processes requests for both HTTP <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Call EJB method to update password
            adminSessionBean.updatePassword(username, oldPassword, newPassword);

            out.println("<html><body>");
            out.println("<h2>Password changed successfully </h2>");
            out.println("</body></html>");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update password", e);
            response.getWriter().println("Failed to update password: " + e.getMessage());
            out.println("<html><body>");
            out.println("<h2>Failed to updated password</h2>");
            out.println("</body></html>");
        }
    }
}
