/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package com.beans.Stateful;

import com.models.Stateful.User;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author dsihl
 */
@Remote
public interface AdminSessionBeanLocal {

    public void insertUser(String username, String email, String password) throws SQLException;

    public boolean loginUser(String username, String password) throws SQLException;

    public User findByUsername(String username) throws SQLException;

    public User findByEmail(String email) throws SQLException;

    public User extractUser(ResultSet rs) throws SQLException;

    public void updateUser(String oldUsername, String newUsername, String newEmail, String newPassword) throws SQLException;

    public void updatePassword(String username, String oldPassword, String newPassword) throws SQLException;

}
