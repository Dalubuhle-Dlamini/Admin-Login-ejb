/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controllers.admin;

import com.beans.Stateful.AdminSessionBeanLocal;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

/**
 *
 * @author dsihl
 */
@WebServlet(name = "EditUser", urlPatterns = {"/edit-user"})
public class EditUser extends HttpServlet {

    private AdminSessionBeanLocal adminSessionBean;

    public void init() throws ServletException {
        try {
            Context ctx = new InitialContext();
            adminSessionBean = (AdminSessionBeanLocal) ctx.lookup("java:global/AdminEAR/AdminEJB/AdminSessionBean!com.beans.Stateful.AdminSessionBeanLocal");
        } catch (NamingException e) {
            throw new ServletException("Failed to lookup Admin Service EJB", e);
        }
    }
    
    private static final Logger logger = Logger.getLogger(EditUser.class.getName());
    
    /**
     * Processes requests for both HTTP <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldUsername = request.getParameter("oldUsername");
        String newUsername = request.getParameter("newUsername");
        String newEmail = request.getParameter("newEmail");
        String newPassword = request.getParameter("newPassword");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            adminSessionBean.updateUser(oldUsername, newUsername, newEmail, newPassword);
            HttpSession session = request.getSession(true);
            session.setAttribute("username", newUsername);
            out.println("<html><body>");
            out.println("<h2>Update Successful</h2>");
            out.println("<p>welcome " + newUsername + " successfully!</p>");
            out.println("</body></html>");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update user", e);
            response.getWriter().println("Failed to update user: " + e.getMessage());
            out.println("<html><body>");
            out.println("<h2>Failed to update user: " + newUsername+ "</h2>");
            out.println("</body></html>");
        }
    }
    }



