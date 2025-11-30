package com.wanli.hellospringboot.repository;

import com.wanli.hellospringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
