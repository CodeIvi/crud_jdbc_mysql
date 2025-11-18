package org.iesvdm.crud_jdbc_project.service;

import org.iesvdm.crud_jdbc_project.DAO.UserDAO;
import org.iesvdm.crud_jdbc_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public List<User> listUsers(){
        return userDAO.getAll();
    }






}
