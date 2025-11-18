package org.iesvdm.crud_jdbc_project;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.crud_jdbc_project.DAO.UserDAO;
import org.iesvdm.crud_jdbc_project.model.User;
import org.iesvdm.crud_jdbc_project.util.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@Slf4j
@SpringBootTest
class CrudJdbcProjectApplicationTests {

    @Autowired
    UserDAO userDAO;
    @Autowired
    HashUtil hashUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void createUserTest() {
        String username = "jose";
        String password = "123456";

        String passwordHashed = null;

        try {
            passwordHashed = hashUtil.hashPassword(password);
            log.info("passwordHashed = {} ", passwordHashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        User user = User.builder()
                .username(username)
                .password(passwordHashed)
                .build();
        userDAO.create(user);

        Assertions.assertTrue(user.getId()>0;


    }
}
