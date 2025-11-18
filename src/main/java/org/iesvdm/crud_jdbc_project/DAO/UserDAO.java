package org.iesvdm.crud_jdbc_project.DAO;

import org.iesvdm.crud_jdbc_project.model.User;

public interface UserDAO {

    User create(User user);
    /**
     *
     * @param user la password debe venir ya encriptada
     * @return user con id actualizado
     */


    /**
     *
     * @param username unico en base de datos
     */
    User findByUsername(String username);
}


