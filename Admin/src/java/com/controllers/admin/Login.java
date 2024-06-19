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
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    private static final Logger logger = Logger.getLogger(Login.class.getName());

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
     * Processes requests for both HTTP <code>GET</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            boolean isAdmin = adminSessionBean.loginUser(username, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);

            if (isAdmin) {
                out.println("<html><body>");
                out.println("<h2>Login Successful</h2>");
                out.println("<p>welcome " + username + " admin!</p>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Login Successful</h2>");
                out.println("<p>welcome " + username + " user!</p>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Login failed for user: " + username, e);
            // Return error message or handle differently
            response.getWriter().println("Login failed. An unexpected error occurred.");
            out.println("<html><body>");
            out.println("<h2>Login failed for user: " + username + "</h2>");
            out.println("</body></html>");
        }
    }
}
