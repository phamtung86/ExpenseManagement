package com.vti.repository;

import com.vti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);

}
