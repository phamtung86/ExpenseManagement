package com.vti.repository;

import com.vti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Integer> {

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);
}
