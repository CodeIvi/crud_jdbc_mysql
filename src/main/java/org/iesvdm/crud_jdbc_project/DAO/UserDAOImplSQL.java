package org.iesvdm.crud_jdbc_project.DAO;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.crud_jdbc_project.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
@Slf4j
@Repository
public class UserDAOImplSQL implements UserDAO{

    private JdbcTemplate jdbcTemplate;


    //INYECCION DE JdcbTemplate POR CONSTRUCTOR
    public UserDAOImplSQL(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User create(User user) {

        String sql = """
               insert into user(username,password)
               values(       ?,          ?) 
               """;
        String[] ids = {"id"};

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con->{

            PreparedStatement ps = con.prepareStatement(sql,ids);

            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());

            return ps;

        },keyHolder);

        user.setId(keyHolder
                .getKey().longValue());


        return user;
    }

    @Override
    public User findByUsername(String username) {

        String sql = """
                select * from user where username = ?
                """;
     User user = jdbcTemplate.queryForObject(sql,(ResultSet rs, int rowNum)-> User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                        .build()
                ,username);

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> listUsers = jdbcTemplate.query("""
                Select * 
                from user
                """,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"))

        );

        log.info("Devueltos {} users",listUsers.size());
        log.debug("Devueltos {} users ",listUsers.size());

        return listUsers;
    }
}
