package com.my.application.repository;

import com.my.webservice.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findUserByUserName(final String userName);
}
